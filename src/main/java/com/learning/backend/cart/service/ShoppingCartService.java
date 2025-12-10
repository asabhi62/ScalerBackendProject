package com.learning.backend.cart.service;

import com.learning.backend.cart.entity.CartEntry;
import com.learning.backend.cart.exception.CartNotFoundException;
import com.learning.backend.cart.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String CACHE_KEY_PREFIX = "shopping_cart:";
    private static final String KAFKA_TOPIC = "shopping-cart-events";

    public List<CartEntry> retrieveCart(String userId) {
        String cacheKey = CACHE_KEY_PREFIX + userId;
        
        // Try to fetch from Redis
        try {
            List<CartEntry> cachedCart = (List<CartEntry>) redisTemplate.opsForValue().get(cacheKey);
            if (cachedCart != null) {
                return cachedCart;
            }
        } catch (Exception e) {
            // Log error and proceed to DB
            System.err.println("Error fetching from Redis: " + e.getMessage());
        }

        // Fetch from DB
        List<CartEntry> cartEntries = shoppingCartRepository.findByUserId(userId);
        if (cartEntries.isEmpty()) {
             throw new CartNotFoundException("No shopping cart entries found for user: " + userId);
        }

        // Update Cache
        try {
            redisTemplate.opsForValue().set(cacheKey, cartEntries);
        } catch (Exception e) {
             System.err.println("Error setting Redis cache: " + e.getMessage());
        }
        
        return cartEntries;
    }

    @Transactional
    public CartEntry addItemToCart(CartEntry entry) {
        CartEntry savedEntry = shoppingCartRepository.save(entry);
        
        // Invalidate Cache
        try {
            redisTemplate.delete(CACHE_KEY_PREFIX + entry.getUserId());
        } catch (Exception e) {
             System.err.println("Error deleting from Redis: " + e.getMessage());
        }
        
        // Publish Event
        try {
            kafkaTemplate.send(KAFKA_TOPIC, "Item added to cart: " + entry.getProductName());
        } catch (Exception e) {
             System.err.println("Error sending to Kafka: " + e.getMessage());
        }
        
        return savedEntry;
    }

    @Transactional
    public void clearCart(String userId) {
        shoppingCartRepository.deleteByUserId(userId);
        
        // Invalidate Cache
        try {
            redisTemplate.delete(CACHE_KEY_PREFIX + userId);
        } catch (Exception e) {
             System.err.println("Error deleting from Redis: " + e.getMessage());
        }
        
        // Publish Event
        try {
            kafkaTemplate.send(KAFKA_TOPIC, "Shopping cart cleared for user: " + userId);
        } catch (Exception e) {
             System.err.println("Error sending to Kafka: " + e.getMessage());
        }
    }
}
