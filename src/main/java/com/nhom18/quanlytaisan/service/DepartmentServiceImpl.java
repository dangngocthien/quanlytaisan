package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.DepartmentDTO;
import com.nhom18.quanlytaisan.entity.Department;
import com.nhom18.quanlytaisan.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    /**
     * Constructor Dependency Injection
     */
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<DepartmentDTO> getAll() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        return mapEntityToDTO(department);
    }

    @Override
    public DepartmentDTO create(DepartmentDTO departmentDTO) {
        // Kiểm tra code đã tồn tại chưa
        if (departmentRepository.existsByCode(departmentDTO.getCode())) {
            throw new RuntimeException("Department code already exists: " + departmentDTO.getCode());
        }

        // Mapping DTO → Entity
        Department department = mapDTOToEntity(departmentDTO);

        // Lưu vào database
        Department savedDepartment = departmentRepository.save(department);

        // Mapping Entity → DTO để trả về
        return mapEntityToDTO(savedDepartment);
    }

    @Override
    public DepartmentDTO update(Long id, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        // Cập nhật dữ liệu
        department.setName(departmentDTO.getName());
        department.setDescription(departmentDTO.getDescription());

        // Nếu code thay đổi, kiểm tra xem code mới đã tồn tại không
        if (!department.getCode().equals(departmentDTO.getCode())) {
            if (departmentRepository.existsByCode(departmentDTO.getCode())) {
                throw new RuntimeException("Department code already exists: " + departmentDTO.getCode());
            }
            department.setCode(departmentDTO.getCode());
        }

        // Lưu cập nhật
        Department updatedDepartment = departmentRepository.save(department);

        // Trả về DTO
        return mapEntityToDTO(updatedDepartment);
    }

    @Override
    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        departmentRepository.delete(department);
    }

    @Override
    public DepartmentDTO findByCode(String code) {
        Department department = departmentRepository.findByCode(code);
        if (department == null) {
            throw new RuntimeException("Department not found with code: " + code);
        }
        return mapEntityToDTO(department);
    }

    /**
     * Mapping Entity → DTO (thủ công)
     */
    private DepartmentDTO mapEntityToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setCode(department.getCode());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        return dto;
    }

    /**
     * Mapping DTO → Entity (thủ công)
     */
    private Department mapDTOToEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setCode(dto.getCode());
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        department.setCreatedAt(java.time.LocalDateTime.now());
        return department;
    }
}
