package com.abrahamyan.pl.util;

/**
 * Created by SEVAK on 22.06.2017.
 */

public class Constant {

    public class Argument {
        public static final String ARGUMENT_DATA = "ARGUMENT_DATA";
        public static final String ARGUMENT_PRODUCT = "ARGUMENT_PRODUCT";
    }

    public class Extra {
        public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";
        public static final String EXTRA_NOTIF_DATA = "EXTRA_NOTIF_DATA";
        public static final String EXTRA_NOTIF_TYPE = "EXTRA_NOTIF_TYPE";
    }

    public class NotifType {
        public static final int ADD = 100;
        public static final int UPDATE = 101;
    }

    public class Bundle {
        public static final String TITLE = "TITLE";
        public static final String DESCRIPTION = "DESCRIPTION";
        public static final String PRICE = "PRICE";
        public static final String FAVORITE = "FAVORITE";
    }

    public class Util {
        public static final String UTF_8 = "UTF-8";
    }

    public class API {
        public static final String HOST = "https://tigransarkisian.github.io";
        public static final String PRODUCT_LIST = HOST + "/aca_pl/products.json";
        public static final String PRODUCT_ITEM = HOST + "/aca_pl/products/"; // id
        public static final String PRODUCT_ITEM_POSTFIX = "/details.json";
        public static final String DEFULT_IMAGE = "https://s3-eu-west-1.amazonaws.com/developer-application-test/images/3.jpg";
        public static final String URL_ACA = "http://aca.am/";
    }

    public class Json {
        public static final String PRODUCTS = "products";
        public static final String ID = "product_id";
        public static final String NAME = "name";
        public static final String PRICE = "price";
        public static final String IMAGE = "image";
        public static final String DESCRIPTION = "description";
    }

    public class RequestCode {
        public static final int ADD_PRODUCT_ACTIVITY = 100;
        public static final int CAMERA = 101;
    }
}
