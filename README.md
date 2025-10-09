#  医院门诊管理系统

一个基于Spring Boot的现代化医院门诊管理系统，提供完整的医院业务流程管理，包括病人管理、医生管理、挂号预约、收费管理、药品管理等核心功能。

## 项目特性

- **安全认证系统**：基于JWT的无状态身份验证，支持管理员和员工角色权限管理
- **病人档案管理**：完整的病人信息管理，包括基本信息、病史、过敏史等
- **医生信息管理**：医生资料管理、科室分类、专业特长、出诊安排
- **挂号预约系统**：在线挂号、预约管理、状态跟踪、排队叫号
- **收费管理系统**：费用计算、支付处理、退费管理、财务报表
- **药品库存管理**：药品信息管理、库存监控、价格管理
- **数据统计分析**：多维度数据统计、业务报表、趋势分析
- **现代化界面**：响应式Web界面，支持多设备访问
- **数据库迁移**：基于Flyway的数据库版本管理

## 技术栈

### 后端技术
- **Spring Boot 3.5.6** - 主框架
- **Spring Security 6.5.5** - 安全框架
- **Spring Data JPA** - 数据访问层
- **MySQL 8.0+** - 关系型数据库
- **Flyway** - 数据库迁移工具
- **JWT (JSON Web Token)** - 身份认证
- **Lombok** - 代码简化工具
- **Jackson** - JSON序列化

### 前端技术
- **HTML5 + CSS3 + JavaScript** - 基础技术
- **Bootstrap 5.1.3** - UI框架
- **响应式设计** - 多设备适配

## 系统要求

### 开发环境
- **JDK 17+** (推荐使用OpenJDK 17或Oracle JDK 17)
- **Maven 3.6+** (用于项目构建和依赖管理)
- **MySQL 8.0+** (数据库服务器)
- **Git** (版本控制)

### 运行环境
- **内存**: 最少2GB RAM，推荐4GB+
- **存储**: 最少1GB可用空间
- **网络**: 支持HTTP/HTTPS访问

### 操作系统支持
- ✅ Windows 10/11
- ✅ macOS 10.15+
- ✅ Linux (Ubuntu 18.04+, CentOS 7+, RHEL 7+)

## 快速开始

### 1. 环境准备

#### 安装JDK 17

**Windows:**
```bash
# 下载并安装OpenJDK 17
# 1. 访问 https://adoptium.net/
# 2. 下载Windows x64版本
# 3. 安装后配置环境变量
# 4. 验证安装
java -version
javac -version
```


#### 安装Maven

**Windows:**
```bash
# 1. 下载Maven二进制包
# 2. 解压到C:\Program Files\Apache\maven
# 3. 配置环境变量MAVEN_HOME和PATH
# 4. 验证安装
mvn -version
```


#### 安装MySQL 8.0

**Windows:**
```bash
# 1. 下载MySQL Installer
# 2. 运行安装程序，选择MySQL Server 8.0
# 3. 设置root密码
# 4. 启动MySQL服务
# 5. 验证安装
mysql --version
```


### 2. 项目部署

#### 克隆项目
```bash
# 克隆项目到本地
git clone <项目仓库地址>
cd Hospital-master
```

#### 数据库配置

**创建数据库:**
```sql
-- 登录MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选，推荐生产环境使用）
CREATE USER 'hospital_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON hospital_db.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;
```

**配置数据库连接:**
编辑 `src/main/resources/application.properties` 文件：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db?createDatabaseIfNotExist=true&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=你的MySQL密码
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT配置
jwt.secret=hospitalSecretKeyForJWTAuthentication256BitsMinimumSecurityRequirement
jwt.expiration=86400

# 服务器配置
server.port=8080
```

#### 编译和启动

**使用Maven命令:**
```bash
# 清理并编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

**使用提供的启动脚本:**

**Windows:**
```bash
# 双击运行
start.bat

# 或在命令行运行
.\start.bat
```


#### 访问系统

启动成功后，在浏览器中访问：

-
- **登录页面**: http://localhost:8080/login.html
- **管理后台**: http://localhost:8080/admin.html (管理员)
- **员工界面**: http://localhost:8080/staff.html (员工)

