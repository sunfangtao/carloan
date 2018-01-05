package com.aioute.carloan.cluster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.MainActivity_;
import com.aioute.carloan.adapter.InfoWindowAdapter;
import com.aioute.carloan.bean.UserBean;
import com.aioute.carloan.common.Contant;
import com.aioute.carloan.util.MapUtil;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;

import java.util.ArrayList;
import java.util.List;

import cn.sft.util.Util;


/**
 * Created by yiyi.qi on 16/10/10.
 * 整体设计采用了两个线程,一个线程用于计算组织聚合数据,一个线程负责处理Marker相关操作
 */
public class ClusterOverlay implements AMap.OnCameraChangeListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, AMap.OnMapClickListener {
    private AMap mAMap;
    private Context mContext;
    private List<ClusterItem> mClusterItems;
    private List<Cluster> mClusters;
    private int mClusterSize;
    private ClusterClickListener mClusterClickListener;
    private MapChangedListener mapChangedListener;
    private ClusterRender mClusterRender;
    private List<Marker> mAddMarkers = new ArrayList<Marker>();
    private double mClusterDistance;
    private LruCache<Integer, BitmapDescriptor> mLruCache;
    private HandlerThread mMarkerHandlerThread = new HandlerThread("addMarker");
    private HandlerThread mSignClusterThread = new HandlerThread("calculateCluster");
    private Handler mMarkerhandler;
    private Handler mSignClusterHandler;
    private float mPXInMeters;
    private boolean mIsCanceled = false;

    private Marker currentMarker;

    /**
     * 构造函数
     *
     * @param amap
     * @param clusterSize 聚合范围的大小（指点像素单位距离内的点会聚合到一个点显示）
     * @param context
     */
    public ClusterOverlay(AMap amap, int clusterSize, Context context) {
        this(amap, null, clusterSize, context);
    }

    /**
     * 构造函数,批量添加聚合元素时,调用此构造函数
     *
     * @param amap
     * @param clusterItems 聚合元素(经纬度点集合，附带状态)
     * @param clusterSize
     * @param context
     */
    public ClusterOverlay(AMap amap, List<ClusterItem> clusterItems, int clusterSize, Context context) {
        //默认最多会缓存80张图片作为聚合显示元素图片,根据自己显示需求和app使用内存情况,可以修改数量
        mLruCache = new LruCache<Integer, BitmapDescriptor>(80) {
            protected void entryRemoved(boolean evicted, Integer key, BitmapDescriptor oldValue, BitmapDescriptor newValue) {
                oldValue.getBitmap().recycle();
            }
        };
        if (clusterItems != null) {
            mClusterItems = clusterItems;
        } else {
            mClusterItems = new ArrayList<ClusterItem>();
        }

        this.mAMap = amap;

        mContext = context;
        mClusters = new ArrayList<Cluster>();
        mClusterSize = clusterSize;
        mPXInMeters = mAMap.getScalePerPixel();
        mClusterDistance = mPXInMeters * mClusterSize;
        amap.setOnCameraChangeListener(this);
        amap.setOnMarkerClickListener(this);
        amap.setOnMapClickListener(this);
        amap.setInfoWindowAdapter(this);
        initThreadHandler();
        assignClusters();
    }

    public void setClusterItems(List<ClusterItem> clusterItems) {
        mClusterItems.addAll(clusterItems);
        mPXInMeters = mAMap.getScalePerPixel();
        mClusterDistance = mPXInMeters * mClusterSize;
        assignClusters();
    }

    public void setOnMapChagnedListener(MapChangedListener listenr) {
        this.mapChangedListener = listenr;
    }

    /**
     * 设置聚合点的点击事件
     *
     * @param clusterClickListener
     */
    public void setOnClusterClickListener(ClusterClickListener clusterClickListener) {
        mClusterClickListener = clusterClickListener;
    }

    /**
     * 添加一个聚合点
     *
     * @param item
     */
    public void addClusterItem(ClusterItem item) {
        Message message = Message.obtain();
        message.what = SignClusterHandler.CALCULATE_SINGLE_CLUSTER;
        message.obj = item;
        mSignClusterHandler.sendMessage(message);
    }

    /**
     * 设置聚合元素的渲染样式，不设置则默认为气泡加数字形式进行渲染
     *
     * @param render
     */
    public void setClusterRenderer(ClusterRender render) {
        mClusterRender = render;
    }

    public void onDestroy() {
        mIsCanceled = true;
        mSignClusterHandler.removeCallbacksAndMessages(null);
        mMarkerhandler.removeCallbacksAndMessages(null);
        mSignClusterThread.quit();
        mMarkerHandlerThread.quit();
        for (Marker marker : mAddMarkers) {
            marker.remove();
        }
        mAddMarkers.clear();
        if (currentMarker != null)
            currentMarker.remove();
        mLruCache.evictAll();
    }

