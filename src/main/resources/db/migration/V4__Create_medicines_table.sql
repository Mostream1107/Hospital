-- 创建药品表
CREATE TABLE medicines (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    specification VARCHAR(50) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT DEFAULT 0 NOT NULL,
    manufacturer VARCHAR(100),
    indication TEXT,
    dosage TEXT,
    side_effects TEXT,
    contraindications TEXT,
    available BOOLEAN DEFAULT true NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    
    INDEX idx_name (name),
    INDEX idx_code (code),
    INDEX idx_available (available),
    INDEX idx_price (price)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;














