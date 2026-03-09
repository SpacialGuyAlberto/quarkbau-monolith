package com.quarkbau.monolith.inventory.repository;

import com.quarkbau.monolith.inventory.model.Machine;
import com.quarkbau.monolith.inventory.model.MachineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByStatus(MachineStatus status);
}
