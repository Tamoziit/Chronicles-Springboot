package com.tamojit.chronicles.service.cart;

import com.tamojit.chronicles.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);
}
