package com.tamojit.chronicles.controller;

import com.tamojit.chronicles.exceptions.ResourceNotFoundException;
import com.tamojit.chronicles.model.Cart;
import com.tamojit.chronicles.model.User;
import com.tamojit.chronicles.response.ApiResponse;
import com.tamojit.chronicles.service.cart.ICartItemService;
import com.tamojit.chronicles.service.cart.ICartService;
import com.tamojit.chronicles.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
        @RequestParam Long productId,
        @RequestParam Integer quantity
    ) {
        try {
            User user = userService.getUserById(6L); // default user1
            Cart cart = cartService.initializeNewCart(user); // getting default cart of user

            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Cart Item added successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/item/remove/{cartId}/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Cart Item removed successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/item/update-quantity/{cartId}/{itemId}")
    public ResponseEntity<ApiResponse> updateItemQuantity(
        @PathVariable Long cartId,
        @PathVariable Long itemId,
        @RequestParam Integer quantity
    ) {
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Cart Item updated successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
