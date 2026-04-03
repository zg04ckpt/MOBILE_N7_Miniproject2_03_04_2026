package com.n7.miniproject2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.n7.miniproject2.R;
import com.n7.miniproject2.dtos.OrderDetailDto;

import java.text.DecimalFormat;
import java.util.List;

public class ListEditProductOrderAdapter extends RecyclerView.Adapter<ListEditProductOrderAdapter.ViewHolder> {
    private List<OrderDetailDto> orderDetails;
    private final DecimalFormat formatter = new DecimalFormat("#,###");
    public ListEditProductOrderAdapter(List<OrderDetailDto> orderDetails) {
        this.orderDetails = orderDetails;
    }

//    public void setOnEditOrderItemClicked() {
//    }

    public void setOrderDetails(List<OrderDetailDto> orderDetails) {
        this.orderDetails = orderDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(orderDetails.get(position));
    }

    @Override
    public int getItemCount() {
        return orderDetails != null ? orderDetails.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvQuantity;
        Button btnDecrease;
        Button btnIncrease;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }

        public void bind(OrderDetailDto item) {
            tvProductName.setText(item.getProductName());
            tvProductPrice.setText(formatter.format(item.getPrice()) + " VNĐ");
            tvQuantity.setText(String.valueOf(item.getQuantity()));

            Glide.with(itemView.getContext())
                    .load(item.getProductUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(ivProductImage);

            btnDecrease.setOnClickListener(v -> {
            });

            btnIncrease.setOnClickListener(v -> {
            });
        }
    }
}
