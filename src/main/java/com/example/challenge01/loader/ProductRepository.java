package com.example.challenge01.loader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByCode(Integer code);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.category where p.code = :productCode")
    Product findByCodeWithCategory(@Param("productCode") Integer code);
}
