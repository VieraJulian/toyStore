package com.toyStore.toys.application;

import com.toyStore.toys.application.exception.ToyNotFoundException;
import com.toyStore.toys.domain.ECategory;
import com.toyStore.toys.domain.Toy;
import com.toyStore.toys.domain.ToyImage;
import com.toyStore.toys.infra.dto.ToyDTO;
import com.toyStore.toys.infra.inputport.IToyInputPort;
import com.toyStore.toys.infra.outputport.IToyMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToyUseCase implements IToyInputPort {

    @Autowired
    private IToyMethods methods;

    @Value("${server.port}")
    private int port;

    private String dirPath = "src/main/java/com/toyStore/toys/infra/uploads/";

    @Override
        public Toy createToy(ToyDTO toy, MultipartFile[] files) throws IOException {

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("The file is empty");
        }

        // Get the discounted price
        BigDecimal finalPrice = toy.getPrice();

        if (toy.getDiscount() > 0) {
            BigDecimal price = toy.getPrice();
            BigDecimal discount = BigDecimal.valueOf(toy.getDiscount());
            BigDecimal discountAmount = price.multiply(discount).divide(BigDecimal.valueOf(100));
            BigDecimal discountedPrice = price.subtract(discountAmount);
            finalPrice = discountedPrice.setScale(2, RoundingMode.UNNECESSARY);
        }

        //Get the category
        String category = toy.getCategory().toUpperCase();
        ECategory toyCategory;

        try {
            toyCategory = ECategory.valueOf(category);
        } catch (Exception e) {
            toyCategory = ECategory.MUÑECOS;
        }

        // Get the toys images and save the toy in the database
        Toy toyInfo = Toy.builder()
                .name(toy.getName())
                .brand(toy.getBrand())
                .description(toy.getDescription())
                .price(toy.getPrice())
                .priceWithDiscount(finalPrice)
                .discount(toy.getDiscount())
                .recommendedAge(toy.getRecommendedAge())
                .batterie(toy.getBatterie())
                .material(toy.getMaterial())
                .stock(toy.getStock())
                .category(toyCategory)
                .build();

        List<ToyImage> images = new ArrayList<>();

        for (MultipartFile file : files) {

            String fileName = StringUtils.cleanPath(System.currentTimeMillis() + "-" + file.getOriginalFilename());
            Path path = Paths.get(dirPath + fileName);

            try {
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String fileUrl = "http://localhost:" + port + "/src/main/java/com/toyStore/toys/infra/uploads/" + fileName;

            images.add(ToyImage.builder()
                    .toy(toyInfo)
                    .imgName(fileName)
                    .imgUrl(fileUrl)
                    .build());
        }

        toyInfo.setImgs(images);

        return methods.save(toyInfo);
    }

    @Override
    public Toy updateToy(Long id, ToyDTO toy, MultipartFile[] files) throws ToyNotFoundException, IOException {
        // Get the toy from the database
        Toy toyDB = methods.getById(id);

        // Get the discounted price
        BigDecimal finalPrice = toy.getPrice();

        if (toy.getDiscount() > 0) {
            BigDecimal price = toy.getPrice();
            BigDecimal discount = BigDecimal.valueOf(toy.getDiscount());
            BigDecimal discountAmount = price.multiply(discount).divide(BigDecimal.valueOf(100));
            BigDecimal discountedPrice = price.subtract(discountAmount);
            finalPrice = discountedPrice.setScale(2, RoundingMode.UNNECESSARY);
        }

        //Get the category
        String category = toy.getCategory().toUpperCase();
        ECategory toyCategory;

        try {
            toyCategory = ECategory.valueOf(category);
        } catch (Exception e) {
            toyCategory = ECategory.MUÑECOS;
        }

        // Setter the toy
        toyDB.setName(toy.getName());
        toyDB.setBrand(toy.getBrand());
        toyDB.setDescription(toy.getDescription());
        toyDB.setPrice(toy.getPrice());
        toyDB.setPriceWithDiscount(finalPrice);
        toyDB.setDiscount(toy.getDiscount());
        toyDB.setRecommendedAge(toy.getRecommendedAge());
        toyDB.setBatterie(toy.getBatterie());
        toyDB.setMaterial(toy.getMaterial());
        toyDB.setStock(toy.getStock());
        toyDB.setCategory(toyCategory);

        // Create local images
        // Save images to the database
        if (files.length > 0 && files != null && !files[0].isEmpty()) {

            int loop = 0;
            List<ToyImage> toyImages = toyDB.getImgs();
            List<ToyImage> images = new ArrayList<>();

            // Delete local images
            for (ToyImage img: toyImages) {
                String oldFileName = img.getImgUrl().replace(
                        "http://localhost:" + port + "/src/main/java/com/toyStore/toys/infra/uploads/",
                        "");

                Path oldFilePath = Paths.get(dirPath + oldFileName);

                try {
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (MultipartFile file : files) {
                String fileName = StringUtils.cleanPath(System.currentTimeMillis() + "-" + file.getOriginalFilename());
                Path path = Paths.get(dirPath + fileName);

                try {
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String fileUrl = "http://localhost:" + port + "/src/main/java/com/toyStore/toys/infra/uploads/" + fileName;

                images.add(ToyImage.builder()
                        .toy(toyDB)
                        .imgName(fileName)
                        .imgUrl(fileUrl)
                        .build());
            }

            toyDB.getImgs().clear();
            toyDB.getImgs().addAll(images);
        }

        return methods.save(toyDB);
    }

    @Override
    public Toy getToyById(Long id) throws ToyNotFoundException {
        return methods.getById(id);
    }

    @Override
    public List<Toy> getAllToys(int page, int size) {
        return methods.getAll(page, size);
    }

    @Override
    public List<Toy> getAllToysByCategory(String category, int page, int size) {
        category = category.toUpperCase();
        ECategory eCategory = ECategory.valueOf(category);
        return methods.getByCategory(eCategory, page, size);
    }

    @Override
    public String deleteToy(Long id) throws ToyNotFoundException {
        // Get the toy from the database
        Toy toyDB = methods.getById(id);

        List<ToyImage> toyImages = toyDB.getImgs();

        int loop = 0;
        for (ToyImage img: toyImages) {
            String oldFileName = toyImages.get(loop).getImgUrl().replace(
                    "http://localhost:" + port + "/src/main/java/com/toyStore/toys/infra/uploads/",
                    "");

            Path oldFilePath = Paths.get(dirPath + oldFileName);

            try {
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            loop++;
        }

        methods.delete(id);

        return "Toy with id " + id + " is deleted successfully";
    }

    @Override
    public String updateStock(Long id, int quantityToDeduct) {
        methods.updateStock(id, quantityToDeduct);

        return "Stock updated";
    }
}
