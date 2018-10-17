package com.abrahamyan.pl.db.handler;

import android.content.Context;
import android.database.Cursor;

import com.abrahamyan.pl.db.PlDataBase;
import com.abrahamyan.pl.db.cursor.CursorReader;
import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.db.provider.UriBuilder;

import java.util.ArrayList;

public class PlQueryHandler {

    // ===========================================================
    // Constants
    // ===========================================================

    private final static String LOG_TAG = PlQueryHandler.class.getSimpleName();

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
     * PRODUCT METHODS
     *************************************************************/

    public synchronized static void addProduct(Context context, Product product) {
        context.getContentResolver().insert(
                UriBuilder.buildProductUri(),
                PlDataBase.composeValues(product, PlDataBase.ContentValuesType.PRODUCTS)
        );
    }

    public synchronized static void addProducts(Context context, ArrayList<Product> products) {
        context.getContentResolver().bulkInsert(
                UriBuilder.buildProductUri(),
                PlDataBase.composeValuesArray(products, PlDataBase.ContentValuesType.PRODUCTS)
        );
    }

    public synchronized static void updateProduct(Context context, Product product) {
        context.getContentResolver().update(
                UriBuilder.buildProductUri(),
                PlDataBase.composeValues(product, PlDataBase.ContentValuesType.PRODUCTS),
                PlDataBase.PRODUCT_ID + "=?",
                new String[]{String.valueOf(product.getId())}
        );
    }

    public synchronized static void updateProductDescription(Context context, Product product) {
        context.getContentResolver().update(
                UriBuilder.buildProductUri(),
                PlDataBase.composeValues(product, PlDataBase.ContentValuesType.DESCRIPTION),
                PlDataBase.PRODUCT_ID + "=?",
                new String[]{String.valueOf(product.getId())}
        );
    }

    public synchronized static void updateProducts(Context context, ArrayList<Product> products) {
        for (Product product : products) {
            context.getContentResolver().update(
                    UriBuilder.buildProductUri(product.getId()),
                    PlDataBase.composeValues(product, PlDataBase.ContentValuesType.PRODUCTS),
                    null,
                    null
            );
        }
    }

    public synchronized static void updateProductsExceptFavorite(Context context, ArrayList<Product> products) {
        for (Product product : products) {
            context.getContentResolver().update(
                    UriBuilder.buildProductUri(product.getId()),
                    PlDataBase.composeValues(product, PlDataBase.ContentValuesType.ALL_EXCEPT_FAVORITE),
                    null,
                    null
            );
        }
    }

    public synchronized static Product getProduct(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(
                UriBuilder.buildProductUri(),
                PlDataBase.Projection.PRODUCT,
                PlDataBase.PRODUCT_ID + "=?",
                new String[]{String.valueOf(id)},
                null
        );
        return CursorReader.parseProduct(cursor);
    }

    public synchronized static ArrayList<Product> getProducts(Context context) {
        Cursor cursor = context.getContentResolver().query(
                UriBuilder.buildProductUri(),
                PlDataBase.Projection.PRODUCT,
                null,
                null,
                null
        );
        return CursorReader.parseProducts(cursor);
    }

    public synchronized static void deleteProduct(Context context, Product product) {
        context.getContentResolver().delete(
                UriBuilder.buildProductUri(),
                PlDataBase.PRODUCT_ID + "=?",
                new String[]{String.valueOf(product.getId())}
        );
    }

    public synchronized static void deleteProducts(Context context) {
        context.getContentResolver().delete(
                UriBuilder.buildProductUri(),
                null,
                null
        );
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}