package com.quarkbau.monolith.infrastructure.service;

import com.quarkbau.monolith.infrastructure.model.Inquiry;
import org.springframework.stereotype.Service;

@Service
public class DocumentAnalysisService {

    public String analyzeDocument(Inquiry inquiry) {
        // Mock analysis logic
        if (inquiry.getUtilityProvider().getName().contains("Telekom")) {
            return "CONFLICT: High-pressure fiber optic cable detected at depth 0.8m. Avoid excavation in the northern 2 meters.";
        }
        return "CLEAR: No documented conflicts found in this sector.";
    }
}
