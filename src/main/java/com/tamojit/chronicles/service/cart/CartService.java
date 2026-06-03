package com.tamojit.chronicles.service.cart;

import com.tamojit.chronicles.exceptions.ResourceNotFoundException;
import com.tamojit.chronicles.model.Cart;
import com.tamojit.chronicles.repository.CartItemRepository;
import com.tamojit.chronicles.repository.CartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(Long id) {
        return cartRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Transactional // a single transactional unit across tables
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id); // clearing all associated cart items

        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();

//        Alternate Method
//        return cart.getItems()
//            .stream()
//            .map(CartItem::getTotalPrice)
//            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // A default cart generated for all users at initial setup
    @Override
    public Long initializeNewCart() {
        Cart newCart = new Cart();
        return cartRepository.save(newCart).getId();
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
