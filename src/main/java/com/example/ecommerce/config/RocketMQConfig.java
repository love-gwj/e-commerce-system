package com.example.ecommerce.config;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

@Configuration
public class RocketMQConfig {
}

@RocketMQTransactionListener(txProducerGroup = "ecommerce-producer-group")
class TransactionListenerImpl implements RocketMQLocalTransactionListener {
    
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        return RocketMQLocalTransactionState.COMMIT;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        return RocketMQLocalTransactionState.COMMIT;
    }
}
