package com.example.hospital.service.impl;

import com.example.hospital.dto.DashboardStatsDTO;
import com.example.hospital.entity.PaymentStatus;
import com.example.hospital.entity.RegistrationStatus;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.repository.PaymentRepository;
import com.example.hospital.repository.RegistrationRepository;
import com.example.hospital.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仪表盘服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final RegistrationRepository registrationRepository;
    private final PaymentRepository paymentRepository;
    
    @Override
    public DashboardStatsDTO getDashboardStats() {
        log.info("开始获取仪表盘统计数据（简约版本）");
        
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        // 核心统计 - 简约版本
        stats.setTotalPatients(patientRepository.count());
        stats.setTotalDoctors(doctorRepository.count());
        stats.setTotalRegistrations(registrationRepository.count());
        
        // 计算总收入 - 使用更直接的方法
        BigDecimal totalRevenue = paymentRepository.sumAmountByStatus(PaymentStatus.PAID).orElse(BigDecimal.ZERO);
        
        // 添加调试信息
        Long paidCount = paymentRepository.countByStatus(PaymentStatus.PAID);
        Long pendingCount = paymentRepository.countByStatus(PaymentStatus.PENDING);
        Long refundedCount = paymentRepository.countByStatus(PaymentStatus.REFUNDED);
        
        log.info("支付统计 - 已支付: {}笔, 待支付: {}笔, 已退款: {}笔", paidCount, pendingCount, refundedCount);
        log.info("总收入计算结果: {}", totalRevenue);
        
        stats.setTotalRevenue(totalRevenue);
        
        // 状态统计
        stats.setRegistrationStatusStats(getRegistrationStatusStats());
        stats.setPaymentStatusStats(getPaymentStatusStats());
        
        // 简化的科室统计 - 只获取第一名用于热门科室显示
        stats.setTopDepartments(getTopDepartments(1));
        
        log.info("仪表盘统计数据获取完成（简约版本）");
        return stats;
    }
    
    /**
     * 获取今日挂号数
     */
    private Long getTodayRegistrations(LocalDateTime start, LocalDateTime end) {
        return registrationRepository.countByCreatedAtBetween(start, end);
    }
    
    /**
     * 获取今日收入
     */
    private BigDecimal getTodayRevenue(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.sumAmountByCreatedAtBetweenAndStatus(start, end, PaymentStatus.PAID)
                .orElse(BigDecimal.ZERO);
    }
    
    /**
     * 获取时间范围内的挂号数
     */
    private Long getRegistrationCount(LocalDateTime start, LocalDateTime end) {
        return registrationRepository.countByCreatedAtBetween(start, end);
    }
    
    /**
     * 获取时间范围内的收入
     */
    private BigDecimal getRevenue(LocalDateTime start, LocalDateTime end) {
        return paymentRepository.sumAmountByCreatedAtBetweenAndStatus(start, end, PaymentStatus.PAID)
                .orElse(BigDecimal.ZERO);
    }
    
    /**
     * 获取挂号状态统计
     */
    private Map<String, Long> getRegistrationStatusStats() {
        Map<String, Long> statusMap = new HashMap<>();
        for (RegistrationStatus status : RegistrationStatus.values()) {
            Long count = registrationRepository.countByStatus(status);
            statusMap.put(status.name(), count);
        }
        return statusMap;
    }
    
    /**
     * 获取支付状态统计
     */
    private Map<String, Long> getPaymentStatusStats() {
        Map<String, Long> statusMap = new HashMap<>();
        for (PaymentStatus status : PaymentStatus.values()) {
            Long count = paymentRepository.countByStatus(status);
            statusMap.put(status.name(), count);
        }
        return statusMap;
    }
    
    /**
     * 获取科室挂号统计前N名
     */
    private List<DashboardStatsDTO.DepartmentStatsDTO> getTopDepartments(int limit) {
        List<Object[]> results = registrationRepository.getDepartmentStats();
        return results.stream()
                .limit(limit)
                .map(row -> new DashboardStatsDTO.DepartmentStatsDTO(
                        (String) row[0], // department
                        ((Number) row[1]).longValue(), // count
                        (BigDecimal) row[2] // revenue
                ))
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getTodayTimeSeriesData() {
        log.info("开始获取今日时间序列数据");
        
        Map<String, Object> timeSeriesData = new HashMap<>();
        
        // 获取今日的开始和结束时间
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime todayEnd = todayStart.plusDays(1);
        
        // 生成时间标签（每2小时一个点）
        List<String> timeLabels = new ArrayList<>();
        List<Long> registrationData = new ArrayList<>();
        List<BigDecimal> revenueData = new ArrayList<>();
        
        for (int hour = 8; hour <= 18; hour += 2) {
            String timeLabel = String.format("%02d:00", hour);
            timeLabels.add(timeLabel);
            
            // 计算该时间点之前的累计数据
            LocalDateTime pointTime = todayStart.withHour(hour);
            
            // 累计挂号数
            Long registrationCount = registrationRepository.countByCreatedAtBetween(todayStart, pointTime);
            registrationData.add(registrationCount);
            
            // 累计收入
            BigDecimal revenue = paymentRepository.sumAmountByCreatedAtBetweenAndStatus(
                    todayStart, pointTime, PaymentStatus.PAID).orElse(BigDecimal.ZERO);
            revenueData.add(revenue);
        }
        
        timeSeriesData.put("labels", timeLabels);
        timeSeriesData.put("registrationData", registrationData);
        timeSeriesData.put("revenueData", revenueData);
        
        log.info("今日时间序列数据获取完成，时间点数量: {}", timeLabels.size());
        return timeSeriesData;
    }
}
