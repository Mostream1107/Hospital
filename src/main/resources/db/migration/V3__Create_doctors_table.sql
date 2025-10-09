-- 创建医生表
CREATE TABLE doctors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    gender ENUM('MALE', 'FEMALE') NOT NULL,
    department VARCHAR(100) NOT NULL,
    title VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    specialization TEXT,
    introduction TEXT,
    consultation_fee DECIMAL(10,2),
    available BOOLEAN DEFAULT true NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    
    INDEX idx_name (name),
    INDEX idx_department (department),
    INDEX idx_title (title),
    INDEX idx_available (available)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;














