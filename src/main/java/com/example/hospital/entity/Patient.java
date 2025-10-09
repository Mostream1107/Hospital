package com.example.hospital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 病人实体类
 */
@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "病人姓名不能为空")
    @Size(min = 2, max = 20, message = "病人姓名长度应在2-20个字符之间")
    @Column(nullable = false, length = 50)
    private String name;
    
    @NotNull(message = "请选择病人性别")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    
    @NotNull(message = "请选择出生日期")
    @Past(message = "出生日期必须是过去的日期")
    @Column(nullable = false)
    private LocalDate birthDate;
    
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", 
             message = "身份证号格式不正确，请输入18位有效身份证号")
    @Column(unique = true, nullable = false, length = 18)
    private String idCard;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确，请输入11位有效手机号")
    @Column(length = 20)
    private String phone;
    
    @Size(max = 100, message = "地址长度不能超过100个字符")
    @Column(length = 200)
    private String address;
    
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    @Column(length = 100)
    private String email;
    
    @Size(min = 2, max = 20, message = "紧急联系人姓名长度应在2-20个字符之间")
    @Column(length = 50)
    private String emergencyContact;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "紧急联系人手机号格式不正确，请输入11位有效手机号")
    @Column(length = 20)
    private String emergencyPhone;
    
    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
    
    @Column(columnDefinition = "TEXT")
    private String allergies;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

