package com.product.images.products.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.images.common.ApplicationUtils;
import com.product.images.common.BaseResponse;
import com.product.images.common.ItemResponse;
import com.product.images.products.model.Products;
import com.product.images.storage.FileStorageService;

@Service
public class ProductService {

    @Autowired
    private FileStorageService fileStorageService;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Path rootPath = Paths.get(ApplicationUtils.getFilePath("PRODUCT"));

    public BaseResponse uploadProducts(MultipartFile[] files) {

        BaseResponse res = new BaseResponse();
        List<Products> products = new ArrayList<>();

        try {
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
            }

            for (MultipartFile file : files) {

                String folderName = fileStorageService.getNextProductFolderName(rootPath);

                Path productPath = rootPath.resolve(folderName);
                Files.createDirectories(productPath);

                String fileExtension = fileStorageService
                        .provideFileExtension(file.getOriginalFilename())
                        .orElse("jpg");

                String finalImageName = folderName + "_photo." + fileExtension;

                Path finalImagePath = productPath.resolve(finalImageName);

                Files.copy(file.getInputStream(), finalImagePath, StandardCopyOption.REPLACE_EXISTING);

                Products product = new Products();
                product.setId(folderName);
                product.setName(file.getOriginalFilename());
                product.setDescription("Description for " + file.getOriginalFilename());
                product.setPrice(500.0);
                product.setImageUrl("/api/products/image/" + folderName + "/" + finalImageName);

                Path jsonPath = productPath.resolve("product.json");
                String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(product);
                Files.write(jsonPath, jsonData.getBytes(StandardCharsets.UTF_8));

                products.add(product);
            }

            res.setMessage("Products uploaded successfully");
            return res;

        } catch (Exception e) {
            logger.error("Bulk upload failed", e);
            res.setMessage("Upload failed due to server error");
            return res;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ItemResponse getAllProducts() {

        ItemResponse res = new ItemResponse<>();
        List<Products> products = new ArrayList<>();

        try {
            if (!Files.exists(rootPath)) {
                res.setMessage("no product Found");
                res.setMessageType(0);
                return res;
            }

            try (Stream<Path> stream = Files.list(rootPath)) {
                List<Path> folders = stream.collect(Collectors.toList());

                for (Path folder : folders) {
                    if (Files.isDirectory(folder)) {
                        Path jsonPath = folder.resolve("product.json");
                        if (Files.exists(jsonPath)) {
                            String jsonData = new String(Files.readAllBytes(jsonPath), StandardCharsets.UTF_8);
                            Products product = objectMapper.readValue(jsonData, Products.class);
                            products.add(product);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error reading products", e);
        }

        res.setItem(products);
        res.setMessage("OK");
        res.setMessageType(1);
        return res;
    }

    public Resource loadProductImage(String folder, String filename) {
        try {
            Path imagePath = rootPath.resolve(folder).resolve(filename);

            if (!Files.exists(imagePath) || !Files.isRegularFile(imagePath)) {
                return null;
            }

            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            logger.error("Malformed URL for image", e);
            return null;
        }
    }

    public String getImageContentType(Resource resource) {
        try {
            Path path = Paths.get(resource.getURI());
            String contentType = Files.probeContentType(path);
            return contentType != null ? contentType : "application/octet-stream";
        } catch (IOException e) {
            logger.error("Cannot determine content type", e);
            return "application/octet-stream";
        }
    }

}
