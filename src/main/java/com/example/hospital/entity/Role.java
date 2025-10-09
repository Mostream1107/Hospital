package com.example.hospital.entity;

/**
 * 用户角色枚举
 */
public enum Role {
    ADMIN("管理员"),
    STAFF("普通员工");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

