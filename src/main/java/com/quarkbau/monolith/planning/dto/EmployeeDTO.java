package com.quarkbau.monolith.planning.dto;

import com.quarkbau.monolith.planning.model.CompanyRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private CompanyRole role;
    private Boolean enabled;
    private Long organizationId;
    private BigInteger subcontractorId;
}