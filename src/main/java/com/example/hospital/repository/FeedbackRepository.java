package com.example.hospital.repository;

import com.example.hospital.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 意见建议数据访问层
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    /**
     * 根据状态查询反馈
     */
    List<Feedback> findByStatusOrderByCreatedAtDesc(Feedback.FeedbackStatus status);
    
    /**
     * 根据反馈类型查询
     */
    List<Feedback> findByFeedbackTypeOrderByCreatedAtDesc(String feedbackType);
    
    /**
     * 根据联系人姓名模糊查询
     */
    List<Feedback> findByContactNameContainingOrderByCreatedAtDesc(String contactName);
    
    /**
     * 根据创建时间范围查询
     */
    @Query("SELECT f FROM Feedback f WHERE f.createdAt BETWEEN :startTime AND :endTime ORDER BY f.createdAt DESC")
    List<Feedback> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);
    
    /**
     * 统计各状态的反馈数量
     */
    @Query("SELECT f.status, COUNT(f) FROM Feedback f GROUP BY f.status")
    List<Object[]> countByStatus();
    
    /**
     * 统计各类型的反馈数量
     */
    @Query("SELECT f.feedbackType, COUNT(f) FROM Feedback f GROUP BY f.feedbackType")
    List<Object[]> countByFeedbackType();
    
    /**
     * 分页查询所有反馈
     */
    Page<Feedback> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
