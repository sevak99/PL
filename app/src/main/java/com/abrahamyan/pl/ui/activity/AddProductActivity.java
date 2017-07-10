package com.abrahamyan.pl.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.db.handler.PlAsyncQueryHandler;
import com.abrahamyan.pl.util.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class AddProductActivity extends BaseActivity implements View.OnClickListener,
        PlAsyncQueryHandler.AsyncQueryListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = AddProductActivity.class.getSimpleName();
    public static final String KEY_TITLE = "com.abrahamyan.pl.ui.activity.AddProductActivity_Title";

    // ===========================================================
    // Fields
    // ===========================================================

    private PlAsyncQueryHandler mPlAsyncQueryHandler;
    private EditText mEtProductTitle;
    private EditText mEtProductPrice;
    private EditText mEtProductDescription;
    private ImageView mIvProductImage;
    private Button mBtnProductAdd;
    private Product mProduct;

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
        return R.layout.activity_add_product;
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
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_product_add:
                parseProduct();
                if (mProduct != null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setMessage(R.string.msg_add_product)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPlAsyncQueryHandler.addProduct(mProduct);
                                    Intent intent = new Intent();
                                    intent.putExtra(KEY_TITLE, mProduct);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.cancel, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
        }
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

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

    private void setListeners() {
        mBtnProductAdd.setOnClickListener(this);
    }

    private void findViews() {
        mIvProductImage = (ImageView) findViewById(R.id.iv_add_product_logo);
        mEtProductTitle = (EditText) findViewById(R.id.et_add_product_title);
        mEtProductPrice = (EditText) findViewById(R.id.et_add_product_price);
        mEtProductDescription = (EditText) findViewById(R.id.et_add_product_description);
        mBtnProductAdd = (Button) findViewById(R.id.btn_add_product_add);
    }

    private void customizeActionBar() {
        setActionBarTitle("Add Product");
    }

    private void init() {
        Glide.with(this)
                .load(Constant.Image.DEFULT_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvProductImage);

        mPlAsyncQueryHandler = new PlAsyncQueryHandler(getApplicationContext(), this);
    }

    private void parseProduct() {
        if(mEtProductTitle.getText().length() == 0) {
            Toast.makeText(this, R.string.msg_no_title, Toast.LENGTH_SHORT).show();
            return;
        }
        if(mEtProductPrice.getText().length() == 0) {
            Toast.makeText(this, R.string.msg_no_price, Toast.LENGTH_SHORT).show();
            return;
        }

        mProduct = new Product();

        mProduct.setId(String.valueOf(System.currentTimeMillis()));
        mProduct.setName(mEtProductTitle.getText().toString());
        mProduct.setPrice(Integer.parseInt(mEtProductPrice.getText().toString()));
        mProduct.setDescription(mEtProductDescription.getText().toString());
        mProduct.setImage(Constant.Image.DEFULT_IMAGE);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}