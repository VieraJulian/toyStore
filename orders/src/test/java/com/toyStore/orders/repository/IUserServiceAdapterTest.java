package com.toyStore.orders.repository;

import com.toyStore.orders.infra.dto.UserDTO;
import com.toyStore.orders.infra.outputAdapter.IUserServiceAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IUserServiceAdapterTest {

    @Mock
    private IUserServiceAdapter userServiceAdapter;

    @Test
    void testGetUser(){
        Long id = 1L;

        UserDTO userDTO = UserDTO.builder()
                .username("Juan Perez")
                .email("juan@gmail.com")
                .address("123 Main St")
                .phone("120301281908")
                .build();

        when(userServiceAdapter.getUser(anyLong())).thenReturn(userDTO);

        userServiceAdapter.getUser(id);

        verify(userServiceAdapter, times(1)).getUser(id);
    }
}
