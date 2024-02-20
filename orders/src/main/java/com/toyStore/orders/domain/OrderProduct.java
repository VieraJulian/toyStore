package com.toyStore.orders.domain;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "ordersProducts")
public class OrderProduct {

    @Id
    private String id;
    @NotNull
    private String order_id;
    @NotNull
    private Long product_id;
    @NotNull
    @Min(value = 1)
    @Max(value = 9999)
    private int quantity;
}
