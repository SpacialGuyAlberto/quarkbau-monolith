package com.quarkbau.monolith.graph.service;

import com.quarkbau.monolith.graph.repository.NetworkIntelligenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NetworkIntelligenceService {

    private final NetworkIntelligenceRepository intelligenceRepository;

    /**
     * Calcula qué HEUPs se ven afectados si un segmento específico se daña.
     * @param brokenSegmentId El ID del segmento dañado
     * @return Una lista de mapas con la información de los HEUPs afectados.
     */
    public List<Map<String, Object>> calculateRiskForSegment(Long brokenSegmentId) {
        return intelligenceRepository.findImpactedHeupsBySegmentFailure(brokenSegmentId);
    }

    /**
     * Encuentra la ruta más corta y barata entre un HEUP y un POP usando el algoritmo de Dijkstra.
     * @param heupId ID de la casa/HEUP
     * @param popId ID del centro de datos/POP
     * @return La lista ordenada de IDs que conforman el camino óptimo, junto a los costos.
     */
    public List<Map<String, Object>> calculateOptimalRoute(Long heupId, Long popId) {
        return intelligenceRepository.findOptimalRouteToPop(heupId, popId);
    }

    /**
     * Devuelve una alerta con todos los Netzverteiler que están a punto de llegar
     * al tope de su capacidad técnica (>90%).
     * @return Lista de Netzverteiler críticos.
     */
    public List<Map<String, Object>> getHighCapacityAlerts() {
        return intelligenceRepository.findHighCapacityNetzverteilers();
    }
}
