package com.toyStore.orders.repository;

import com.toyStore.orders.domain.Order;
import com.toyStore.orders.infra.outputAdapter.MongoOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@DataMongoTest
public class MongoOrderRepositoryTest {

    @Autowired
    private MongoOrderRepository mongoOrderRepository;

    private Order order;

    @BeforeEach
    void setup() {
        order = Order.builder()
                .user_id(1L)
                .price(BigDecimal.valueOf(10999))
                .date(LocalDateTime.of(2024, 02, 22, 17, 30, 00))
                .codeOperation("af630d3c-04b2-4233-8a58-ee2b8c503db7")
                .build();
    }

    @Test
    void testSave() {
        Order orderCreated = mongoOrderRepository.save(order);

        assertThat(orderCreated).isNotNull();
        assertThat(orderCreated.getUser_id()).isEqualTo(1L);
        assertThat(orderCreated.getPrice()).isEqualTo(BigDecimal.valueOf(10999));
        assertThat(orderCreated.getDate()).isEqualTo(LocalDateTime.of(2024, 02, 22, 17, 30, 00));
        assertThat(orderCreated.getCodeOperation()).isEqualTo("af630d3c-04b2-4233-8a58-ee2b8c503db7");
    }

    @Test
    void testGetById(){
        Order orderCreated = mongoOrderRepository.save(order);

        String id = orderCreated.getId();

        Order orderFound = mongoOrderRepository.getById(id);

        assertThat(orderFound).isNotNull();
        assertThat(orderFound.getId()).isEqualTo(orderCreated.getId());
    }

    @Test
    void testGetByCodeOperation(){
        Order orderCreated = mongoOrderRepository.save(order);

        String code = orderCreated.getCodeOperation();

        Order orderFound = mongoOrderRepository.getByCodeOperation(code);

        assertThat(orderFound).isNotNull();
        assertThat(orderFound.getCodeOperation()).isEqualTo(orderCreated.getCodeOperation());
    }

    @Test
    void testGetAllOrders(){
        int page = 0;
        int size = 2;

        Order order2 = Order.builder()
                .user_id(2L)
                .price(BigDecimal.valueOf(12999))
                .date(LocalDateTime.of(2024, 02, 22, 17, 30, 00))
                .codeOperation("af630d3c-04b2-4233-8a58-ee2b8c503db8")
                .build();

        mongoOrderRepository.save(order);
        mongoOrderRepository.save(order2);

        List<Order> orderList = mongoOrderRepository.getAllOrders(page, size);

        assertThat(orderList).isNotNull();
        assertThat(orderList.size()).isEqualTo(2);
    }

    @Test
    void testDelete(){
        Order orderCreated = mongoOrderRepository.save(order);

        String id = orderCreated.getId();

        mongoOrderRepository.delete(id);

        Order orderFound = mongoOrderRepository.getById(id);

        assertThat(orderFound).isNull();

    }
}
