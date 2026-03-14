package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ecommerce.entity.Orders;

import java.util.List;

public interface OrdersService extends IService<Orders> {
    IPage<Orders> getOrderPage(Long userId, Integer status, Integer pageNum, Integer pageSize);
    Orders getOrderDetail(Long orderId);
    Orders createOrder(Long userId, Long addressId);
    void cancelOrder(Long orderId);
    void confirmReceive(Long orderId);
    
    // 发送订单消息
    void sendOrderMessage(Orders order);
}
