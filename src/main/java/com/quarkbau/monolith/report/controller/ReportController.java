package com.quarkbau.monolith.report.controller;

import com.quarkbau.monolith.field.model.Evidence;
import com.quarkbau.monolith.field.repository.EvidenceRepository;
import com.quarkbau.monolith.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final EvidenceRepository evidenceRepository;

    @GetMapping("/segment/{segmentId}")
    public ResponseEntity<byte[]> getSegmentReport(@PathVariable Long segmentId) {
        List<Evidence> evidences = evidenceRepository.findBySegmentId(segmentId);

        // signatureBase64 would logically come from the last evidence record or a
        // dedicated audit record
        byte[] pdfContent = reportService.generateSegmentReport(segmentId, evidences, "");

        String filename = "QuarkBau_Report_Segment_" + segmentId + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }
}
