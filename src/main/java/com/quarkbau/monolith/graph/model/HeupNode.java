package com.quarkbau.monolith.graph.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Node("Heup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeupNode {
    @Id
    private Long id;
    private String address;
    private String status;
}
