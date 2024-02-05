package com.toyStore.toys.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "toys")
public class Toy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(min = 2, max = 100)
    private String name;
    @NotEmpty
    @Size(min = 1, max = 100)
    private String brand;
    @NotEmpty
    @Size(min = 5, max = 500)
    private String description;
    @NotNull
    @DecimalMin(value = "1.00")
    @DecimalMax(value = "999999.99")
    private BigDecimal price;
    @NotNull
    @DecimalMin(value = "1.00", inclusive = true)
    @DecimalMax(value = "999999.99", inclusive = true)
    private BigDecimal priceWithDiscount;
    @NotNull
    @DecimalMin(value = "0", inclusive = true)
    @DecimalMax(value = "80", inclusive = true)
    private int discount;
    @NotEmpty
    @Size(min = 1, max = 50)
    private String recommendedAge;
    @Size(min = 1, max = 100)
    private String batterie;
    @Size(min = 2, max = 100)
    private String material;
    @NotNull
    @DecimalMin(value = "1", inclusive = true)
    @DecimalMax(value = "9999", inclusive = true)
    private int stock;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ECategory category;
    @OneToMany(mappedBy = "toy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ToyImage> imgs;
}
