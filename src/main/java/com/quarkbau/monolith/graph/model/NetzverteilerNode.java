package com.quarkbau.monolith.graph.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Node("Netzverteiler")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NetzverteilerNode {
    @Id
    private Long id;
    private String identifier;
    private Integer usedPorts;
    private Integer totalPorts;
}
