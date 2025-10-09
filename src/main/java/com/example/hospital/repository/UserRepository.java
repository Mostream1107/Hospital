package com.example.hospital.repository;

import com.example.hospital.entity.Role;
import com.example.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓库接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 根据角色查找用户
     */
    List<User> findByRole(Role role);
    
    /**
     * 根据启用状态查找用户
     */
    List<User> findByEnabled(Boolean enabled);
    
    /**
     * 根据真实姓名模糊查询
     */
    List<User> findByRealNameContaining(String realName);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 根据角色和启用状态查找用户
     */
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.enabled = :enabled")
    List<User> findByRoleAndEnabled(@Param("role") Role role, @Param("enabled") Boolean enabled);
}














