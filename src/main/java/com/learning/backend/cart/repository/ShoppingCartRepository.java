package com.learning.backend.cart.repository;

import com.learning.backend.cart.entity.CartEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<CartEntry, Integer> {
    List<CartEntry> findByUserId(String userId);
    void deleteByUserId(String userId);
}
