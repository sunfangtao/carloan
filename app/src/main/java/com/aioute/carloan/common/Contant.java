package com.aioute.carloan.common;

import java.io.File;

/**
 * Created by Administrator on 2017/12/19.
 */

public class Contant {

    public static String IP_PORT = VarConstant.IP_PORT;
    public static int PASSWORD_MIN_LENGTH = 4;
    public static int TAKE_TASK_PHOTO_MAX_COUNT = 3;
    public static int TAKE_TASK_PHOTO_COLUMN = 3;
    public static int MAP_MAX_ZOOM_LEVEL = 19;

    public static final class HttpType {
        // public static final String LOGIN_TYPE = "logintype";
    }

    public static final class HttpUrl {
        // public static final String LOGIN_URL = IP_PORT + "ApplicationListenerManager/loginController/login";
    }

    public static final class FilePath {
        public static final String PHOTO_PATH = "photo" + File.separator;
    }

    public static final class ShareKey {
        // public static final String LAST_LOGIN_USER_ACCOUND = "lastLoginUserAccount";
    }

    public static final class BroadcastKey {
        public static final String SINGLEDEVICE_MENU_REFRESH = "singleDeviceMenuRefresh";
        public static final String SINGLEDEVICE_MENU_TIME = "singleDeviceMenuTime";

        public static final String PHOTOGRAPH_ITEM_CONFIRM = "takeTaskPhotoItemConfirm";
        public static final String PHOTOGRAPH_ITEM_TAKEPHOTO = "takeTaskPhotoItemTakePhoto";

        public static final String TAKETASKPHOTO_ITEM_PHOTO = "takeTaskPhotoItemPhoto";
        public static final String TAKETASKPHOTO_ITEM_SHOW = "takeTaskPhotoItemShow";
        public static final String TAKETASKPHOTO_ITEM_CANCEL = "takeTaskPhotoItemCancel";

        public static final String TASK_ITEM_REMOVE = "taskItemRemove";
        public static final String TASK_ITEM_PHOTO = "taskItemPhoto";
        public static final String TASK_ITEM_NAV = "taskItemNav";

        public static final String INFOWINDOW_CLICK = "infoWindowClick";

        public static final String POSITION = "position";
        public static final String BEAN = "bean";
    }
}
