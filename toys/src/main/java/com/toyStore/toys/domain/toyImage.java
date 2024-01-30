package com.toyStore.toys.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "toysImages")
public class toyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "toy_id")
    private toy toy;
    @NotEmpty
    private String img;
}
