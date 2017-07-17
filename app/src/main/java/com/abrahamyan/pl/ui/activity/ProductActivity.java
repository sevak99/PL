package com.abrahamyan.pl.ui.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.db.handler.PlAsyncQueryHandler;
import com.abrahamyan.pl.io.bus.BusProvider;
import com.abrahamyan.pl.io.bus.event.ApiEvent;
import com.abrahamyan.pl.io.rest.HttpRequestManager;
import com.abrahamyan.pl.io.service.PLIntentService;
import com.abrahamyan.pl.util.AppUtil;
import com.abrahamyan.pl.util.Constant;
import com.abrahamyan.pl.util.NetworkUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.eventbus.Subscribe;

public class ProductActivity extends BaseActivity
        implements PlAsyncQueryHandler.AsyncQueryListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = ProductActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private TextView mTvProductTitle;
    private TextView mTvProductPrice;
    private TextView mTvProductDescription;
    private EditText mEvProductTitle;
    private EditText mEvProductPrice;
    private EditText mEvProductDescription;
    private ImageView mIvProductImage;
    private LinearLayout mLlProductView;
    private LinearLayout mLlProductEdit;
    private MenuItem mMenuEdit;
    private MenuItem mMenuDone;
    private MenuItem mMenuFavorite;
    private Product mProduct;
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
        getData();
        init();
        customizeActionBar();

        if (!mProduct.isUser()) {
            loadProduct();
        }
    }

    @Override
    protected void onDestroy() {
        BusProvider.unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_product;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_item, menu);
        mMenuEdit = menu.findItem(R.id.menu_product_edit);
        mMenuDone = menu.findItem(R.id.menu_product_done);
        mMenuFavorite = menu.findItem(R.id.menu_product_favorite);

        if (mProduct.isUser()) {
            mMenuEdit.setVisible(true);
        }
        if (mProduct.isFavorite()) {
            mMenuFavorite.setIcon(R.drawable.ic_favorite);
        }
        return true;
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_product_edit:
                mMenuDone.setVisible(true);
                mMenuEdit.setVisible(false);
                openEditLayout(mProduct);
                return true;

            case R.id.menu_product_done:
                if (mEvProductTitle.getText().length() == 0) {
                    Toast.makeText(this, R.string.msg_edt_title_error, Toast.LENGTH_SHORT).show();
                } else if (mEvProductPrice.getText().length() == 0) {
                    Toast.makeText(this, R.string.msg_edt_price_error, Toast.LENGTH_SHORT).show();
                } else {
                    mMenuDone.setVisible(false);
                    mMenuEdit.setVisible(true);

                    updateProduct(
                            mEvProductTitle.getText().toString(),
                            Long.parseLong(mEvProductPrice.getText().toString()),
                            mEvProductDescription.getText().toString()
                    );
                    openViewLayout(mProduct);
                }
                return true;

            case R.id.menu_product_favorite:
                if (mProduct.isFavorite()) {
                    mMenuFavorite.setIcon(R.drawable.ic_not_favorite);
                    mProduct.setFavorite(false);
                } else {
                    mMenuFavorite.setIcon(R.drawable.ic_favorite);
                    mProduct.setFavorite(true);
                }
                mPlAsyncQueryHandler.updateProduct(mProduct);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Subscribe
    public void onEventReceived(ApiEvent<Object> apiEvent) {
        if (apiEvent.isSuccess()) {
            mProduct = (Product) apiEvent.getEventData();
            openViewLayout(mProduct);
        } else {
            Toast.makeText(this, "Something went wrong, please try again",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
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

    // ===========================================================
    // Methods
    // ===========================================================

    private void getData() {
        mProduct = getIntent().getParcelableExtra(Constant.Extra.EXTRA_PRODUCT);
    }

    private void findViews() {
        mIvProductImage = (ImageView) findViewById(R.id.iv_product_image);
        mLlProductView = (LinearLayout) findViewById(R.id.ll_product_not_editable);
        mTvProductTitle = (TextView) findViewById(R.id.tv_product_title);
        mTvProductPrice = (TextView) findViewById(R.id.tv_product_price);
        mTvProductDescription = (TextView) findViewById(R.id.tv_product_description);
        mLlProductEdit = (LinearLayout) findViewById(R.id.ll_product_editable);
        mEvProductTitle = (EditText) findViewById(R.id.ev_product_title);
        mEvProductPrice = (EditText) findViewById(R.id.ev_product_price);
        mEvProductDescription = (EditText) findViewById(R.id.ev_product_description);
    }

    private void customizeActionBar() {
        setActionBarTitle(getString(R.string.text_product));
    }

    private void init() {
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(ProductActivity.this, this);

        Glide.with(this)
                .load(mProduct.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvProductImage);
        openViewLayout(mProduct);
    }

    private void loadProduct() {
        if (NetworkUtil.getInstance().isConnected(this)) {
            PLIntentService.start(
                    this,
                    Constant.API.PRODUCT_ITEM + mProduct.getId() + Constant.API.PRODUCT_ITEM_POSTFIX,
                    HttpRequestManager.RequestType.PRODUCT_ITEM
            );

        }
    }

    private void updateProduct(String name, long price, String description) {
        mProduct.setName(name);
        mProduct.setPrice(price);
        mProduct.setDescription(description);

        mPlAsyncQueryHandler.updateProduct(mProduct);
    }

    private void openViewLayout(Product product) {
        AppUtil.closeKeyboard(this);

        mLlProductEdit.setVisibility(View.GONE);
        mLlProductView.setVisibility(View.VISIBLE);

        mTvProductTitle.setText(product.getName());
        mTvProductPrice.setText(String.valueOf(product.getPrice()));
        mTvProductDescription.setText(product.getDescription());
    }

    private void openEditLayout(Product product) {
        mLlProductView.setVisibility(View.GONE);
        mLlProductEdit.setVisibility(View.VISIBLE);

        mEvProductTitle.setText(product.getName());
        mEvProductPrice.setText(String.valueOf(product.getPrice()));
        mEvProductDescription.setText(product.getDescription());
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}