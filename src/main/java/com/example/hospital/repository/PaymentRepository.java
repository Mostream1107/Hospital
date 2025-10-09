package com.example.hospital.repository;

import com.example.hospital.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 收费仓库接口
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * 查找收费记录并加载关联的病人信息
     */
    @Query("SELECT p FROM Payment p LEFT JOIN FETCH p.patient LEFT JOIN FETCH p.registration LEFT JOIN FETCH p.medicine WHERE p.id = :id")
    Optional<Payment> findByIdWithPatient(@Param("id") Long id);
    
    /**
     * 分页查询所有收费记录并加载关联信息
     */
    @Query("SELECT p FROM Payment p LEFT JOIN FETCH p.patient LEFT JOIN FETCH p.registration LEFT JOIN FETCH p.medicine")
    List<Payment> findAllWithAssociations();
    
    /**
     * 根据病人查找收费记录
     */
    List<Payment> findByPatient(Patient patient);
    
    /**
     * 根据病人ID查找收费记录并按创建时间倒序排列
     */
    List<Payment> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    
    /**
     * 根据挂号记录查找收费记录
     */
    List<Payment> findByRegistration(Registration registration);
    
    /**
     * 根据药品查找收费记录
     */
    List<Payment> findByMedicine(Medicine medicine);
    
    /**
     * 根据支付状态查找收费记录
     */
    List<Payment> findByStatus(PaymentStatus status);
    
    /**
     * 根据支付类型查找收费记录
     */
    List<Payment> findByPaymentType(PaymentType paymentType);
    
    /**
     * 根据病人和支付状态查找收费记录
     */
    List<Payment> findByPatientAndStatus(Patient patient, PaymentStatus status);
    
    /**
     * 根据创建时间范围查找收费记录
     */
    List<Payment> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据支付时间范围查找收费记录
     */
    List<Payment> findByPaidAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据创建时间范围和支付状态查找收费记录
     */
    List<Payment> findByCreatedAtBetweenAndStatus(LocalDateTime startTime, LocalDateTime endTime, PaymentStatus status);
    
    /**
     * 统计指定状态的收费记录数量
     */
    Long countByStatus(PaymentStatus status);
    
    /**
     * 统计指定病人的收费记录数量
     */
    Long countByPatient(Patient patient);
    
    /**
     * 统计指定时间范围内的收费记录数量
     */
    Long countByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计指定时间范围内指定状态的收费记录数量
     */
    Long countByCreatedAtBetweenAndStatus(LocalDateTime startTime, LocalDateTime endTime, PaymentStatus status);
    
    /**
     * 统计指定状态的支付总额
     */
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    Optional<BigDecimal> sumAmountByStatus(@Param("status") PaymentStatus status);
    
    /**
     * 统计指定病人的支付总额
     */
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.patient = :patient")
    Optional<BigDecimal> sumAmountByPatient(@Param("patient") Patient patient);
    
    /**
     * 统计指定时间范围内的支付总额
     */
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.createdAt BETWEEN :startTime AND :endTime")
    Optional<BigDecimal> sumAmountByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 统计指定时间范围内指定状态的支付总额
     */
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.createdAt BETWEEN :startTime AND :endTime AND p.status = :status")
    Optional<BigDecimal> sumAmountByCreatedAtBetweenAndStatus(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("status") PaymentStatus status);
    
    /**
     * 获取今日支付统计数据
     */
    @Query("SELECT p.status, COUNT(*), SUM(p.amount) FROM Payment p " +
           "WHERE DATE(p.createdAt) = CURRENT_DATE " +
           "GROUP BY p.status")
    List<Object[]> getTodayPaymentStatistics();
    
    /**
     * 统计指定时间范围内已支付的金额总和
     */
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paidAt BETWEEN :startTime AND :endTime")
    BigDecimal sumPaidAmountByPaidAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}