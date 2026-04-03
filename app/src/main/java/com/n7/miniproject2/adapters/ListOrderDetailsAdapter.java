package com.n7.miniproject2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.n7.miniproject2.R;
import com.n7.miniproject2.dtos.OrderDetailDto;

import java.text.DecimalFormat;
import java.util.List;

public class ListOrderDetailsAdapter extends RecyclerView.Adapter<ListOrderDetailsAdapter.ViewHolder> {
    private List<OrderDetailDto> orderDetails;
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    public ListOrderDetailsAdapter(List<OrderDetailDto> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDto> orderDetails) {
        this.orderDetails = orderDetails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_item_readonly, parent, false)
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
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }

        public void bind(OrderDetailDto item) {
            tvProductName.setText(item.getProductName());
            tvProductPrice.setText("Đơn giá: " + formatter.format(item.getPrice()) + " VNĐ");
            tvQuantity.setText("Số lượng: " + item.getQuantity());
        }
    }
}
