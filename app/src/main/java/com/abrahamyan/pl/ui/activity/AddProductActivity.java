package com.abrahamyan.pl.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
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

    private EditText mEdtProductTitle;
    private EditText mEdtProductPrice;
    private EditText mEdtProductDescription;
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
        setListeners();
        if(savedInstanceState == null) {
            init();
        } else {
            getData(savedInstanceState);
        }
        customizeActionBar();
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        mProduct.setName(String.valueOf(mEdtProductTitle.getText()));
        mProduct.setPrice(Long.parseLong(String.valueOf(mEdtProductPrice.getText())));
        mProduct.setDescription(String.valueOf(mEdtProductDescription.getText()));
        saveInstanceState.putParcelable(Constant.Extra.EXTRA_PRODUCT, mProduct);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.RequestCode.CAMERA_ACTIVITY:
                    Uri photoUri = (Uri) data.getExtras().get(Constant.Extra.EXTRA_PHOTO_URI);
                    mProduct.setImage(String.valueOf(photoUri));
                    Glide.with(this)
                            .load(photoUri)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mIvProductImage);
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
                if (mEdtProductTitle.getText().length() == 0) {
                    Toast.makeText(this, R.string.msg_edt_title_error, Toast.LENGTH_SHORT).show();
                    break;
                } else if (mEdtProductPrice.getText().length() == 0) {
                    Toast.makeText(this, R.string.msg_edt_price_error, Toast.LENGTH_SHORT).show();
                    break;
                }
                mProduct.setName(String.valueOf(mEdtProductTitle.getText()));
                mProduct.setPrice(Long.valueOf(String.valueOf(mEdtProductPrice.getText())));
                mProduct.setDescription(String.valueOf(mEdtProductDescription.getText()));

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
                Intent intent = new Intent(this, CameraActivity.class);
                startActivityForResult(intent, Constant.RequestCode.CAMERA_ACTIVITY);
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

    private void findViews() {
        mIvProductImage = (ImageView) findViewById(R.id.iv_add_product_logo);
        mEdtProductTitle = (EditText) findViewById(R.id.edt_add_product_title);
        mEdtProductPrice = (EditText) findViewById(R.id.edt_add_product_price);
        mEdtProductDescription = (EditText) findViewById(R.id.edt_add_product_description);
        mBtnProductAdd = (Button) findViewById(R.id.btn_add_product_add);
    }

    private void setListeners() {
        mBtnProductAdd.setOnClickListener(this);
        mIvProductImage.setOnClickListener(this);
        mEdtProductPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1 && s.charAt(0) == '0') {
                    int i = 0;
                    while ((s.length() >= i + 2) && (s.toString().charAt(i) == '0')) {
                        i++;
                    }
                    mEdtProductPrice.removeTextChangedListener(this);
                    mEdtProductPrice.setText(s.subSequence(i, s.length()));
                    mEdtProductPrice.addTextChangedListener(this);
                    if (count > 0) {
                        mEdtProductPrice.setSelection(start + count - i);
                    } else {
                        mEdtProductPrice.setSelection(start);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    mEdtProductPrice.removeTextChangedListener(this);
                    mEdtProductPrice.setText("0");
                    mEdtProductPrice.addTextChangedListener(this);
                    mEdtProductPrice.setSelection(1);
                }
            }
        });
    }

    private void getData(Bundle bundle) {
        if (bundle.getParcelable(Constant.Extra.EXTRA_PRODUCT) != null) {
            mProduct = bundle.getParcelable(Constant.Extra.EXTRA_PRODUCT);
            mEdtProductTitle.setText(mProduct.getName());
            mEdtProductPrice.setText(String.valueOf(mProduct.getPrice()));
            mEdtProductDescription.setText(mProduct.getDescription());
            Glide.with(this)
                    .load(mProduct.getImage())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mIvProductImage);
        }
    }

    private void customizeActionBar() {
        setActionBarTitle(getString(R.string.text_add_product));
    }

    private void init() {
        mProduct = new Product();
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(getApplicationContext(), this);

        mProduct.setId(System.currentTimeMillis());
        mProduct.setUser(true);
        mProduct.setImage(Constant.API.DEFULT_IMAGE);

        mEdtProductPrice.setText("0");
        Glide.with(this)
                .load(mProduct.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvProductImage);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}