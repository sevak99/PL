package com.abrahamyan.pl.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
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

    // ===========================================================
    // Fields
    // ===========================================================

    private EditText mEtProductTitle;
    private EditText mEtProductPrice;
    private EditText mEtProductDescription;
    private ImageView mIvProductImage;
    private Button mBtnProductAdd;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_item, menu);
        mMenuFavorite = menu.findItem(R.id.menu_product_favorite);

        return true;
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_product_add:
                if (mEtProductTitle.getText().length() == 0) {
                    Toast.makeText(this, R.string.msg_no_title, Toast.LENGTH_SHORT).show();
                    break;
                } else if (mEtProductPrice.getText().length() == 0) {
                    Toast.makeText(this, R.string.msg_no_price, Toast.LENGTH_SHORT).show();
                    break;
                }

                createProduct(
                        mEtProductTitle.getText().toString(),
                        Long.parseLong(mEtProductPrice.getText().toString()),
                        mEtProductDescription.getText().toString()
                );


                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage(R.string.msg_add_product)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPlAsyncQueryHandler.addProduct(mProduct);
                            }
                        })
                        .setNegativeButton(R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_product_favorite:
                if (mProduct.isFavorite()) {
                    mMenuFavorite.setIcon(R.drawable.ic_not_favorite);
                    mProduct.setFavorite(false);
                } else {
                    mMenuFavorite.setIcon(R.drawable.ic_favorite);
                    mProduct.setFavorite(true);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
    }

    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {
        Intent intent = new Intent();
        intent.putExtra(Constant.Extra.EXTRA_PRODUCT, mProduct);
        setResult(RESULT_OK, intent);
        finish();
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
        setActionBarTitle(getString(R.string.text_add_product));
    }

    private void init() {
        Glide.with(this)
                .load(Constant.Image.DEFULT_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvProductImage);

        mProduct = new Product();
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(getApplicationContext(), this);
    }

    private void createProduct(String name, long price, String description) {

        mProduct.setId(System.currentTimeMillis());
        mProduct.setName(name);
        mProduct.setPrice(price);
        mProduct.setFromUser(true);
        mProduct.setDescription(description);
        mProduct.setImage(Constant.Image.DEFULT_IMAGE);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}