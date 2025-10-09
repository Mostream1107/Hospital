package com.example.hospital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 挂号实体类
 */
@Entity
@Table(name = "registrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Registration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "请选择病人")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @NotNull(message = "请选择医生")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @NotNull(message = "请选择预约时间")
    @Future(message = "预约时间必须是将来的时间")
    @Column(nullable = false)
    private LocalDateTime appointmentTime;
    
    @NotNull(message = "挂号费不能为空")
    @DecimalMin(value = "0.01", message = "挂号费必须大于0")
    @DecimalMax(value = "9999.99", message = "挂号费不能超过9999.99元")
    @Digits(integer = 4, fraction = 2, message = "挂号费格式不正确，最多4位整数2位小数")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal registrationFee;
    
    @NotNull(message = "挂号状态不能为空")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status = RegistrationStatus.PENDING;
    
    @Size(max = 500, message = "症状描述不能超过500个字符")
    @Column(columnDefinition = "TEXT")
    private String symptoms;
    
    @Size(max = 500, message = "诊断结果不能超过500个字符")
    @Column(columnDefinition = "TEXT")
    private String diagnosis;
    
    @Size(max = 500, message = "治疗方案不能超过500个字符")
    @Column(columnDefinition = "TEXT")
    private String treatment;
    
    @Size(max = 500, message = "处方内容不能超过500个字符")
    @Column(columnDefinition = "TEXT")
    private String prescription;
    
    @Size(max = 300, message = "备注信息不能超过300个字符")
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
