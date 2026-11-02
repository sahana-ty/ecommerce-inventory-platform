package com.ecommerce.store_backend.repository;

import com.ecommerce.store_backend.model.Cart;
import com.ecommerce.store_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}