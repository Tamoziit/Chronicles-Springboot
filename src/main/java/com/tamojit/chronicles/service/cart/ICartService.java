package com.tamojit.chronicles.service.cart;

import com.tamojit.chronicles.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    // A default cart generated for all users at initial setup
    Long initializeNewCart();

    Cart getCartByUserId(Long userId);
}
