package com.toyStore.users.application;
import com.toyStore.users.application.exception.UserIsExistsException;
import com.toyStore.users.application.exception.UserNotFoundException;
import com.toyStore.users.domain.service.encryptor;
import com.toyStore.users.domain.User;
import com.toyStore.users.infra.dto.UserDTO;
import com.toyStore.users.infra.dto.UserUpdatingDTO;
import com.toyStore.users.infra.inputport.IUserInputport;
import com.toyStore.users.infra.outputport.IUserMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserUseCase implements IUserInputport {

    @Autowired
    private IUserMethods userMethods;

    @Override
    public UserDTO createUser(User user) throws UserIsExistsException {
        User userExists = userMethods.getByEmail(user.getEmail());

        if (userExists != null) {
            throw new UserIsExistsException("User already exists");
        }

        user.setPassword(encryptor.encrypt(user.getPassword()));

        if (user.getEmail().toLowerCase().contains("@toystore.com")) {
            user.setAdmin(true);
        } else {
            user.setAdmin(false);
        }

        User userCreated = userMethods.save(user);

        return UserDTO.builder()
                .id(userCreated.getId())
                .username(userCreated.getUsername())
                .email(userCreated.getEmail())
                .address(userCreated.getAddress())
                .phone(userCreated.getPhone())
                .admin(userCreated.isAdmin())
                .build();
    }

    @Override
    public UserDTO updateUser(Long id, UserUpdatingDTO user) throws UserNotFoundException {
        User userFound = userMethods.getById(id);

        if (user.getPassword() != null && user.getNewPassword() != null) {
            String userPassword = encryptor.encrypt(user.getPassword());

            if (userPassword.equals(userFound.getPassword())){
                userFound.setPassword(encryptor.encrypt(user.getNewPassword()));
            }

        }

        userFound.setUsername(user.getUsername());
        userFound.setAddress(user.getAddress());
        userFound.setPhone(user.getPhone());

        User EditedUser = userMethods.save(userFound);

        return UserDTO.builder()
                .id(EditedUser.getId())
                .username(EditedUser.getUsername())
                .email(EditedUser.getEmail())
                .address(EditedUser.getAddress())
                .phone(EditedUser.getPhone())
                .admin(EditedUser.isAdmin())
                .build();

    }

    @Override
    public UserDTO getUserById(Long id) throws UserNotFoundException {
        User userFound = userMethods.getById(id);

        return UserDTO.builder()
                .id(userFound.getId())
                .username(userFound.getUsername())
                .email(userFound.getEmail())
                .address(userFound.getAddress())
                .phone(userFound.getPhone())
                .admin(userFound.isAdmin())
                .build();
    }

    @Override
    public List<UserDTO> getAllUsers(int page, int size) {
        List<User> usersFound = userMethods.getAll(page, size);
        List<UserDTO> users = new ArrayList<>();

        for (User user : usersFound) {
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .phone(user.getPhone())
                    .admin(user.isAdmin())
                    .build();

            users.add(userDTO);
        }

        return users;

    }

    @Override
    public String deleteUser(Long id) {
        userMethods.delete(id);

        return "User with id " + id + "deleted";
    }
}
