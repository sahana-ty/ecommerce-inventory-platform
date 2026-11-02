package com.ecommerce.store_backend.repository;

import com.ecommerce.store_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA automatically parses this method name to generate the SQL query
    List<Product> findByCategoryCategoryId(Long categoryId);
}