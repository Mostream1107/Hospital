package com.example.hospital.controller;

import com.example.hospital.dto.ApiResponse;
import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Doctor;
import com.example.hospital.service.DoctorService;
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
 * 医生控制器
 */
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DoctorController {
    
    private final DoctorService doctorService;
    
    /**
     * 创建医生
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Doctor>> createDoctor(@Valid @RequestBody Doctor doctor) {
        try {
            Doctor savedDoctor = doctorService.createDoctor(doctor);
            return ResponseEntity.ok(ApiResponse.success("创建医生成功", savedDoctor));
        } catch (Exception e) {
            log.error("创建医生失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取医生
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Doctor>> getDoctor(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.getDoctorById(id);
            return ResponseEntity.ok(ApiResponse.success(doctor));
        } catch (Exception e) {
            log.error("获取医生失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 更新医生信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Doctor>> updateDoctor(@PathVariable Long id, 
                                                           @Valid @RequestBody Doctor doctor) {
        try {
            Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
            return ResponseEntity.ok(ApiResponse.success("更新医生成功", updatedDoctor));
        } catch (Exception e) {
            log.error("更新医生失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 删除医生
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok(ApiResponse.success("删除医生成功", null));
        } catch (Exception e) {
            log.error("删除医生失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 分页获取医生列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<Doctor>>> getDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PageResponse<Doctor> doctors = doctorService.getDoctors(pageable);
        return ResponseEntity.ok(ApiResponse.success(doctors));
    }
    
    /**
     * 搜索医生
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Doctor>>> searchDoctors(@RequestParam String name) {
        List<Doctor> doctors = doctorService.searchDoctorsByName(name);
        return ResponseEntity.ok(ApiResponse.success(doctors));
    }
    
    /**
     * 根据科室获取医生
     */
    @GetMapping("/by-department")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Doctor>>> getDoctorsByDepartment(@RequestParam String department) {
        List<Doctor> doctors = doctorService.getDoctorsByDepartment(department);
        return ResponseEntity.ok(ApiResponse.success(doctors));
    }
    
    /**
     * 获取所有科室
     */
    @GetMapping("/departments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<String>>> getAllDepartments() {
        List<String> departments = doctorService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }
    
    /**
     * 获取可用医生
     */
    @GetMapping("/available")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Doctor>>> getAvailableDoctors() {
        List<Doctor> doctors = doctorService.getAvailableDoctors();
        return ResponseEntity.ok(ApiResponse.success(doctors));
    }
}














