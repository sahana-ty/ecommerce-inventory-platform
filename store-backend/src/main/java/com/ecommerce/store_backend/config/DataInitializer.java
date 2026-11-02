package com.ecommerce.store_backend.config;

import com.ecommerce.store_backend.model.Category;
import com.ecommerce.store_backend.model.Product;
import com.ecommerce.store_backend.model.User;
import com.ecommerce.store_backend.repository.CategoryRepository;
import com.ecommerce.store_backend.repository.ProductRepository;
import com.ecommerce.store_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepo, 
                                   ProductRepository productRepo, 
                                   UserRepository userRepo) {
        return args -> {
            if (categoryRepo.count() == 0) {
                // 1. Create Categories
                Category electronics = new Category();
                electronics.setCategoryName("Electronics");
                categoryRepo.save(electronics);

                Category apparel = new Category();
                apparel.setCategoryName("Apparel");
                categoryRepo.save(apparel);

                // 2. Create Products
                Product phone = new Product();
                phone.setName("Smartphone");
                phone.setDescription("Latest 5G smartphone with 128GB storage");
                phone.setPrice(BigDecimal.valueOf(49999.00));
                phone.setStockQuantity(10);
                phone.setCategory(electronics);
                productRepo.save(phone);

                Product tshirt = new Product();
                tshirt.setName("Cotton T-Shirt");
                tshirt.setDescription("Comfortable 100% cotton casual t-shirt");
                tshirt.setPrice(BigDecimal.valueOf(999.00));
                tshirt.setStockQuantity(50);
                tshirt.setCategory(apparel);
                productRepo.save(tshirt);

                // 3. Create a Test User
                User user = new User();
                user.setUsername("TestUser");
                user.setEmail("user@test.com");
                user.setPasswordHash("password123");
                user.setRole("CUSTOMER");
                userRepo.save(user);

                System.out.println("--> Sample Data Initialized Successfully in Oracle DB!");
            }
        };
    }
}