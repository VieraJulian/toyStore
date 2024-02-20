package com.toyStore.orders.infra.dto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToyDTO {

    private Long id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private BigDecimal priceWithDiscount;
    private int discount;
    private String batterie;
    private String material;
    private int stock;
    private String category;
    private List<ToyImageDTO> imgs;
    private int quantity;
}

class ToyImageDTO {

    private Long id;
    private ToyDTO toy;
    private String imgUrl;
    private String imgName;
}