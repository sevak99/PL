package com.abrahamyan.pl.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.abrahamyan.pl.io.bus.BusProvider;
import com.abrahamyan.pl.ui.activity.BaseActivity;

public abstract class BaseFragment extends Fragment {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.unregister(this);
    }

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    protected void hideActionBarIcon() {
        ((BaseActivity) getActivity()).hideActionBarIcon();
    }

    protected void showActionBarIcon() {
        ((BaseActivity) getActivity()).showActionBarIcon();
    }

    protected void setActionBarIcon(int iconRes) {
        ((BaseActivity) getActivity()).setActionBarIcon(iconRes);
    }

    protected void setActionBarTitle(String actionBarTitle) {
        ((BaseActivity) getActivity()).setActionBarTitle(actionBarTitle);
    }

    public boolean onBackPressed () {
        return false;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}