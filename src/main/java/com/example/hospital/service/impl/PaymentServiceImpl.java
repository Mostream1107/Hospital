package com.example.hospital.service.impl;

import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Payment;
import com.example.hospital.entity.PaymentStatus;
import com.example.hospital.repository.PaymentRepository;
import com.example.hospital.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 收费服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;
    
    @Override
    @Transactional
    public Payment createPayment(Payment payment) {
        Payment savedPayment = paymentRepository.save(payment);
        log.info("创建收费记录成功: 病人ID={}, 金额={}", 
                savedPayment.getPatient().getId(), 
                savedPayment.getAmount());
        return savedPayment;
    }
    
    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findByIdWithPatient(id)
            .orElseThrow(() -> new RuntimeException("收费记录不存在"));
    }
    
    @Override
    @Transactional
    public Payment updatePayment(Long id, Payment payment) {
        Payment existingPayment = paymentRepository.findByIdWithPatient(id)
            .orElseThrow(() -> new RuntimeException("收费记录不存在"));
        
        // 更新病人关联（重要！）
        if (payment.getPatient() != null) {
            existingPayment.setPatient(payment.getPatient());
        }
        
        // 更新其他字段
        existingPayment.setPaymentType(payment.getPaymentType());
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setPaymentMethod(payment.getPaymentMethod());
        existingPayment.setQuantity(payment.getQuantity());
        existingPayment.setDescription(payment.getDescription());
        existingPayment.setNotes(payment.getNotes());
        
        Payment savedPayment = paymentRepository.save(existingPayment);
        log.info("更新收费记录成功: ID={}", savedPayment.getId());
        return savedPayment;
    }
    
    @Override
    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("收费记录不存在"));
        
        paymentRepository.delete(payment);
        log.info("删除收费记录成功: ID={}", id);
    }
    
    @Override
    public PageResponse<Payment> getPayments(Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.findAll(pageable);
        
        // 手动加载关联的Patient信息以解决LazyInitializationException
        List<Payment> payments = paymentPage.getContent();
        for (Payment payment : payments) {
            if (payment.getPatient() != null) {
                // 触发懒加载
                payment.getPatient().getName();
            }
            if (payment.getRegistration() != null && payment.getRegistration().getPatient() != null) {
                payment.getRegistration().getPatient().getName();
            }
            if (payment.getRegistration() != null && payment.getRegistration().getDoctor() != null) {
                payment.getRegistration().getDoctor().getName();
            }
        }
        
        return new PageResponse<>(payments, paymentPage);
    }
    
    @Override
    public List<Payment> getPaymentsByPatientId(Long patientId) {
        return paymentRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }
    
    @Override
    @Transactional
    public Payment updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("收费记录不存在"));
        
        payment.setStatus(status);
        Payment savedPayment = paymentRepository.save(payment);
        log.info("更新支付状态成功: ID={}, 新状态={}", id, status);
        return savedPayment;
    }
    
    @Override
    @Transactional
    public Payment processPayment(Long id, String transactionId) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("收费记录不存在"));
        
        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionId(transactionId);
        payment.setPaidAt(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);
        log.info("处理支付成功: ID={}, 交易ID={}", id, transactionId);
        return savedPayment;
    }
    
    @Override
    @Transactional
    public Payment processRefund(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("收费记录不存在"));
        
        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new RuntimeException("只有已支付的记录才能退费");
        }
        
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);
        log.info("处理退费成功: ID={}", id);
        return savedPayment;
    }
    
    @Override
    public List<Object[]> getTodayPaymentStatistics() {
        return paymentRepository.getTodayPaymentStatistics();
    }
    
    @Override
    public BigDecimal getTotalRevenue(LocalDateTime startTime, LocalDateTime endTime) {
        BigDecimal revenue = paymentRepository.sumPaidAmountByPaidAtBetween(startTime, endTime);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
}


