package com.n7.miniproject2.listeners;

import com.n7.miniproject2.dtos.OrderDetailDto;

public interface OnEditOrderItemClicked {
    void onIncreaseQuantity(OrderDetailDto item);
    void onDecreaseQuantity(OrderDetailDto item);
}
