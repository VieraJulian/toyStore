package com.toyStore.orders.infra.outputport;

import com.toyStore.orders.infra.dto.UserDTO;

public interface IUserServicePort {

    public UserDTO getUser(Long id);
}
