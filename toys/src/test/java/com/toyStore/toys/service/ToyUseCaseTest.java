package com.toyStore.toys.service;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.toyStore.toys.application.ToyUseCase;
import com.toyStore.toys.application.exception.ToyNotFoundException;
import com.toyStore.toys.domain.ECategory;
import com.toyStore.toys.domain.Toy;
import com.toyStore.toys.domain.ToyImage;
import com.toyStore.toys.infra.dto.ToyDTO;
import com.toyStore.toys.infra.outputport.IToyMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ToyUseCaseTest {

    @Mock
    private IToyMethods methods;

    @InjectMocks
    private ToyUseCase toyUseCase;

    @Value("${server.port}")
    private int port;

    private ToyDTO toyDTO;

    private MultipartFile[] files;

    private Toy expectedToy;

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
        byte[] imageBytes = Files.readAllBytes(path);

        files = new MultipartFile[1];
        files[0] = new MockMultipartFile("file", "domino.jpg", "image/jpeg", imageBytes);

        List<ToyImage> images = new ArrayList<>();

        images.add(ToyImage.builder()
                .imgName("1707305987196-domino.jpg")
                .imgUrl("http://localhost:" + port + "/src/main/java/com/toyStore/toys/infra/uploads/1707305987196-domino.png")
                .build());

        expectedToy = Toy.builder()
                .name("Juego De Mesa Dominó Multicolor 28 Fichas")
                .brand("Ruibal")
                .description("DOMINÓ MULTICOLOR La combinación de colores y cantidades hacen de este dominó un juego ideal para niños de más de 5 años. Contenido: 28 fichas plásticas. Edad + 5 j Jugadores 2 a 4 Medidas 180 x 240 x 35 mm")
                .price(BigDecimal.valueOf(3000))
                .priceWithDiscount(BigDecimal.valueOf(2700))
                .discount(10)
                .recommendedAge("+3")
                .batterie("No")
                .material("Plástico")
                .stock(12)
                .category(ECategory.JUEGOSDEMESA)
                .imgs(images)
                .build();
    }

    @DisplayName("Test to create a toy")
    @Test
    void testCreateToy() throws IOException {
        given(methods.save(any(Toy.class))).willReturn(expectedToy);

        Toy toyCreated = toyUseCase.createToy(toyDTO, files);

        assertThat(toyCreated).isNotNull();
        assertEquals(expectedToy, toyCreated);
        assertEquals(expectedToy.getImgs().get(0).getImgUrl(), toyCreated.getImgs().get(0).getImgUrl());
        verify(methods, times(1)).save(any(Toy.class));
    }

    @DisplayName("Test to create a toy with exception")
    @Test
    void testCreateToyException(){
        assertThrows(IllegalArgumentException.class, () -> {
            toyUseCase.createToy(toyDTO, new MultipartFile[0]);
        });
    }

    @DisplayName("Test to edit a toy")
    @Test
    void testUpdateToy() throws ToyNotFoundException, IOException {
        given(methods.getById(anyLong())).willReturn(expectedToy);

        Mockito.mockStatic(Files.class);
        given(Files.deleteIfExists(any(Path.class))).willReturn(true);
        given(Files.copy(any(InputStream.class), any(Path.class), any(CopyOption.class))).willReturn(123L);

        given(methods.save(any(Toy.class))).willReturn(expectedToy);

        Toy toyUpdated = toyUseCase.updateToy(1L, toyDTO, files);

        assertThat(toyUpdated).isNotNull();
        assertEquals(expectedToy, toyUpdated);
        assertEquals(expectedToy.getImgs().get(0).getImgUrl(), toyUpdated.getImgs().get(0).getImgUrl());
        verify(methods, times(1)).save(any(Toy.class));
    }

    @DisplayName("Test for editing toy not found")
    @Test
    void testUpdateToyNotFound() throws ToyNotFoundException, IOException {
        given(methods.getById(anyLong())).willThrow(new ToyNotFoundException("Toy not found"));

        assertThrows(ToyNotFoundException.class, () -> {
            toyUseCase.updateToy(1L, toyDTO, files);
        });

        verify(methods, never()).save(any(Toy.class));
    }

    @DisplayName("Test to fetch toy by id")
    @Test
    void testGetToyById() throws ToyNotFoundException {
        given(methods.getById(anyLong())).willReturn(expectedToy);

        Toy foundToy = toyUseCase.getToyById(1L);

        assertThat(foundToy).isNotNull();
        assertEquals(expectedToy, foundToy);
        verify(methods, times(1)).getById(anyLong());
    }

    @DisplayName("Test to fetch toy by id not found")
    @Test
    void testGetToyByIdNotFound() throws ToyNotFoundException {
        given(methods.getById(anyLong())).willThrow(new ToyNotFoundException("Toy not found"));

        assertThrows(ToyNotFoundException.class, () -> {
            toyUseCase.getToyById(1L);
        });
    }

    @DisplayName("Test to fetch all toys")
    @Test
    void testGetAllToys(){
        List<ToyImage> images = new ArrayList<>();

        images.add(ToyImage.builder()
                .imgName("1707305987180-gyarados.jpg")
                .imgUrl("http://localhost:" + port + "/src/main/java/com/toyStore/toys/infra/uploads/1707305987180-gyarados.png")
                .build());

        Toy toy = Toy.builder()
                .name("Figura Pokemon Gyarados - 25 Cm")
                .brand("Pokemon")
                .description("Pokemon Gyarados articulado Epic Battle Figure 25 cm de largo")
                .price(BigDecimal.valueOf(30000))
                .priceWithDiscount(BigDecimal.valueOf(27000))
                .discount(10)
                .recommendedAge("+4")
                .batterie("No")
                .material("Plástico")
                .stock(12)
                .category(ECategory.MUÑECOS)
                .imgs(images)
                .build();

        given(methods.getAll(anyInt(), anyInt())).willReturn(List.of(expectedToy, toy));

        List<Toy> toyList = toyUseCase.getAllToys(0, 1);

        assertThat(toyList).isNotEmpty();
        assertThat(toyList.size()).isEqualTo(2);
    }

    @DisplayName("Test to fetch all toys by category")
    @Test
    void testGetAllToysByCategory(){
        List<ToyImage> images = new ArrayList<>();

        images.add(ToyImage.builder()
                .imgName("1707305987180-gyarados.jpg")
                .imgUrl("http://localhost:" + port + "/src/main/java/com/toyStore/toys/infra/uploads/1707305987180-gyarados.png")
                .build());

        Toy toy = Toy.builder()
                .name("Figura Pokemon Gyarados - 25 Cm")
                .brand("Pokemon")
                .description("Pokemon Gyarados articulado Epic Battle Figure 25 cm de largo")
                .price(BigDecimal.valueOf(30000))
                .priceWithDiscount(BigDecimal.valueOf(27000))
                .discount(10)
                .recommendedAge("+4")
                .batterie("No")
                .material("Plástico")
                .stock(12)
                .category(ECategory.MUÑECOS)
                .imgs(images)
                .build();

        given(methods.getByCategory(any(ECategory.class), anyInt(), anyInt())).willReturn(List.of(toy));

        List<Toy> toyList = toyUseCase.getAllToysByCategory("muñecos", 0, 1);

        assertThat(toyList).isNotEmpty();
        assertThat(toyList.size()).isEqualTo(1);
    }

    @DisplayName("Test to delete a toy")
    @Test
    void testDeleteToy() throws ToyNotFoundException, IOException {
        given(methods.getById(anyLong())).willReturn(expectedToy);

        given(Files.deleteIfExists(any(Path.class))).willReturn(true);

        willDoNothing().given(methods).delete(anyLong());

        toyUseCase.deleteToy(1L);

        verify(methods, times(1)).delete(anyLong());
    }

    @DisplayName("Test to delete a toy not found")
    @Test
    void testDeleteToyNotFound() throws ToyNotFoundException, IOException {
        given(methods.getById(anyLong())).willThrow(new ToyNotFoundException("Toy not found"));

        assertThrows(ToyNotFoundException.class, () -> {
            toyUseCase.deleteToy(1L);
        });
    }

    @DisplayName("Test to update stock")
    @Test
    void testUpdateTest(){
        willDoNothing().given(methods).updateStock(anyLong(), anyInt());

        toyUseCase.updateStock(1L, 2);

        verify(methods, times(1)).updateStock(anyLong(), anyInt());
    }
}
