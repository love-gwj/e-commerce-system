package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.CartMapper;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private ProductService productService;

    @Override
    public List<Cart> getCartList(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        return list(wrapper);
    }

    @Override
    public void addToCart(Long userId, Long productId, Integer quantity) {
        Product product = productService.getById(productId);
        if (product == null || product.getStatus() == 0) {
            throw new BusinessException("商品不存在");
        }
        
        if (product.getStock() < quantity) {
            throw new BusinessException("库存不足");
        }
        
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        wrapper.eq(Cart::getProductId, productId);
        Cart existCart = getOne(wrapper);
        
        if (existCart != null) {
            existCart.setQuantity(existCart.getQuantity() + quantity);
            updateById(existCart);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setSelected(true);
            save(cart);
        }
    }

    @Override
    public void updateCartQuantity(Long cartId, Integer quantity) {
        Cart cart = getById(cartId);
        if (cart == null) {
            throw new BusinessException("购物车项不存在");
        }
        
        cart.setQuantity(quantity);
        updateById(cart);
    }

    @Override
    public void removeFromCart(Long cartId) {
        removeById(cartId);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        remove(wrapper);
    }
}
