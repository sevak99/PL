package com.abrahamyan.pl.ui.activity;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
        implements PlAsyncQueryHandler.AsyncQueryListener, View.OnClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = ProductActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private boolean isStillEditing;
    private TextView mTvProductTitle;
    private TextView mTvProductPrice;
    private TextView mTvProductDescription;
    private EditText mEdtProductTitle;
    private EditText mEdtProductPrice;
    private EditText mEdtProductDescription;
    private ImageView mIvProductImage;
    private LinearLayout mLlProductView;
    private LinearLayout mLlProductEdit;
    private MenuItem mMenuEdit;
    private MenuItem mMenuDone;
    private MenuItem mMenuFavorite;
    private Product mProduct;
    private Product mOldProduct;
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
        setListeners();
        try {
            getData(savedInstanceState);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        init();
        customizeActionBar();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_product;
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        if (isStillEditing) {
            mProduct.setName(String.valueOf(mEdtProductTitle.getText()));
            mProduct.setPrice(Long.parseLong(String.valueOf(mEdtProductPrice.getText())));
            mProduct.setDescription(String.valueOf(mEdtProductDescription.getText()));
            saveInstanceState.putParcelable(Constant.Extra.EXTRA_PRODUCT, mProduct);
        }
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
        if (isStillEditing) {
            mMenuDone.setVisible(true);
            mMenuEdit.setVisible(false);
            mMenuFavorite.setVisible(false);
        }
        if (mProduct.isFavorite()) {
            mMenuFavorite.setIcon(R.drawable.ic_favorite);
        }
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
            case R.id.iv_product_image:
                Intent intent = new Intent(this, CameraActivity.class);
                startActivityForResult(intent, Constant.RequestCode.CAMERA_ACTIVITY);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.menu_product_edit:
                isStillEditing = true;
                mMenuEdit.setVisible(false);
                mMenuDone.setVisible(true);
                mMenuFavorite.setVisible(false);
                openEditLayout(mProduct);
                return true;

            case R.id.menu_product_done:
                if (mEdtProductTitle.getText().length() == 0) {
                    Toast.makeText(this, R.string.msg_edt_title_error, Toast.LENGTH_SHORT).show();
                } else {
                    isStillEditing = false;
                    mMenuEdit.setVisible(true);
                    mMenuDone.setVisible(false);
                    mMenuFavorite.setVisible(true);
                    updateProduct(
                            String.valueOf(mEdtProductTitle.getText()),
                            Long.parseLong(String.valueOf(mEdtProductPrice.getText())),
                            String.valueOf(mEdtProductDescription.getText())
                    );
                    openViewLayout(mProduct);
                }
                return true;

            case R.id.menu_product_favorite:
                if (mProduct.isFavorite()) {
                    mMenuFavorite.setIcon(R.drawable.ic_unfavorite);
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
        if (apiEvent.getEventType() == ApiEvent.EventType.PRODUCT_ITEM_LOADED) {
            if (apiEvent.isSuccess()) {
                mProduct = (Product) apiEvent.getEventData();
                openViewLayout(mProduct);
            } else {
                Toast.makeText(this, R.string.msg_some_error, Toast.LENGTH_SHORT).show();
            }
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
        AppUtil.sendNotification(
                getApplicationContext(),
                MainActivity.class,
                getString(R.string.app_name),
                getString(R.string.notif_update) + " " + mProduct.getName(),
                mProduct.getName(),
                Constant.NotifType.UPDATE
        );
    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void findViews() {
        mIvProductImage = (ImageView) findViewById(R.id.iv_product_image);
        mLlProductView = (LinearLayout) findViewById(R.id.ll_product_not_editable);
        mTvProductTitle = (TextView) findViewById(R.id.tv_product_title);
        mTvProductPrice = (TextView) findViewById(R.id.tv_product_price);
        mTvProductDescription = (TextView) findViewById(R.id.tv_product_description);
        mLlProductEdit = (LinearLayout) findViewById(R.id.ll_product_editable);
        mEdtProductTitle = (EditText) findViewById(R.id.edt_product_title);
        mEdtProductPrice = (EditText) findViewById(R.id.edt_product_price);
        mEdtProductDescription = (EditText) findViewById(R.id.edt_product_description);
    }

    private void setListeners() {
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

    private void init() {
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(ProductActivity.this, this);

        Glide.with(this)
                .load(mProduct.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvProductImage);

        if (isStillEditing) {
            openEditLayout(mProduct);
        } else {
            openViewLayout(mProduct);
        }
    }

    private void customizeActionBar() {
        setActionBarTitle(getString(R.string.text_product));
    }

    private void getData(Bundle bundle) throws CloneNotSupportedException {
        if (bundle != null && bundle.getParcelable(Constant.Extra.EXTRA_PRODUCT) != null) {
            mProduct = bundle.getParcelable(Constant.Extra.EXTRA_PRODUCT);
            isStillEditing = true;
        } else {
            mOldProduct = getIntent().getParcelableExtra(Constant.Extra.EXTRA_PRODUCT);
            mProduct = mOldProduct.clone();
            if (!mProduct.isUser()) {
                loadProduct();
            }
        }
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

        if (!mProduct.equals(mOldProduct)) {
            try {
                mOldProduct = mProduct.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            mPlAsyncQueryHandler.updateProduct(mProduct);
        }
    }

    private void openViewLayout(Product product) {
        AppUtil.closeKeyboard(this);

        mLlProductEdit.setVisibility(View.GONE);
        mLlProductView.setVisibility(View.VISIBLE);

        mTvProductTitle.setText(product.getName());
        mTvProductPrice.setText(String.valueOf(product.getPrice()));
        mTvProductDescription.setText(product.getDescription());
        mIvProductImage.setOnClickListener(null);
    }

    private void openEditLayout(Product product) {
        mLlProductView.setVisibility(View.GONE);
        mLlProductEdit.setVisibility(View.VISIBLE);

        mEdtProductTitle.setText(product.getName());
        mEdtProductPrice.setText(String.valueOf(product.getPrice()));
        mEdtProductDescription.setText(product.getDescription());
        mIvProductImage.setOnClickListener(this);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}