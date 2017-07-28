package com.abrahamyan.pl.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.ui.fragment.AboutFragment;
import com.abrahamyan.pl.ui.fragment.BaseFragment;
import com.abrahamyan.pl.ui.fragment.FavoriteProductsFragment;
import com.abrahamyan.pl.ui.fragment.ProductListFragment;
import com.abrahamyan.pl.util.Constant;
import com.abrahamyan.pl.util.FragmentTransactionManager;
import com.abrahamyan.pl.util.Preference;

public class MainActivity extends  BaseActivity  implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private int currentItem;

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
        customizeActionBar();
        initDrawer();
        openScreen(
                ProductListFragment.newInstance(),
                R.id.nav_product_list,
                false
        );

        catchNotificationData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNavigationView.getMenu().findItem(currentItem).setChecked(true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    // ===========================================================
    // Observer callback
    // ===========================================================

    // ===========================================================
    // Observer methods
    // ===========================================================

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_product_list:
                openScreen(
                        ProductListFragment.newInstance(),
                        R.id.nav_product_list,
                        false
                );
                break;

            case R.id.nav_about:
                openScreen(
                        AboutFragment.newInstance(),
                        R.id.nav_about,
                        true
                );
                break;

            case R.id.nav_favorite_products:
                openScreen(
                        FavoriteProductsFragment.newInstance(),
                        R.id.nav_favorite_products,
                        false
                );

                break;

            case R.id.nav_product_vp:
                Intent intent = new Intent(this, ProductListActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_logout:
                logout();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_main_container);
            if(!((BaseFragment) fragment).onBackPressed()) {
                if(!(fragment instanceof ProductListFragment)) {
                    openScreen(
                            ProductListFragment.newInstance(),
                            R.id.nav_product_list,
                            false
                    );
                } else {
                    finish();
                }
            }
        }
    }


    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void findViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
        mNavigationView = (NavigationView) findViewById(R.id.nav_main);
    }

    private void customizeActionBar() {
        setActionBarTitle(getString(R.string.app_name));
    }

    private void initDrawer() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                getToolBar(),
                R.string.msg_navigation_drawer_open,
                R.string.msg_navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void openScreen(Fragment fragment, int item, boolean mustAddToBackStack) {
        mNavigationView.getMenu().findItem(item).setChecked(true);
        currentItem = item;

        FragmentTransactionManager.displayFragment(
                getSupportFragmentManager(),
                fragment,
                R.id.fl_main_container,
                mustAddToBackStack
        );
    }

    private void logout() {
        mNavigationView.getMenu().findItem(R.id.nav_logout).setChecked(true);
        Preference.getInstance(this).setUserMail(null);
        Preference.getInstance(this).setUserPass(null);

        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void catchNotificationData() {
        String notifMessage = getIntent().getStringExtra(Constant.Extra.EXTRA_NOTIF_DATA);
        int notifType = getIntent().getIntExtra(Constant.Extra.EXTRA_NOTIF_TYPE, -1);

        switch (notifType) {
            case Constant.NotifType.ADD:
                Toast.makeText(this,getString(R.string.notif_add) + " " + notifMessage, Toast.LENGTH_SHORT).show();
                break;
            case Constant.NotifType.UPDATE:
                Toast.makeText(this,getString(R.string.notif_update) + " " + notifMessage, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
