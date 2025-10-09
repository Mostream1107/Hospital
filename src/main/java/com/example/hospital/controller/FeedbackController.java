package com.example.hospital.controller;

import com.example.hospital.dto.ApiResponse;
import com.example.hospital.entity.Feedback;
import com.example.hospital.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 意见建议控制器
 */
@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Slf4j
public class FeedbackController {
    
    private final FeedbackService feedbackService;
    
    /**
     * 创建新的反馈 - 员工端可用
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Feedback>> createFeedback(@Valid @RequestBody Feedback feedback) {
        try {
            Feedback createdFeedback = feedbackService.createFeedback(feedback);
            return ResponseEntity.ok(ApiResponse.success(createdFeedback));
        } catch (Exception e) {
            log.error("创建反馈失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 获取所有反馈（分页）- 管理员专用
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<Feedback>>> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Feedback> feedbacks = feedbackService.getAllFeedbacks(pageable);
            return ResponseEntity.ok(ApiResponse.success(feedbacks));
        } catch (Exception e) {
            log.error("获取反馈列表失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取反馈详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<Feedback>> getFeedbackById(@PathVariable Long id) {
        try {
            return feedbackService.getFeedbackById(id)
                .map(feedback -> ResponseEntity.ok(ApiResponse.success(feedback)))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("获取反馈详情失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 根据状态获取反馈列表
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Feedback>>> getFeedbacksByStatus(@PathVariable String status) {
        try {
            Feedback.FeedbackStatus feedbackStatus = Feedback.FeedbackStatus.valueOf(status.toUpperCase());
            List<Feedback> feedbacks = feedbackService.getFeedbacksByStatus(feedbackStatus);
            return ResponseEntity.ok(ApiResponse.success(feedbacks));
        } catch (Exception e) {
            log.error("根据状态获取反馈失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 根据类型获取反馈列表
     */
    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Feedback>>> getFeedbacksByType(@PathVariable String type) {
        try {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByType(type);
            return ResponseEntity.ok(ApiResponse.success(feedbacks));
        } catch (Exception e) {
            log.error("根据类型获取反馈失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 搜索反馈
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Feedback>>> searchFeedbacks(@RequestParam String contactName) {
        try {
            List<Feedback> feedbacks = feedbackService.searchFeedbacksByContactName(contactName);
            return ResponseEntity.ok(ApiResponse.success(feedbacks));
        } catch (Exception e) {
            log.error("搜索反馈失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 处理反馈
     */
    @PutMapping("/{id}/process")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Feedback>> processFeedback(
            @PathVariable Long id,
            @RequestParam String processingNotes,
            @RequestParam Long processedByUserId) {
        try {
            Feedback processedFeedback = feedbackService.processFeedback(id, processingNotes, processedByUserId);
            return ResponseEntity.ok(ApiResponse.success(processedFeedback));
        } catch (Exception e) {
            log.error("处理反馈失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 关闭反馈
     */
    @PutMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Feedback>> closeFeedback(@PathVariable Long id) {
        try {
            Feedback closedFeedback = feedbackService.closeFeedback(id);
            return ResponseEntity.ok(ApiResponse.success(closedFeedback));
        } catch (Exception e) {
            log.error("关闭反馈失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 获取反馈统计信息
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFeedbackStatistics() {
        try {
            Map<String, Object> statistics = feedbackService.getFeedbackStatistics();
            return ResponseEntity.ok(ApiResponse.success(statistics));
        } catch (Exception e) {
            log.error("获取反馈统计失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 更新反馈状态
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Feedback>> updateFeedbackStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String resolutionNotes) {
        try {
            Feedback updatedFeedback = feedbackService.updateFeedbackStatus(id, status, resolutionNotes);
            return ResponseEntity.ok(ApiResponse.success(updatedFeedback));
        } catch (Exception e) {
            log.error("更新反馈状态失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 删除反馈
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            log.error("删除反馈失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
