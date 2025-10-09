package com.example.hospital.config;

import com.example.hospital.entity.Role;
import com.example.hospital.entity.User;
import com.example.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器 - 自动创建必要用户
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("开始检查并初始化用户数据...");
        
        // 创建管理员用户
        createUserIfNotExists("admin", "admin123", "系统管理员", 
            "admin@hospital.com", "13800138000", Role.ADMIN);
            
        // 创建员工用户
        createUserIfNotExists("staff1", "admin123", "张护士", 
            "zhang.nurse@hospital.com", "13800138001", Role.STAFF);
            
        log.info("用户数据初始化完成！");
    }
    
    private void createUserIfNotExists(String username, String password, String realName,
                                     String email, String phone, Role role) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRealName(realName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role);
            user.setEnabled(true);
            
            userRepository.save(user);
            log.info("创建用户成功: {} ({})", username, realName);
        } else {
            log.info("用户已存在: {}", username);
        }
    }
}
