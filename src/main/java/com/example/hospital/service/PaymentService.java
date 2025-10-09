package com.example.hospital.service;

import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Payment;
import com.example.hospital.entity.PaymentStatus;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 收费服务接口
 */
public interface PaymentService {
    
    /**
     * 创建收费记录
     */
    Payment createPayment(Payment payment);
    
    /**
     * 根据ID获取收费记录
     */
    Payment getPaymentById(Long id);
    
    /**
     * 更新收费记录
     */
    Payment updatePayment(Long id, Payment payment);
    
    /**
     * 删除收费记录
     */
    void deletePayment(Long id);
    
    /**
     * 分页获取收费记录列表
     */
    PageResponse<Payment> getPayments(Pageable pageable);
    
    /**
     * 根据病人ID获取收费记录
     */
    List<Payment> getPaymentsByPatientId(Long patientId);
    
    /**
     * 更新支付状态
     */
    Payment updatePaymentStatus(Long id, PaymentStatus status);
    
    /**
     * 处理支付
     */
    Payment processPayment(Long id, String transactionId);
    
    /**
     * 处理退费
     */
    Payment processRefund(Long id);
    
    /**
     * 获取今日收费统计
     */
    List<Object[]> getTodayPaymentStatistics();
    
    /**
     * 统计指定时间范围内的总收入
     */
    BigDecimal getTotalRevenue(LocalDateTime startTime, LocalDateTime endTime);
}














