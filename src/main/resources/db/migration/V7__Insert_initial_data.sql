-- 插入初始数据

-- 插入管理员用户（密码：admin123）
INSERT INTO users (username, password, real_name, email, phone, role, enabled) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '系统管理员', 'admin@hospital.com', '13800138000', 'ADMIN', true),
('staff1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', '张护士', 'zhang.nurse@hospital.com', '13800138001', 'STAFF', true);

-- 插入医生数据
INSERT INTO doctors (name, gender, department, title, phone, email, specialization, introduction, consultation_fee, available) VALUES
('李主任', 'MALE', '内科', '主任医师', '13901234567', 'li.doctor@hospital.com', '心血管疾病、高血压、冠心病', '从事心血管内科临床工作20余年，擅长心血管疾病的诊断和治疗。', 50.00, true),
('王医生', 'FEMALE', '外科', '副主任医师', '13901234568', 'wang.doctor@hospital.com', '普通外科、微创手术', '专业从事普通外科临床工作15年，在微创手术方面有丰富经验。', 40.00, true),
('陈医生', 'MALE', '儿科', '主治医师', '13901234569', 'chen.doctor@hospital.com', '儿童常见病、多发病', '儿科临床工作10年，对儿童常见疾病有丰富的诊疗经验。', 30.00, true),
('赵医生', 'FEMALE', '妇产科', '主任医师', '13901234570', 'zhao.doctor@hospital.com', '妇科肿瘤、产科急症', '妇产科临床工作25年，擅长妇科肿瘤和产科急症的处理。', 60.00, true);

-- 插入药品数据
INSERT INTO medicines (name, code, specification, unit, price, stock, manufacturer, indication, dosage, side_effects, contraindications, available) VALUES
('阿莫西林胶囊', 'AMX001', '0.25g', '粒', 0.50, 1000, '华北制药', '用于敏感菌所致的各种感染', '成人一次0.5g，一日3次', '可能出现皮疹、腹泻等', '对青霉素过敏者禁用', true),
('布洛芬缓释胶囊', 'IBU001', '0.3g', '粒', 2.50, 500, '中美史克', '用于缓解轻至中度疼痛', '成人一次0.3g，一日2次', '可能出现胃肠道反应', '对本品过敏者禁用', true),
('复方甘草片', 'GAN001', '复方', '片', 0.10, 2000, '太极集团', '用于镇咳祛痰', '成人一次3-4片，一日3次', '长期使用可能成瘾', '孕妇及哺乳期妇女禁用', true),
('维生素C片', 'VTC001', '100mg', '片', 0.05, 5000, '华润三九', '用于预防坏血病，也可用于各种急慢性传染疾病及紫癜等的辅助治疗', '成人一日100-200mg', '一般无不良反应', '无特殊禁忌', true),
('感冒灵颗粒', 'GML001', '10g', '袋', 3.00, 800, '同仁堂', '解热镇痛，用于感冒引起的头痛、发热、鼻塞、流涕、咽痛', '开水冲服，一次1袋，一日3次', '偶见皮疹、恶心', '肝功能不全者慎用', true);

-- 插入病人样本数据
INSERT INTO patients (name, gender, birth_date, id_card, phone, address, email, emergency_contact, emergency_phone, medical_history, allergies) VALUES
('张三', 'MALE', '1980-05-15', '110101198005151234', '13612345678', '北京市朝阳区某某街道123号', 'zhangsan@email.com', '张四', '13712345678', '高血压病史3年', '青霉素过敏'),
('李四', 'FEMALE', '1992-08-20', '110101199208201234', '13712345679', '北京市海淀区某某路456号', 'lisi@email.com', '李五', '13812345678', '无特殊病史', '无已知过敏'),
('王五', 'MALE', '1975-12-03', '110101197512031234', '13812345680', '北京市西城区某某胡同789号', 'wangwu@email.com', '王六', '13912345678', '糖尿病病史5年', '磺胺类药物过敏');
