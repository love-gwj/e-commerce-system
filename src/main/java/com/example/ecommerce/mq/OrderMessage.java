package com.example.ecommerce.mq;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage implements Serializable {
    private Long orderId;
    private Long userId;
    private String orderNo;
    private Double totalAmount;
    private Integer status;
}
