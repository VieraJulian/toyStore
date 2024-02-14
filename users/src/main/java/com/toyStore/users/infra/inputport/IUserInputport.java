package com.toyStore.users.infra.inputport;

import com.toyStore.users.application.exception.UserIsExistsException;
import com.toyStore.users.application.exception.UserNotFoundException;
import com.toyStore.users.domain.User;
import com.toyStore.users.infra.dto.UserDTO;
import com.toyStore.users.infra.dto.UserUpdatingDTO;

import java.util.List;

public interface IUserInputport {

    public UserDTO createUser (User user) throws UserIsExistsException;

    public UserDTO updateUser (Long id, UserUpdatingDTO user) throws UserNotFoundException;

    public UserDTO getUserById(Long id) throws UserNotFoundException;

    public List<UserDTO> getAllUsers(int page, int size);

    public String deleteUser(Long id);

}
