package com.example.hospital.repository;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 医生仓库接口
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    /**
     * 根据姓名查找医生
     */
    List<Doctor> findByNameContaining(String name);
    
    /**
     * 根据科室查找医生
     */
    List<Doctor> findByDepartment(String department);
    
    /**
     * 根据职称查找医生
     */
    List<Doctor> findByTitle(String title);
    
    /**
     * 根据性别查找医生
     */
    List<Doctor> findByGender(Gender gender);
    
    /**
     * 根据可用状态查找医生
     */
    List<Doctor> findByAvailable(Boolean available);
    
    /**
     * 根据电话号码查找医生
     */
    Optional<Doctor> findByPhone(String phone);
    
    /**
     * 根据邮箱查找医生
     */
    Optional<Doctor> findByEmail(String email);
    
    /**
     * 根据科室和可用状态查找医生
     */
    List<Doctor> findByDepartmentAndAvailable(String department, Boolean available);
    
    /**
     * 根据职称和科室查找医生
     */
    List<Doctor> findByTitleAndDepartment(String title, String department);
    
    /**
     * 根据诊疗费范围查找医生
     */
    List<Doctor> findByConsultationFeeBetween(BigDecimal minFee, BigDecimal maxFee);
    
    /**
     * 根据专长查找医生
     */
    @Query("SELECT d FROM Doctor d WHERE d.specialization IS NOT NULL AND d.specialization LIKE %:specialization%")
    List<Doctor> findBySpecializationContaining(@Param("specialization") String specialization);
    
    /**
     * 获取所有科室列表
     */
    @Query("SELECT DISTINCT d.department FROM Doctor d ORDER BY d.department")
    List<String> findAllDepartments();
    
    /**
     * 获取所有职称列表
     */
    @Query("SELECT DISTINCT d.title FROM Doctor d ORDER BY d.title")
    List<String> findAllTitles();
}














