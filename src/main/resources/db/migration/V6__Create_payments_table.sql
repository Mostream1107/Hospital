-- 创建收费表
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    registration_id BIGINT,
    medicine_id BIGINT,
    payment_type ENUM('REGISTRATION', 'MEDICINE', 'EXAMINATION', 'TREATMENT') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING', 'PAID', 'REFUNDED') DEFAULT 'PENDING' NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100),
    quantity INT DEFAULT 1,
    description TEXT,
    notes TEXT,
    paid_at TIMESTAMP NULL,
    refunded_at TIMESTAMP NULL,
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (registration_id) REFERENCES registrations(id) ON DELETE SET NULL,
    FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    
    INDEX idx_patient_id (patient_id),
    INDEX idx_registration_id (registration_id),
    INDEX idx_medicine_id (medicine_id),
    INDEX idx_payment_type (payment_type),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_paid_at (paid_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;














