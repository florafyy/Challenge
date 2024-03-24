package com.example.challenge01.loader;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CSVRepresentation {
    @CsvBindByName(column = "CATEGORY_CODE")
    private Integer categoryCode;

    @CsvBindByName(column = "PRODUCT_CODE")
    private Integer productCode;

    @CsvBindByName(column = "PRODUCT_CATEGORY_CODE")
    private Integer productCategoryCode;

    @CsvBindByName(column = "PRODUCT_NAME")
    private String productName;

    @CsvBindByName(column = "CATEGORY_NAME")
    private String categoryName;
}
