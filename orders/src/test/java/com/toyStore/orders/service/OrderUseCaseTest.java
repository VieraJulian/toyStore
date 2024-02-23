package com.toyStore.orders.service;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.toyStore.orders.application.OrderUseCase;
import com.toyStore.orders.application.exception.OrderNotFoundException;
import com.toyStore.orders.application.exception.ToyNotFoundException;
import com.toyStore.orders.application.exception.UpdateStockException;
import com.toyStore.orders.application.exception.UserNotFoundException;
import com.toyStore.orders.domain.Order;
import com.toyStore.orders.domain.OrderProduct;
import com.toyStore.orders.infra.dto.*;
import com.toyStore.orders.infra.outputport.IOrderProductRepository;
import com.toyStore.orders.infra.outputport.IOrderRepository;
import com.toyStore.orders.infra.outputport.IToyServicePort;
import com.toyStore.orders.infra.outputport.IUserServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderUseCaseTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderProductRepository orderProductRepository;

    @Mock
    private IToyServicePort toySrv;

    @Mock
    private IUserServicePort userServ;

    @InjectMocks
    private OrderUseCase useCase;

    private OrderRequest orderRequest;

    private UserDTO userDTO;

    private ProductDTO productDTO;

    private ToyDTO toyDTO;

    private Order order;

    private OrderProduct orderProduct;

    @BeforeEach
    void setup(){
        List<ProductRequest> products = new ArrayList<>();

        ProductRequest pr1 = new ProductRequest();
        pr1.setProduct_id(4L);
        pr1.setQuantity(2);

        products.add(pr1);

        orderRequest = OrderRequest.builder()
                .user_id(1L)
                .products(products)
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .username("Juan Perez")
                .email("jp@gmail.com")
                .address("Calle 123 Sbn")
                .phone("72384516230")
                .build();

        productDTO = ProductDTO.builder()
                .id(4L)
                .name("Tiburón de peluche")
                .price(BigDecimal.valueOf(20000))
                .discount(10)
                .priceWithDiscount(BigDecimal.valueOf(18000))
                .quantity(2)
                .build();

        toyDTO = ToyDTO.builder()
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

        order = Order.builder()
                .id("65d527c25e0fdd2241e07e31")
                .user_id(1L)
                .price(BigDecimal.valueOf(36000))
                .date(LocalDateTime.of(2024, 02, 22, 17, 30, 00))
                .codeOperation("af630d3c-04b2-4233-8a58-ee2b8c503db8")
                .build();

        orderProduct = OrderProduct.builder()
                .id("64d527c24e0fdd2241e08")
                .order_id("65d527c25e0fdd2241e07e31")
                .product_id(4L)
                .quantity(2)
                .build();
    }

    @Test
    void testCreateOrder() throws UserNotFoundException, ToyNotFoundException, UpdateStockException {
        when(userServ.getUser(anyLong())).thenReturn(userDTO);
        when(toySrv.getToy(anyLong())).thenReturn(toyDTO);
        when(toySrv.updateStock(anyLong(), anyInt())).thenReturn("Stock updated");
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderProductRepository.save(any(OrderProduct.class))).thenReturn(orderProduct);

        OrderDTO orderCreated = useCase.createOrder(orderRequest);

        assertThat(orderCreated).isNotNull();
        verify(userServ, times(1)).getUser(anyLong());
        verify(toySrv, times(1)).getToy(anyLong());
        verify(toySrv, times(1)).updateStock(anyLong(), anyInt());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderProductRepository, times(1)).save(any(OrderProduct.class));
    }

    @Test
    void testCreateOrderUserException(){
        when(userServ.getUser(anyLong())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            useCase.createOrder(orderRequest);
        });

         verify(orderRepository, never()).save(any(Order.class));
         verify(orderProductRepository, never()).save(any(OrderProduct.class));
    }

    @Test
    void testCreateOrderToyException(){
        when(userServ.getUser(anyLong())).thenReturn(userDTO);
        when(toySrv.getToy(anyLong())).thenReturn(null);

        assertThrows(ToyNotFoundException.class, () -> {
            useCase.createOrder(orderRequest);
        });

        verify(orderRepository, never()).save(any(Order.class));
        verify(orderProductRepository, never()).save(any(OrderProduct.class));
    }

    @Test
    void testCreateOrderStockException(){
        when(userServ.getUser(anyLong())).thenReturn(userDTO);
        when(toySrv.getToy(anyLong())).thenReturn(toyDTO);
        when(toySrv.updateStock(anyLong(), anyInt())).thenReturn(null);

        assertThrows(UpdateStockException.class, () -> {
            useCase.createOrder(orderRequest);
        });

        verify(orderRepository, never()).save(any(Order.class));
        verify(orderProductRepository, never()).save(any(OrderProduct.class));
    }

    @Test
    void testGetOrderById() throws OrderNotFoundException, UserNotFoundException, ToyNotFoundException {
        String id = "65d527c25e0fdd2241e07e31";

        when(orderRepository.getById(anyString())).thenReturn(order);
        when(userServ.getUser(anyLong())).thenReturn(userDTO);
        when(orderProductRepository.getByOrderId(anyString())).thenReturn(List.of(orderProduct));
        when(toySrv.getToy(anyLong())).thenReturn(toyDTO);

        OrderDTO orderDTO = useCase.getOrderById(id);

        assertThat(orderDTO).isNotNull();
        verify(orderRepository, times(1)).getById(anyString());
        verify(userServ, times(1)).getUser(anyLong());
        verify(orderProductRepository, times(1)).getByOrderId(anyString());
        verify(toySrv, times(1)).getToy(anyLong());
    }

    @Test
    void testGetOrderByIdOrderException(){
        String id = "65d527c25e0fdd2241e07e31";

        when(orderRepository.getById(anyString())).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> {
            useCase.getOrderById(id);
        });
    }

    @Test
    void testGetOrderByIdUserException(){
        String id = "65d527c25e0fdd2241e07e31";

        when(orderRepository.getById(anyString())).thenReturn(order);
        when(userServ.getUser(anyLong())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            useCase.getOrderById(id);
        });
    }

    @Test
    void testGetOrderByIdToyException(){
        String id = "65d527c25e0fdd2241e07e31";

        when(orderRepository.getById(anyString())).thenReturn(order);
        when(userServ.getUser(anyLong())).thenReturn(userDTO);
        when(orderProductRepository.getByOrderId(anyString())).thenReturn(List.of(orderProduct));
        when(toySrv.getToy(anyLong())).thenReturn(null);

        assertThrows(ToyNotFoundException.class, () -> {
            useCase.getOrderById(id);
        });
    }

    @Test
    void testGetOrderByCodeOperation() throws OrderNotFoundException, UserNotFoundException, ToyNotFoundException {
        String code = "af630d3c-04b2-4233-8a58-ee2b8c503db8";

        when(orderRepository.getByCodeOperation(anyString())).thenReturn(order);
        when(userServ.getUser(anyLong())).thenReturn(userDTO);
        when(orderProductRepository.getByOrderId(anyString())).thenReturn(List.of(orderProduct));
        when(toySrv.getToy(anyLong())).thenReturn(toyDTO);

        OrderDTO orderDTO = useCase.getOrderByCodeOperation(code);

        assertThat(orderDTO).isNotNull();
        verify(orderRepository, times(1)).getByCodeOperation(anyString());
        verify(userServ, times(1)).getUser(anyLong());
        verify(orderProductRepository, times(1)).getByOrderId(anyString());
        verify(toySrv, times(1)).getToy(anyLong());
    }

    @Test
    void testGetOrderByCodeOperationOrderException(){
        String code = "af630d3c-04b2-4233-8a58-ee2b8c503db8";

        when(orderRepository.getByCodeOperation(anyString())).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> {
            useCase.getOrderByCodeOperation(code);
        });
    }

    @Test
    void testGetOrderByCodeOperationUserException(){
        String code = "af630d3c-04b2-4233-8a58-ee2b8c503db8";

        when(orderRepository.getByCodeOperation(anyString())).thenReturn(order);
        when(userServ.getUser(anyLong())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            useCase.getOrderByCodeOperation(code);
        });
    }

    @Test
    void testGetOrderByCodeOperationToyException(){
        String code = "af630d3c-04b2-4233-8a58-ee2b8c503db8";

        when(orderRepository.getByCodeOperation(anyString())).thenReturn(order);
        when(userServ.getUser(anyLong())).thenReturn(userDTO);
        when(orderProductRepository.getByOrderId(anyString())).thenReturn(List.of(orderProduct));
        when(toySrv.getToy(anyLong())).thenReturn(null);

        assertThrows(ToyNotFoundException.class, () -> {
            useCase.getOrderByCodeOperation(code);
        });

    }

    @Test
    void testGetAllOrders() throws UserNotFoundException, ToyNotFoundException {
        int page = 0;
        int size = 2;

        Order order2 = Order.builder()
                .id("65d527c25e0fdd2241e07e31")
                .user_id(1L)
                .price(BigDecimal.valueOf(70000))
                .date(LocalDateTime.of(2024, 04, 13, 12, 19, 00))
                .codeOperation("af630d3c-u6s0-4233-8a58-ee2b8c503mw5")
                .build();

        when(orderRepository.getAllOrders(anyInt(), anyInt())).thenReturn(List.of(order, order2));
        when(userServ.getUser(anyLong())).thenReturn(userDTO);
        when(orderProductRepository.getByOrderId(anyString())).thenReturn(List.of(orderProduct));
        when(toySrv.getToy(anyLong())).thenReturn(toyDTO);

        List<OrderDTO> orderList = useCase.getAllOrders(page, size);

        assertThat(orderList).isNotEmpty();
        assertThat(orderList.size()).isEqualTo(2);
    }

    @Test
    void testGetAllOrdersUserException(){
        int page = 0;
        int size = 2;

        Order order2 = Order.builder()
                .id("65d527c25e0fdd2241e07e31")
                .user_id(1L)
                .price(BigDecimal.valueOf(70000))
                .date(LocalDateTime.of(2024, 04, 13, 12, 19, 00))
                .codeOperation("af630d3c-u6s0-4233-8a58-ee2b8c503mw5")
                .build();

        when(orderRepository.getAllOrders(anyInt(), anyInt())).thenReturn(List.of(order, order2));
        when(userServ.getUser(anyLong())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            useCase.getAllOrders(page, size);
        });
    }

    @Test
    void testGetAllOrdersToyException(){
        int page = 0;
        int size = 2;

        Order order2 = Order.builder()
                .id("65d527c25e0fdd2241e07e31")
                .user_id(1L)
                .price(BigDecimal.valueOf(70000))
                .date(LocalDateTime.of(2024, 04, 13, 12, 19, 00))
                .codeOperation("af630d3c-u6s0-4233-8a58-ee2b8c503mw5")
                .build();

        when(orderRepository.getAllOrders(anyInt(), anyInt())).thenReturn(List.of(order, order2));
        when(userServ.getUser(anyLong())).thenReturn(userDTO);
        when(orderProductRepository.getByOrderId(anyString())).thenReturn(List.of(orderProduct));
        when(toySrv.getToy(anyLong())).thenReturn(null);

        assertThrows(ToyNotFoundException.class, () -> {
            useCase.getAllOrders(page, size);
        });
    }

    @Test
    void testDeleteOrder() throws OrderNotFoundException {
        String id = "65d527c25e0fdd2241e07e31";

        when(orderRepository.getById(anyString())).thenReturn(order);
        when(orderProductRepository.getByOrderId(anyString())).thenReturn(List.of(orderProduct));
        willDoNothing().given(orderProductRepository).delete(anyString());
        willDoNothing().given(orderRepository).delete(anyString());

        String msj = useCase.deleteOrder(id);

        assertThat(msj).isEqualTo("Order deleted success");
        verify(orderRepository, times(1)).delete(anyString());
    }

    @Test
    void testDeleteOrderNotFoundException(){
        String id = "65d527c25e0fdd2241e07e31";

        when(orderRepository.getById(anyString())).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> {
            useCase.deleteOrder(id);
        });

        verify(orderProductRepository, never()).delete(anyString());
        verify(orderRepository, never()).delete(anyString());
    }
}
