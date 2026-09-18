package com.quarkbau.monolith.auth.dto;

import com.quarkbau.monolith.auth.model.UserRole;
import com.quarkbau.monolith.planning.model.CompanyRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role;
    private CompanyRole companyRole;
    private Long subcontractorId;
    private Long organizationId;
}
