package com.abrahamyan.pl.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.db.entity.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final String LOG_TAG = ProductAdapter.class.getSimpleName();

    // ===========================================================
    // Fields
    // ===========================================================

    private ArrayList<Product> mProductArrayList;
    private OnItemClickListener mOnItemClickListener;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ProductAdapter(ArrayList<Product> productArrayList, OnItemClickListener onItemClickListener) {
        mProductArrayList = productArrayList;
        mOnItemClickListener = onItemClickListener;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_item_product, viewGroup, false);
        return new ProductViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bindData(mProductArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return mProductArrayList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Click Listeners
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProductTitle;
        private TextView tvProductPrice;
        private ImageView ivProductImage;
        private ImageView ivProductFavorite;
        private LinearLayout llItemContainer;
        private OnItemClickListener onItemClickListener;
        private Context context;


        public ProductViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.context = itemView.getContext();
            this.onItemClickListener = onItemClickListener;
            findViews(itemView);
        }

        void findViews(View view) {
            llItemContainer = (LinearLayout) view.findViewById(R.id.ll_product_item_container);
            tvProductTitle = (TextView) view.findViewById(R.id.tv_product_item_title);
            tvProductPrice = (TextView) view.findViewById(R.id.tv_product_item_price);
            ivProductImage = (ImageView) view.findViewById(R.id.iv_product_item_logo);
            ivProductFavorite = (ImageView) view.findViewById(R.id.iv_product_item_favorite);
        }

        void bindData(final Product product) {

            Glide.with(context)
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_get_app_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivProductImage);

            tvProductTitle.setText(product.getName());
            tvProductPrice.setText(String.valueOf(product.getPrice()));
            if(product.isFavorite())
                ivProductFavorite.setVisibility(View.VISIBLE);
            else
                ivProductFavorite.setVisibility(View.GONE);

            llItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(product);
                }
            });

            llItemContainer.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(product);
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {

        void onItemClick(Product product);

        void onItemLongClick(Product product);

    }
}