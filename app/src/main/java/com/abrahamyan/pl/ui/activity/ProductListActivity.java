package com.abrahamyan.pl.ui.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.db.cursor.CursorReader;
import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.db.handler.PlAsyncQueryHandler;
import com.abrahamyan.pl.io.bus.BusProvider;
import com.abrahamyan.pl.io.bus.event.ApiEvent;
import com.abrahamyan.pl.io.rest.HttpRequestManager;
import com.abrahamyan.pl.io.service.PLIntentService;
import com.abrahamyan.pl.ui.adapter.TabFragmentAdapter;
import com.abrahamyan.pl.ui.fragment.ProductFragment;
import com.abrahamyan.pl.util.Constant;
import com.abrahamyan.pl.util.NetworkUtil;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class ProductListActivity extends BaseActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener, PlAsyncQueryHandler.AsyncQueryListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = ProductListActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private ViewPager mViewPager;
    private ArrayList<Product> mProductArrayList;
    private TabFragmentAdapter mFragmentAdapter;
    private PlAsyncQueryHandler mPlAsyncQueryHandler;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.register(this);
        findViews();
        init();
        setListeners();

        if (NetworkUtil.getInstance().isConnected(this)) {
            PLIntentService.start(
                    this,
                    Constant.API.PRODUCT_LIST,
                    HttpRequestManager.RequestType.PRODUCT_LIST
            );
        } else {
            mPlAsyncQueryHandler.getProducts();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_product_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Subscribe
    public void onEventReceived(ApiEvent<Object> apiEvent) {
        if (apiEvent.getEventType() == ApiEvent.EventType.PRODUCT_LIST_LOADED) {
            if (!apiEvent.isSuccess()) {
                Toast.makeText(this, R.string.msg_some_error, Toast.LENGTH_SHORT).show();
            }
            mPlAsyncQueryHandler.getProducts();
        }

    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        switch (token) {
            case PlAsyncQueryHandler.QueryToken.GET_PRODUCTS:
                mProductArrayList = CursorReader.parseProducts(cursor);
                if (mProductArrayList.size() != 0) {
                    setupTabs(mProductArrayList);
                    customizeActionBar();
                    if (!mProductArrayList.get(0).isUser()) {
                        loadProduct(mProductArrayList.get(0).getId());
                    }
                } else {

                }
        }
    }

    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {
    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {
    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setActionBarTitle(String.valueOf(mFragmentAdapter.getPageTitle(position)));
        if (!mProductArrayList.get(position).isUser()) {
            loadProduct(mProductArrayList.get(position).getId());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
        mViewPager.addOnPageChangeListener(this);
    }

    private void findViews() {
        mViewPager = (ViewPager) findViewById(R.id.vp_product_list);
    }

    private void init() {
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(getApplicationContext(), this);
    }

    private void customizeActionBar() {
        setActionBarTitle(mProductArrayList.get(0).getName());
    }

    private void setupTabs(ArrayList<Product> productArrayList) {
        if (mViewPager != null && getTabLayout() != null) {
            mFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());

            for (Product product : productArrayList) {
                mFragmentAdapter.addFragment(ProductFragment.newInstance(product),
                        product.getName());
            }

            mViewPager.setAdapter(mFragmentAdapter);
            getTabLayout().setupWithViewPager(mViewPager);
        }
    }

    private void loadProduct(long productId) {
        if (NetworkUtil.getInstance().isConnected(this)) {
            PLIntentService.start(
                    this,
                    Constant.API.PRODUCT_ITEM + String.valueOf(productId)
                            + Constant.API.PRODUCT_ITEM_POSTFIX,
                    HttpRequestManager.RequestType.PRODUCT_ITEM
            );
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}