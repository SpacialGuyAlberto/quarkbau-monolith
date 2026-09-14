package com.quarkbau.monolith.auth.service;

import com.quarkbau.monolith.auth.dto.AuthResponse;
import com.quarkbau.monolith.auth.dto.LoginRequest;
import com.quarkbau.monolith.auth.dto.RegisterRequest;
import com.quarkbau.monolith.auth.model.User;
import com.quarkbau.monolith.auth.repository.UserRepository;
import com.quarkbau.monolith.planning.model.CompanyRole;
import com.quarkbau.monolith.planning.model.Employee;
import com.quarkbau.monolith.planning.model.InternalEmployee;
import com.quarkbau.monolith.planning.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        var user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setOrganizationId(request.getOrganizationId());
        InternalEmployee employee = (InternalEmployee) employeeService.save(request);

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .companyRole(employee.getRole())
                .userId(user.getId())
                .organizationId(user.getOrganizationId())
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.getEmail()));

        var jwtToken = jwtService.generateToken(user);
        CompanyRole role = employeeService.findById(user.getId()).getRole();
        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .companyRole(role)
                .userId(user.getId())
                .organizationId(user.getOrganizationId())
                .build();
    }
}
