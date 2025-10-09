package com.example.hospital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 意见建议实体类
 */
@Entity
@Table(name = "feedbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "联系人姓名不能为空")
    @Size(max = 50, message = "联系人姓名不能超过50个字符")
    @Column(nullable = false, length = 50)
    private String contactName;
    
    @Size(max = 20, message = "联系电话不能超过20个字符")
    @Column(length = 20)
    private String contactPhone;
    
    @Size(max = 100, message = "邮箱地址不能超过100个字符")
    @Column(length = 100)
    private String contactEmail;
    
    @NotBlank(message = "反馈类型不能为空")
    @Size(max = 20, message = "反馈类型不能超过20个字符")
    @Column(nullable = false, length = 20)
    private String feedbackType; // 投诉、建议、表扬、其他
    
    @NotBlank(message = "反馈内容不能为空")
    @Size(max = 2000, message = "反馈内容不能超过2000个字符")
    @Column(nullable = false, length = 2000)
    private String content;
    
    @Size(max = 500, message = "处理备注不能超过500个字符")
    @Column(length = 500)
    private String processingNotes;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackStatus status = FeedbackStatus.PENDING; // 待处理、已处理、已关闭
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime processedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private User processedBy; // 处理人员
    
    public enum FeedbackStatus {
        PENDING("待处理"),
        PROCESSED("已处理"), 
        CLOSED("已关闭");
        
        private final String description;
        
        FeedbackStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
