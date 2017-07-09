package com.abrahamyan.pl.io.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.db.entity.ProductResponse;
import com.abrahamyan.pl.db.handler.PlQueryHandler;
import com.abrahamyan.pl.io.bus.BusProvider;
import com.abrahamyan.pl.io.rest.HttpRequestManager;
import com.abrahamyan.pl.io.rest.HttpResponseUtil;
import com.abrahamyan.pl.util.Constant;
import com.abrahamyan.pl.util.NetworkUtil;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class PLIntentService extends IntentService {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = PLIntentService.class.getSimpleName();

    private class Extra {
        static final String URL = "URL";
        static final String POST_ENTITY = "POST_ENTITY";
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
                    ArrayList<Product> products = productResponse.getProducts();

                    PlQueryHandler.deleteProducts(this);

                    PlQueryHandler.addProducts(this, products);

                    BusProvider.getInstance().post(products);

                } else if(!NetworkUtil.getInstance().isConnected(this)){
                    BusProvider.getInstance().post(Constant.ERROR_MSG.NO_INTERNET);
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

                } else if(!NetworkUtil.getInstance().isConnected(this)){
                    BusProvider.getInstance().post(Constant.ERROR_MSG.NO_INTERNET);
                }
                break;
        }

    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void sendNotification(String status, String message) {
    }

    // ===========================================================
    // Util
    // ===========================================================
}