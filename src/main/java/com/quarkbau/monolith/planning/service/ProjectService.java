package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.dto.ProjectDTO;
import com.quarkbau.monolith.planning.dto.mappers.ProjectMapper;
import com.quarkbau.monolith.planning.model.Project;
import com.quarkbau.monolith.planning.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public List<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public ProjectDTO create(ProjectDTO project) {
        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setDescription(project.getDescription());
        newProject.setStartDate(project.getStartDate());
        newProject.setEndDate(project.getEndDate());
        newProject.setGeometry(project.getGeometry());
        newProject.setLifecycleTodo(project.getLifecycleTodo());
        newProject.setLifecycleDone(project.getLifecycleDone());
        projectRepository.save(newProject);

        return projectMapper.toDto(newProject);
    }

    public ProjectDTO edit(ProjectDTO project) {
        Project mappedProject = projectMapper.toEntity(project);
        return projectMapper.toDto(projectRepository.save(mappedProject));
    }

    public void delete(ProjectDTO project) {
        Project mappedProject = projectMapper.toEntity(project);
        projectRepository.delete(mappedProject);
    }

    public ProjectDTO update(ProjectDTO project) {
        Project mappedProject = projectMapper.toEntity(project);
        projectRepository.save(mappedProject);
        return projectMapper.toDto(mappedProject);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }
}
