package com.toyStore.toys.infra.inputAdapter;

import com.toyStore.toys.domain.Toy;
import com.toyStore.toys.infra.dto.ToyDTO;
import com.toyStore.toys.infra.inputport.IToyInputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/toys")
public class ToyController {

    @Autowired
    private IToyInputPort inputPort;

    private static final Logger logger = LoggerFactory.getLogger(ToyController.class);

    @GetMapping("/{id}")
    public ResponseEntity<Toy> getToyById(@PathVariable Long id) {
        try {
            Toy toy = inputPort.getToyById(id);
            return new ResponseEntity<>(toy, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting toy", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Toy>> getAllToys(@RequestParam int page, @RequestParam int size){
        try {
            List<Toy> toys = inputPort.getAllToys(page, size);

            return new ResponseEntity<>(toys, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting toys", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Toy>> getAllToys(@PathVariable String category, @RequestParam int page, @RequestParam int size){
        try {
            List<Toy> toys = inputPort.getAllToysByCategory(category, page, size);

            return new ResponseEntity<>(toys, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting toys", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Toy> createToy(@ModelAttribute ToyDTO toyDTO, @RequestParam("files") MultipartFile[] files) {
        try {
            Toy toyCreated = inputPort.createToy(toyDTO, files);
            return new ResponseEntity<>(toyCreated, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating toy", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Toy> updateToy(@ModelAttribute ToyDTO toyDTO, @PathVariable Long id, @RequestParam("files") MultipartFile[] files) {
        try {
            Toy toy = inputPort.updateToy(id, toyDTO, files);
            return new ResponseEntity<>(toy, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error editing toy", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteToy(@PathVariable Long id) {
        try {
            String msj = inputPort.deleteToy(id);

            return new ResponseEntity<>(msj, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting toy", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/stock/{id}/{quantityToDeduct}")
    public ResponseEntity<String> updateStock(@PathVariable Long id, @PathVariable int quantityToDeduct){
        try {
            String msj = inputPort.updateStock(id, quantityToDeduct);

            return new ResponseEntity<>(msj, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error editing stock", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
