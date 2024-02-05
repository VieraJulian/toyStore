package com.toyStore.toys.infra.dto;

import com.toyStore.toys.domain.ToyImage;
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
    private int discount;
    private String recommendedAge;
    private String batterie;
    private String material;
    private int stock;
    private String category;
    private List<ToyImage> imgs;
}
