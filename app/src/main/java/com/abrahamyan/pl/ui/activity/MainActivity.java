package com.abrahamyan.pl.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.ui.fragment.AboutFragment;
import com.abrahamyan.pl.ui.fragment.FavoriteProductsFragment;
import com.abrahamyan.pl.ui.fragment.ProductListFragment;
import com.abrahamyan.pl.util.FragmentTransactionManager;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return true;
    }

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
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if(mNavigationView.getMenu().findItem(R.id.nav_product_list).isChecked() == false) {
            for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }

            mNavigationView.getMenu().findItem(R.id.nav_product_list).setChecked(true);
            openScreen(
                    ProductListFragment.newInstance(),
                    R.id.nav_product_list,
                    false
            );
        } else {
            finish();
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

        FragmentTransactionManager.displayFragment(
                getSupportFragmentManager(),
                fragment,
                R.id.fl_main_container,
                mustAddToBackStack
        );
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
