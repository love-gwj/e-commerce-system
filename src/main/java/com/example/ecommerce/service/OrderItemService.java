package com.example.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ecommerce.entity.OrderItem;

import java.util.List;

public interface OrderItemService extends IService<OrderItem> {
    List<OrderItem> getByOrderId(Long orderId);
}
