package com.tamojit.chronicles.service.order;

import com.tamojit.chronicles.dto.OrderDto;
import com.tamojit.chronicles.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);
}
