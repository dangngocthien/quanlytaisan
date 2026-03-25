package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.EmployeeDTO;
import com.nhom18.quanlytaisan.entity.Department;
import com.nhom18.quanlytaisan.entity.Employee;
import com.nhom18.quanlytaisan.repository.DepartmentRepository;
import com.nhom18.quanlytaisan.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    /**
     * Constructor Dependency Injection
     */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<EmployeeDTO> getAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return mapEntityToDTO(employee);
    }

    @Override
    public EmployeeDTO create(EmployeeDTO employeeDTO) {
        // Kiểm tra employee code đã tồn tại chưa
        if (employeeRepository.existsByEmployeeCode(employeeDTO.getEmployeeCode())) {
            throw new RuntimeException("Employee code already exists: " + employeeDTO.getEmployeeCode());
        }

        // Mapping DTO → Entity
        Employee employee = mapDTOToEntity(employeeDTO);

        // Lưu vào database
        Employee savedEmployee = employeeRepository.save(employee);

        // Mapping Entity → DTO để trả về
        return mapEntityToDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO update(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // Cập nhật dữ liệu
        employee.setFullName(employeeDTO.getFullName());
        employee.setJobTitle(employeeDTO.getJobTitle());

        // Nếu employee code thay đổi, kiểm tra xem code mới đã tồn tại không
        if (!employee.getEmployeeCode().equals(employeeDTO.getEmployeeCode())) {
            if (employeeRepository.existsByEmployeeCode(employeeDTO.getEmployeeCode())) {
                throw new RuntimeException("Employee code already exists: " + employeeDTO.getEmployeeCode());
            }
            employee.setEmployeeCode(employeeDTO.getEmployeeCode());
        }

        // Cập nhật phòng ban nếu departmentId thay đổi
        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + employeeDTO.getDepartmentId()));
            employee.setDepartment(department);
        }

        // Lưu cập nhật
        Employee updatedEmployee = employeeRepository.save(employee);

        // Trả về DTO
        return mapEntityToDTO(updatedEmployee);
    }

    @Override
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        employeeRepository.delete(employee);
    }

    @Override
    public EmployeeDTO findByEmployeeCode(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode);
        if (employee == null) {
            throw new RuntimeException("Employee not found with code: " + employeeCode);
        }
        return mapEntityToDTO(employee);
    }

    /**
     * Mapping Entity → DTO (thủ công) - Includes Department info
     */
    private EmployeeDTO mapEntityToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setEmployeeCode(employee.getEmployeeCode());
        dto.setFullName(employee.getFullName());
        dto.setJobTitle(employee.getJobTitle());

        // Map Department Info
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }

        return dto;
    }

    /**
     * Mapping DTO → Entity (thủ công)
     */
    private Employee mapDTOToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setFullName(dto.getFullName());
        employee.setJobTitle(dto.getJobTitle());
        employee.setCreatedAt(java.time.LocalDateTime.now());

        // Set Department nếu có departmentId
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartmentId()));
            employee.setDepartment(department);
        }

        return employee;
    }
}
