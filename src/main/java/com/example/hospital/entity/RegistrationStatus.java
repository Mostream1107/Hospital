package com.example.hospital.entity;

/**
 * 挂号状态枚举
 */
public enum RegistrationStatus {
    PENDING("待就诊"),
    IN_PROGRESS("就诊中"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;

    RegistrationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

