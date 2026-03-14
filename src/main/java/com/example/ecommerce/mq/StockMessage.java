package com.example.ecommerce.mq;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMessage implements Serializable {
    private Long productId;
    private Integer quantity;
    private String type; // DEDUCT: 扣减, REVERT: 恢复
}
