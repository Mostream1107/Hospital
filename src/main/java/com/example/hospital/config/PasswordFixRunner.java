package com.example.hospital.config;

import com.example.hospital.entity.User;
import com.example.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 应用启动时自动修复用户密码
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordFixRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) throws Exception {
        log.info("开始修复用户密码...");

        try {
            // 修复admin用户密码
            User admin = userRepository.findByUsername("admin").orElse(null);
            if (admin != null) {
                String newPassword = passwordEncoder.encode("admin123");
                admin.setPassword(newPassword);
                userRepository.save(admin);
                log.info("已成功修复admin用户密码");
            } else {
                log.warn("未找到admin用户");
            }

            // 修复staff1用户密码
            User staff1 = userRepository.findByUsername("staff1").orElse(null);
            if (staff1 != null) {
                String newPassword = passwordEncoder.encode("admin123");
                staff1.setPassword(newPassword);
                userRepository.save(staff1);
                log.info("已成功修复staff1用户密码");
            } else {
                log.warn("未找到staff1用户");
            }

            log.info("密码修复完成！请使用以下凭据登录：");
            log.info("管理员 - 用户名: admin, 密码: admin123");
            log.info("员工 - 用户名: staff1, 密码: admin123");

        } catch (Exception e) {
            log.error("密码修复过程中出现错误: {}", e.getMessage(), e);
        }
    }
}














