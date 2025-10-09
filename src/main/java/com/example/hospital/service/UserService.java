package com.example.hospital.service;

import com.example.hospital.dto.*;
import com.example.hospital.entity.Role;
import com.example.hospital.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 创建用户
     */
    UserDTO createUser(CreateUserRequest request);
    
    /**
     * 根据ID获取用户
     */
    UserDTO getUserById(Long id);
    
    /**
     * 根据用户名获取用户
     */
    UserDTO getUserByUsername(String username);
    
    /**
     * 更新用户信息
     */
    UserDTO updateUser(Long id, CreateUserRequest request);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
    
    /**
     * 分页获取用户列表
     */
    PageResponse<UserDTO> getUsers(Pageable pageable);
    
    /**
     * 根据角色获取用户列表
     */
    List<UserDTO> getUsersByRole(Role role);
    
    /**
     * 启用/禁用用户
     */
    void toggleUserStatus(Long id);
    
    /**
     * 重置用户密码
     */
    void resetPassword(Long id, String newPassword);
    
    /**
     * 修改密码
     */
    void changePassword(Long id, String oldPassword, String newPassword);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 根据真实姓名搜索用户
     */
    List<UserDTO> searchUsersByRealName(String realName);
}














