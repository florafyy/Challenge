package com.example.challenge01.loader;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileReadService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final @Autowired ServletContext context;

    public void processRecords(MultipartFile file) throws IOException {
        CSVReader csvReader = new CSVReader((new InputStreamReader(file.getInputStream())));
        HeaderColumnNameMappingStrategy<CSVRepresentation> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CSVRepresentation.class);

        CsvToBean<CSVRepresentation> csvBean = new CsvToBeanBuilder<CSVRepresentation>(csvReader).withMappingStrategy(strategy).withIgnoreEmptyLine(true).withIgnoreLeadingWhiteSpace(true).build();

        List<CSVRepresentation> csvRepresentations = csvBean.parse();
        List<Product> productToSave = new ArrayList<>();
        List<Category> categorieToSave = new ArrayList<>();
        HashMap<Integer, Category> categoryMap = new HashMap<>();
        for (CSVRepresentation csvRepresentation : csvRepresentations) {
            if (!categoryRepository.existsByCode(csvRepresentation.getCategoryCode())) {
                Category category = Category.builder().code(csvRepresentation.getCategoryCode()).name(csvRepresentation.getCategoryName()).createDate(LocalDateTime.now()).build();
                categorieToSave.add(category);
                categoryMap.put(csvRepresentation.getCategoryCode(), category);
            }
        }
        categoryRepository.saveAll(categorieToSave);

        for (CSVRepresentation csvRepresentation : csvRepresentations) {
            Category c = categoryMap.get(csvRepresentation.getCategoryCode());
            if (!productRepository.existsByCode(csvRepresentation.getProductCode()) && c != null) {
                Product product = Product.builder().category(c).code(csvRepresentation.getProductCode()).name(csvRepresentation.getProductName()).createDate(LocalDateTime.now()).build();
                productToSave.add(product);
            }
        }
        productRepository.saveAll(productToSave);

    }

    public Page<Product> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort.Direction sortDirection = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));
        return productRepository.findAll(pageable);
    }

    public Page<Category> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort.Direction sortDirection = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));
        return categoryRepository.findAll(pageable);
    }

    public Product getProductByCode(int productCode) {
        return productRepository.findByCodeWithCategory(productCode);
    }

}
