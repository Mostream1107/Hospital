-- 创建病人表
CREATE TABLE patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    gender ENUM('MALE', 'FEMALE') NOT NULL,
    birth_date DATE NOT NULL,
    id_card VARCHAR(18) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(200),
    email VARCHAR(100),
    emergency_contact VARCHAR(50),
    emergency_phone VARCHAR(20),
    medical_history TEXT,
    allergies TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    
    INDEX idx_name (name),
    INDEX idx_id_card (id_card),
    INDEX idx_phone (phone),
    INDEX idx_birth_date (birth_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;














