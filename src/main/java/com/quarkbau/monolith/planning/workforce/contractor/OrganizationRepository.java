package com.quarkbau.monolith.planning.workforce.contractor;

import com.quarkbau.monolith.planning.workforce.contractor.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
