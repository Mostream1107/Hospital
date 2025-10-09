package com.example.hospital.entity;

/**
 * 支付状态枚举
 */
public enum PaymentStatus {
    PENDING("待支付"),
    PAID("已支付"),
    REFUNDED("已退费");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

