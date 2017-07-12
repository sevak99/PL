package com.abrahamyan.pl.ui.activity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.db.cursor.CursorReader;
import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.db.handler.PlAsyncQueryHandler;
import com.abrahamyan.pl.util.Constant;
import com.bumptech.glide.Glide;

public class ProductActivity extends BaseActivity
        implements View.OnClickListener, PlAsyncQueryHandler.AsyncQueryListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = ProductActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private ImageView mIvProductImage;
    private LinearLayout mLlProductNotEditable;
    private TextView mTvProductTitle;
    private TextView mTvProductPrice;
    private TextView mTvProductDescription;
    private LinearLayout mLlProductEditable;
    private EditText mEvProductTitle;
    private EditText mEvProductPrice;
    private EditText mEvProductDescription;
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
        findViews();
        init();
        setListeners();
        customizeActionBar();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_product;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_edit_product:
                int llId;
                llId = (mLlProductNotEditable.getVisibility() == View.VISIBLE) ?
                        mLlProductEditable.getId() : mLlProductNotEditable.getId();

                if(setData(llId)) {
                    int vis = mLlProductNotEditable.getVisibility();
                    mLlProductNotEditable.setVisibility(mLlProductEditable.getVisibility());
                    mLlProductEditable.setVisibility(vis);
                }

                break;

            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
//        super.onBackPressed();
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        switch (token) {
            case PlAsyncQueryHandler.QueryToken.GET_PRODUCT:
                mProduct = CursorReader.parseProduct(cursor);

                Log.d(LOG_TAG, mProduct.getName());

                Glide.with(this)
                        .load(mProduct.getImage())
                        .into(mIvProductImage);

                mTvProductTitle.setText(mProduct.getName());
                mTvProductPrice.setText(String.valueOf(mProduct.getPrice()));
                mTvProductDescription.setText(mProduct.getDescription());

                break;
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

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
    }

    private void findViews() {
        mIvProductImage = (ImageView)findViewById( R.id.iv_product_image );
        mLlProductNotEditable = (LinearLayout)findViewById( R.id.ll_product_not_editable );
        mTvProductTitle = (TextView)findViewById( R.id.tv_product_title );
        mTvProductPrice = (TextView)findViewById( R.id.tv_product_price );
        mTvProductDescription = (TextView)findViewById( R.id.tv_product_description );
        mLlProductEditable = (LinearLayout)findViewById( R.id.ll_product_editable );
        mEvProductTitle = (EditText)findViewById( R.id.ev_product_title );
        mEvProductPrice = (EditText)findViewById( R.id.ev_product_price );
        mEvProductDescription = (EditText)findViewById( R.id.ev_product_description );
    }

    private void customizeActionBar() {
        setActionBarTitle(getString(R.string.product_activity));
    }

    private void init() {
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(getApplicationContext(), this);

        long id = Long.parseLong(getIntent().getStringExtra(Constant.Extra.EXTRA_PRODUCT_ID));

        mPlAsyncQueryHandler.getProduct(id);
    }

    private boolean setData(int llId) {
        switch (llId) {
            case R.id.ll_product_editable:
                mEvProductTitle.setText(mProduct.getName());
                mEvProductPrice.setText(String.valueOf(mProduct.getPrice()));
                mEvProductDescription.setText(mProduct.getDescription());
                break;

            case R.id.ll_product_not_editable:
                if(mEvProductTitle.getText().length() == 0) {
                    Toast.makeText(this,R.string.msg_no_title, Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(mEvProductPrice.getText().length() == 0) {
                    Toast.makeText(this,R.string.msg_no_price, Toast.LENGTH_SHORT).show();
                    return false;
                }

                updateProduct(
                        mEvProductTitle.getText().toString(),
                        Integer.parseInt(mEvProductPrice.getText().toString()),
                        mEvProductDescription.getText().toString()
                );

                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                mTvProductTitle.setText(mProduct.getName());
                mTvProductPrice.setText(String.valueOf(mProduct.getPrice()));
                mTvProductDescription.setText(mProduct.getDescription());

                break;
        }

        return true;
    }

    private void updateProduct(String name, int price, String description) {
        mProduct.setName(name);
        mProduct.setPrice(price);
        mProduct.setDescription(description);

        mPlAsyncQueryHandler.updateProduct(mProduct);
    }



    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}