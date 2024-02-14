package com.toyStore.users.infra.outputAdapter;

import com.toyStore.users.application.exception.UserNotFoundException;
import com.toyStore.users.domain.User;
import com.toyStore.users.infra.outputAdapter.IMySQLRepository;
import com.toyStore.users.infra.outputport.IUserMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepository implements IUserMethods {

    @Autowired
    private IMySQLRepository myRepository;

    @Override
    public User save(User user) {
        return myRepository.save(user);
    }

    @Override
    public User getById(Long id) throws UserNotFoundException {
        return myRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public List<User> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return myRepository.findAllUsers(pageable);
    }

    @Override
    public User getByEmail(String email) {
        return myRepository.findByEmail(email);
        // User userFound = myRepository.findByEmail(email);
        // return userFound != null ? userFound : null;
    }

    @Override
    public void delete(Long id) {
        myRepository.deleteById(id);
    }
}
