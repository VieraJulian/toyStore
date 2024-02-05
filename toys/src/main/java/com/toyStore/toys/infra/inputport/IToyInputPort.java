package com.toyStore.toys.infra.inputport;

import com.toyStore.toys.application.exception.ToyNotFoundException;
import com.toyStore.toys.domain.Toy;
import com.toyStore.toys.infra.dto.ToyDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IToyInputPort {

    public Toy createToy(ToyDTO toy, MultipartFile[] files) throws IOException;

    public Toy updateToy(Long id, ToyDTO toy, MultipartFile[] files) throws ToyNotFoundException, IOException;

    public Toy getToyById(Long id) throws ToyNotFoundException;

    public List<Toy> getAllToys(int page, int size);

    public List<Toy> getAllToysByCategory(String category, int page, int size);

    public String deleteToy(Long id) throws ToyNotFoundException;

    public String updateStock(Long id, int quantityToDeduct);
}
