package com.example.hospital.service.impl;

import com.example.hospital.dto.*;
import com.example.hospital.entity.Role;
import com.example.hospital.entity.User;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.service.UserService;
import com.example.hospital.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("用户登录尝试: {}", request.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            
            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            UserDTO userDTO = convertToDTO(user);
            long expiresIn = jwtTokenProvider.getExpirationTime();
            
            log.info("用户登录成功: {}, 角色: {}", user.getUsername(), user.getRole());
            return new LoginResponse(token, userDTO, expiresIn);
        } catch (Exception e) {
            log.error("用户登录失败: {}, 错误: {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("用户名或密码错误");
        }
    }
    
    @Override
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setEnabled(true);
        
        User savedUser = userRepository.save(user);
        log.info("创建用户成功: {}", savedUser.getUsername());
        
        return convertToDTO(savedUser);
    }
    
    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToDTO(user);
    }
    
    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToDTO(user);
    }
    
    @Override
    @Transactional
    public UserDTO updateUser(Long id, CreateUserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 检查用户名是否被其他用户使用
        if (!user.getUsername().equals(request.getUsername()) && 
            userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否被其他用户使用
        if (request.getEmail() != null && 
            !request.getEmail().equals(user.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        user.setUsername(request.getUsername());
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        
        // 如果提供了新密码，则更新密码
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        User savedUser = userRepository.save(user);
        log.info("更新用户成功: {}", savedUser.getUsername());
        
        return convertToDTO(savedUser);
    }
    
    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        userRepository.delete(user);
        log.info("删除用户成功: {}", user.getUsername());
    }
    
    @Override
    public PageResponse<UserDTO> getUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDTO> userDTOs = userPage.getContent().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return new PageResponse<>(userDTOs, userPage);
    }
    
    
    @Override
    @Transactional
    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("重置密码成功: {}", user.getUsername());
    }
    
    @Override
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码不正确");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("修改密码成功: {}", user.getUsername());
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public List<UserDTO> searchUsersByRealName(String realName) {
        List<User> users = userRepository.findByRealNameContaining(realName);
        return users.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<UserDTO> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
        log.info("切换用户状态成功: {}, 新状态: {}", user.getUsername(), user.getEnabled());
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRealName(user.getRealName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setEnabled(user.getEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}