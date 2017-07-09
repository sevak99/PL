package com.abrahamyan.pl.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.db.cursor.CursorReader;
import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.db.handler.PlAsyncQueryHandler;
import com.abrahamyan.pl.io.bus.BusProvider;
import com.abrahamyan.pl.io.rest.HttpRequestManager;
import com.abrahamyan.pl.io.service.PLIntentService;
import com.abrahamyan.pl.ui.activity.AddProductActivity;
import com.abrahamyan.pl.ui.adapter.ProductAdapter;
import com.abrahamyan.pl.util.Constant;
import com.abrahamyan.pl.util.NetworkUtil;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ProductListFragment extends BaseFragment
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        PlAsyncQueryHandler.AsyncQueryListener, ProductAdapter.OnItemClickListener{

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = ProductListFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 1;

    // ===========================================================
    // Fields
    // ===========================================================

    private Bundle mArgumentData;
    private PlAsyncQueryHandler mPlAsyncQueryHandler;
    private RecyclerView mRecyclerView;
    private ProductAdapter mRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView mConnectionMsg;
    private ArrayList<Product> mProductArrayList;

    // ===========================================================
    // Constructors
    // ===========================================================

    public static ProductListFragment newInstance() {
        return new ProductListFragment();
    }

    public static ProductListFragment newInstance(Bundle args) {
        ProductListFragment fragment = new ProductListFragment();
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
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        BusProvider.register(this);
        findViews(view);
        init();
        setListeners();
        getData();
        customizeActionBar();

        onRefresh();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusProvider.unregister(this);
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onItemClick(Product product) {
        Log.d(LOG_TAG, product.getName());
    }

    @Override
    public void onItemLongClick(Product product) {
        Log.d(LOG_TAG, product.getName());
    }


    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Subscribe
    public void onEventReceived(ArrayList<Product> products) {
        mProductArrayList.clear();
        mProductArrayList.addAll(products);
        mRecyclerViewAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void onEventReceived(String string) {
        mProductArrayList.clear();
        mRecyclerViewAdapter.notifyDataSetChanged();
        mConnectionMsg.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mConnectionMsg.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(true);
        if(NetworkUtil.getInstance().isConnected(getActivity())) {
            PLIntentService.start(
                    getActivity(),
                    Constant.API.PRODUCT_LIST,
                    HttpRequestManager.RequestType.PRODUCT_LIST
            );
        } else {
            mPlAsyncQueryHandler.getProducts();
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_product:
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_CODE) {
                Product product = data.getParcelableExtra(AddProductActivity.KEY_TITLE);
                mProductArrayList.add(product);
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        switch (token) {
            case PlAsyncQueryHandler.QueryToken.GET_PRODUCTS:
                ArrayList<Product> products = CursorReader.parseProducts(cursor);
                if(products != null) {
                    mProductArrayList.clear();
                    mProductArrayList.addAll(products);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                }
                break;
        }
    }

    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {

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
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void findViews(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_product_list);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_product_list);
        mConnectionMsg = (TextView) view.findViewById(R.id.tv_product_list);
    }

    private void init() {
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(getActivity().getApplicationContext(), this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mProductArrayList = new ArrayList<>();
        mRecyclerViewAdapter = new ProductAdapter(mProductArrayList, this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }


    public void getData() {
        if (getArguments() != null) {
            mArgumentData = getArguments().getBundle(Constant.Argument.ARGUMENT_DATA);
        }
    }

    private void customizeActionBar() {
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}