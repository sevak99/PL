package com.abrahamyan.pl.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.abrahamyan.pl.util.AppUtil;
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

    private boolean useCamera;
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

        if (savedInstanceState != null) {
            fillData(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(Constant.Bundle.TITLE, mEtProductTitle.getText().toString());
        savedInstanceState.putString(Constant.Bundle.PRICE, mEtProductPrice.getText().toString());
        savedInstanceState.putString(Constant.Bundle.DESCRIPTION, mEtProductDescription.getText().toString());
        savedInstanceState.putBoolean(Constant.Bundle.FAVORITE, mProduct.isFavorite());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_product;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_item, menu);
        mMenuFavorite = menu.findItem(R.id.menu_product_favorite);

        if (mProduct.isFavorite())
            mMenuFavorite.setIcon(R.drawable.ic_favorite);

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.RequestCode.CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                useCamera = false;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.RequestCode.CAMERA:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    mIvProductImage.setImageBitmap(photo);
                    break;
            }
        }
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_product_add:
                if (mEtProductTitle.getText().length() == 0) {
                    Toast.makeText(this, R.string.msg_edt_title_error, Toast.LENGTH_SHORT).show();
                    break;
                } else if (mEtProductPrice.getText().length() == 0) {
                    Toast.makeText(this, R.string.msg_edt_price_error, Toast.LENGTH_SHORT).show();
                    break;
                }

                createProduct(
                        mEtProductTitle.getText().toString(),
                        Long.parseLong(mEtProductPrice.getText().toString()),
                        mEtProductDescription.getText().toString(),
                        Constant.API.DEFULT_IMAGE
                );
                
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage(R.string.msg_dialog_add_product)
                        .setPositiveButton(R.string.text_btn_dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPlAsyncQueryHandler.addProduct(mProduct);
                            }
                        })
                        .setNegativeButton(R.string.text_btn_dialog_cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.iv_add_product_logo:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                Constant.RequestCode.CAMERA);
                    }
                } else if (useCamera) {
                    openCamera();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_product_favorite:
                if (mProduct.isFavorite()) {
                    mMenuFavorite.setIcon(R.drawable.ic_unfavorite);
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

        AppUtil.sendNotification(
                getApplicationContext(),
                MainActivity.class,
                getString(R.string.app_name),
                getString(R.string.notif_add) + " " + mProduct.getName(),
                mProduct.getName(),
                Constant.NotifType.ADD
        );
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
        mIvProductImage.setOnClickListener(this);
    }

    private void findViews() {
        mIvProductImage = (ImageView) findViewById(R.id.iv_add_product_logo);
        mEtProductTitle = (EditText) findViewById(R.id.edt_add_product_title);
        mEtProductPrice = (EditText) findViewById(R.id.et_add_product_price);
        mEtProductDescription = (EditText) findViewById(R.id.et_add_product_description);
        mBtnProductAdd = (Button) findViewById(R.id.btn_add_product_add);
    }

    private void customizeActionBar() {
        setActionBarTitle(getString(R.string.text_add_product));
    }

    private void init() {
        useCamera = true;

        Glide.with(this)
                .load(Constant.API.DEFULT_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvProductImage);

        mProduct = new Product();
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(getApplicationContext(), this);
    }

    private void createProduct(String name, long price, String description, String imageUrl) {
        mProduct.setId(System.currentTimeMillis());
        mProduct.setName(name);
        mProduct.setPrice(price);
        mProduct.setUser(true);
        mProduct.setDescription(description);
        mProduct.setImage(imageUrl);
    }

    private void fillData(Bundle bundle) {
        mEtProductTitle.setText(bundle.getString(Constant.Bundle.TITLE));
        mEtProductPrice.setText(bundle.getString(Constant.Bundle.PRICE));
        mEtProductDescription.setText(bundle.getString(Constant.Bundle.DESCRIPTION));
        mProduct.setFavorite(bundle.getBoolean(Constant.Bundle.FAVORITE));
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constant.RequestCode.CAMERA);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}