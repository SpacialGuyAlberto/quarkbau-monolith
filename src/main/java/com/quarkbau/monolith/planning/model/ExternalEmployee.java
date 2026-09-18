package com.quarkbau.monolith.planning.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@DiscriminatorValue("EXTERNAL")
@Getter
@Setter
public class ExternalEmployee extends Employee {

    @Column(name = "subcontractor_id", nullable = true)
    private Long subcontractorId;
}
