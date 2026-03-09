package com.quarkbau.monolith.infrastructure.service;

import com.quarkbau.monolith.infrastructure.model.Inquiry;
import com.quarkbau.monolith.infrastructure.model.UtilityProvider;
import com.quarkbau.monolith.infrastructure.repository.InquiryRepository;
import com.quarkbau.monolith.infrastructure.repository.UtilityProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final UtilityProviderRepository providerRepository;
    private final InquiryRepository inquiryRepository;
    private final DocumentAnalysisService documentAnalysisService;

    @Transactional
    public void seedData() {
        if (providerRepository.count() == 0) {
            UtilityProvider gas = new UtilityProvider();
            gas.setName("Berlin GasNetz GmbH");
            gas.setType("GAS");
            gas.setServiceRegion("Berlin");
            gas.setContactEmail("plananauskunft@gasnetz.berlin");
            providerRepository.save(gas);

            UtilityProvider water = new UtilityProvider();
            water.setName("Berliner Wasserbetriebe");
            water.setType("WATER");
            water.setServiceRegion("Berlin");
            water.setContactEmail("info@bwb.de");
            providerRepository.save(water);

            UtilityProvider telekom = new UtilityProvider();
            telekom.setName("Deutsche Telekom Technik");
            telekom.setType("TELECOM");
            telekom.setServiceRegion("Berlin");
            telekom.setContactEmail("trassen@telekom.de");
            providerRepository.save(telekom);
        }
    }

    public List<Inquiry> getInquiriesForProject(Long projectId) {
        return inquiryRepository.findByProjectId(projectId);
    }

    @Transactional
    public List<Inquiry> generateInquiries(Long projectId, String region) {
        List<UtilityProvider> providers = providerRepository.findByServiceRegion(region);
        List<Inquiry> createdInquiries = new ArrayList<>();
        List<Inquiry> existing = inquiryRepository.findByProjectId(projectId);

        for (UtilityProvider provider : providers) {
            boolean alreadyExists = existing.stream()
                    .anyMatch(i -> i.getUtilityProvider().getId().equals(provider.getId()));

            if (!alreadyExists) {
                Inquiry inquiry = new Inquiry();
                inquiry.setProjectId(projectId);
                inquiry.setUtilityProvider(provider);
                inquiry.setStatus(Inquiry.InquiryStatus.PENDING);
                createdInquiries.add(inquiryRepository.save(inquiry));
            }
        }
        return createdInquiries;
    }

    @Transactional
    public Inquiry analyzeInquiry(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId).orElseThrow();
        inquiry.setAnalysisStatus(Inquiry.AnalysisStatus.PENDING);
        inquiryRepository.save(inquiry);

        String result = documentAnalysisService.analyzeDocument(inquiry);
        inquiry.setAnalysisResult(result);
        inquiry.setAnalysisStatus(Inquiry.AnalysisStatus.COMPLETED);

        return inquiryRepository.save(inquiry);
    }

    @Transactional
    public Inquiry updateStatus(Long inquiryId, Inquiry.InquiryStatus status) {
        Inquiry i = inquiryRepository.findById(inquiryId).orElseThrow();
        i.setStatus(status);
        if (status == Inquiry.InquiryStatus.SENT) {
            i.setRequestDate(LocalDate.now());
        } else if (status == Inquiry.InquiryStatus.RECEIVED_CONFLICT) {
            i.setResponseFileUrl("http://localhost:4200/assets/mock-plan.svg");
        }
        return inquiryRepository.save(i);
    }
}
