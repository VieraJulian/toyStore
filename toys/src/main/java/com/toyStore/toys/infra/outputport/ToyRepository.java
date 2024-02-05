package com.toyStore.toys.infra.outputport;

import com.toyStore.toys.application.exception.ToyNotFoundException;
import com.toyStore.toys.domain.ECategory;
import com.toyStore.toys.domain.Toy;
import com.toyStore.toys.infra.outputAdapter.IMySQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ToyRepository implements IToyMethods{

    @Autowired
    private IMySQLRepository myRepository;

    @Override
    public Toy save(Toy toy) {
        return myRepository.save(toy);
    }

    @Override
    public Toy getById(Long id) throws ToyNotFoundException {
        return myRepository.findById(id).orElseThrow(() -> new ToyNotFoundException("Toy not found"));
    }

    @Override
    public List<Toy> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return myRepository.findAllToy(pageable);
    }

    @Override
    public List<Toy> getByCategory(ECategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return myRepository.findByCategoryEquals(category, pageable);
    }

    @Override
    public void delete(Long id) {
        myRepository.deleteById(id);
    }

    @Override
    public void updateStock(Long id, int quantityToDeduct) {
        myRepository.updateStock(id, quantityToDeduct);
    }
}
