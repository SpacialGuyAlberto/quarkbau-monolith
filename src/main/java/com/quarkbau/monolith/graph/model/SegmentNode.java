package com.quarkbau.monolith.graph.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Node("Segment")
@Getter
@Setter
@NoArgsConstructor
public class SegmentNode {

    @Id
    private Long id; // Same as PostgreSQL ID

    private String name;
    private String workType;
    private String state;

    @Relationship(type = "FOLLOWED_BY", direction = Relationship.Direction.OUTGOING)
    private Set<SegmentNode> nextSegments = new HashSet<>();

    public SegmentNode(Long id, String name, String workType, String state) {
        this.id = id;
        this.name = name;
        this.workType = workType;
        this.state = state;
    }
}
