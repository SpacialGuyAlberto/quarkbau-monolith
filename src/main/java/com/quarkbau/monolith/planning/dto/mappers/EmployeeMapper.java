package com.quarkbau.monolith.planning.dto.mappers;


import com.quarkbau.monolith.auth.dto.RegisterRequest;
import com.quarkbau.monolith.planning.dto.EmployeeDTO;
import com.quarkbau.monolith.planning.model.Employee;
import com.quarkbau.monolith.planning.model.ExternalEmployee;
import com.quarkbau.monolith.planning.model.InternalEmployee;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {


    public static Employee RequestToEntity(RegisterRequest request) {
        if (request == null) {
            return null;
        }

        Employee employee;

        if (request.getCompanyRole() != null && request.getCompanyRole().isInternal()) {
            InternalEmployee internal = new InternalEmployee();
            internal.setOrganizationId(request.getOrganizationId());
            employee = internal;
        } else {
            ExternalEmployee external = new ExternalEmployee();
            external.setSubcontractorId(request.getSubcontractorId());
            employee = external;
        }
        employee.setEmail(request.getEmail());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setRole(request.getCompanyRole());

        // El empleado nace habilitado por defecto
        employee.setEnabled(true);

        return employee;
    }

    private static InternalEmployee buildInternalEmployee(RegisterRequest request, String encodedPassword) {
        InternalEmployee internal = new InternalEmployee();
        setCommonFields(internal, request, encodedPassword);
        internal.setOrganizationId(request.getOrganizationId());
        return internal;
    }

    private static ExternalEmployee buildExternalEmployee(RegisterRequest request, String encodedPassword) {
        ExternalEmployee external = new ExternalEmployee();
        setCommonFields(external, request, encodedPassword);

        external.setSubcontractorId(request.getSubcontractorId());
        return external;
    }

    private static void setCommonFields(Employee employee, RegisterRequest request, String encodedPassword) {
        employee.setEmail(request.getEmail());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setRole(request.getCompanyRole());
        employee.setEnabled(true);
    }

    public static Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee employee;

        if (dto.getRole() != null && dto.getRole().isInternal()) {
            InternalEmployee internal = new InternalEmployee();
            internal.setOrganizationId(dto.getOrganizationId());
            employee = internal;
        } else {
            ExternalEmployee external = new ExternalEmployee();
            external.setSubcontractorId(dto.getSubcontractorId());
            employee = external;
        }

        employee.setId(dto.getId());
        employee.setEmail(dto.getEmail());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setRole(dto.getRole());

        employee.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : true);

        return employee;
    }
}
