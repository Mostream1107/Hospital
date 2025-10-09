package com.example.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘统计数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    
    /**
     * 核心统计数据 - 简约版本
     */
    private Long totalPatients;
    private Long totalDoctors;
    private Long totalRegistrations;  // 总挂号数
    private BigDecimal totalRevenue;
    
    /**
     * 挂号状态统计
     */
    private Map<String, Long> registrationStatusStats;
    
    /**
     * 支付状态统计
     */
    private Map<String, Long> paymentStatusStats;
    
    /**
     * 科室挂号统计（前5名）
     */
    private List<DepartmentStatsDTO> topDepartments;
    
    
    /**
     * 科室统计DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentStatsDTO {
        private String departmentName;
        private Long registrationCount;
        private BigDecimal revenue;
    }
    
}
