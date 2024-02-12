package com.toyStore.toys.repository;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class IMySQLRepositoryTest {

    @Autowired
    private IMySQLRepositoryTest iMySQLRepositoryTest;

    @DisplayName("Test to fetch a list of toys by category")
    void testFindByCategoryEquals(){

    }
}
