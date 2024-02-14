package com.toyStore.users.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Size(min = 2, max = 50)
    private String username;
    @NotNull
    @Email
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    @Size(min = 2, max = 100)
    private String address;
    @Size(min = 10, max = 30)
    private String phone;
    private boolean admin;
}