    //初始化Handler
    private void initThreadHandler() {
        mMarkerHandlerThread.start();
        mSignClusterThread.start();
        mMarkerhandler = new MarkerHandler(mMarkerHandlerThread.getLooper());
        mSignClusterHandler = new SignClusterHandler(mSignClusterThread.getLooper());
    }

    @Override
    public void onCameraChange(CameraPosition arg0) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition arg0) {
        mPXInMeters = mAMap.getScalePerPixel();
        mClusterDistance = mPXInMeters * mClusterSize;
        assignClusters();

        if (this.mapChangedListener != null) {
            this.mapChangedListener.onCameraChangeFinish(arg0);
        }
    }

    //点击事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        Cluster cluster = (Cluster) marker.getObject();
        if (cluster != null) {
            if (cluster.getClusterItems().size() == 1) {
                // 只有一个点，点击显示详情的InfoWindow
                currentMarker = marker;
                currentMarker.showInfoWindow();
            }
            if (mClusterClickListener != null) {
                mClusterClickListener.onClick(marker, cluster.getClusterItems());
            }
        }
        return true;
    }

    /**
     * 关闭InfoWindow
     */
    void closeInfoWindow() {
        if (currentMarker != null && currentMarker.isInfoWindowShown()) {
            currentMarker.hideInfoWindow();
//            currentMarker = null;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = LayoutInflater.from(mContext).inflate(R.layout.marker_infowindow, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoWindow = LayoutInflater.from(mContext).inflate(R.layout.marker_infowindow, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    void render(Marker marker, View view) {
        List deviceDataList = new ArrayList<>();
        deviceDataList.add("1111111111111111111111111111111111111111111111111111111");
        deviceDataList.add(marker.getTitle());
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");
        deviceDataList.add("1");

        InfoWindowAdapter infoWindowAdapter = new InfoWindowAdapter(mContext, deviceDataList);
        RecyclerView recyclerView = view.findViewById(R.id.infowindow_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        recyclerView.setAdapter(infoWindowAdapter);

        view.findViewById(R.id.infowindow_detail_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.sendBroadcast(new Intent(MainActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.INFOWINDOW_CLICK, true)
                        .putExtra(Contant.BroadcastKey.POSITION, 0)
                        // TODO 设备对象传递
                        .putExtra(Contant.BroadcastKey.BEAN, new UserBean()));
            }
        });

        view.findViewById(R.id.infowindow_position_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.sendBroadcast(new Intent(MainActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.INFOWINDOW_CLICK, true)
                        .putExtra(Contant.BroadcastKey.POSITION, 1)
                        // TODO 设备对象传递
                        .putExtra(Contant.BroadcastKey.BEAN, new UserBean()));
            }
        });

        view.findViewById(R.id.infowindow_trace_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.sendBroadcast(new Intent(MainActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.INFOWINDOW_CLICK, true)
                        .putExtra(Contant.BroadcastKey.POSITION, 2)
                        // TODO 设备对象传递
                        .putExtra(Contant.BroadcastKey.BEAN, new UserBean()));
            }
        });

        view.findViewById(R.id.infowindow_setting_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.sendBroadcast(new Intent(MainActivity_.class.getName())
                        .putExtra(Contant.BroadcastKey.INFOWINDOW_CLICK, true)
                        .putExtra(Contant.BroadcastKey.POSITION, 3)
                        // TODO 设备对象传递
                        .putExtra(Contant.BroadcastKey.BEAN, new UserBean()));
            }
        });

    }

    @Override
    public void onMapClick(LatLng latLng) {
        closeInfoWindow();
    }


    /**
     * 将聚合元素添加至地图上
     */
    private void addClusterToMap(List<Cluster> clusters) {
        ArrayList<Marker> removeMarkers = new ArrayList<>();
        removeMarkers.addAll(mAddMarkers);
        if (removeMarkers.contains(currentMarker) && currentMarker.isInfoWindowShown()) {
            // 显示InfoWindow的Marker不参与点聚合，继续显示InfoWindow,不移除
            removeMarkers.remove(currentMarker);
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        MyAnimationListener myAnimationListener = new MyAnimationListener(removeMarkers);
        for (Marker marker : removeMarkers) {
            marker.setAnimation(alphaAnimation);
            marker.setAnimationListener(myAnimationListener);
            marker.startAnimation();
        }
        mAddMarkers.clear();

        for (Cluster cluster : clusters) {
            addSingleClusterToMap(cluster);
        }

    }

    private AlphaAnimation mADDAnimation = new AlphaAnimation(0, 1);

    /**
     * 将单个聚合元素添加至地图显示
     *
     * @param cluster
     */
    private void addSingleClusterToMap(Cluster cluster) {
        LatLng latlng = cluster.getCenterLatLng();
        MarkerOptions markerOptions = new MarkerOptions();

        int sum = cluster.getClusterCount();

        if (sum > 1) {
            // 聚合显示
            markerOptions.anchor(0.5f, 0.5f).icon(getBitmapForCluster(cluster)).position(latlng);
        } else {
            // marker详情展示
            View markerView = View.inflate(mContext, R.layout.marker_layout, null);
            ImageView statusImg = markerView.findViewById(R.id.marker_img);
            TextView deviceNumTV = markerView.findViewById(R.id.marker_title_tv);

            int index = 1;
            GradientDrawable drawable1 = new GradientDrawable();
            int strokeColor = Color.parseColor("#616161");
            int fillColor = Color.parseColor("#838383");
            int resourceId = R.mipmap.car_gray;
            String title = cluster.getClusterItems().get(0).getTitle();
            switch (index) {
                case 0:
                    // 离线
                    break;
                case 1:
                    // 长期未行驶
                    strokeColor = Color.parseColor("#D83806");
                    fillColor = Color.parseColor("#FF0000");
                    resourceId = R.mipmap.car_red;
                    break;
                case 2:
                    // 正常
                    strokeColor = Color.parseColor("#03A964");
                    fillColor = Color.parseColor("#45BE37");
                    resourceId = R.mipmap.car_green;
                    break;
            }
            drawable1.setColor(fillColor);
            drawable1.setStroke(Util.dp2px(mContext, 1), strokeColor);
            drawable1.setShape(GradientDrawable.RECTANGLE);
            drawable1.setCornerRadius(10);
            statusImg.setBackgroundResource(resourceId);
            deviceNumTV.setText(title);
            deviceNumTV.setBackgroundDrawable(drawable1);

            Bitmap bitmap = MapUtil.convertViewToBitmap(markerView);
            markerOptions.anchor(25f / bitmap.getWidth(), 1)
                    .position(latlng)
                    .title(cluster.getClusterItems().get(0).getTitle())
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }
        Marker marker = mAMap.addMarker(markerOptions);
        marker.setAnimation(mADDAnimation);
        marker.setObject(cluster);

        marker.startAnimation();
        cluster.setMarker(marker);
        mAddMarkers.add(marker);
    }

    private void calculateClusters() {
        mIsCanceled = false;
        mClusters.clear();
        LatLngBounds visibleBounds = mAMap.getProjection().getVisibleRegion().latLngBounds;
        for (ClusterItem clusterItem : mClusterItems) {
            if (mIsCanceled) {
                return;
            }
            if (currentMarker != null && currentMarker.isInfoWindowShown()) {
                Cluster cluster = (Cluster) currentMarker.getObject();
                if (cluster != null && cluster.getClusterCount() == 1) {
                    if (cluster.getClusterItems().get(0).equals(clusterItem)) {
                        continue;
                    }
                }
            }

            if (currentMarker != null && !currentMarker.isInfoWindowShown()) {
                currentMarker.remove();
            }

            // 第一个点作为聚合的中心点
            LatLng latlng = clusterItem.getPosition();
            if (visibleBounds.contains(latlng)) {
                Cluster cluster = getCluster(latlng, mClusters);
                if (cluster != null) {
                    cluster.addClusterItem(clusterItem);
                } else {
                    cluster = new Cluster(latlng);
                    mClusters.add(cluster);
                    cluster.addClusterItem(clusterItem);
                }
            }
        }

        //复制一份数据，规避同步
        List<Cluster> clusters = new ArrayList<Cluster>();
        clusters.addAll(mClusters);

        Message message = Message.obtain();
        message.what = MarkerHandler.ADD_CLUSTER_LIST;
        message.obj = clusters;
        if (mIsCanceled) {
            return;
        }
        mMarkerhandler.sendMessage(message);
    }

    /**
     * 对点进行聚合
     */
    private void assignClusters() {
        mIsCanceled = true;
        mSignClusterHandler.removeMessages(SignClusterHandler.CALCULATE_CLUSTER);
        mSignClusterHandler.sendEmptyMessage(SignClusterHandler.CALCULATE_CLUSTER);
    }

    /**
     * 在已有的聚合基础上，对添加的单个元素进行聚合
     *
     * @param clusterItem
     */
    private void calculateSingleCluster(ClusterItem clusterItem) {
        LatLngBounds visibleBounds = mAMap.getProjection().getVisibleRegion().latLngBounds;
        LatLng latlng = clusterItem.getPosition();
        if (!visibleBounds.contains(latlng)) {
            return;
        }
        Cluster cluster = getCluster(latlng, mClusters);
        if (cluster != null) {
            cluster.addClusterItem(clusterItem);
            Message message = Message.obtain();
            message.what = MarkerHandler.UPDATE_SINGLE_CLUSTER;

            message.obj = cluster;
            mMarkerhandler.removeMessages(MarkerHandler.UPDATE_SINGLE_CLUSTER);
            mMarkerhandler.sendMessageDelayed(message, 5);
        } else {
            cluster = new Cluster(latlng);
            mClusters.add(cluster);
            cluster.addClusterItem(clusterItem);
            Message message = Message.obtain();
            message.what = MarkerHandler.ADD_SINGLE_CLUSTER;
            message.obj = cluster;
            mMarkerhandler.sendMessage(message);
        }
    }

    /**
     * 根据一个点获取是否可以依附的聚合点，没有则返回null
     *
     * @param latLng
     * @return
     */
    private Cluster getCluster(LatLng latLng, List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            LatLng clusterCenterPoint = cluster.getCenterLatLng();
            double distance = AMapUtils.calculateLineDistance(latLng, clusterCenterPoint);
            if (distance < mClusterDistance && mAMap.getCameraPosition().zoom < 19) {
                return cluster;
            }
        }
        return null;
    }

    /**
     * 获取每个聚合点的绘制样式
     */
    private BitmapDescriptor getBitmapForCluster(Cluster cluster) {
        int num = cluster.getClusterCount();
        BitmapDescriptor bitmapDescriptor = mLruCache.get(num);
        if (bitmapDescriptor == null) {
            TextView textView = new TextView(mContext);
            String title = String.valueOf(num);
            textView.setText(title);
            int padding = Util.dp2px(mContext, 5);
            textView.setPadding(padding, padding, padding, padding);
            textView.setMinWidth(Util.dp2px(mContext, 30));
            textView.setMinHeight(Util.dp2px(mContext, 30));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            if (mClusterRender != null && mClusterRender.getDrawable(cluster) != null) {
                textView.setBackgroundDrawable(mClusterRender.getDrawable(cluster));
            } else {
                textView.setBackgroundResource(R.mipmap.ic_launcher_round);
            }
            bitmapDescriptor = BitmapDescriptorFactory.fromView(textView);
            mLruCache.put(num, bitmapDescriptor);
        }
        return bitmapDescriptor;
    }

    /**
     * 更新已加入地图聚合点的样式
     */
    private void updateCluster(Cluster cluster) {
        Marker marker = cluster.getMarker();
        marker.setIcon(getBitmapForCluster(cluster));
    }

