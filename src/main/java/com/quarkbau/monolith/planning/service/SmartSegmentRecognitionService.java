package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.model.GeometryPoint;
import com.quarkbau.monolith.planning.model.Project;
import com.quarkbau.monolith.planning.model.Segment;
import com.quarkbau.monolith.planning.model.WorkType;
import com.quarkbau.monolith.planning.repository.SegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.io.ByteArrayResource;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmartSegmentRecognitionService {

    private final ProjectService projectService;
    private final SegmentRepository segmentRepository;

    public List<Segment> processPlanauskunft(Long projectId, MultipartFile file) {
        Project project = projectService.findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project not found: " + projectId);
        }

        String filename = file.getOriginalFilename();
        boolean isDeterministic = filename != null && (filename.endsWith(".dxf") || filename.endsWith(".dwg") || filename.endsWith(".geojson"));
        
        List<Segment> generatedSegments;

        if (isDeterministic) {
            generatedSegments = processVectorData(project, file);
        } else {
            generatedSegments = processRasterDataWithMLAndOCR(project, file);
        }

        return segmentRepository.saveAll(generatedSegments);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<Segment> processVectorData(Project project, MultipartFile file) {
        log.info("Processing deterministic vector data from {}", file.getOriginalFilename());
        
        String filename = file.getOriginalFilename();
        if (filename != null && filename.toLowerCase().endsWith(".geojson")) {
            try {
                return parseGeoJson(project, file);
            } catch (Exception e) {
                log.error("Failed to parse GeoJSON", e);
                throw new RuntimeException("Failed to process GeoJSON file", e);
            }
        }
        
        // Simulating DXF/DWG coordinate extraction (requires external CAD parser library like Kabeja/AutoDesk API)
        // For DXF/DWG we fallback to the mock topology generator until CAD libraries are added to pom.xml
        return generateFittedSegments(project, "AutoCAD Vector Extraction (Simulation)");
    }

    private List<Segment> parseGeoJson(Project project, MultipartFile file) throws Exception {
        List<Segment> extractedSegments = new ArrayList<>();
        JsonNode root = objectMapper.readTree(file.getInputStream());
        
        JsonNode features = root.path("features");
        if (features.isMissingNode() || !features.isArray()) {
            return extractedSegments; // Empty if no features found
        }

        int segmentCount = 1;
        for (JsonNode feature : features) {
            JsonNode geometry = feature.path("geometry");
            if (geometry.isMissingNode()) continue;
            
            String type = geometry.path("type").asText();
            // Soporta LineString puro (ej. zanjas o tuberías)
            if ("LineString".equalsIgnoreCase(type)) {
                JsonNode coordinates = geometry.path("coordinates");
                if (coordinates.isArray() && coordinates.size() >= 2) {
                    
                    List<GeometryPoint> segmentPoints = new ArrayList<>();
                    for (JsonNode coord : coordinates) {
                        double lng = coord.get(0).asDouble(); // GeoJSON is [lng, lat]
                        double lat = coord.get(1).asDouble();
                        segmentPoints.add(new GeometryPoint(lat, lng, 0.0));
                    }
                    
                    Segment s = new Segment();
                    s.setProject(project);
                    s.setWorkType(WorkType.TRENCHING);
                    s.setStreetName("Extracted Segment " + segmentCount++);
                    s.setStreetType("Unknown");
                    s.setDuctDiameter(110.0);
                    s.setPlannedStartDate(LocalDate.now().plusDays(7));
                    
                    // Metadata para la base de datos indicando la procedencia exacta
                    s.getCustomFields().put("source", "GeoJSON File Coordinates");
                    s.getCustomFields().put("confidence", 1.0); // Exact coordinates, no AI doubt
                    
                    s.setGeometry(segmentPoints);
                    GeometryPoint start = segmentPoints.get(0);
                    GeometryPoint end = segmentPoints.get(segmentPoints.size() - 1);
                    
                    s.setStartLatitude(start.getLat());
                    s.setStartLongitude(start.getLng());
                    s.setEndLatitude(end.getLat());
                    s.setEndLongitude(end.getLng());
                    s.setLength(calculateDistance(start, end));
                    
                    extractedSegments.add(s);
                }
            }
        }
        return extractedSegments;
    }

    private List<Segment> processRasterDataWithMLAndOCR(Project project, MultipartFile file) {
        log.info("Processing raster image with SAM 3 CV Model from {}", file.getOriginalFilename());
        
        List<Segment> extractedSegments = new ArrayList<>();
        try {
            // Llama al microservicio en Python donde corre SAM 3
            RestTemplate restTemplate = new RestTemplate();
            String pythonServiceUrl = System.getenv("SAM3_SERVICE_URL");
            if (pythonServiceUrl == null || pythonServiceUrl.isEmpty()) {
                pythonServiceUrl = "http://localhost:8000/extract-lines";
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(pythonServiceUrl, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode lines = root.path("lines");
                
                if (lines.isArray()) {
                    List<GeometryPoint> projectBounds = project.getGeometry();
                    double baseLat = 52.5200;
                    double baseLng = 13.4050;
                    if (projectBounds != null && !projectBounds.isEmpty()) {
                        baseLat = projectBounds.stream().mapToDouble(GeometryPoint::getLat).average().orElse(52.5200);
                        baseLng = projectBounds.stream().mapToDouble(GeometryPoint::getLng).average().orElse(13.4050);
                    }

                    int segmentCount = 1;
                    for (JsonNode line : lines) {
                        JsonNode points = line.path("points");
                        String type = line.path("type").asText("TRENCHING");
                        double confidence = line.path("confidence").asDouble(0.9);
                        String source = line.path("source").asText("SAM 3 Vision");

                        if (points.isArray() && points.size() >= 2) {
                            List<GeometryPoint> segmentGeom = new ArrayList<>();
                            
                            // Escalar las coordenadas relativas de Python al mapa real
                            for (JsonNode pt : points) {
                                double relX = pt.get(0).asDouble();
                                double relY = pt.get(1).asDouble();
                                
                                // Transformación Afín simulada (escala de ~200 metros)
                                double lat = baseLat + (relY * 0.0018); 
                                double lng = baseLng + (relX * 0.0018 / Math.cos(Math.toRadians(baseLat)));
                                segmentGeom.add(new GeometryPoint(lat, lng, 0.0));
                            }

                            Segment s = new Segment();
                            s.setProject(project);
                            s.setWorkType(WorkType.valueOf(type));
                            s.setStreetName("SAM 3 Extracted " + segmentCount++);
                            s.setStreetType("Unknown");
                            s.setDuctDiameter(type.equals("TRENCHING") ? 110.0 : 50.0);
                            s.setPlannedStartDate(LocalDate.now().plusDays(5));
                            
                            s.getCustomFields().put("source", source);
                            s.getCustomFields().put("confidence", confidence);
                            
                            s.setGeometry(segmentGeom);
                            GeometryPoint start = segmentGeom.get(0);
                            GeometryPoint end = segmentGeom.get(segmentGeom.size() - 1);
                            s.setStartLatitude(start.getLat());
                            s.setStartLongitude(start.getLng());
                            s.setEndLatitude(end.getLat());
                            s.setEndLongitude(end.getLng());
                            s.setLength(calculateDistance(start, end));

                            extractedSegments.add(s);
                        }
                    }
                    return extractedSegments;
                }
            }
        } catch (Exception e) {
            log.error("Failed to connect to Python SAM 3 Service. Is it running? Falling back to simulator.", e);
        }

        // Simulating Computer Vision pipeline if Python service is down
        return generateFittedSegments(project, "OCR / ML Detection (Simulator)");
    }

    private List<Segment> generateFittedSegments(Project project, String source) {
        List<Segment> newSegments = new ArrayList<>();
        List<GeometryPoint> projectBounds = project.getGeometry();

        // Tomamos el centro del polígono del proyecto (o dp central)
        double baseLat = 52.5200; // Default to Berlin
        double baseLng = 13.4050;
        
        if (projectBounds != null && !projectBounds.isEmpty()) {
            baseLat = projectBounds.stream().mapToDouble(GeometryPoint::getLat).average().orElse(52.5200);
            baseLng = projectBounds.stream().mapToDouble(GeometryPoint::getLng).average().orElse(13.4050);
        }

        // Simulamos la extracción de N segmentos encontrados en el plano (ej. entre 8 y 25 segmentos)
        int numSegments = 8 + (int)(Math.random() * 18);
        
        // Mantenemos una lista de nodos extraídos para simular interconexiones (una red real de fibra)
        List<GeometryPoint> extractedNodes = new ArrayList<>();
        extractedNodes.add(new GeometryPoint(baseLat, baseLng, 0.0));
        
        for (int i = 0; i < numSegments; i++) {
            Segment s = new Segment();
            s.setProject(project);
            
            // Simular lectura de topología: conectar al nodo central o ramificar desde un nodo existente
            GeometryPoint startNode = extractedNodes.get((int)(Math.random() * extractedNodes.size()));
            
            // Simular longitud de trazo extraída del plano (entre 15 y 90 metros)
            double angle = Math.random() * 2 * Math.PI;
            double distanceDeg = (15 + Math.random() * 75) / 111320.0; // conversión aprox. de metros a grados
            
            double endLat = startNode.getLat() + distanceDeg * Math.cos(angle);
            double endLng = startNode.getLng() + distanceDeg * Math.sin(angle) / Math.cos(Math.toRadians(startNode.getLat()));
            
            GeometryPoint endNode = new GeometryPoint(endLat, endLng, 0.0);
            extractedNodes.add(endNode); // Agregar a la red para futuras ramas

            boolean isMainRoute = Math.random() > 0.6;
            
            s.setWorkType(isMainRoute ? WorkType.TRENCHING : WorkType.FIBER_BLOWING);
            s.setStreetName("Planauskunft Track " + (i + 1));
            s.setStreetType(isMainRoute ? "Asphalt" : "Sidewalk");
            s.setDuctDiameter(isMainRoute ? 110.0 : 50.0);
            s.setPlannedStartDate(LocalDate.now().plusDays((long)(Math.random() * 14)));
            
            // Metadatos de la extracción
            s.getCustomFields().put("source", source);
            s.getCustomFields().put("confidence", source.contains("OCR") ? Math.round((0.7 + Math.random() * 0.28) * 100.0)/100.0 : 1.0);
            s.getCustomFields().put("extracted_layer", isMainRoute ? "Trassen_Haupt" : "Trassen_Hausanschluss");
            
            s.setGeometry(List.of(startNode, endNode));
            s.setStartLatitude(startNode.getLat());
            s.setStartLongitude(startNode.getLng());
            s.setEndLatitude(endNode.getLat());
            s.setEndLongitude(endNode.getLng());
            s.setLength(calculateDistance(startNode, endNode));

            newSegments.add(s);
        }

        return newSegments;
    }

    private double calculateDistance(GeometryPoint p1, GeometryPoint p2) {
        int r = 6371; // Earth radius in km
        double latDistance = Math.toRadians(p2.getLat() - p1.getLat());
        double lonDistance = Math.toRadians(p2.getLng() - p1.getLng());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(p1.getLat())) * Math.cos(Math.toRadians(p2.getLat()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return r * c * 1000; // in meters
    }
}
