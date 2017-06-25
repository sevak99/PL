package com.abrahamyan.pl.io.rest;

import android.content.Context;
import android.os.Bundle;

import com.abrahamyan.pl.io.rest.entity.HttpConnection;
import com.abrahamyan.pl.io.rest.util.HttpResponseUtil;

public class HttpRequestManager {
    // ===========================================================
    // Constants
    // ===========================================================
    private static final String LOG_TAG = HttpRequestManager.class.getSimpleName();
    public class RequestType {
        public static final int PRODUCT_LIST = 1;
    }
    // ===========================================================
    // Fields
    // ===========================================================
    // ===========================================================
    // Constructors
    // ===========================================================
    // ===========================================================
    // Getter & Setter
    // ===========================================================
    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================
    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================
    // ===========================================================
    // Methods
    // ===========================================================
    /**
     * @param url           - api url
     * @param token         - pass authorization token if required, otherwise pass null
     * @param postEntity    - post request json entity) if required, otherwise pass null
     * @param requestMethod - post, put, delete, get or other
     */
    public static HttpConnection executeRequest(Context context, String requestMethod, String url,
                                                String token, String postEntity) {
        Bundle bundle = new Bundle();
        bundle.putString(RestHttpClient.BundleData.JSON_ENTITY, postEntity);
        bundle.putString(RestHttpClient.BundleData.TOKEN, token);
        HttpConnection httpConnection = null;
        switch (requestMethod) {
//            case RestHttpClient.RequestMethod.POST:
//                httpConnection = RestHttpClient.executePostRequest(context, url, bundle);
//                break;

            case RestHttpClient.RequestMethod.GET:
                httpConnection = RestHttpClient.executeGetRequest(context, url, bundle);
                break;

//            case RestHttpClient.RequestMethod.PATCH:
//                httpConnection = RestHttpClient.executePatchRequest(context, url, bundle);
//                break;

//            case RestHttpClient.RequestMethod.PUT:
//                httpConnection = RestHttpClient.executePutRequest(context, url, bundle);
//                break;

//            case RestHttpClient.RequestMethod.DELETE:
//                httpConnection = RestHttpClient.executeDeleteRequest(context, url, bundle);
//                break;
        }
        httpConnection = HttpResponseUtil.handleConnection(httpConnection);
        return httpConnection;
    }
    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}