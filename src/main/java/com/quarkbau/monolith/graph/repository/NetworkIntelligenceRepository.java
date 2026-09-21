package com.quarkbau.monolith.graph.repository;

import com.quarkbau.monolith.graph.model.SegmentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface NetworkIntelligenceRepository extends Neo4jRepository<SegmentNode, Long> {

    // ========================================================================
    // EJEMPLO 1: Cálculo de Riesgo (Single Point of Failure)
    // ========================================================================
    // Encuentra todos los HEUPs (casas) que se quedarían sin conexión si el 
    // Segmento (id: $brokenSegmentId) es cortado por una excavadora.
    // La consulta busca caminos desde el POP hasta los HEUPs, pero solo retorna 
    // aquellos HEUPs cuyo ÚNICO camino pase obligatoriamente por el segmento roto.
    @Query("""
        MATCH p = (pop:Pop)-[:CONNECTED_VIA*]->(heup:Heup)
        WHERE $brokenSegmentId IN [rel IN relationships(p) | rel.segmentId]
        // Aquí agregarías lógica adicional en Cypher para verificar si hay rutas alternativas
        RETURN heup.id AS affectedHeupId, heup.address AS address
    """)
    List<Map<String, Object>> findImpactedHeupsBySegmentFailure(Long brokenSegmentId);


    // ========================================================================
    // EJEMPLO 2: Enrutamiento Óptimo (Pathfinding)
    // ========================================================================
    // Usa el algoritmo Dijkstra preconstruido en Graph Data Science (GDS) de Neo4j
    // para encontrar el camino más corto (y barato basado en 'cost') entre un POP y un HEUP.
    @Query("""
        MATCH (start:Pop {id: $popId}), (end:Heup {id: $heupId})
        CALL gds.shortestPath.dijkstra.stream({
            nodeProjection: '*',
            relationshipProjection: {
                CONNECTED_VIA: { type: 'CONNECTED_VIA', properties: 'cost' }
            },
            sourceNode: start,
            targetNode: end,
            relationshipWeightProperty: 'cost'
        })
        YIELD nodeIds, costs
        RETURN [node in gds.util.asNodes(nodeIds) | node.id] AS optimalPathIds, costs
    """)
    List<Map<String, Object>> findOptimalRouteToPop(Long heupId, Long popId);


    // ========================================================================
    // EJEMPLO 3: Análisis de Capacidad
    // ========================================================================
    // Encuentra todos los Netzverteiler (DP) que están al >90% de capacidad
    // y que comparten un segmento de fibra específico.
    @Query("""
        MATCH (nvt:Netzverteiler)-[rel:CONNECTED_VIA]->(seg:Segment)
        WHERE (nvt.usedPorts * 1.0 / nvt.totalPorts) > 0.90
        RETURN nvt.id AS dpId, nvt.usedPorts AS used, nvt.totalPorts AS total, seg.id AS sharedSegmentId
    """)
    List<Map<String, Object>> findHighCapacityNetzverteilers();

    // ========================================================================
    // MUTATION QUERIES (CREATING RELATIONSHIPS)
    // ========================================================================
    
    @Query("MATCH (a {id: $startId}), (b {id: $endId}) " +
           "MERGE (a)-[r:CONNECTED_VIA {segmentId: $segmentId, length: coalesce($length, 0.0)}]->(b)")
    void connectNodes(Long startId, Long endId, Long segmentId, Double length);

}
