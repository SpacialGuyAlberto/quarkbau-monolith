package com.quarkbau.monolith.planning.network.node;

import com.quarkbau.monolith.planning.network.node.NetzverteilerDTO;
import com.quarkbau.monolith.planning.network.node.NetzverteilerMapper;
import com.quarkbau.monolith.planning.network.node.Netzverteiler;
import com.quarkbau.monolith.planning.network.node.NetzverteilerRepository;
import com.quarkbau.monolith.planning.network.node.PopRepository;
import com.quarkbau.monolith.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NetzverteilerService {
    private final NetzverteilerRepository repository;
    private final PopRepository popRepository;
    private final NetzverteilerMapper mapper;
    private final ScheduleService scheduleService;

    public List<NetzverteilerDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<NetzverteilerDTO> findByProjectId(Long projectId) {
        return repository.findByProjectId(projectId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public NetzverteilerDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    public NetzverteilerDTO save(NetzverteilerDTO dto) {
        Netzverteiler entity = mapper.toEntity(dto);
        if (dto.getPopId() != null) {
            com.quarkbau.monolith.planning.network.node.Pop pop = popRepository.findById(dto.getPopId()).orElseThrow(() -> new IllegalArgumentException("Pop not found"));
            entity.setPop(pop);
        }
        Netzverteiler saved = repository.save(entity);

        if (dto.getAssignedCrewId() != null && dto.getPlannedStartDate() != null && dto.getPlannedEndDate() != null) {
            scheduleService.syncNodeAssignments(
                    dto.getAssignedCrewId(),
                    saved.getId(),
                    "NVT",
                    saved.getIdentifier(),
                    dto.getPlannedStartDate(),
                    dto.getPlannedEndDate()
            );
        }

        return mapper.toDto(saved);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
