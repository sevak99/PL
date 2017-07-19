package com.abrahamyan.pl.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.util.Constant;

public class AboutFragment extends BaseFragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener{

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = AboutFragment.class.getSimpleName();
    public static final String URL = "http://aca.am/";

    // ===========================================================
    // Fields
    // ===========================================================

    private Bundle mArgumentData;
    private WebView mWv;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // ===========================================================
    // Constructors
    // ===========================================================

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    public static AboutFragment newInstance(Bundle args) {
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        findViews(view);
        setListeners();
        getData();
        customizeActionBar();
        setupWebView();
        loadUrl();
        return view;
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Override
    public void onRefresh() {
        mWv.reload();
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void findViews(View view) {
        mWv = (WebView) view.findViewById(R.id.wv_about);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_about);
    }

    private void setupWebView() {
        mWv.setWebChromeClient(new CustomWebChromeClient());
        mWv.setWebViewClient(new CustomWebViewClient());

        mWv.getSettings().setJavaScriptEnabled(true);
        mWv.getSettings().setBuiltInZoomControls(true);
        mWv.getSettings().setDisplayZoomControls(false);
    }

    private void loadUrl() {
        mSwipeRefreshLayout.setRefreshing(true);
        mWv.loadUrl(URL);
    }

    public void getData() {
        if (getArguments() != null) {
            mArgumentData = getArguments().getBundle(Constant.Argument.ARGUMENT_DATA);
        }
    }

    private void customizeActionBar() {
    }

    @Override
    public boolean onBackPressed() {
        if(mWv.canGoBack()) {
            mWv.goBack();
            return true;
        }
        return super.onBackPressed();
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
            if (!view.getUrl().contains(URL)) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(view.getUrl()));
                startActivity(i);
                return true;
            }
            return false;
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (isAdded()) {
                Log.d(LOG_TAG, String.valueOf(progress));
                if (progress >= 90) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    }



}