package com.example.hospital.controller;

import com.example.hospital.dto.ApiResponse;
import com.example.hospital.dto.PageResponse;
import com.example.hospital.dto.PaymentDTO;
import com.example.hospital.entity.Payment;
import com.example.hospital.entity.Patient;
import com.example.hospital.entity.Registration;
import com.example.hospital.entity.Medicine;
import com.example.hospital.entity.PaymentStatus;
import com.example.hospital.service.PaymentService;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.repository.RegistrationRepository;
import com.example.hospital.repository.MedicineRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 收费控制器
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PaymentController {
    
    private final PaymentService paymentService;
    private final PatientRepository patientRepository;
    private final RegistrationRepository registrationRepository;
    private final MedicineRepository medicineRepository;
    
    /**
     * 创建收费记录
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Payment>> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        try {
            Payment payment = convertDTOToEntity(paymentDTO);
            Payment savedPayment = paymentService.createPayment(payment);
            return ResponseEntity.ok(ApiResponse.success("创建收费记录成功", savedPayment));
        } catch (Exception e) {
            log.error("创建收费记录失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取收费记录
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Payment>> getPayment(@PathVariable Long id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            return ResponseEntity.ok(ApiResponse.success(payment));
        } catch (Exception e) {
            log.error("获取收费记录失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 更新收费记录
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Payment>> updatePayment(@PathVariable Long id, 
                                                             @Valid @RequestBody PaymentDTO paymentDTO) {
        try {
            Payment payment = convertDTOToEntity(paymentDTO);
            Payment updatedPayment = paymentService.updatePayment(id, payment);
            return ResponseEntity.ok(ApiResponse.success("更新收费记录成功", updatedPayment));
        } catch (Exception e) {
            log.error("更新收费记录失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 删除收费记录
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable Long id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.ok(ApiResponse.success("删除收费记录成功", null));
        } catch (Exception e) {
            log.error("删除收费记录失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 分页获取收费记录列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<Payment>>> getPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PageResponse<Payment> payments = paymentService.getPayments(pageable);
        return ResponseEntity.ok(ApiResponse.success(payments));
    }
    
    /**
     * 根据病人ID获取收费记录
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByPatient(@PathVariable Long patientId) {
        List<Payment> payments = paymentService.getPaymentsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(payments));
    }
    
    /**
     * 更新支付状态
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Payment>> updatePaymentStatus(@PathVariable Long id, 
                                                                   @RequestParam PaymentStatus status) {
        try {
            Payment payment = paymentService.updatePaymentStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("更新支付状态成功", payment));
        } catch (Exception e) {
            log.error("更新支付状态失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 处理支付
     */
    @PutMapping("/{id}/pay")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Payment>> processPayment(@PathVariable Long id, 
                                                              @RequestParam String transactionId) {
        try {
            Payment payment = paymentService.processPayment(id, transactionId);
            return ResponseEntity.ok(ApiResponse.success("支付成功", payment));
        } catch (Exception e) {
            log.error("支付失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 处理退费
     */
    @PutMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Payment>> processRefund(@PathVariable Long id) {
        try {
            Payment payment = paymentService.processRefund(id);
            return ResponseEntity.ok(ApiResponse.success("退费成功", payment));
        } catch (Exception e) {
            log.error("退费失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 获取今日收费统计
     */
    @GetMapping("/statistics/today")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<Object[]>>> getTodayStatistics() {
        List<Object[]> statistics = paymentService.getTodayPaymentStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }
    
    /**
     * 获取指定时间范围内的总收入
     */
    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        BigDecimal revenue = paymentService.getTotalRevenue(startTime, endTime);
        return ResponseEntity.ok(ApiResponse.success(revenue));
    }
    
    /**
     * 将PaymentDTO转换为Payment实体
     */
    private Payment convertDTOToEntity(PaymentDTO dto) {
        Payment payment = new Payment();
        
        // 基本字段
        payment.setId(dto.getId());
        payment.setPaymentType(dto.getPaymentType());
        payment.setAmount(dto.getAmount());
        payment.setStatus(dto.getStatus());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTransactionId(dto.getTransactionId());
        payment.setQuantity(dto.getQuantity());
        payment.setDescription(dto.getDescription());
        payment.setNotes(dto.getNotes());
        
        // 关联实体 - Patient（必需）
        if (dto.getPatient() != null && dto.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(dto.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("病人不存在: ID=" + dto.getPatient().getId()));
            payment.setPatient(patient);
        }
        
        // 关联实体 - Registration（可选）
        if (dto.getRegistration() != null && dto.getRegistration().getId() != null) {
            Registration registration = registrationRepository.findById(dto.getRegistration().getId())
                .orElseThrow(() -> new RuntimeException("挂号记录不存在: ID=" + dto.getRegistration().getId()));
            payment.setRegistration(registration);
        }
        
        // 关联实体 - Medicine（可选）
        if (dto.getMedicine() != null && dto.getMedicine().getId() != null) {
            Medicine medicine = medicineRepository.findById(dto.getMedicine().getId())
                .orElseThrow(() -> new RuntimeException("药品不存在: ID=" + dto.getMedicine().getId()));
            payment.setMedicine(medicine);
        }
        
        return payment;
    }
}


