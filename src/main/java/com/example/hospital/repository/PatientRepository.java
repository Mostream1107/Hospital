package com.example.hospital.repository;

import com.example.hospital.entity.Gender;
import com.example.hospital.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 病人仓库接口
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    /**
     * 根据姓名查找病人
     */
    List<Patient> findByNameContaining(String name);
    
    /**
     * 根据身份证号查找病人
     */
    Optional<Patient> findByIdCard(String idCard);
    
    /**
     * 根据身份证号模糊查找病人
     */
    List<Patient> findByIdCardContaining(String idCard);
    
    /**
     * 根据电话号码查找病人
     */
    Optional<Patient> findByPhone(String phone);
    
    /**
     * 根据电话号码模糊查找病人
     */
    List<Patient> findByPhoneContaining(String phone);
    
    /**
     * 根据性别查找病人
     */
    List<Patient> findByGender(Gender gender);
    
    /**
     * 根据出生日期范围查找病人
     */
    List<Patient> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * 检查身份证号是否存在
     */
    boolean existsByIdCard(String idCard);
    
    /**
     * 根据年龄范围查找病人
     */
    @Query("SELECT p FROM Patient p WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(p.birthDate) - " +
           "(CASE WHEN MONTH(CURRENT_DATE) < MONTH(p.birthDate) OR " +
           "(MONTH(CURRENT_DATE) = MONTH(p.birthDate) AND DAY(CURRENT_DATE) < DAY(p.birthDate)) " +
           "THEN 1 ELSE 0 END) BETWEEN :minAge AND :maxAge")
    List<Patient> findByAgeBetween(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
    
    /**
     * 根据紧急联系人查找病人
     */
    List<Patient> findByEmergencyContactContaining(String emergencyContact);
    
    /**
     * 根据过敏史查找病人
     */
    @Query("SELECT p FROM Patient p WHERE p.allergies IS NOT NULL AND p.allergies LIKE %:allergy%")
    List<Patient> findByAllergiesContaining(@Param("allergy") String allergy);
}








