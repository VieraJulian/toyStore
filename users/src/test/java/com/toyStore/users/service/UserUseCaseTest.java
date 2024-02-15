package com.toyStore.users.service;

import com.toyStore.users.application.UserUseCase;
import com.toyStore.users.application.exception.UserIsExistsException;
import com.toyStore.users.application.exception.UserNotFoundException;
import com.toyStore.users.domain.User;
import com.toyStore.users.infra.dto.UserDTO;
import com.toyStore.users.infra.dto.UserUpdatingDTO;
import com.toyStore.users.infra.outputport.IUserMethods;
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
public class UserUseCaseTest {

    @Mock
    private IUserMethods userMethods;

    @InjectMocks
    private UserUseCase userUseCase;

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
    void testCreateUser() throws UserIsExistsException, UserNotFoundException {
        given(userMethods.getByEmail(anyString())).willReturn(null);
        given(userMethods.save(any(User.class))).willReturn(user1);

        UserDTO createdUser = userUseCase.createUser(user1);

        assertEquals(user1.getUsername(), createdUser.getUsername());
        assertEquals(user1.getEmail(), createdUser.getEmail());
        assertEquals(user1.getAddress(), createdUser.getAddress());
        assertEquals(user1.getPhone(), createdUser.getPhone());
        assertEquals(user1.isAdmin(), createdUser.isAdmin());
        verify(userMethods, times(1)).getByEmail(anyString());
        verify(userMethods, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserException(){
        given(userMethods.getByEmail(anyString())).willReturn(user1);

        assertThrows(UserIsExistsException.class, () -> userUseCase.createUser(user1));

        verify(userMethods, times(1)).getByEmail(anyString());
    }

    @Test
    void testUpdateUser() throws UserNotFoundException {
        Long id = 1L;

        UserUpdatingDTO userUpdatingDTO = UserUpdatingDTO.builder()
                .username("user1")
                .password("1234")
                .newPassword("1234")
                .address("Calle San Martin, altura 1209")
                .phone("0123456789")
                .build();

        given(userMethods.getById(anyLong())).willReturn(user1);
        given(userMethods.save(any(User.class))).willReturn(user1);

        UserDTO updatedUser = userUseCase.updateUser(id, userUpdatingDTO);

        assertEquals(user1.getUsername(), updatedUser.getUsername());
        assertEquals(user1.getEmail(), updatedUser.getEmail());
        assertEquals(user1.getAddress(), updatedUser.getAddress());
        assertEquals(user1.getPhone(), updatedUser.getPhone());
        assertEquals(user1.isAdmin(), updatedUser.isAdmin());
        verify(userMethods, times(1)).getById(anyLong());
        verify(userMethods, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() throws UserNotFoundException {
        Long id = 1L;

        UserUpdatingDTO userUpdatingDTO = UserUpdatingDTO.builder()
                .username("user1")
                .password("1234")
                .newPassword("1234")
                .address("Calle San Martin, altura 1209")
                .phone("0123456789")
                .build();

        given(userMethods.getById(anyLong())).willThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> userUseCase.updateUser(id, userUpdatingDTO));

        verify(userMethods, times(1)).getById(anyLong());
    }

    @Test
    void testGetUserById() throws UserNotFoundException {
        Long id = 1L;
        given(userMethods.getById(anyLong())).willReturn(user1);

        UserDTO foundUser = userUseCase.getUserById(id);

        assertEquals(user1.getUsername(), foundUser.getUsername());
        assertEquals(user1.getEmail(), foundUser.getEmail());
        assertEquals(user1.getAddress(), foundUser.getAddress());
        assertEquals(user1.getPhone(), foundUser.getPhone());
        assertEquals(user1.isAdmin(), foundUser.isAdmin());

        verify(userMethods, times(1)).getById(anyLong());
    }

    @Test
    void testGetUserByIdNotFound() throws UserNotFoundException {
        Long id = 1L;
        given(userMethods.getById(anyLong())).willThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> userUseCase.getUserById(id));

        verify(userMethods, times(1)).getById(anyLong());
    }

    @Test
    void testGetAllUsers(){
        int page = 0;
        int size = 2;

        User user2 = User.builder()
                .username("user2")
                .email("user2@example.com")
                .password("1234")
                .address("Calle San Martin, altura 1222")
                .phone("12938712780")
                .admin(false)
                .build();

        given(userMethods.getAll(anyInt(), anyInt())).willReturn(List.of(user1, user2));

        List<UserDTO> userDTOList = userUseCase.getAllUsers(page, size);

        assertThat(userDTOList.size()).isEqualTo(2);
        verify(userMethods, times(1)).getAll(anyInt(), anyInt());
    }

    @Test
    void testDeleteUser(){
        Long id = 2L;

        willDoNothing().given(userMethods).delete(id);

        userUseCase.deleteUser(id);

        verify(userMethods, times(1)).delete(anyLong());
    }
}
