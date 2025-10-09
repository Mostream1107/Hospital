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
 * 药品实体类
 */
@Entity
@Table(name = "medicines")
@Data
@NoArgsConstructor
public class Medicine {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "请输入药品名称")
    @Size(min = 2, max = 50, message = "药品名称长度应在2-50个字符之间")
    @Column(nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "请输入药品编码")
    @Pattern(regexp = "^[A-Z0-9]{4,20}$", message = "药品编码格式不正确，应为4-20位大写字母和数字组合")
    @Column(unique = true, nullable = false, length = 50)
    private String code;
    
    @Size(max = 20, message = "药品分类不能超过20个字符")
    @Column(length = 50)
    private String category;
    
    @NotBlank(message = "请输入药品规格")
    @Size(min = 1, max = 30, message = "规格长度应在1-30个字符之间")
    @Column(nullable = false, length = 50)
    private String specification;
    
    @NotBlank(message = "请输入药品单位")
    @Size(min = 1, max = 10, message = "单位长度应在1-10个字符之间")
    @Column(nullable = false, length = 20)
    private String unit;
    
    @NotNull(message = "请输入药品价格")
    @DecimalMin(value = "0.01", message = "药品价格必须大于0")
    @DecimalMax(value = "99999.99", message = "药品价格不能超过99999.99元")
    @Digits(integer = 5, fraction = 2, message = "价格格式不正确，最多5位整数2位小数")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @NotNull(message = "请输入库存数量")
    @Min(value = 0, message = "库存数量不能为负数")
    @Max(value = 999999, message = "库存数量不能超过999999")
    @Column(nullable = false)
    private Integer stock = 0;
    
    @Size(max = 50, message = "生产厂家名称不能超过50个字符")
    @Column(length = 100)
    private String manufacturer;
    
    @Size(max = 500, message = "适应症描述不能超过500个字符")
    @Column(columnDefinition = "TEXT")
    private String indication;
    
    @Size(max = 300, message = "用法用量描述不能超过300个字符")
    @Column(columnDefinition = "TEXT")
    private String dosage;
    
    @Size(max = 50, message = "剂型不能超过50个字符")
    @Column(length = 50)
    private String dosageForm;
    
    @Size(max = 500, message = "不良反应描述不能超过500个字符")
    @Column(columnDefinition = "TEXT")
    private String sideEffects;
    
    @Size(max = 500, message = "禁忌症描述不能超过500个字符")
    @Column(columnDefinition = "TEXT")
    private String contraindications;
    
    @NotNull(message = "可用状态不能为空")
    @Column(nullable = false)
    private Boolean available = true;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
