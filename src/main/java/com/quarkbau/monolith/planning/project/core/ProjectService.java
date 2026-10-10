package com.quarkbau.monolith.planning.project.core;

import com.quarkbau.monolith.planning.project.core.ProjectDTO;
import com.quarkbau.monolith.planning.project.core.ProjectMapper;
import com.quarkbau.monolith.planning.project.core.Project;
import com.quarkbau.monolith.planning.project.core.ProjectRepository;
import lombok.RequiredArgsConstructor;
import com.quarkbau.monolith.shared.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService extends BaseServiceImpl<Project, ProjectDTO, Long> {
    public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        super(projectRepository, projectMapper);
    }
    
    // We can add custom methods here if needed
}
