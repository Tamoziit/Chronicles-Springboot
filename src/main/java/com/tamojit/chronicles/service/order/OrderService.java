package com.tamojit.chronicles.service.order;

import com.tamojit.chronicles.enums.OrderStatus;
import com.tamojit.chronicles.exceptions.ResourceNotFoundException;
import com.tamojit.chronicles.model.Cart;
import com.tamojit.chronicles.model.Order;
import com.tamojit.chronicles.model.OrderItem;
import com.tamojit.chronicles.model.Product;
import com.tamojit.chronicles.repository.OrderRepository;
import com.tamojit.chronicles.repository.ProductRepository;
import com.tamojit.chronicles.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Override
    public Order placeOrder(Long userId) {
        // initializing order
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);

        // setting order details
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);

        // After saving - clear cart
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Helpers
    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());

        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems()
            .stream()
            .map(cartItem -> {
                Product product = cartItem.getProduct();
                product.setInventory(product.getInventory() - cartItem.getQuantity()); // updating inventory after user placed an order
                productRepository.save(product);

                return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
                );
            }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
            .map(item -> item.getPrice()
                .multiply(new BigDecimal(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
