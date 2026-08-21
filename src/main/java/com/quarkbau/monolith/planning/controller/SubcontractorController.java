package com.quarkbau.monolith.planning.controller;
import com.quarkbau.monolith.planning.dto.SubcontractorDTO;
import com.quarkbau.monolith.planning.service.SubcontractorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubcontractorController {
    @Autowired
    SubcontractorService service;

    @GetMapping("/subcontractors/{organizationId}")
    public List<SubcontractorDTO> getAllSubcontractors(@PathVariable Long organizationId) {
        return service.getAllSubcontractors(organizationId);
    }

    @PostMapping("/subcontractors/{organizationId}")
    public SubcontractorDTO createSubcontractor(@PathVariable Long organizationId, @RequestBody SubcontractorDTO subcontractorDTO) {
        return service.createSubcontractor(organizationId, subcontractorDTO);
    }

}
