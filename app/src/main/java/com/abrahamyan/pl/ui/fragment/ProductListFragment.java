package com.abrahamyan.pl.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.db.cursor.CursorReader;
import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.db.handler.PlAsyncQueryHandler;
import com.abrahamyan.pl.io.bus.BusProvider;
import com.abrahamyan.pl.io.bus.event.ApiEvent;
import com.abrahamyan.pl.io.rest.HttpRequestManager;
import com.abrahamyan.pl.io.service.PLIntentService;
import com.abrahamyan.pl.ui.activity.AddProductActivity;
import com.abrahamyan.pl.ui.activity.ProductActivity;
import com.abrahamyan.pl.ui.adapter.ProductAdapter;
import com.abrahamyan.pl.util.Constant;
import com.abrahamyan.pl.util.NetworkUtil;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ProductListFragment extends BaseFragment
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        PlAsyncQueryHandler.AsyncQueryListener, ProductAdapter.OnItemClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = ProductListFragment.class.getSimpleName();
    public static final int ADD_PRODUCT_ACTIVITY = 1;

    // ===========================================================
    // Fields
    // ===========================================================

    private Bundle mArgumentData;
    private TextView mErrorMsg;
    private FloatingActionButton mFabAddProduct;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<Product> mProductArrayList;
    private ProductAdapter mRecyclerViewAdapter;
    private PlAsyncQueryHandler mPlAsyncQueryHandler;

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
    public void onResume() {
        mPlAsyncQueryHandler.getProducts();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        BusProvider.unregister(this);
        super.onDestroyView();
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_main_add_product:
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                startActivityForResult(intent, ADD_PRODUCT_ACTIVITY);
                break;
        }
    }

    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(getActivity(), ProductActivity.class);
        intent.putExtra(Constant.Extra.EXTRA_PRODUCT, product);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(final Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.msg_delete_product)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlAsyncQueryHandler.deleteProduct(product);
                        mProductArrayList.remove(product);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Subscribe
    public void onEventReceived(ApiEvent<Object> apiEvent) {
        if (!apiEvent.isSuccess()) {
            Toast.makeText(getActivity(), "Something went wrong, please try again",
                    Toast.LENGTH_SHORT).show();
        }
        mPlAsyncQueryHandler.getProducts();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mErrorMsg.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(true);
        if (NetworkUtil.getInstance().isConnected(getActivity())) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_PRODUCT_ACTIVITY) {
                Product product = data.getParcelableExtra(Constant.Extra.EXTRA_PRODUCT);
                mErrorMsg.setVisibility(View.GONE);
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
                if (products.size() != 0) {
                    mErrorMsg.setVisibility(View.GONE);
                    mProductArrayList.clear();
                    mProductArrayList.addAll(products);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    mProductArrayList.clear();
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    mErrorMsg.setText(R.string.msg_connection_error);
                    mErrorMsg.setVisibility(View.VISIBLE);
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
        mFabAddProduct.setOnClickListener(this);
    }

    private void findViews(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_product_list);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_product_list);
        mErrorMsg = (TextView) view.findViewById(R.id.tv_product_list);
        mFabAddProduct = (FloatingActionButton) view.findViewById(R.id.fab_main_add_product);
    }

    private void init() {
        mPlAsyncQueryHandler = new PlAsyncQueryHandler(getActivity().getApplicationContext(), this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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