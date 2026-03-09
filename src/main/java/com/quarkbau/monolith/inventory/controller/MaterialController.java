package com.quarkbau.monolith.inventory.controller;

import com.quarkbau.monolith.inventory.model.Material;
import com.quarkbau.monolith.inventory.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialRepository materialRepository;

    @GetMapping
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @GetMapping("/low-stock")
    public List<Material> getLowStockMaterials() {
        return materialRepository.findAll();
    }

    @PostMapping
    public Material createMaterial(@RequestBody Material material) {
        return materialRepository.save(material);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Material> updateStock(@PathVariable Long id, @RequestParam Double quantityChange) {
        return materialRepository.findById(id)
                .map(material -> {
                    material.setQuantityOnHand(material.getQuantityOnHand() + quantityChange);
                    return ResponseEntity.ok(materialRepository.save(material));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
