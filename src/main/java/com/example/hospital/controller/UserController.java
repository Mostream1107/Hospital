package com.example.hospital.controller;

import com.example.hospital.dto.ApiResponse;
import com.example.hospital.dto.CreateUserRequest;
import com.example.hospital.dto.PageResponse;
import com.example.hospital.dto.UserDTO;
import com.example.hospital.entity.Role;
import com.example.hospital.service.UserService;
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
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    /**
     * 创建用户
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO savedUser = userService.createUser(request);
            return ResponseEntity.ok(ApiResponse.success("创建用户成功", savedUser));
        } catch (Exception e) {
            log.error("创建用户失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            log.error("获取用户失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, 
                                                          @Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(ApiResponse.success("更新用户成功", updatedUser));
        } catch (Exception e) {
            log.error("更新用户失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("删除用户成功", null));
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 分页获取用户列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PageResponse<UserDTO> users = userService.getUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    /**
     * 根据角色获取用户
     */
    @GetMapping("/by-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getUsersByRole(@RequestParam Role role) {
        List<UserDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    /**
     * 切换用户状态
     */
    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> toggleUserStatus(@PathVariable Long id) {
        try {
            userService.toggleUserStatus(id);
            return ResponseEntity.ok(ApiResponse.success("用户状态切换成功", null));
        } catch (Exception e) {
            log.error("切换用户状态失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 重置密码
     */
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@PathVariable Long id, 
                                                          @RequestParam String newPassword) {
        try {
            userService.resetPassword(id, newPassword);
            return ResponseEntity.ok(ApiResponse.success("重置密码成功", null));
        } catch (Exception e) {
            log.error("重置密码失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}














