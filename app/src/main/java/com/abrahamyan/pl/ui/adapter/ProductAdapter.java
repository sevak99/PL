package com.abrahamyan.pl.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abrahamyan.pl.R;
import com.abrahamyan.pl.db.entity.Product;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    // ===========================================================
    // Fields
    // ===========================================================

    private ProductViewHolder.OnItemClickListener onItemClickListener;
    private ArrayList<Product> products;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ProductAdapter(ProductViewHolder.OnItemClickListener onItemClickListener, ArrayList<Product> products) {
        this.onItemClickListener = onItemClickListener;
        this.products = products;
    }

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_product, parent, false);
        return new ProductViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================

    public void setProducts(ArrayList<Product> products) {
        this.products.addAll(0, products);
        //notifyDataSetChanged();
    }

    public void clear() {
        products.clear();
        notifyDataSetChanged();
    }


    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static class ProductViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        // ===========================================================
        // Fields
        // ===========================================================

        private Context context;
        private OnItemClickListener onItemClickListener;
        private LinearLayout container;
        private ImageView logo;
        private TextView title;
        private TextView price;
        private Product product;

        // ===========================================================
        // Constructors
        // ===========================================================

        public ProductViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            findViews(itemView);
            this.onItemClickListener = onItemClickListener;
        }

        // ===========================================================
        // Click Listeners
        // ===========================================================

        @Override
        public void onClick(View v) {
            Log.d("testt", String.valueOf(v.getId()));
            switch (v.getId()) {
                case R.id.ll_product_item_container:
                    nonotifyItemClicked();
                    break;
            }
        }

        // ===========================================================
        // Methods
        // ===========================================================

        private void findViews(View view) {
            container = (LinearLayout) view.findViewById(R.id.ll_product_item_container);
            logo = (ImageView) view.findViewById( R.id.iv_product_item_logo );
            title = (TextView) view.findViewById( R.id.tv_product_item_title );
            price = (TextView) view.findViewById( R.id.tv_product_item_price );
            context = container.getContext();

            container.setOnClickListener(this);
        }


        public void bind(Product product) {
            this.product = product;
            title.setText(product.getName());
            price.setText(String.valueOf(product.getPrice()));
            Glide.with(context)
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_get_app_black_24dp)
                    .into(logo);
        }

        public void nonotifyItemClicked() {
            if(onItemClickListener != null) {
                onItemClickListener.onItemClick(product);
            }
        }

        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================

        public interface OnItemClickListener {
            void onItemClick(Product product);
        }
    }
}