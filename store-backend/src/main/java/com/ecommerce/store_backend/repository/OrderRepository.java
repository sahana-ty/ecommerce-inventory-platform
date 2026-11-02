package com.ecommerce.store_backend.repository;

import com.ecommerce.store_backend.model.Order;
import com.ecommerce.store_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}