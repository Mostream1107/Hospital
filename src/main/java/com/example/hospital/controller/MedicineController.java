package com.example.hospital.controller;

import com.example.hospital.dto.ApiResponse;
import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Medicine;
import com.example.hospital.service.MedicineService;
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
import java.util.Map;

/**
 * 药品控制器
 */
@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MedicineController {
    
    private final MedicineService medicineService;
    
    /**
     * 创建药品
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Medicine>> createMedicine(@Valid @RequestBody Medicine medicine) {
        try {
            Medicine savedMedicine = medicineService.createMedicine(medicine);
            return ResponseEntity.ok(ApiResponse.success("创建药品成功", savedMedicine));
        } catch (Exception e) {
            log.error("创建药品失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取药品
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Medicine>> getMedicine(@PathVariable Long id) {
        try {
            Medicine medicine = medicineService.getMedicineById(id);
            return ResponseEntity.ok(ApiResponse.success(medicine));
        } catch (Exception e) {
            log.error("获取药品失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 更新药品信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Medicine>> updateMedicine(@PathVariable Long id, 
                                                               @Valid @RequestBody Medicine medicine) {
        try {
            Medicine updatedMedicine = medicineService.updateMedicine(id, medicine);
            return ResponseEntity.ok(ApiResponse.success("更新药品成功", updatedMedicine));
        } catch (Exception e) {
            log.error("更新药品失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 删除药品
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMedicine(@PathVariable Long id) {
        try {
            medicineService.deleteMedicine(id);
            return ResponseEntity.ok(ApiResponse.success("删除药品成功", null));
        } catch (Exception e) {
            log.error("删除药品失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 分页获取药品列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<Medicine>>> getMedicines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String category) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PageResponse<Medicine> medicines;
        if (category != null && !category.trim().isEmpty()) {
            medicines = medicineService.getMedicinesByCategory(category.trim(), pageable);
        } else {
            medicines = medicineService.getMedicines(pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(medicines));
    }
    
    /**
     * 搜索药品
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Medicine>>> searchMedicines(@RequestParam String name) {
        List<Medicine> medicines = medicineService.searchMedicinesByName(name);
        return ResponseEntity.ok(ApiResponse.success(medicines));
    }
    
    /**
     * 根据药品编码查找药品
     */
    @GetMapping("/by-code")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Medicine>> getMedicineByCode(@RequestParam String code) {
        try {
            Medicine medicine = medicineService.getMedicineByCode(code);
            return ResponseEntity.ok(ApiResponse.success(medicine));
        } catch (Exception e) {
            log.error("根据编码查找药品失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 获取库存不足的药品
     */
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Medicine>>> getLowStockMedicines(@RequestParam(defaultValue = "10") Integer threshold) {
        List<Medicine> medicines = medicineService.getLowStockMedicines(threshold);
        return ResponseEntity.ok(ApiResponse.success(medicines));
    }
    
    /**
     * 更新药品库存
     */
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Medicine>> updateStock(@PathVariable Long id, 
                                                           @RequestParam Integer quantity) {
        try {
            Medicine medicine = medicineService.updateStock(id, quantity);
            return ResponseEntity.ok(ApiResponse.success("更新库存成功", medicine));
        } catch (Exception e) {
            log.error("更新库存失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 初始化常用药品数据
     */
    @PostMapping("/init-common-medicines")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> initializeCommonMedicines() {
        try {
            Map<String, Integer> result = medicineService.initializeCommonMedicines();
            return ResponseEntity.ok(ApiResponse.success("初始化常用药品数据成功", result));
        } catch (Exception e) {
            log.error("初始化常用药品数据失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}













