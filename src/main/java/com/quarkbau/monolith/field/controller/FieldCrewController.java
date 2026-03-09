package com.quarkbau.monolith.field.controller;

import com.quarkbau.monolith.field.model.FieldCrew;
import com.quarkbau.monolith.field.repository.FieldCrewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/field/crews")
@RequiredArgsConstructor
public class FieldCrewController {

    private final FieldCrewRepository repository;

    @GetMapping
    public List<FieldCrew> getAllCrews() {
        List<FieldCrew> crews = repository.findAll();
        if (crews.isEmpty()) {
            FieldCrew c1 = new FieldCrew();
            c1.setName("Team Alpha");
            c1.setLeader("Hans Mueller");
            c1.setMembers(5);
            c1.setCurrentProject("Berlin Fiber Deployment");
            c1.setStatus("active");
            c1.setLocation("Berlin Mitte");
            repository.save(c1);

            FieldCrew c2 = new FieldCrew();
            c2.setName("Team Beta");
            c2.setLeader("Anna Schmidt");
            c2.setMembers(4);
            c2.setCurrentProject("Munich Network Expansion");
            c2.setStatus("active");
            c2.setLocation("Munich Center");
            repository.save(c2);

            return repository.findAll();
        }
        return crews;
    }

    @GetMapping("/{id}")
    public FieldCrew getCrewById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crew not found with id: " + id));
    }

    @PostMapping
    public FieldCrew createCrew(@RequestBody FieldCrew crew) {
        return repository.save(crew);
    }

    @PutMapping("/{id}")
    public FieldCrew updateCrew(@PathVariable Long id, @RequestBody FieldCrew crewDetails) {
        FieldCrew crew = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crew not found with id: " + id));

        crew.setName(crewDetails.getName());
        crew.setLeader(crewDetails.getLeader());
        crew.setMembers(crewDetails.getMembers());
        crew.setCurrentProject(crewDetails.getCurrentProject());
        crew.setStatus(crewDetails.getStatus());
        crew.setLocation(crewDetails.getLocation());

        return repository.save(crew);
    }

    @DeleteMapping("/{id}")
    public void deleteCrew(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
