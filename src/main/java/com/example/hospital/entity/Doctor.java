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
 * 医生实体类
 */
@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "医生姓名不能为空")
    @Size(min = 2, max = 20, message = "医生姓名长度应在2-20个字符之间")
    @Column(nullable = false, length = 50)
    private String name;
    
    @NotNull(message = "请选择医生性别")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    
    @NotBlank(message = "请输入所属科室")
    @Size(min = 2, max = 50, message = "科室名称长度应在2-50个字符之间")
    @Column(nullable = false, length = 100)
    private String department;
    
    @NotBlank(message = "请输入医生职称")
    @Size(min = 2, max = 20, message = "职称长度应在2-20个字符之间")
    @Column(nullable = false, length = 50)
    private String title;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确，请输入11位有效手机号")
    @Column(length = 20)
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    @Column(length = 100)
    private String email;
    
    @Size(max = 200, message = "专业特长描述不能超过200个字符")
    @Column(columnDefinition = "TEXT")
    private String specialization;
    
    @Size(max = 500, message = "医生简介不能超过500个字符")
    @Column(columnDefinition = "TEXT")
    private String introduction;
    
    @DecimalMin(value = "0.01", message = "挂号费必须大于0")
    @DecimalMax(value = "9999.99", message = "挂号费不能超过9999.99元")
    @Digits(integer = 4, fraction = 2, message = "挂号费格式不正确，最多4位整数2位小数")
    @Column(precision = 10, scale = 2)
    private BigDecimal consultationFee;
    
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
