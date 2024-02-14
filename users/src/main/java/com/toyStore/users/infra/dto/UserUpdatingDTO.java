package com.toyStore.users.infra.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdatingDTO {
    private Long id;
    private String username;
    private String password;
    private String newPassword;
    private String address;
    private String phone;
}
