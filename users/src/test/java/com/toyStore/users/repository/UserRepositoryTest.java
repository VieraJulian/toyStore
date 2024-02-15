package com.toyStore.users.repository;

import com.toyStore.users.application.exception.UserNotFoundException;
import com.toyStore.users.domain.User;
import com.toyStore.users.infra.outputAdapter.IMySQLRepository;
import com.toyStore.users.infra.outputAdapter.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private IMySQLRepository myRepository;

    @InjectMocks
    private UserRepository userRepository;

    private User user1;

    @BeforeEach
    void setup() {
        user1 = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password("1234")
                .address("Calle San Martin, altura 1209")
                .phone("0123456789")
                .admin(false)
                .build();
    }

    @Test
    void testSave(){
        given(myRepository.save(any(User.class))).willReturn(user1);

        User savedUser = userRepository.save(user1);

        assertEquals(user1, savedUser);
        verify(myRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetById() throws UserNotFoundException {
        Long id = 1L;
        given(myRepository.findById(anyLong())).willReturn(Optional.ofNullable(user1));

        User FoundUser = userRepository.getById(id);

        assertEquals(user1, FoundUser);
        verify(myRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetByIdNotFound() throws UserNotFoundException {
        Long id = 1L;
        given(myRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userRepository.getById(id));

        verify(myRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAll(){
        int page = 0;
        int size = 2;

        User user2 = User.builder()
                .username("user2")
                .email("user2@example.com")
                .password("1234")
                .address("Calle San Martin, altura 1210")
                .phone("1111111111")
                .admin(false)
                .build();

        given(myRepository.findAllUsers(any(Pageable.class))).willReturn(List.of(user1, user2));

        List<User> userList = userRepository.getAll(page, size);

        assertEquals(userList.size(), 2);
        verify(myRepository, times(1)).findAllUsers(any(Pageable.class));
    }

    @Test
    void testGetByEmail(){
        String email = "user1@example.com";
        given(myRepository.findByEmail(anyString())).willReturn(user1);

        User FoundUser = userRepository.getByEmail(email);

        assertEquals(user1, FoundUser);
        verify(myRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void testDelete(){
        Long id = 1L;

        willDoNothing().given(myRepository).deleteById(anyLong());

        userRepository.delete(id);

        verify(myRepository, times(1)).deleteById(anyLong());
    }
}
