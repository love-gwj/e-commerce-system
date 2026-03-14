package com.example.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ecommerce.entity.Cart;

import java.util.List;

public interface CartService extends IService<Cart> {
    List<Cart> getCartList(Long userId);
    void addToCart(Long userId, Long productId, Integer quantity);
    void updateCartQuantity(Long cartId, Integer quantity);
    void removeFromCart(Long cartId);
    void clearCart(Long userId);
}
