package com.quarkbau.monolith.inventory.controller;

import com.quarkbau.monolith.inventory.model.Machine;
import com.quarkbau.monolith.inventory.model.MachineStatus;
import com.quarkbau.monolith.inventory.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/machines")
@RequiredArgsConstructor
public class MachineController {

    private final MachineRepository machineRepository;

    @GetMapping
    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }

    @GetMapping("/status/{status}")
    public List<Machine> getMachinesByStatus(@PathVariable MachineStatus status) {
        return machineRepository.findByStatus(status);
    }

    @PostMapping
    public Machine createMachine(@RequestBody Machine machine) {
        return machineRepository.save(machine);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Machine> updateMachineStatus(@PathVariable Long id, @RequestParam MachineStatus status) {
        return machineRepository.findById(id)
                .map(machine -> {
                    machine.setStatus(status);
                    return ResponseEntity.ok(machineRepository.save(machine));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
