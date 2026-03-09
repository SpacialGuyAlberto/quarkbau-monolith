package com.quarkbau.monolith.field.repository;

import com.quarkbau.monolith.field.model.FieldCrew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldCrewRepository extends JpaRepository<FieldCrew, Long> {
}
