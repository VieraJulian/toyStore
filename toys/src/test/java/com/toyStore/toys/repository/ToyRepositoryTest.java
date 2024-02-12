package com.toyStore.toys.repository;

import static org.assertj.core.api.Assertions.*;

import com.toyStore.toys.domain.ECategory;
import com.toyStore.toys.domain.Toy;
import com.toyStore.toys.domain.ToyImage;
import com.toyStore.toys.infra.outputport.ToyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ToyRepositoryTest {

    @Autowired
    private ToyRepository toyRepository;

    @DisplayName("Test to create a toy")
    @Test
    void testSaveToy(){

        List<ToyImage> images = new ArrayList<>();

        Toy toy = Toy.builder()
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
                .build();

        images.add(ToyImage.builder()
                        .toy(toy)
                        .imgUrl("http://localhost:3000/src/main/java/com/toyStore/toys/infra/uploads/1707164411463-domino.png")
                        .imgName("1707164411463-domino.png")
                        .build());

        toy.setImgs(images);

        Toy toyCreated = toyRepository.save(toy);

        assertThat(toyCreated).isNotNull();
        assertThat(toyCreated.getId()).isGreaterThan(0);
    }
}
