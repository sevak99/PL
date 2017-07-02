package com.abrahamyan.pl.io.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.db.entity.ProductResponse;
import com.abrahamyan.pl.io.bus.BusProvider;
import com.abrahamyan.pl.io.rest.HttpRequestManager;
import com.abrahamyan.pl.io.rest.HttpResponseUtil;
import com.abrahamyan.pl.ui.fragment.ProductListFragment;
import com.abrahamyan.pl.util.Constant;
import com.google.gson.Gson;

import java.net.HttpURLConnection;

public class PLIntentService extends IntentService {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = PLIntentService.class.getSimpleName();

    private class Extra {
        static final String URL = "URL";
        static final String POST_ENTITY = "POST_ENTITY";
        static final String SUBSCRIBER = "SUBSCRIBER";
        static final String REQUEST_TYPE = "REQUEST_TYPE";
    }

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public PLIntentService() {
        super(PLIntentService.class.getName());
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Start/stop commands
    // ===========================================================

    /**
     * @param url         - calling api url
     * @param requestType - string constant that helps us to distinguish what request it is
     * @param postEntity  - POST request entity (json string that must be sent on server)
     */

    public static void start(Context context, String url, String postEntity, int requestType) {
        Intent intent = new Intent(context, PLIntentService.class);
        intent.putExtra(Extra.URL, url);
        intent.putExtra(Extra.REQUEST_TYPE, requestType);
        intent.putExtra(Extra.POST_ENTITY, postEntity);
        context.startService(intent);
    }

    public static void start(Context context, String url, int requestType) {
        Intent intent = new Intent(context, PLIntentService.class);
        intent.putExtra(Extra.URL, url);
        intent.putExtra(Extra.REQUEST_TYPE, requestType);
        context.startService(intent);
    }

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getExtras().getString(Extra.URL);
        String data = intent.getExtras().getString(Extra.POST_ENTITY);
        int requestType = intent.getExtras().getInt(Extra.REQUEST_TYPE);
        Log.i(LOG_TAG, requestType + Constant.Symbol.SPACE + url);
        HttpURLConnection connection;

        switch (requestType) {
            case HttpRequestManager.RequestType.PRODUCT_LIST:
                connection = HttpRequestManager.executeRequest(
                        url,
                        HttpRequestManager.RequestMethod.GET,
                        data
                );

                String jsonList = HttpResponseUtil.parseResponse(connection);

                if(jsonList != null) {
                    Log.d(LOG_TAG, jsonList);
                    ProductResponse productResponse = (new Gson()).fromJson(jsonList, ProductResponse.class);
//                    ArrayList<Product> products = productResponse.getProducts();
//                    BusProvider.getInstance().post(products);
                    Intent intent1 = new Intent(ProductListFragment.BROADCAST_ACTION);
                    intent1.putExtra(ProductListFragment.NAME, productResponse);
                    sendBroadcast(intent1);
                } else {

                }
                break;

            case HttpRequestManager.RequestType.PRODUCT_ITEM:
                connection = HttpRequestManager.executeRequest(
                        url,
                        HttpRequestManager.RequestMethod.GET,
                        data
                );

                String jsonItem = HttpResponseUtil.parseResponse(connection);

                if(jsonItem != null) {
                    Log.d(LOG_TAG, jsonItem);
                    Product product = (new Gson()).fromJson(jsonItem, Product.class);
                    BusProvider.getInstance().post(product);

                } else {

                }
                break;
        }

    }

    private void sendNotification(String status, String message) {
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Util
    // ===========================================================
}