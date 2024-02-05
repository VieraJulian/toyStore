package com.toyStore.toys.infra.outputport;

import com.toyStore.toys.application.exception.ToyNotFoundException;
import com.toyStore.toys.domain.ECategory;
import com.toyStore.toys.domain.Toy;

import java.util.List;
import java.util.Optional;

public interface IToyMethods {

    public Toy save(Toy toy);

    public Toy getById(Long id) throws ToyNotFoundException;

    public List<Toy> getAll(int page, int size);

    public List<Toy> getByCategory(ECategory category, int page, int size);

    public void delete(Long id);

    public void updateStock(Long id, int quantityToDeduct);
}
