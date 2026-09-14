package com.quarkbau.monolith.planning.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("INTERNAL")
@Getter
@Setter
public class InternalEmployee extends Employee {
    @Column(name = "organization_id")
    private Long organizationId;
}
