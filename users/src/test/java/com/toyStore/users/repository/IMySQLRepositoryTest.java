package com.toyStore.users.repository;

import com.toyStore.users.domain.User;
import com.toyStore.users.infra.outputAdapter.IMySQLRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

import java.util.*;

@DataJpaTest
public class IMySQLRepositoryTest {

    @Autowired
    private IMySQLRepository myRepository;

    private List<User> userList = new ArrayList<>();

    @BeforeEach
    void setup() {
        User user1 = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password("1234")
                .address("Calle San Martin, altura 1209")
                .phone("0123456789")
                .build();

        User user2 = User.builder()
                .username("user2")
                .email("user2@toystore.com")
                .password("1234")
                .address("Calle San Martin, altura 1210")
                .phone("0123456789")
                .build();

        userList.add(user1);
        userList.add(user2);
    }

    @DisplayName("Test to fetch all users")
    @Test
    void testFindAllUsers(){
        int page = 0;
        int size = 2;
        myRepository.save(userList.get(0));
        myRepository.save(userList.get(1));
        Pageable pageable = PageRequest.of(page, size);

        List<User> usersDB = myRepository.findAllUsers(pageable);

        assertThat(usersDB).isNotNull();
        assertThat(usersDB.size()).isEqualTo(2);
    }

    @DisplayName("Test to fetch by email user")
    @Test()
    void testFindByEmail(){
        User userSaved = myRepository.save(userList.get(0));
        String email = userSaved.getEmail();

        User userDB = myRepository.findByEmail(email);

        assertThat(userDB).isNotNull();
        assertThat(userDB.getId()).isEqualTo(1);
    }
}
