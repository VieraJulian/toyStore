package com.toyStore.toys.controller;

import com.toyStore.toys.domain.Toy;
import com.toyStore.toys.infra.dto.ToyDTO;
import com.toyStore.toys.infra.inputAdapter.ToyController;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private ToyDTO toyDTO;

    private MultipartFile[] files;

    @BeforeEach
    void setup() throws IOException {
        toyDTO = ToyDTO.builder()
                .name("Juego De Mesa Dominó Multicolor 28 Fichas")
                .brand("Ruibal")
                .description("DOMINÓ MULTICOLOR La combinación de colores y cantidades hacen de este dominó un juego ideal para niños de más de 5 años. Contenido: 28 fichas plásticas. Edad + 5 j Jugadores 2 a 4 Medidas 180 x 240 x 35 mm")
                .price(BigDecimal.valueOf(3000))
                .discount(10)
                .recommendedAge("+3")
                .batterie("No")
                .material("Plástico")
                .stock(12)
                .category("juegosdemesa")
                .build();

        Path path = Paths.get("src/test/java/resources/domino.jpg");
        String name = "file";
        String originalFileName = "domino.jpg";
        String contentType = "image/jpeg";

        byte[] content = Files.readAllBytes(path);
        MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content);

        files = new MultipartFile[1];
        files[0] = multipartFile;
    }

    @Test
    @Order(2)
    void testGetToyById(){
        webTestClient.get().uri("http://localhost:3000/toys/1")
                .exchange()

                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.priceWithDiscount").isEqualTo(2700);
    }

    @Test
    @Order(3)
    void testGetAllToys(){
        webTestClient.get().uri("http://localhost:3000/toys/all?page=0&size=1")
                .exchange()

                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Juego De Mesa Dominó Multicolor 28 Fichas")
                .jsonPath("$[0].priceWithDiscount").isEqualTo(2700)
                .jsonPath("$").value(hasSize(1));
    }

    @Test
    @Order(4)
    void testAllToysByCategory(){
        webTestClient.get().uri("http://localhost:3000/toys/category/juegosdemesa?page=0&size=1")
                .exchange()

                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Toy.class)
                .consumeWith(response -> {
                    List<Toy> toys = response.getResponseBody();

                    assertEquals(1, toys.size());
                    assertNotNull(toys);
                });
    }

    @Test
    @Order(1)
    void testCreateToy() throws IOException {
        ByteArrayResource resource = new ByteArrayResource(files[0].getBytes()) {
            @Override
            public String getFilename() {
                return files[0].getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", toyDTO.getName());
        body.add("brand", toyDTO.getBrand());
        body.add("description", toyDTO.getDescription());
        body.add("price", toyDTO.getPrice());
        body.add("discount", toyDTO.getDiscount());
        body.add("recommendedAge", toyDTO.getRecommendedAge());
        body.add("batterie", toyDTO.getBatterie());
        body.add("material", toyDTO.getMaterial());
        body.add("stock", toyDTO.getStock());
        body.add("category", toyDTO.getCategory());
        body.add("files", resource);

        webTestClient.post().uri("http://localhost:3000/toys/create")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.priceWithDiscount").isEqualTo(2700);
    }

    @Test
    @Order(5)
    void testUpdateToy() throws IOException {
        ByteArrayResource resource = new ByteArrayResource(files[0].getBytes()) {
            @Override
            public String getFilename() {
                return files[0].getOriginalFilename();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", "Juego De Mesa Dominó Multicolor");
        body.add("brand", toyDTO.getBrand());
        body.add("description", toyDTO.getDescription());
        body.add("price", toyDTO.getPrice());
        body.add("discount", toyDTO.getDiscount());
        body.add("recommendedAge", "+4");
        body.add("batterie", toyDTO.getBatterie());
        body.add("material", toyDTO.getMaterial());
        body.add("stock", toyDTO.getStock());
        body.add("category", toyDTO.getCategory());
        body.add("files", resource);

        webTestClient.put().uri("http://localhost:3000/toys/update/1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .exchange()

                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo("Juego De Mesa Dominó Multicolor")
                .jsonPath("$.recommendedAge").isEqualTo("+4");
    }

    @Test
    @Order(7)
    void testDeleteToy(){
        webTestClient.delete().uri("http://localhost:3000/toys/delete/1")
                .exchange()

                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class).isEqualTo("Toy with id 1 is deleted successfully");
    }

    @Test
    @Order(6)
    void testUpdateStock(){
        webTestClient.patch().uri("http://localhost:3000/toys/stock/1/2")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()

                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class).isEqualTo("Stock updated");
    }
}
