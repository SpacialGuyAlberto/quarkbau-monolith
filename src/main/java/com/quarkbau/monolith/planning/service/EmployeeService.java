package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.auth.dto.RegisterRequest;
import com.quarkbau.monolith.planning.dto.mappers.EmployeeMapper;
import com.quarkbau.monolith.planning.model.CompanyRole;
import com.quarkbau.monolith.planning.model.Employee;
import com.quarkbau.monolith.planning.model.ExternalEmployee;
import com.quarkbau.monolith.planning.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public Employee save(RegisterRequest registerRequest) {
        Employee employee = EmployeeMapper.RequestToEntity(registerRequest);
        assignRoleToEmployee(employee, CompanyRole.PROJEKTLEITER);
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con email: " + email));
    }

    @Transactional
    public Employee update(Long id, Employee employeeDetails) {
        Employee existingEmployee = findById(id);
        existingEmployee.setFirstName(employeeDetails.getFirstName());
        existingEmployee.setLastName(employeeDetails.getLastName());
        existingEmployee.setRole(employeeDetails.getRole());
        existingEmployee.setEnabled(employeeDetails.getEnabled());

        if (employeeDetails.getEmail() != null && !existingEmployee.getEmail().equals(employeeDetails.getEmail())) {
            existingEmployee.setEmail(employeeDetails.getEmail());
        }
        return employeeRepository.save(existingEmployee);
    }

    @Transactional
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Empleado no encontrado con ID: " + id);
        }
        employeeRepository.deleteById(id);
    }

    public void assignRoleToEmployee(Employee employee, CompanyRole requestedRole) {

        boolean isExternal = employee instanceof ExternalEmployee;
        boolean elRolEsParaInternos = requestedRole.isInternal();
        if (isExternal && elRolEsParaInternos) {
            throw new RuntimeException("¡Error! Estás intentando darle un rol de Quarkbau a un empleado de la subcontrata.");
        }

        employee.setRole(requestedRole);
    }
}