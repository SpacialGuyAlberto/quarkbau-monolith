package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.infrastructure.storage.LocalFileStorageService;
import com.quarkbau.monolith.planning.model.Segment;
import com.quarkbau.monolith.planning.model.SegmentPermit;
import com.quarkbau.monolith.planning.repository.SegmentPermitRepository;
import com.quarkbau.monolith.planning.repository.SegmentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermitService {

    private final SegmentPermitRepository permitRepository;
    private final SegmentRepository segmentRepository;
    private final LocalFileStorageService storageService;

    public SegmentPermit uploadPermit(Long segmentId, MultipartFile file) throws Exception {
        Segment segment = segmentRepository.findById(segmentId)
            .orElseThrow(() -> new RuntimeException("Segment not found"));

        byte[] pdfBytes = file.getBytes();
        
        // 1. Upload Original PDF
        String pdfUrl = storageService.uploadFile(file.getOriginalFilename(), pdfBytes, "application/pdf");

        // 2. Generate PNG Preview
        String previewUrl = null;
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            PDFRenderer renderer = new PDFRenderer(document);
            if (document.getNumberOfPages() > 0) {
                // Render first page with 150 DPI for good balance of quality and size
                BufferedImage image = renderer.renderImageWithDPI(0, 150, ImageType.RGB);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                byte[] imageBytes = baos.toByteArray();
                
                previewUrl = storageService.uploadFile("preview_" + file.getOriginalFilename() + ".png", imageBytes, "image/png");
            }
        } catch (Exception e) {
            System.err.println("Failed to generate PDF preview: " + e.getMessage());
        }

        // 3. Save DB Record
        SegmentPermit permit = new SegmentPermit();
        permit.setSegment(segment);
        permit.setName(file.getOriginalFilename());
        permit.setPdfUrl(pdfUrl);
        permit.setPreviewImageUrl(previewUrl);

        return permitRepository.save(permit);
    }

    public List<SegmentPermit> getPermitsForSegment(Long segmentId) {
        return permitRepository.findBySegmentId(segmentId);
    }
}
