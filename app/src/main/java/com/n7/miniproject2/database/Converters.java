package com.n7.miniproject2.database;

import androidx.room.TypeConverter;

import com.n7.miniproject2.enums.OrderStatus;

public class Converters {
    @TypeConverter
    public static String fromOrderStatus(OrderStatus status) {
        return status == null ? null : status.name();
    }

    @TypeConverter
    public static OrderStatus toOrderStatus(String status) {
        return status == null ? null : OrderStatus.valueOf(status);
    }
}