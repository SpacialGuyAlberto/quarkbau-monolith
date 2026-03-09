package com.quarkbau.monolith.infrastructure.controller;

import com.quarkbau.monolith.infrastructure.model.Inquiry;
import com.quarkbau.monolith.infrastructure.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("/project/{projectId}")
    public List<Inquiry> getProjectInquiries(@PathVariable Long projectId) {
        inquiryService.seedData();
        return inquiryService.getInquiriesForProject(projectId);
    }

    @PostMapping("/generate")
    public List<Inquiry> generateInquiries(@RequestParam Long projectId,
            @RequestParam(defaultValue = "Berlin") String region) {
        return inquiryService.generateInquiries(projectId, region);
    }

    @PutMapping("/{id}/status")
    public Inquiry updateStatus(@PathVariable Long id, @RequestParam Inquiry.InquiryStatus status) {
        return inquiryService.updateStatus(id, status);
    }

    @PostMapping("/{id}/analyze")
    public Inquiry analyzeInquiry(@PathVariable Long id) {
        return inquiryService.analyzeInquiry(id);
    }
}
