package com.toyStore.orders.repository;

import com.toyStore.orders.domain.Order;
import com.toyStore.orders.domain.OrderProduct;
import com.toyStore.orders.infra.outputAdapter.MongoOrderProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MongoOrderProductRepositoryTest {

    @Autowired
    private MongoOrderProductRepository MongoOrderProductRepository;

    private OrderProduct orderProduct;

    @BeforeEach
    void setup() {
        orderProduct = OrderProduct.builder()
                .order_id("65d527c25e0fdd2241e07e31")
                .product_id(1L)
                .quantity(2)
                .build();
    }

    @Test
    void testSave(){
        OrderProduct orderProductCreated = MongoOrderProductRepository.save(orderProduct);

        assertThat(orderProductCreated).isNotNull();
        assertThat(orderProductCreated.getOrder_id()).isEqualTo("65d527c25e0fdd2241e07e31");
        assertThat(orderProductCreated.getProduct_id()).isEqualTo(1L);
        assertThat(orderProductCreated.getQuantity()).isEqualTo(2);
    }

    @Test
    void testGetByOrderId(){
        String orderId = "65d527c25e0fdd2241e07e31";

        OrderProduct orderProduct2 = OrderProduct.builder()
                .order_id("65d527c25e0fdd2241e07e31")
                .product_id(2L)
                .quantity(4)
                .build();

        MongoOrderProductRepository.save(orderProduct);
        MongoOrderProductRepository.save(orderProduct2);

        List<OrderProduct> orderProductFound = MongoOrderProductRepository.getByOrderId(orderId);

        assertThat(orderProductFound).isNotNull();
        assertThat(orderProductFound.size()).isEqualTo(2);
    }

    @Test
    void testDelete(){
        OrderProduct orderProductCreated = MongoOrderProductRepository.save(orderProduct);

        String id = orderProductCreated.getId();

        MongoOrderProductRepository.delete(id);

        List<OrderProduct> orderProductFound = MongoOrderProductRepository.getByOrderId(orderProductCreated.getOrder_id());

        assertThat(orderProductFound).doesNotContain(orderProductCreated);
    }
}
