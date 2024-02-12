package com.toyStore.toys.repository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.toyStore.toys.domain.ECategory;
import com.toyStore.toys.domain.Toy;
import com.toyStore.toys.domain.ToyImage;
import com.toyStore.toys.infra.outputAdapter.IMySQLRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IMySQLRepositoryTest {

    @Autowired
    private IMySQLRepository myRepository;

    @Value("${server.port}")
    private int port;

    private Toy toy;

    @BeforeEach
    void setUp(){
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

    @DisplayName("Test to search for a toy by category")
    @Test
    void testFindByCategoryEquals(){
        ECategory eCategory = ECategory.JUEGOSDEMESA;
        Pageable pageable = PageRequest.of(0, 1);
        myRepository.save(toy);

        List<Toy> toyList = myRepository.findByCategoryEquals(eCategory, pageable);

        assertThat(toyList).isNotNull();
        assertThat(toyList.size()).isEqualTo(1);
    }

    @DisplayName("Test to retrieve all toys with pagination")
    @Test
    void testFindAllToy(){
        Pageable pageable = PageRequest.of(0, 1);

        List<Toy> toyList = myRepository.findAllToy(pageable);

        assertThat(toyList).isNotNull();
        assertThat(toyList.size()).isEqualTo(1);
    }

    @DisplayName("Test to edit the stock")
    @Test
    void testUpdateStock() {
        Long id = 2L;
        int quantityToDeduct = 7;

        myRepository.updateStock(id, quantityToDeduct);

        Toy toyUpdated = myRepository.findById(id).orElse(null);

        assertThat(toyUpdated.getStock()).isEqualTo(5);
    }
}
