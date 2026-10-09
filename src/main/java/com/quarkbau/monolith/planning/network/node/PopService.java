package com.quarkbau.monolith.planning.network.node;

import com.quarkbau.monolith.planning.project.cluster.Cluster;
import com.quarkbau.monolith.planning.network.node.PopDTO;
import com.quarkbau.monolith.planning.network.node.PopMapper;
import com.quarkbau.monolith.planning.network.node.Pop;
import com.quarkbau.monolith.planning.network.node.PopRepository;
import com.quarkbau.monolith.planning.project.cluster.ClusterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopService {
    private final PopRepository repository;
    private final ClusterRepository clusterRepository;
    private final PopMapper mapper;

    public List<PopDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<PopDTO> findByClusterId(Long clusterId) {
        return repository.findByClusterId(clusterId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<PopDTO> findByProjectId(Long projectId) {
        return repository.findByProjectId(projectId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public PopDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    public PopDTO save(PopDTO dto) {
        Pop entity = mapper.toEntity(dto);
        if (dto.getClusterId() != null) {
            com.quarkbau.monolith.planning.project.cluster.Cluster cluster = clusterRepository.findById(dto.getClusterId()).orElseThrow(() -> new IllegalArgumentException("Cluster not found"));
            entity.setCluster(cluster);
        }
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
