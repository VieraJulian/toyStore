package com.toyStore.orders.domain;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    @NotNull
    private Long user_id;
    @NotNull
    @DecimalMin(value = "1.00")
    @DecimalMax(value = "999999.99")
    private BigDecimal price;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    @NotEmpty
    @Size(min = 1, max = 50)
    private String codeOperation;
}