## 👤 默认账号

系统预置了以下测试账号：

| 角色 | 用户名 | 密码 | 权限说明 |
|------|--------|------|----------|
| 管理员 | admin | admin123 | 系统管理、用户管理、所有业务功能 |
| 员工 | staff1 | admin123 | 病人管理、挂号管理、收费管理 |

>

## 📊 数据库结构

系统包含以下主要数据表：

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `users` | 用户表 | username, password, real_name, role, enabled |
| `patients` | 病人信息表 | name, gender, birth_date, id_card, phone, address |
| `doctors` | 医生信息表 | name, department, title, specialization, consultation_fee |
| `medicines` | 药品信息表 | name, code, specification, price, stock, manufacturer |
| `registrations` | 挂号记录表 | patient_id, doctor_id, registration_date, status |
| `payments` | 收费记录表 | registration_id, amount, payment_type, status |
| `feedback` | 反馈表 | patient_id, content, rating, created_at |

## 🔗 API接口文档

### 认证相关
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/api/auth/login` | 用户登录 | 公开 |
| POST | `/api/auth/logout` | 用户登出 | 需要认证 |

### 用户管理
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/api/users` | 获取用户列表 | ADMIN |
| POST | `/api/users` | 创建用户 | ADMIN |
| PUT | `/api/users/{id}` | 更新用户信息 | ADMIN |
| DELETE | `/api/users/{id}` | 删除用户 | ADMIN |

### 病人管理
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/api/patients` | 获取病人列表 | ADMIN, STAFF |
| POST | `/api/patients` | 创建病人 | ADMIN, STAFF |
| GET | `/api/patients/{id}` | 获取病人详情 | ADMIN, STAFF |
| PUT | `/api/patients/{id}` | 更新病人信息 | ADMIN, STAFF |
| DELETE | `/api/patients/{id}` | 删除病人 | ADMIN |

### 医生管理
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/api/doctors` | 获取医生列表 | ADMIN, STAFF |
| POST | `/api/doctors` | 创建医生 | ADMIN |
| GET | `/api/doctors/{id}` | 获取医生详情 | ADMIN, STAFF |
| PUT | `/api/doctors/{id}` | 更新医生信息 | ADMIN |
| DELETE | `/api/doctors/{id}` | 删除医生 | ADMIN |

### 药品管理
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/api/medicines` | 获取药品列表 | ADMIN, STAFF |
| POST | `/api/medicines` | 创建药品 | ADMIN |
| PUT | `/api/medicines/{id}` | 更新药品信息 | ADMIN |
| DELETE | `/api/medicines/{id}` | 删除药品 | ADMIN |

### 挂号管理
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/api/registrations` | 获取挂号列表 | ADMIN, STAFF |
| POST | `/api/registrations` | 创建挂号 | ADMIN, STAFF |
| PUT | `/api/registrations/{id}/status` | 更新挂号状态 | ADMIN, STAFF |

### 收费管理
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/api/payments` | 获取收费记录 | ADMIN, STAFF |
| POST | `/api/payments` | 创建收费记录 | ADMIN, STAFF |
| PUT | `/api/payments/{id}/pay` | 处理支付 | ADMIN, STAFF |
| PUT | `/api/payments/{id}/refund` | 处理退费 | ADMIN, STAFF |

### 数据统计
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | `/api/dashboard/stats` | 获取仪表板统计 | ADMIN, STAFF |

## 🔐 权限说明

### 管理员权限 (ADMIN)
- ✅ 用户管理（增删改查）
- ✅ 医生管理（增删改查）
- ✅ 病人管理（增删改查）
- ✅ 药品管理（增删改查）
- ✅ 挂号管理（增删改查）
- ✅ 收费管理（增删改查）
- ✅ 系统统计报表
- ✅ 系统配置管理

### 员工权限 (STAFF)
- ❌ 用户管理
- ✅ 医生信息查看
- ✅ 病人管理（查看、添加、修改）
- ✅ 药品信息查看
- ✅ 挂号管理（增删改查）
- ✅ 收费管理（增删改查）
- ✅ 基础统计功能
