package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.dto.ProjectDTO;
import com.quarkbau.monolith.planning.model.Project;
import com.quarkbau.monolith.planning.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project create(ProjectDTO project) {
        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setDescription(project.getDescription());
        newProject.setStartDate(project.getStartDate());
        newProject.setEndDate(project.getEndDate());

        return projectRepository.save(newProject);
    }

    public void delete(ProjectDTO project) {
        projectRepository.delete(project);
    }

    public Project update(ProjectDTO project) {
        return projectRepository.save(project);
    }

}
