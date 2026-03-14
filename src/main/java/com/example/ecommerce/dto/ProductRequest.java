package com.example.ecommerce.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "商品名称不能为空")
    @Length(max = 100, message = "商品名称长度不能超过100")
    private String name;

    @Length(max = 500, message = "商品描述长度不能超过500")
    private String description;

    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    private BigDecimal price;

    @NotNull(message = "商品原价不能为空")
    @DecimalMin(value = "0.01", message = "商品原价必须大于0")
    private BigDecimal originalPrice;

    @NotNull(message = "库存不能为空")
    @Range(min = 0, max = 999999, message = "库存必须在0-999999之间")
    private Integer stock;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @Length(max = 255, message = "主图URL长度不能超过255")
    private String mainImage;
}
