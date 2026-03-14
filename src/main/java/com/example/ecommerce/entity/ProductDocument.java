package com.example.ecommerce.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "product")
public class ProductDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String description;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Double)
    private Double originalPrice;

    @Field(type = FieldType.Integer)
    private Integer stock;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Text)
    private String mainImage;

    @Field(type = FieldType.Integer)
    private Integer sales;

    @Field(type = FieldType.Integer)
    private Integer status;
}
