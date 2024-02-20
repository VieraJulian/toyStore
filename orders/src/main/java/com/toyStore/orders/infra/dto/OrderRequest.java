package com.toyStore.orders.infra.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    private Long user_id;
    private List<ProductRequest> products;
}
