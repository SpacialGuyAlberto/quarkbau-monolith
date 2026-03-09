package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.dto.ProjectDTO;
import com.quarkbau.monolith.planning.dto.mappers.ProjectMapper;
import com.quarkbau.monolith.planning.model.Project;
import com.quarkbau.monolith.planning.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public Project create(ProjectDTO project) {
        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setDescription(project.getDescription());
        newProject.setStartDate(project.getStartDate());
        newProject.setEndDate(project.getEndDate());

        return projectRepository.save(newProject);
    }

    public void delete(ProjectDTO project) {
        Project mappedProject = projectMapper.toEntity(project);
        projectRepository.delete(mappedProject);
    }

    public Project update(ProjectDTO project) {
        Project mappedProject = projectMapper.toEntity(project);
        return projectRepository.save(mappedProject);
    }

}