//-----------------------辅助内部类用---------------------------------------------

    /**
     * marker渐变动画，动画结束后将Marker删除
     */
    class MyAnimationListener implements Animation.AnimationListener {
        private List<Marker> mRemoveMarkers;

        MyAnimationListener(List<Marker> removeMarkers) {
            mRemoveMarkers = removeMarkers;
        }

        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationEnd() {
            for (Marker marker : mRemoveMarkers) {
                marker.remove();
            }
            mRemoveMarkers.clear();
        }
    }

    /**
     * 处理market添加，更新等操作
     */
    class MarkerHandler extends Handler {

        static final int ADD_CLUSTER_LIST = 0;

        static final int ADD_SINGLE_CLUSTER = 1;

        static final int UPDATE_SINGLE_CLUSTER = 2;

        MarkerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {

            switch (message.what) {
                case ADD_CLUSTER_LIST:
                    List<Cluster> clusters = (List<Cluster>) message.obj;
                    addClusterToMap(clusters);
                    break;
                case ADD_SINGLE_CLUSTER:
                    Cluster cluster = (Cluster) message.obj;
                    addSingleClusterToMap(cluster);
                    break;
                case UPDATE_SINGLE_CLUSTER:
                    Cluster updateCluster = (Cluster) message.obj;
                    updateCluster(updateCluster);
                    break;
            }
        }
    }

    /**
     * 处理聚合点算法线程
     */
    class SignClusterHandler extends Handler {
        static final int CALCULATE_CLUSTER = 0;
        static final int CALCULATE_SINGLE_CLUSTER = 1;

        SignClusterHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case CALCULATE_CLUSTER:
                    calculateClusters();
                    break;
                case CALCULATE_SINGLE_CLUSTER:
                    ClusterItem item = (ClusterItem) message.obj;
                    mClusterItems.add(item);
                    calculateSingleCluster(item);
                    break;
            }
        }
    }
}