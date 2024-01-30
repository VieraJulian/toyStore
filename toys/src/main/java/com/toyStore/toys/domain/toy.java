package com.toyStore.toys.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
public class toy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(min = 2, max = 100)
    private String name;
    @NotEmpty
    private String brand;
    @NotEmpty
    @Size(min = 5, max = 500)
    private String description;
    @NotEmpty
    @Range(min = 1, max = 1000000)
    private BigDecimal price;
    @NotEmpty
    @Size(min = 1, max = 2)
    @Range(min = 0, max = 99)
    private String recommendedAge;
    @Size(min = 1, max = 100)
    private String batterie;
    @Size(min = 2, max = 100)
    private String material;
    @NotEmpty
    @Size(min = 1, max = 4)
    @Range(min = 0, max = 9999)
    private int stock;
    @NotEmpty
    @Size(min = 1, max = 2)
    @Range(min = 0, max = 80)
    private int discount;
    @NotEmpty
    private category category;

    @OneToMany(mappedBy = "toy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<toyImage> img;
}
