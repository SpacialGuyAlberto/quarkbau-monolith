package com.quarkbau.monolith.infrastructure.repository;

import com.quarkbau.monolith.infrastructure.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByProjectId(Long projectId);
}
