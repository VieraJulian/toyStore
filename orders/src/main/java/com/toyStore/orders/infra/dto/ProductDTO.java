package com.toyStore.orders.infra.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private int discount;
    private BigDecimal priceWithDiscount;
    private int quantity;
}
