package com.product.images.products.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.product.images.common.BaseResponse;
import com.product.images.common.ItemResponse;
import com.product.images.products.service.ProductService;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/upload/images")
    public ResponseEntity<BaseResponse> uploadProducts(@RequestParam("files") MultipartFile[] files) {

        BaseResponse baseResponse = productService.uploadProducts(files);
        return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/all/projucts")
    public ResponseEntity<ItemResponse> getAllProducts() {
        ItemResponse response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/image/{folder}/{filename:.+}")
    public ResponseEntity<Resource> getProductImage(@PathVariable String folder, @PathVariable String filename) {

        Resource resource = productService.loadProductImage(folder, filename);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = productService.getImageContentType(resource);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

}
