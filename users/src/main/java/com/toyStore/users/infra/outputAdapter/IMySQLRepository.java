package com.toyStore.users.infra.outputAdapter;

import com.toyStore.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface IMySQLRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u")
    public List<User> findAllUsers(Pageable pageable);
    public User findByEmail(String email);
}
