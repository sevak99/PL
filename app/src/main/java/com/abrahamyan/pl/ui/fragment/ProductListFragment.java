package com.abrahamyan.pl.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.io.bus.BusProvider;
import com.abrahamyan.pl.io.rest.HttpRequestManager;
import com.abrahamyan.pl.io.service.PLIntentService;
import com.abrahamyan.pl.ui.Adapter.ProductAdapter;
import com.abrahamyan.pl.util.Constant;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;

public class ProductListFragment extends BaseFragment implements View.OnClickListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = ProductListFragment.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private Bundle mArgumentData;
    private RecyclerView recyclerView;

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
        findViews(view);
        BusProvider.register(this);
        setListeners();
        getData();
        customizeActionBar();

        PLIntentService.start(
                getActivity(),
                Constant.API.PRODUCT_LIST,
                HttpRequestManager.RequestType.PRODUCT_LIST
        );

//        PLIntentService.start(
//                getActivity(),
//                Constant.API.PRODUCT_ITEM,
//                HttpRequestManager.RequestType.PRODUCT_ITEM
//        );
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

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Subscribe
    public void onEventReceived(ArrayList<Product> productArrayList) {
        Log.d(LOG_TAG, "Size " + productArrayList.size());
        setAdapter(productArrayList);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void setListeners() {

    }

    private void findViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_product_list);
    }

    public void getData() {
        if (getArguments() != null) {
            mArgumentData = getArguments().getBundle(Constant.Argument.ARGUMENT_DATA);
        }
    }

    private void customizeActionBar() {
    }

    private void setAdapter(ArrayList<Product> products) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ProductAdapter.ProductViewHolder.OnItemClickListener onItemClickListener =
                new ProductAdapter.ProductViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(Product product) {
                        Toast.makeText(getActivity(), product.getId(), Toast.LENGTH_LONG).show();
                    }
                };
        recyclerView.setAdapter(new ProductAdapter(onItemClickListener, products));
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}