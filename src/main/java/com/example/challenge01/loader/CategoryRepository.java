package com.example.challenge01.loader;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByCode(Integer code);

    Page<Category> findAll(Pageable pageable);

}
