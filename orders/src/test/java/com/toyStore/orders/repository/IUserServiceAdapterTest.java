package com.toyStore.orders.repository;

import com.toyStore.orders.infra.dto.ToyDTO;
import com.toyStore.orders.infra.outputAdapter.IToyServiceAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class IUserServiceAdapterTest {

    @Mock
    private IToyServiceAdapter toyServiceAdapter;

    @Test
    void testGetToy(){
        Long id = 1L;

        ToyDTO toyDTO = ToyDTO.builder()
                .id(1L)
                .name("Toy Name")
                .brand("Toy Brand")
                .description("This is a description of the toy.")
                .price(BigDecimal.valueOf(19.99))
                .priceWithDiscount(BigDecimal.valueOf(9.99))
                .discount(50)
                .batterie("AA")
                .material("Plastic")
                .stock(100)
                .category("Action Figures")
                .imgs(null)
                .quantity(1)
                .build();

        when(toyServiceAdapter.getToy(anyLong())).thenReturn(toyDTO);

        toyServiceAdapter.getToy(id);

        verify(toyServiceAdapter, times(1)).getToy(id);
    }

    @Test
    void testUpdateStock(){
        Long productId = 1L;
        int quantity = 4;

        when(toyServiceAdapter.updateStock(anyLong(), anyInt())).thenReturn("Stock updated");

        toyServiceAdapter.updateStock(productId, quantity);

        verify(toyServiceAdapter, times(1)).updateStock(productId, quantity);
    }
}
