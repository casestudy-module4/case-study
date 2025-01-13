package com.example.casestudy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryDTO {
    private Integer orderId;
    private LocalDateTime orderTime;
    private Integer statusOrder;
    private Double totalPrice;
    private
    List<OrderItemDTO> items;

    public OrderHistoryDTO(Integer orderId, LocalDateTime orderTime, Integer statusOrder, Double totalPrice) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.statusOrder = statusOrder;
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return orderId;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.items = orderItems;
    }

}
