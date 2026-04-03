package com.n7.miniproject2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.n7.miniproject2.R;
import com.n7.miniproject2.dtos.ProductListItemDto;
import com.n7.miniproject2.listeners.OnItemClicked;

import java.text.DecimalFormat;
import java.util.List;

public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsAdapter.ViewHolder> {
    private List<ProductListItemDto> products;
    private OnItemClicked<ProductListItemDto> onItemClicked;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    public ListProductsAdapter(List<ProductListItemDto> products) {
        this.products = products;
    }

    public void setOnItemClicked(OnItemClicked<ProductListItemDto> onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public void setProducts(List<ProductListItemDto> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }

        public void bind(ProductListItemDto product) {
            tvProductName.setText(product.getName());
            tvProductPrice.setText(formatter.format(product.getPrice()) + " VNĐ");


            itemView.setOnClickListener(v -> {
                if (onItemClicked != null) {
                    onItemClicked.onItemClick(product);
                }
            });
        }
    }
}