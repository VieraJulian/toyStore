package com.toyStore.orders.infra.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private String order_id;
    private BigDecimal price;
    private LocalDateTime date;
    private String codeOperation;
    private UserDTO user;
    private List<ProductDTO> products;

}