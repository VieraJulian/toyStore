package com.toyStore.orders.infra.outputAdapter;

import com.toyStore.orders.infra.dto.UserDTO;
import com.toyStore.orders.infra.outputport.IUserServicePort;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-srv")
public interface IUserServiceAdapter extends IUserServicePort {

    @Override
    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable Long id);
}
