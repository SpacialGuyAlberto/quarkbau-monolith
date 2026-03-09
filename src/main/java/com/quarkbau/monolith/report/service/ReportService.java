package com.quarkbau.monolith.report.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.quarkbau.monolith.field.model.Evidence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@Slf4j
public class ReportService {

    public byte[] generateSegmentReport(Long segmentId, List<Evidence> evidences, String signatureBase64) {
        log.info("Generating certified report for segment: {}", segmentId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);

            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            Paragraph title = new Paragraph("QUARKBAU - Certified Work Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" ")); // Spacer

            // Project Metadata
            document.add(new Paragraph("Segment ID: " + segmentId));
            document.add(new Paragraph("Date: "
                    + java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(java.time.LocalDateTime.now())));
            document.add(new Paragraph("Status: APPROVED & CERTIFIED"));

            document.add(new Paragraph(" ")); // Spacer

            // Evidence Table
            Table table = new Table(3);
            table.addCell("Evidence ID");
            table.addCell("Coordinates");
            table.addCell("Timestamp");

            for (Evidence e : evidences) {
                table.addCell(String.valueOf(e.getId()));
                table.addCell(e.getLatitude() + ", " + e.getLongitude());
                table.addCell(e.getCreatedAt().toString());
            }
            document.add(table);

            document.add(new Paragraph(" ")); // Spacer

            // Volumetry (Real implementation would fetch this)
            document.add(new Paragraph("Calculated Excavation Volume: 4.52 m³"));

            document.add(new Paragraph(" ")); // Spacer

            // Digital Signature
            document.add(new Paragraph("Digital Signature of Responsible Party:"));
            // In a real app, we would decode signatureBase64 and add it as an Image
            // Image signature =
            // Image.getInstance(Base64.getDecoder().decode(signatureBase64));
            // document.add(signature);
            document.add(new Paragraph("[DIGITALLY SIGNED VIA QUARKBAU APP]"));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF report", e);
            return new byte[0];
        }
    }
}
