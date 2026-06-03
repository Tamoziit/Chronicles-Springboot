package com.tamojit.chronicles.service.order;

import com.tamojit.chronicles.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);

    Order getOrder(Long orderId);

    List<Order> getUserOrders(Long userId);
}
