package com.abrahamyan.pl.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by SEVAK on 20.07.2017.
 */

public class TabFragmentAdapter extends FragmentPagerAdapter{

    private static final String LOG_TAG = TabFragmentAdapter.class.getSimpleName();

    private final ArrayList<Fragment> mFragments = new ArrayList<>();
    private final ArrayList<String> mFragmentTitles = new ArrayList<>();

    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment (Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
