package com.example.hospital.controller;

import com.example.hospital.dto.ApiResponse;
import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Registration;
import com.example.hospital.entity.RegistrationStatus;
import com.example.hospital.service.RegistrationService;
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
 * 挂号控制器
 */
@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RegistrationController {
    
    private final RegistrationService registrationService;
    
    /**
     * 创建挂号
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Registration>> createRegistration(@Valid @RequestBody Registration registration) {
        try {
            Registration savedRegistration = registrationService.createRegistration(registration);
            return ResponseEntity.ok(ApiResponse.success("创建挂号成功", savedRegistration));
        } catch (Exception e) {
            log.error("创建挂号失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取挂号
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Registration>> getRegistration(@PathVariable Long id) {
        try {
            Registration registration = registrationService.getRegistrationById(id);
            return ResponseEntity.ok(ApiResponse.success(registration));
        } catch (Exception e) {
            log.error("获取挂号失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 更新挂号信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Registration>> updateRegistration(@PathVariable Long id, 
                                                                       @Valid @RequestBody Registration registration) {
        try {
            Registration updatedRegistration = registrationService.updateRegistration(id, registration);
            return ResponseEntity.ok(ApiResponse.success("更新挂号成功", updatedRegistration));
        } catch (Exception e) {
            log.error("更新挂号失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 分页获取挂号列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<Registration>>> getRegistrations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PageResponse<Registration> registrations = registrationService.getRegistrations(pageable);
        return ResponseEntity.ok(ApiResponse.success(registrations));
    }
    
    /**
     * 根据病人ID获取挂号列表
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Registration>>> getRegistrationsByPatient(@PathVariable Long patientId) {
        List<Registration> registrations = registrationService.getRegistrationsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(registrations));
    }
    
    /**
     * 根据医生ID获取挂号列表
     */
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Registration>>> getRegistrationsByDoctor(@PathVariable Long doctorId) {
        List<Registration> registrations = registrationService.getRegistrationsByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.success(registrations));
    }
    
    /**
     * 更新挂号状态
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Registration>> updateRegistrationStatus(@PathVariable Long id, 
                                                                            @RequestParam RegistrationStatus status) {
        try {
            Registration registration = registrationService.updateRegistrationStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("更新挂号状态成功", registration));
        } catch (Exception e) {
            log.error("更新挂号状态失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 删除挂号
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRegistration(@PathVariable Long id) {
        try {
            registrationService.deleteRegistration(id);
            return ResponseEntity.ok(ApiResponse.success("删除挂号成功"));
        } catch (Exception e) {
            log.error("删除挂号失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 获取今日挂号统计
     */
    @GetMapping("/statistics/today")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Object[]>>> getTodayStatistics() {
        List<Object[]> statistics = registrationService.getTodayRegistrationStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }
}
