package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.dto.ClusterDTO;
import com.quarkbau.monolith.planning.dto.mappers.ClusterMapper;
import com.quarkbau.monolith.planning.model.Cluster;
import com.quarkbau.monolith.planning.model.Project;
import com.quarkbau.monolith.planning.repository.ClusterRepository;
import com.quarkbau.monolith.planning.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClusterService {

    private final ClusterRepository clusterRepository;
    private final ProjectRepository projectRepository;
    private final ClusterMapper clusterMapper;

    public List<ClusterDTO> findAll() {
        return clusterRepository.findAll().stream()
                .map(clusterMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ClusterDTO> findByProjectId(Long projectId) {
        return clusterRepository.findByProjectId(projectId).stream()
                .map(clusterMapper::toDto)
                .collect(Collectors.toList());
    }

    public ClusterDTO findById(Long id) {
        return clusterRepository.findById(id)
                .map(clusterMapper::toDto)
                .orElse(null);
    }

    @Transactional
    public ClusterDTO save(ClusterDTO dto) {
        Cluster cluster = clusterMapper.toEntity(dto);
        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found: " + dto.getProjectId()));
            cluster.setProject(project);
        }
        cluster = clusterRepository.save(cluster);
        return clusterMapper.toDto(cluster);
    }

    @Transactional
    public void deleteById(Long id) {
        clusterRepository.deleteById(id);
    }
}
