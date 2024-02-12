package com.toyStore.toys.infra.outputAdapter;

import com.toyStore.toys.domain.ECategory;
import com.toyStore.toys.domain.Toy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface IMySQLRepository extends JpaRepository<Toy, Long> {

    List<Toy> findByCategoryEquals(ECategory category, Pageable pageable);

    @Query(value = "SELECT t FROM Toy t")
    List<Toy> findAllToy(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE Toy t SET t.stock = t.stock - :quantityToDeduct WHERE t.id = :id")
    void updateStock(@Param("id") Long id, @Param("quantityToDeduct") int quantityToDeduct);
}
