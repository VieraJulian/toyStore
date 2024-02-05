package com.toyStore.toys.infra.outputAdapter;

import com.toyStore.toys.domain.ECategory;
import com.toyStore.toys.domain.Toy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMySQLRepository extends JpaRepository<Toy, Long> {

    List<Toy> findByCategoryEquals(ECategory category, Pageable pageable);

    @Query(value = "SELECT t FROM Toy t")
    List<Toy> findAllToy(Pageable pageable);
}
