package com.learning.backend.cart.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {
    
    @Bean
    public NewTopic shoppingCartEventsTopic() {
        return new NewTopic("shopping-cart-events", 1, (short) 1);
    }
    
}
