package com.learning.backend.cart.controller;

import com.learning.backend.cart.entity.CartEntry;
import com.learning.backend.cart.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartEntry>> getUserCart(@PathVariable String userId) {
        return ResponseEntity.ok(shoppingCartService.retrieveCart(userId));
    }

    @PostMapping
    public ResponseEntity<CartEntry> addItem(@RequestBody CartEntry entry) {
        return ResponseEntity.ok(shoppingCartService.addItemToCart(entry));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> emptyCart(@PathVariable String userId) {
        shoppingCartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
