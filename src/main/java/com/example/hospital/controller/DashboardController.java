package com.example.hospital.controller;

import com.example.hospital.dto.ApiResponse;
import com.example.hospital.dto.DashboardStatsDTO;
import com.example.hospital.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 仪表盘控制器
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getDashboardStats() {
        try {
            DashboardStatsDTO stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            log.error("获取仪表盘统计数据失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error("获取统计数据失败"));
        }
    }
    
    /**
     * 获取今日时间序列数据
     */
    @GetMapping("/time-series")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<java.util.Map<String, Object>>> getTodayTimeSeriesData() {
        try {
            java.util.Map<String, Object> timeSeriesData = dashboardService.getTodayTimeSeriesData();
            return ResponseEntity.ok(ApiResponse.success(timeSeriesData));
        } catch (Exception e) {
            log.error("获取时间序列数据失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error("获取时间序列数据失败"));
        }
    }
}
