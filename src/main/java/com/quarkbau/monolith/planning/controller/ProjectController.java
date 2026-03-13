package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.model.Project;
import com.quarkbau.monolith.planning.repository.ProjectRepository;
import com.quarkbau.monolith.planning.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository repository;
    private final ProjectService service;

    @GetMapping
    public List<Project> getAllProjects() {
        List<Project> projects = service.findAllProjects();
        if (projects.isEmpty()) {
            Project p1 = new Project();
            p1.setName("Berlin Fiber Optics");
            p1.setDescription("Deploying fiber optics in Berlin Mitte");
            repository.save(p1);

            Project p2 = new Project();
            p2.setName("Munich 5G Expansion");
            p2.setDescription("Expanding 5G coverage in Munich");
            repository.save(p2);

            return repository.findAll();
        }
        return projects;
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }
}
