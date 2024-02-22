package com.toyStore.orders.repository;

import com.toyStore.orders.infra.outputAdapter.MongoOrderProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

@ExtendWith(MockitoExtension.class)
public class MongoOrderProductRepositoryTest {

    @Mock
    private MongoTemplate mt;

    @InjectMocks
    private MongoOrderProductRepository MongoOrderProductRepository;

    @Test
    void testSave(){

    }

    @Test
    void testGetByOrderId(){

    }

    @Test
    void testDelete(){

    }
}
