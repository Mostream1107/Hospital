package com.example.hospital.service;

import com.example.hospital.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 意见建议服务接口
 */
public interface FeedbackService {
    
    /**
     * 创建新的反馈
     */
    Feedback createFeedback(Feedback feedback);
    
    /**
     * 根据ID获取反馈
     */
    Optional<Feedback> getFeedbackById(Long id);
    
    /**
     * 获取所有反馈（分页）
     */
    Page<Feedback> getAllFeedbacks(Pageable pageable);
    
    /**
     * 根据状态获取反馈列表
     */
    List<Feedback> getFeedbacksByStatus(Feedback.FeedbackStatus status);
    
    /**
     * 根据反馈类型获取反馈列表
     */
    List<Feedback> getFeedbacksByType(String feedbackType);
    
    /**
     * 根据联系人姓名搜索反馈
     */
    List<Feedback> searchFeedbacksByContactName(String contactName);
    
    /**
     * 根据时间范围获取反馈
     */
    List<Feedback> getFeedbacksByDateRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 处理反馈
     */
    Feedback processFeedback(Long id, String processingNotes, Long processedByUserId);
    
    /**
     * 关闭反馈
     */
    Feedback closeFeedback(Long id);
    
    /**
     * 获取反馈统计信息
     */
    Map<String, Object> getFeedbackStatistics();
    
    /**
     * 更新反馈状态
     */
    Feedback updateFeedbackStatus(Long id, String status, String resolutionNotes);
    
    /**
     * 删除反馈
     */
    void deleteFeedback(Long id);
}
