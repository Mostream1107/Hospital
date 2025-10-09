package com.example.hospital.service;

import com.example.hospital.dto.DashboardStatsDTO;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘服务接口
 */
public interface DashboardService {
    
    /**
     * 获取仪表盘统计数据
     */
    DashboardStatsDTO getDashboardStats();
    
    /**
     * 获取今日时间序列数据（用于折线图）
     */
    Map<String, Object> getTodayTimeSeriesData();
}
