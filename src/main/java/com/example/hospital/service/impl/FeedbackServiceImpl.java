package com.example.hospital.service.impl;

import com.example.hospital.entity.Feedback;
import com.example.hospital.entity.User;
import com.example.hospital.repository.FeedbackRepository;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 意见建议服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
    
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    
    @Override
    public Feedback createFeedback(Feedback feedback) {
        log.info("创建新的反馈: 联系人={}, 类型={}", feedback.getContactName(), feedback.getFeedbackType());
        
        // 设置默认状态
        feedback.setStatus(Feedback.FeedbackStatus.PENDING);
        feedback.setCreatedAt(LocalDateTime.now());
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        log.info("反馈创建成功, ID: {}", savedFeedback.getId());
        
        return savedFeedback;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Feedback> getAllFeedbacks(Pageable pageable) {
        return feedbackRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getFeedbacksByStatus(Feedback.FeedbackStatus status) {
        return feedbackRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getFeedbacksByType(String feedbackType) {
        return feedbackRepository.findByFeedbackTypeOrderByCreatedAtDesc(feedbackType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Feedback> searchFeedbacksByContactName(String contactName) {
        return feedbackRepository.findByContactNameContainingOrderByCreatedAtDesc(contactName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getFeedbacksByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return feedbackRepository.findByCreatedAtBetween(startTime, endTime);
    }
    
    @Override
    public Feedback processFeedback(Long id, String processingNotes, Long processedByUserId) {
        log.info("处理反馈: ID={}, 处理人={}", id, processedByUserId);
        
        Feedback feedback = feedbackRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("反馈不存在: " + id));
        
        // 查找处理人员
        User processedBy = null;
        if (processedByUserId != null) {
            processedBy = userRepository.findById(processedByUserId)
                .orElse(null);
        }
        
        // 更新反馈状态
        feedback.setStatus(Feedback.FeedbackStatus.PROCESSED);
        feedback.setProcessingNotes(processingNotes);
        feedback.setProcessedBy(processedBy);
        feedback.setProcessedAt(LocalDateTime.now());
        
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        log.info("反馈处理完成: ID={}", id);
        
        return updatedFeedback;
    }
    
    @Override
    public Feedback closeFeedback(Long id) {
        log.info("关闭反馈: ID={}", id);
        
        Feedback feedback = feedbackRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("反馈不存在: " + id));
        
        feedback.setStatus(Feedback.FeedbackStatus.CLOSED);
        
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        log.info("反馈已关闭: ID={}", id);
        
        return updatedFeedback;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getFeedbackStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 统计各状态数量
        List<Object[]> statusCounts = feedbackRepository.countByStatus();
        Map<String, Long> statusStats = new HashMap<>();
        for (Object[] row : statusCounts) {
            Feedback.FeedbackStatus status = (Feedback.FeedbackStatus) row[0];
            Long count = (Long) row[1];
            statusStats.put(status.name(), count);
        }
        statistics.put("statusStats", statusStats);
        
        // 统计各类型数量
        List<Object[]> typeCounts = feedbackRepository.countByFeedbackType();
        Map<String, Long> typeStats = new HashMap<>();
        for (Object[] row : typeCounts) {
            String type = (String) row[0];
            Long count = (Long) row[1];
            typeStats.put(type, count);
        }
        statistics.put("typeStats", typeStats);
        
        // 总数统计
        long totalCount = feedbackRepository.count();
        statistics.put("totalCount", totalCount);
        
        // 今日新增
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime todayEnd = todayStart.plusDays(1);
        List<Feedback> todayFeedbacks = feedbackRepository.findByCreatedAtBetween(todayStart, todayEnd);
        statistics.put("todayCount", todayFeedbacks.size());
        
        return statistics;
    }
    
    @Override
    public Feedback updateFeedbackStatus(Long id, String status, String resolutionNotes) {
        log.info("更新反馈状态: ID={}, 状态={}", id, status);
        
        Feedback feedback = feedbackRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("反馈不存在: " + id));
        
        // 转换状态字符串为枚举
        Feedback.FeedbackStatus feedbackStatus;
        try {
            feedbackStatus = Feedback.FeedbackStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("无效的状态值: " + status);
        }
        
        // 更新反馈状态
        feedback.setStatus(feedbackStatus);
        if (resolutionNotes != null && !resolutionNotes.trim().isEmpty()) {
            feedback.setProcessingNotes(resolutionNotes);
        }
        
        // 如果状态是已处理或已关闭，设置处理时间
        if (feedbackStatus == Feedback.FeedbackStatus.PROCESSED || 
            feedbackStatus == Feedback.FeedbackStatus.CLOSED) {
            feedback.setProcessedAt(LocalDateTime.now());
        }
        
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        log.info("反馈状态更新完成: ID={}, 新状态={}", id, feedbackStatus);
        
        return updatedFeedback;
    }
    
    @Override
    public void deleteFeedback(Long id) {
        log.info("删除反馈: ID={}", id);
        
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("反馈不存在: " + id);
        }
        
        feedbackRepository.deleteById(id);
        log.info("反馈删除成功: ID={}", id);
    }
}
