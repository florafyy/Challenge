package com.example.challenge01.loader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/csv")
@RequiredArgsConstructor
public class CsvLoaderController {
    public final FileReadService service;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public String csvProcess(@RequestPart("file") MultipartFile file) throws IOException {
        service.processRecords(file);
        return "File persistent Success!";
    }

    @GetMapping("/products")
    public ResponseEntity<String> getAllProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "code") String sortBy, @RequestParam(defaultValue = "asc") String sortOrder) {
        Page<Product> products = service.getAllProducts(page, size, sortBy, sortOrder);
        String json = convertPageToJson(products);
        return ResponseEntity.ok(json);
    }

    @GetMapping("/categories")
    public ResponseEntity<String> getAllCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "code") String sortBy, @RequestParam(defaultValue = "asc") String sortOrder) {
        Page<Category> categories = service.getAllCategories(page, size, sortBy, sortOrder);
        String json = convertPageToJson(categories);
        return ResponseEntity.ok(json);
    }

    @GetMapping("/products/byCode")
    public ResponseEntity<String> getProductByCode(@RequestParam(defaultValue = "0") int code) {
        Product product = service.getProductByCode(code);
        if (product != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String json = objectMapper.writeValueAsString(product);
                return ResponseEntity.ok(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while converting to JSON");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String convertPageToJson(Page page) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(page.getContent());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error occurred while converting to JSON";
        }
    }

}
