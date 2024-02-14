package com.toyStore.users.infra.outputport;

import com.toyStore.users.application.exception.UserNotFoundException;
import com.toyStore.users.domain.User;

import java.awt.print.Pageable;
import java.util.List;

public interface IUserMethods {

    public User save(User user);

    public User getById(Long id) throws UserNotFoundException;

    public List<User> getAll(int page, int size);

    public User getByEmail(String email);

    public void delete(Long id);
}
