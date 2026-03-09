package com.quarkbau.monolith.gis.controller;

import com.quarkbau.monolith.gis.model.GisFeature;
import com.quarkbau.monolith.gis.repository.GisFeatureRepository;
import com.quarkbau.monolith.gis.service.GeoJsonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gis")
@RequiredArgsConstructor
public class GisController {

    private final GisFeatureRepository repository;
    private final GeoJsonService geoJsonService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<GisFeature>> uploadGeoJson(@RequestParam("file") MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        List<GisFeature> imported = geoJsonService.importGeoJson(content, "PLAN", "APPROVED");
        return ResponseEntity.ok(imported);
    }

    @GetMapping("/features")
    public List<GisFeature> getFeatures(@RequestParam(defaultValue = "PLAN") String version,
            @RequestParam(required = false) String featureType) {
        seedDataIfEmpty();
        if (featureType != null) {
            return repository.findByVersionAndFeatureType(version, featureType);
        }
        return repository.findByVersion(version);
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        seedDataIfEmpty();
        Map<String, Long> stats = new HashMap<>();
        stats.put("planFeatures", repository.countByVersion("PLAN"));
        stats.put("asBuiltFeatures", repository.countByVersion("AS_BUILT"));
        stats.put("correctionFeatures", repository.countByVersion("CORRECTION"));
        return stats;
    }

    private void seedDataIfEmpty() {
        if (repository.count() == 0) {
            // Seed a sample segment
            GisFeature f1 = new GisFeature();
            f1.setFeatureType("SEGMENT");
            f1.setVersion("PLAN");
            f1.setStatus("APPROVED");
            f1.setGeometry("{\"type\":\"LineString\",\"coordinates\":[[13.405,52.52],[13.41,52.525]]}");
            f1.setProperties("{\"street\":\"Friedrichstraße\",\"material\":\"PE-HD 40mm\"}");
            repository.save(f1);

            // Seed a sample node
            GisFeature f2 = new GisFeature();
            f2.setFeatureType("NODE");
            f2.setVersion("PLAN");
            f2.setStatus("APPROVED");
            f2.setGeometry("{\"type\":\"Point\",\"coordinates\":[13.405,52.52]}");
            f2.setProperties("{\"nodeType\":\"MANHOLE\",\"ref\":\"MH-001\"}");
            repository.save(f2);

            // Seed for München
            GisFeature f3 = new GisFeature();
            f3.setFeatureType("SEGMENT");
            f3.setVersion("PLAN");
            f3.setStatus("APPROVED");
            f3.setGeometry("{\"type\":\"LineString\",\"coordinates\":[[11.582,48.135],[11.59,48.14]]}");
            f3.setProperties("{\"street\":\"Marienplatz\",\"material\":\"PVC 110mm\"}");
            repository.save(f3);
        }
    }
}
