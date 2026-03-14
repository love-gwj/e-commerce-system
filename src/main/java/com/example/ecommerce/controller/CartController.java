package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/list")
    public Result<List<Cart>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Cart> cartList = cartService.getCartList(userId);
        return Result.success(cartList);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Cart cart, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        cartService.addToCart(userId, cart.getProductId(), cart.getQuantity());
        return Result.success();
    }

    @PutMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @RequestParam Integer quantity) {
        cartService.updateCartQuantity(id, quantity);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return Result.success();
    }

    @DeleteMapping("/clear")
    public Result<?> clear(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        cartService.clearCart(userId);
        return Result.success();
    }
}
