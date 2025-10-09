package com.example.hospital.entity;

/**
 * 支付类型枚举
 */
public enum PaymentType {
    REGISTRATION("挂号费"),
    MEDICINE("药费"),
    EXAMINATION("检查费"),
    TREATMENT("治疗费");

    private final String description;

    PaymentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

