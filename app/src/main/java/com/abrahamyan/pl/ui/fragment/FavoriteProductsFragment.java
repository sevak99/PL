package com.abrahamyan.pl.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.db.cursor.CursorReader;
import com.abrahamyan.pl.db.entity.Product;
import com.abrahamyan.pl.db.handler.PlAsyncQueryHandler;
import com.abrahamyan.pl.ui.activity.ProductActivity;
import com.abrahamyan.pl.ui.adapter.ProductAdapter;
import com.abrahamyan.pl.util.Constant;

import java.util.ArrayList;

public class FavoriteProductsFragment extends BaseFragment implements View.OnClickListener,
        ProductAdapter.OnItemClickListener, PlAsyncQueryHandler.AsyncQueryListener {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = FavoriteProductsFragment.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private Bundle mArgumentData;
    private TextView mErrorMsg;
    private RecyclerView mRecyclerView;
    private ArrayList<Product> mProductArrayList;
    private ProductAdapter mRecyclerViewAdapter;
    private PlAsyncQueryHandler mPlAsyncQueryHandler;

    // ===========================================================
    // Constructors
    // ===========================================================

    public static FavoriteProductsFragment newInstance() {
        return new FavoriteProductsFragment();
    }

    public static FavoriteProductsFragment newInstance(Bundle args) {
        FavoriteProductsFragment fragment = new FavoriteProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_products, container, false);
        findViews(view);
        init();
        setListeners();
        getData();
        customizeActionBar();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPlAsyncQueryHandler.getFavoriteProducts();
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
        Intent intent = new Intent(getActivity(), ProductActivity.class);
        intent.putExtra(Constant.Extra.EXTRA_PRODUCT, product);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(final Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.msg_dialog_delete_product)
                .setPositiveButton(R.string.text_btn_dialog_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlAsyncQueryHandler.deleteProduct(product);
                        mProductArrayList.remove(product);
                        mRecyclerViewAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.text_btn_dialog_cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        switch (token) {
            case PlAsyncQueryHandler.QueryToken.GET_FAVORITE_PRODUCTS:
                ArrayList<Product> favoriteProducts = CursorReader.parseProducts(cursor);
                if (favoriteProducts.size() != 0) {
                    mErrorMsg.setVisibility(View.GONE);
                    mProductArrayList.clear();
                    mProductArrayList.addAll(favoriteProducts);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    mProductArrayList.clear();
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    mErrorMsg.setText(R.string.msg_no_favorites);
                    mErrorMsg.setVisibility(View.VISIBLE);
                }
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

    }

    private void findViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fv_products);
        mErrorMsg = (TextView) view.findViewById(R.id.tv_fv_products);
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