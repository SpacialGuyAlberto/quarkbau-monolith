package com.quarkbau.monolith.planning.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryIntegrationService {

    @Transactional
    public void consumeMaterial(String sku, Double quantity) {
        // Implementation will be refined as needed, but this enables cross-module calls
        // In a microservices world this would be a Feign client or REST call
        // In the monolith it is a direct method call
    }
}
