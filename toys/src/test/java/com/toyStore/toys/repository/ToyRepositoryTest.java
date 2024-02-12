package com.toyStore.toys.repository;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.toyStore.toys.application.exception.ToyNotFoundException;
import com.toyStore.toys.domain.ECategory;
import com.toyStore.toys.domain.Toy;
import com.toyStore.toys.domain.ToyImage;
import com.toyStore.toys.infra.outputAdapter.IMySQLRepository;
import com.toyStore.toys.infra.outputport.ToyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ToyRepositoryTest {

    @Mock
    private IMySQLRepository myRepository;

    @InjectMocks
    private ToyRepository toyRepository;

    private Toy toy;

    private MultipartFile[] files;

    @Value("${server.port")
    private int port;

    @BeforeEach
    void setup() throws IOException {

        Path path = Paths.get("src/test/java/resources/domino.jpg");
        byte[] imageBytes = Files.readAllBytes(path);

        files = new MultipartFile[1];
        files[0] = new MockMultipartFile("file", "domino.jpg", "image/jpeg", imageBytes);

        List<ToyImage> images = new ArrayList<>();

        images.add(ToyImage.builder()
                .imgName("1707305987196-domino.jpg")
                .imgUrl("http://localhost:" + port + "/src/main/java/com/toyStore/toys/infra/uploads/1707305987196-domino.png")
                .build());

        toy = Toy.builder()
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

    @DisplayName("Test to save a toy")
    @Test
    void testSaveToy() {
        given(myRepository.save(toy)).willReturn(toy);

        Toy savedToy = toyRepository.save(toy);

        verify(myRepository, times(1)).save(toy);

        assertEquals(toy, savedToy);
    }

    @DisplayName("Test to retrieve a toy by ID")
    @Test
    void testGetById() throws ToyNotFoundException {
        Long id = 1L;

        given(myRepository.findById(anyLong())).willReturn(Optional.ofNullable(toy));

        Toy getToy = toyRepository.getById(id);

        assertEquals(toy, getToy);
        verify(myRepository, times(1)).findById(id);
    }

    @DisplayName("Test to retrieve a toy by ID not found")
    @Test
    void testGetByIdNotFound() {
        Long id = 1L;

        given(myRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ToyNotFoundException.class, () ->
                toyRepository.getById(id));
    }

    @DisplayName("Test to retrieve toys with pagination")
    @Test
    void testGetAll(){
        int page = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(page, size);

        given(myRepository.findAllToy(pageable)).willReturn(List.of(toy));

        List<Toy> listToys = toyRepository.getAll(page, size);

        verify(myRepository, times(1)).findAllToy(any(Pageable.class));
        assertThat(listToys.size()).isEqualTo(1);
    }

    @DisplayName("Test to retrieve toys by category")
    @Test
    void testGetByCategory(){
        int page = 0;
        int size = 1;
        ECategory eCategory = ECategory.JUEGOSDEMESA;
        Pageable pageable = PageRequest.of(page, size);

        given(myRepository.findByCategoryEquals(eCategory, pageable)).willReturn(List.of(toy));

        List<Toy> listToys = toyRepository.getByCategory(eCategory, page, size);

        verify(myRepository, times(1)).findByCategoryEquals(any(ECategory.class), any(Pageable.class));
        assertThat(listToys.size()).isEqualTo(1);
    }

    @DisplayName("Test to delete a toy")
    @Test
    void testDelete(){
        Long id = 1L;

        willDoNothing().given(myRepository).deleteById(anyLong());

        toyRepository.delete(id);

        verify(myRepository, times(1)).deleteById(anyLong());
    }

    @DisplayName("Test to update stock")
    @Test
    void testUpdateStock(){
        Long id = 1L;
        int quantityToDeduct = 2;

        willDoNothing().given(myRepository).updateStock(anyLong(), anyInt());

        toyRepository.updateStock(id, quantityToDeduct);

        verify(myRepository, times(1)).updateStock(anyLong(), anyInt());
    }
}
