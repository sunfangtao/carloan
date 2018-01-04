package com.aioute.carloan.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.os.Parcelable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.SparseArray;

import com.aioute.carloan.R;

import cn.sft.privateutil.CustomSavedState;
import cn.sft.taghandler.FontSizeTagHandler;
import cn.sft.util.Util;

/**
 * Created by Administrator on 2017/12/27.
 */

public class WarnCarCountTextView extends android.support.v7.widget.AppCompatTextView {

    private int count;
    private int total;

    public WarnCarCountTextView(Context context) {
        super(context);
    }

    public WarnCarCountTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WarnCarCountTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCount(int count, int total) {
        if (count < 0 || total < 0) {
            throw new IllegalArgumentException("数量错误！");
        }

        this.count = count;
        this.total = total;

        int outerWidth = Util.dp2px(getContext(), 2);
        int innerWidth = Util.dp2px(getContext(), 15) + outerWidth;

        GradientDrawable drawable1 = new GradientDrawable();
        drawable1.setColor(Color.parseColor("#C4E4F1"));
        drawable1.setStroke(outerWidth, Color.parseColor("#EAECEB"));
        drawable1.setShape(GradientDrawable.OVAL);

        float sweepAngle = 0;
        if (total > 0)
            sweepAngle = 360f * count / total;
        ArcShape arcShape = new ArcShape(-90, sweepAngle);
        ShapeDrawable drawable2 = new ShapeDrawable(arcShape);
        drawable2.getPaint().setColor(getResources().getColor(R.color.colorPrimary));
        drawable2.getPaint().setStyle(Paint.Style.FILL);
        drawable2.setPadding(outerWidth, outerWidth, outerWidth, outerWidth);

        GradientDrawable drawable3 = new GradientDrawable();
        drawable3.setColor(getResources().getColor(R.color.colorWhite));
        drawable3.setStroke(outerWidth, Color.parseColor("#EAECEB"));
        drawable3.setShape(GradientDrawable.OVAL);

        LayerDrawable ld = new LayerDrawable(new Drawable[]{drawable1, drawable2, drawable3});
        ld.setLayerInset(1, outerWidth, outerWidth, outerWidth, outerWidth);
        ld.setLayerInset(2, innerWidth, innerWidth, innerWidth, innerWidth);
        setBackgroundDrawable(ld);

        String text = "<customfont size='20sp' color='#01A9F2'>" + count
                + "</customfont><br><customfont size='12sp' color='#404040'>报警车辆</customfont>";

        CharSequence charSequence = Html.fromHtml("<default>" + text.toString() + "</default>",
                null, new FontSizeTagHandler(getContext()));
        setText(charSequence);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void onRestoreInstanceState(Parcelable state) {
        CustomSavedState ss = (CustomSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        int index = 1;
        this.count = ((Integer) ss.childrenStates.get(index++)).intValue();
        this.total = ((Integer) ss.childrenStates.get(index++)).intValue();
        setCount(count, total);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        CustomSavedState ss = new CustomSavedState(superState);
        ss.childrenStates = new SparseArray();
        int index = 1;
        ss.childrenStates.put(index++, count);
        ss.childrenStates.put(index++, total);
        return ss;
    }
}
