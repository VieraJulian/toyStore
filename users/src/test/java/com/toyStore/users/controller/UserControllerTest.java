package com.toyStore.users.controller;

import com.toyStore.users.domain.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.hasSize;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebClient
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private User user1;

    @BeforeEach
    void setup() {
        user1 = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password("1234")
                .address("Calle San Martin, altura 1209")
                .phone("0123456789")
                .build();
    }

    @Order(3)
    @Test
    void testGetUserById(){
        webTestClient.get().uri("/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.email").isEqualTo("user1@example.com")
                .jsonPath("$.username").isEqualTo("user0")
                .jsonPath("$.address").isEqualTo("Calle San Martin, altura 1219")
                .jsonPath("$.phone").isEqualTo("8888888888")
                .jsonPath("$.admin").isEqualTo(false);
    }

    @Order(4)
    @Test
    void testGetAllUsers(){
        webTestClient.get().uri("/users/all?page=0&size=2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].username").isEqualTo("user0")
                .jsonPath("$[0].email").isEqualTo("user1@example.com")
                .jsonPath("$").value(hasSize(1));

    }

    @Order(1)
    @Test
    void testUserCreate(){
        webTestClient.post().uri("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.username").isEqualTo("user1");
    }

    @Order(2)
    @Test
    void testUserUpdate(){
        User userInfo = User.builder()
                .username("user0")
                .email("user1@example.com")
                .password("1234")
                .address("Calle San Martin, altura 1219")
                .phone("8888888888")
                .build();

        webTestClient.put().uri("/users/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userInfo)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.username").isEqualTo("user0")
                .jsonPath("$.address").isEqualTo("Calle San Martin, altura 1219")
                .jsonPath("$.phone").isEqualTo("8888888888");
    }

    @Order(5)
    @Test
    void testDeleteUser(){
        webTestClient.delete().uri("/users/delete/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class).isEqualTo("User with id 1 deleted");
    }
}
