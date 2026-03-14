package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.OrdersMapper;
import com.example.ecommerce.mq.OrderMessage;
import com.example.ecommerce.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ProductService productService;

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public IPage<Orders> getOrderPage(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        Page<Orders> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, userId);
        
        if (status != null) {
            wrapper.eq(Orders::getStatus, status);
        }
        
        wrapper.orderByDesc(Orders::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public Orders getOrderDetail(Long orderId) {
        Orders order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        List<OrderItem> orderItems = orderItemService.getByOrderId(orderId);
        return order;
    }

    @Override
    @Transactional
    public Orders createOrder(Long userId, Long addressId) {
        List<Cart> cartList = cartService.getCartList(userId);
        
        if (cartList == null || cartList.isEmpty()) {
            throw new BusinessException("购物车为空");
        }
        
        Address address = addressService.getById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在");
        }
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        Orders order = new Orders();
        order.setUserId(userId);
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress());
        order.setStatus(0);
        
        for (Cart cart : cartList) {
            if (!cart.getSelected()) {
                continue;
            }
            
            Product product = productService.getById(cart.getProductId());
            if (product == null || product.getStatus() == 0) {
                throw new BusinessException("商品不存在: " + product.getName());
            }
            
            if (product.getStock() < cart.getQuantity()) {
                throw new BusinessException("商品库存不足: " + product.getName());
            }
            
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(cart.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            
            productService.updateStock(cart.getProductId(), -cart.getQuantity());
        }
        
        order.setTotalAmount(totalAmount);
        save(order);
        
        for (Cart cart : cartList) {
            if (!cart.getSelected()) {
                continue;
            }
            
            Product product = productService.getById(cart.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            orderItemService.save(orderItem);
        }
        
        cartService.clearCart(userId);
        
        // 发送订单创建消息
        sendOrderMessage(order);
        
        return order;
    }

    @Override
    public void cancelOrder(Long orderId) {
        Orders order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (order.getStatus() != 0) {
            throw new BusinessException("订单无法取消");
        }
        
        List<OrderItem> orderItems = orderItemService.getByOrderId(orderId);
        for (OrderItem item : orderItems) {
            productService.updateStock(item.getProductId(), item.getQuantity());
        }
        
        order.setStatus(4);
        order.setCancelTime(LocalDateTime.now());
        updateById(order);
    }

    @Override
    public void confirmReceive(Long orderId) {
        Orders order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        if (order.getStatus() != 2) {
            throw new BusinessException("订单状态异常");
        }
        
        order.setStatus(3);
        order.setReceiveTime(LocalDateTime.now());
        updateById(order);
    }

    @Override
    public void sendOrderMessage(Orders order) {
        try {
            OrderMessage message = new OrderMessage();
            message.setOrderId(order.getId());
            message.setUserId(order.getUserId());
            message.setOrderNo(order.getOrderNo());
            message.setTotalAmount(order.getTotalAmount().doubleValue());
            message.setStatus(order.getStatus());
            
            rocketMQTemplate.asyncSend("ecommerce-order-topic", MessageBuilder.withPayload(message).build(), null);
            log.info("订单消息发送成功: {}", order.getId());
        } catch (Exception e) {
            log.error("订单消息发送失败: {}", order.getId(), e);
        }
    }
}
