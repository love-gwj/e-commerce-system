package com.example.ecommerce.mq.consumer;

import com.example.ecommerce.mq.OrderMessage;
import com.example.ecommerce.mq.StockMessage;
import com.example.ecommerce.service.OrderItemService;
import com.example.ecommerce.service.OrdersService;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "ecommerce-order-topic", consumerGroup = "ecommerce-consumer-group")
@RequiredArgsConstructor
public class OrderConsumer implements RocketMQListener<OrderMessage> {

    private final OrdersService ordersService;

    @Override
    public void onMessage(OrderMessage message) {
        log.info("收到订单消息: orderId={}, orderNo={}", message.getOrderId(), message.getOrderNo());
        
        switch (message.getStatus()) {
            case 0:
                log.info("新订单创建: {}", message.getOrderId());
                break;
            case 1:
                log.info("订单已支付: {}", message.getOrderId());
                break;
            case 2:
                log.info("订单已发货: {}", message.getOrderId());
                break;
            case 3:
                log.info("订单已完成: {}", message.getOrderId());
                break;
            case 4:
                log.info("订单已取消: {}", message.getOrderId());
                break;
            default:
                log.warn("未知订单状态: {}", message.getStatus());
        }
    }
}
