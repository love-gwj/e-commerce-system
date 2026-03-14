package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/list")
    public Result<IPage<Orders>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        IPage<Orders> page = ordersService.getOrderPage(userId, status, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<Orders> detail(@PathVariable Long id) {
        Orders order = ordersService.getOrderDetail(id);
        return Result.success(order);
    }

    @PostMapping("/create")
    public Result<Orders> create(@RequestBody Map<String, Long> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Long addressId = params.get("addressId");
        Orders order = ordersService.createOrder(userId, addressId);
        return Result.success(order);
    }

    @PutMapping("/cancel/{id}")
    public Result<?> cancel(@PathVariable Long id) {
        ordersService.cancelOrder(id);
        return Result.success();
    }

    @PutMapping("/confirm/{id}")
    public Result<?> confirm(@PathVariable Long id) {
        ordersService.confirmReceive(id);
        return Result.success();
    }
}
