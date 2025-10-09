package com.example.hospital.repository;

import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.Patient;
import com.example.hospital.entity.Registration;
import com.example.hospital.entity.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 挂号仓库接口
 */
@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    
    /**
     * 根据ID查找挂号记录（预加载Patient和Doctor）
     */
    @Query("SELECT r FROM Registration r " +
           "LEFT JOIN FETCH r.patient p " +
           "LEFT JOIN FETCH r.doctor d " +
           "WHERE r.id = :id")
    Optional<Registration> findByIdWithPatientAndDoctor(@Param("id") Long id);
    
    /**
     * 查找所有挂号记录（预加载Patient和Doctor）
     */
    @Query("SELECT r FROM Registration r " +
           "LEFT JOIN FETCH r.patient p " +
           "LEFT JOIN FETCH r.doctor d " +
           "ORDER BY r.id DESC")
    List<Registration> findAllWithPatientAndDoctor();
    
    /**
     * 分页查询挂号记录（预加载Patient和Doctor）
     */
    @Query(value = "SELECT r FROM Registration r " +
           "LEFT JOIN FETCH r.patient p " +
           "LEFT JOIN FETCH r.doctor d",
           countQuery = "SELECT COUNT(r) FROM Registration r")
    Page<Registration> findAllWithPatientAndDoctor(Pageable pageable);
    
    /**
     * 根据病人查找挂号记录
     */
    List<Registration> findByPatient(Patient patient);
    
    /**
     * 根据医生查找挂号记录
     */
    List<Registration> findByDoctor(Doctor doctor);
    
    /**
     * 根据挂号状态查找
     */
    List<Registration> findByStatus(RegistrationStatus status);
    
    /**
     * 根据病人和状态查找挂号记录
     */
    List<Registration> findByPatientAndStatus(Patient patient, RegistrationStatus status);
    
    /**
     * 根据医生和状态查找挂号记录
     */
    List<Registration> findByDoctorAndStatus(Doctor doctor, RegistrationStatus status);
    
    /**
     * 根据预约时间范围查找挂号记录
     */
    List<Registration> findByAppointmentTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据创建时间范围查找挂号记录
     */
    List<Registration> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据医生和预约时间范围查找挂号记录
     */
    List<Registration> findByDoctorAndAppointmentTimeBetween(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计某医生在指定时间范围内的挂号数量
     */
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.doctor = :doctor AND r.appointmentTime BETWEEN :startTime AND :endTime")
    Long countByDoctorAndAppointmentTimeBetween(@Param("doctor") Doctor doctor, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 统计某状态在指定时间范围内的挂号数量
     */
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.status = :status AND r.createdAt BETWEEN :startTime AND :endTime")
    Long countByStatusAndCreatedAtBetween(@Param("status") RegistrationStatus status, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据病人ID查找挂号记录
     */
    @Query("SELECT r FROM Registration r WHERE r.patient.id = :patientId ORDER BY r.createdAt DESC")
    List<Registration> findByPatientIdOrderByCreatedAtDesc(@Param("patientId") Long patientId);
    
    /**
     * 根据医生ID查找挂号记录
     */
    @Query("SELECT r FROM Registration r WHERE r.doctor.id = :doctorId ORDER BY r.appointmentTime ASC")
    List<Registration> findByDoctorIdOrderByAppointmentTimeAsc(@Param("doctorId") Long doctorId);
    
    /**
     * 获取今日挂号统计
     */
    @Query("SELECT r.status, COUNT(r) FROM Registration r WHERE DATE(r.createdAt) = CURRENT_DATE GROUP BY r.status")
    List<Object[]> getTodayRegistrationStatistics();
    
    /**
     * 获取医生工作量统计
     */
    @Query("SELECT r.doctor.name, r.doctor.department, COUNT(r) FROM Registration r " +
           "WHERE r.createdAt BETWEEN :startTime AND :endTime " +
           "GROUP BY r.doctor.id, r.doctor.name, r.doctor.department " +
           "ORDER BY COUNT(r) DESC")
    List<Object[]> getDoctorWorkloadStatistics(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 统计指定时间范围内的挂号数量
     */
    Long countByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计指定状态的挂号数量
     */
    Long countByStatus(RegistrationStatus status);
    
    /**
     * 获取科室挂号统计
     */
    @Query("SELECT r.doctor.department, COUNT(r), SUM(r.registrationFee) FROM Registration r " +
           "GROUP BY r.doctor.department " +
           "ORDER BY COUNT(r) DESC")
    List<Object[]> getDepartmentStats();
}
