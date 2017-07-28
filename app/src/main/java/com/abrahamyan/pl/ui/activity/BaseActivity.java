package com.abrahamyan.pl.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.io.bus.BusProvider;

/**
 * Created by SEVAK on 22.06.2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = BaseActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private TextView mTvToolbarTitle;
    private TextView mTvSubToolbarTitle;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;

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
        setContentView(getLayoutResource());
        findViews();

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.unregister(this);
    }

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.tb);
        if (mToolbar != null) {
            mTvToolbarTitle = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
            mTvSubToolbarTitle = (TextView) mToolbar.findViewById(R.id.tv_toolbar_subtitle);
        }

        mTabLayout = (TabLayout) findViewById(R.id.ctl);
    }

    protected abstract int getLayoutResource();

    @Nullable
    protected TabLayout getTabLayout() {
        return mTabLayout;
    }

    @Nullable
    protected Toolbar getToolBar() {
        return mToolbar;
    }

    protected void hideActionBar() {
        getSupportActionBar().hide();
    }

    protected void showActionBar() {
        getSupportActionBar().show();
    }

    public void setActionBarTitle(String title) {
        mTvToolbarTitle.setText(title);
    }

    public void setActionBarSubTitle(String subtitle) {
        mTvSubToolbarTitle.setVisibility(View.VISIBLE);
        mTvSubToolbarTitle.setText(subtitle);
    }

    protected void hideActionBarTitle() {
        mTvSubToolbarTitle.setVisibility(View.GONE);
    }

    protected void hideActionBarSubTitle() {
        mTvToolbarTitle.setVisibility(View.GONE);
    }

    protected void showActionBarTitle() {
        mTvToolbarTitle.setVisibility(View.VISIBLE);
    }

    public void setActionBarIcon(int iconRes) {
        getSupportActionBar().setHomeAsUpIndicator(iconRes);
    }

    public void hideActionBarIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void showActionBarIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
