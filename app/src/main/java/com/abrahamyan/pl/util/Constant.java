package com.abrahamyan.pl.util;

/**
 * Created by SEVAK on 22.06.2017.
 */

public class Constant {
    public class Action  {
        public static final String ACTION_UPLOAD = "ACTION_UPLOAD";
    }

    public class Argument {
        public static final String ARGUMENT_DATA = "ARGUMENT_DATA";
    }

    public class Extra {
        public static final String EXTRA_USER = "EXTRA_USER";
        public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";
    }

    public static class RequestCode {
        public static final int ADD_PRODUCT_ACTIVITY = 1;
    }

    public class Bundle {
    }

    public class Symbol {
        public static final String ASTERISK = "*";
        public static final String NEW_LINE = "\n";
        public static final String SPACE = " ";
        public static final String NULL = "";
        public static final String COLON = ":";
        public static final String COMMA = ",";
        public static final String SLASH = "/";
        public static final String DOT = ".";
        public static final String UNDERLINE = "_";
        public static final String DASH = "-";
        public static final String AT = "@";
        public static final String AMPERSAND = "&";
    }

    public class Boolean {
        public static final String TRUE = "true";
        public static final String FALSE = "false";
    }

    public class Util {
        public static final int QUALITY = 100;
        public static final String ANDROID_DATA_ROOT = "Android/data/";
        public static final String SD = "file://";
        public static final String SHA = "SHA";
        public static final String UTF_8 = "UTF-8";
    }

    public class Identifier {
        public static final String ID = "id";
        public static final String ANDROID = "android.support";
        public static final String ALERT_TITLE = "alertTitle";
    }

    public class BuildType {
        public static final String RELEASE = "release";
        public static final String DEBUG = "debug";
    }

    public class RequestMode {
        public static final int INITIAL = 1;
        public static final int UPDATE = 2;
        public static final int NEXT = 3;
        public static final int NONE = 4;
        public static final int PREVIOUS = 5;
    }

    public class MapType {
        public static final int NORMAL_MAP_TYPE = 1;
        public static final int SATELLITE_MAP_TYPE = 2;
    }

    public class Build {
        public static final String RELEASE = "release";
        public static final String DEBUG = "debug";
    }

    public class API {
        public static final String HOST = "https://tigransarkisian.github.io";
        public static final String PRODUCT_LIST = HOST + "/aca_pl/products.json";
        public static final String PRODUCT_ITEM = HOST + "/aca_pl/products/"; // id
        public static final String PRODUCT_ITEM_POSTFIX = "/details.json";
    }

    public class Json {
        public static final String PRODUCTS = "products";
        public static final String ID = "product_id";
        public static final String NAME = "name";
        public static final String PRICE = "price";
        public static final String IMAGE = "image";
        public static final String DESCRIPTION = "description";
    }

    public class Image {
        public static final String DEFULT_IMAGE = "https://s3-eu-west-1.amazonaws.com/developer-application-test/images/3.jpg";
    }
}
