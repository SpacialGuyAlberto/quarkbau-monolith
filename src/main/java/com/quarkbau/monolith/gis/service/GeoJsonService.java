package com.quarkbau.monolith.gis.service;

import com.quarkbau.monolith.gis.model.GisFeature;
import com.quarkbau.monolith.gis.repository.GisFeatureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoJsonService {

    private final GisFeatureRepository repository;

    @Transactional
    public List<GisFeature> importGeoJson(String jsonContent, String version, String status) {
        JSONObject root = new JSONObject(jsonContent);
        if (!root.has("type") || !root.getString("type").equals("FeatureCollection")) {
            throw new IllegalArgumentException("Only FeatureCollection GeoJSON is supported");
        }

        JSONArray features = root.getJSONArray("features");
        List<GisFeature> importedFeatures = new ArrayList<>();

        for (int i = 0; i < features.length(); i++) {
            JSONObject featureJson = features.getJSONObject(i);

            GisFeature feature = new GisFeature();

            // Extract geometry as raw JSON string
            if (featureJson.has("geometry") && !featureJson.isNull("geometry")) {
                feature.setGeometry(featureJson.getJSONObject("geometry").toString());
            } else {
                log.warn("Feature at index {} has no geometry, skipping", i);
                continue;
            }

            // Extract feature type from properties if exists, otherwise default
            JSONObject properties = featureJson.optJSONObject("properties");
            if (properties != null) {
                feature.setProperties(properties.toString());
                feature.setFeatureType(properties.optString("featureType", "UTILITY_CABLE"));
            } else {
                feature.setFeatureType("UTILITY_CABLE");
            }

            feature.setVersion(version);
            feature.setStatus(status);
            feature.setCreatedAt(LocalDateTime.now());
            feature.setUpdatedAt(LocalDateTime.now());

            importedFeatures.add(repository.save(feature));
        }

        log.info("Successfully imported {} features from GeoJSON", importedFeatures.size());
        return importedFeatures;
    }
}
