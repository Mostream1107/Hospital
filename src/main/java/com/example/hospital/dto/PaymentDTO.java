package com.example.hospital.dto;

import com.example.hospital.entity.PaymentStatus;
import com.example.hospital.entity.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 收费数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    
    private Long id;
    
    @NotNull(message = "病人不能为空")
    private PatientRef patient;
    
    private RegistrationRef registration;
    
    private MedicineRef medicine;
    
    @NotNull(message = "支付类型不能为空")
    private PaymentType paymentType;
    
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.00", message = "金额不能为负数")
    private BigDecimal amount;
    
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @NotBlank(message = "支付方式不能为空")
    private String paymentMethod;
    
    private String transactionId;
    
    @Min(value = 1, message = "数量不能小于1")
    private Integer quantity = 1;
    
    private String description;
    private String notes;
    
    // 内部引用类
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientRef {
        @NotNull(message = "病人ID不能为空")
        private Long id;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistrationRef {
        private Long id;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicineRef {
        private Long id;
    }
}

