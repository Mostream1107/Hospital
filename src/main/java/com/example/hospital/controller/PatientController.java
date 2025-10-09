package com.example.hospital.controller;

import com.example.hospital.dto.ApiResponse;
import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Patient;
import com.example.hospital.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 病人控制器
 */
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PatientController {
    
    private final PatientService patientService;
    
    /**
     * 创建病人
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Patient>> createPatient(@Valid @RequestBody Patient patient) {
        try {
            Patient savedPatient = patientService.createPatient(patient);
            return ResponseEntity.ok(ApiResponse.success("创建病人成功", savedPatient));
        } catch (Exception e) {
            log.error("创建病人失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取病人
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Patient>> getPatient(@PathVariable Long id) {
        try {
            Patient patient = patientService.getPatientById(id);
            return ResponseEntity.ok(ApiResponse.success(patient));
        } catch (Exception e) {
            log.error("获取病人失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 更新病人信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Patient>> updatePatient(@PathVariable Long id, 
                                                             @Valid @RequestBody Patient patient) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patient);
            return ResponseEntity.ok(ApiResponse.success("更新病人成功", updatedPatient));
        } catch (Exception e) {
            log.error("更新病人失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 删除病人
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.ok(ApiResponse.success("删除病人成功", null));
        } catch (Exception e) {
            log.error("删除病人失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 分页获取病人列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<Patient>>> getPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PageResponse<Patient> patients = patientService.getPatients(pageable);
        return ResponseEntity.ok(ApiResponse.success(patients));
    }
    
    /**
     * 搜索病人 - 支持多字段搜索（姓名、身份证号、手机号）
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Patient>>> searchPatients(@RequestParam String keyword) {
        try {
            List<Patient> patients = patientService.searchPatients(keyword);
            return ResponseEntity.ok(ApiResponse.success(patients));
        } catch (Exception e) {
            log.error("搜索病人失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 根据身份证号查找病人
     */
    @GetMapping("/by-idcard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Patient>> getPatientByIdCard(@RequestParam String idCard) {
        try {
            Patient patient = patientService.getPatientByIdCard(idCard);
            return ResponseEntity.ok(ApiResponse.success(patient));
        } catch (Exception e) {
            log.error("根据身份证号查找病人失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}



