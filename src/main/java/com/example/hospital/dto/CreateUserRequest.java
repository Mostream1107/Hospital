package com.example.hospital.dto;

import com.example.hospital.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建用户请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private String phone;
    
    @NotNull(message = "用户角色不能为空")
    private Role role;
}














