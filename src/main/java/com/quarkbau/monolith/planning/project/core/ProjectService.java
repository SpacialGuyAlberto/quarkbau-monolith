package com.quarkbau.monolith.planning.project.core;

import com.quarkbau.monolith.planning.project.core.ProjectDTO;
import com.quarkbau.monolith.planning.project.core.ProjectMapper;
import com.quarkbau.monolith.planning.project.core.Project;
import com.quarkbau.monolith.planning.project.core.ProjectRepository;
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

        Double lat = project.getLatitude() != null ? project.getLatitude() : project.getStartLatitude();
        Double lng = project.getLongitude() != null ? project.getLongitude() : project.getStartLongitude();
        if (lat == null && project.getGeometry() != null && !project.getGeometry().isEmpty()) {
            lat = project.getGeometry().get(0).getLat();
            lng = project.getGeometry().get(0).getLng();
        }
        newProject.setStartLatitude(lat);
        newProject.setStartLongitude(lng);

        projectRepository.save(newProject);

        return projectMapper.toDto(newProject);
    }

    public ProjectDTO edit(ProjectDTO project) {
        Project mappedProject = projectMapper.toEntity(project);
        if (mappedProject.getStartLatitude() == null) {
            Double lat = project.getLatitude() != null ? project.getLatitude() : project.getStartLatitude();
            mappedProject.setStartLatitude(lat);
        }
        if (mappedProject.getStartLongitude() == null) {
            Double lng = project.getLongitude() != null ? project.getLongitude() : project.getStartLongitude();
            mappedProject.setStartLongitude(lng);
        }
        return projectMapper.toDto(projectRepository.save(mappedProject));
    }

    public void delete(ProjectDTO project) {
        Project mappedProject = projectMapper.toEntity(project);
        projectRepository.delete(mappedProject);
    }

    public ProjectDTO update(ProjectDTO project) {
        Project mappedProject = projectMapper.toEntity(project);
        if (mappedProject.getStartLatitude() == null) {
            Double lat = project.getLatitude() != null ? project.getLatitude() : project.getStartLatitude();
            mappedProject.setStartLatitude(lat);
        }
        if (mappedProject.getStartLongitude() == null) {
            Double lng = project.getLongitude() != null ? project.getLongitude() : project.getStartLongitude();
            mappedProject.setStartLongitude(lng);
        }
        projectRepository.save(mappedProject);
        return projectMapper.toDto(mappedProject);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }
}
