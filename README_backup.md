# 医院门诊管理系统 - 原始README备份

一个基于Spring Boot的现代化医院门诊管理系统，提供病人管理、医生管理、挂号管理、收费管理等功能。

## 🚀 项目特性

- **用户权限管理**：支持管理员和普通员工不同权限
- **病人信息管理**：完整的病人档案管理
- **医生信息管理**：医生资料、科室管理
- **挂号预约系统**：在线挂号、状态跟踪
- **收费管理系统**：费用计算、支付处理、退费管理
- **数据统计报表**：多维度统计分析
- **JWT安全认证**：无状态身份验证
- **响应式界面**：现代化的Web界面

## 🛠️ 技术栈

### 后端
- Spring Boot 3.5.6
- Spring Security 6.5.5
- Spring Data JPA
- MySQL 8.0
- Flyway (数据库迁移)
- JWT (JSON Web Token)
- Lombok

### 前端
- HTML5 + CSS3 + JavaScript
- Bootstrap 5.1.3
- 响应式设计

## 📋 系统要求

- JDK 17 或更高版本
- MySQL 8.0 或更高版本
- Maven 3.6 或更高版本

## ⚙️ 快速开始

### 1. 数据库准备

首先创建MySQL数据库：

```sql
CREATE DATABASE hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 配置数据库连接

编辑 `src/main/resources/application.properties` 文件，修改数据库连接信息：

```properties
# 数据库配置 - 根据你的实际情况修改
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db?createDatabaseIfNotExist=true&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=你的数据库密码
```

### 3. 启动项目

```bash
# 克隆项目后进入项目目录
cd Hospital

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

### 4. 访问系统

启动成功后，在浏览器访问：

- **系统首页**: http://localhost:8080
- **登录页面**: http://localhost:8080/login.html
- **管理后台**: http://localhost:8080/admin.html (管理员)

## 👤 默认账号

系统预置了以下测试账号：

| 角色 | 用户名 | 密码 | 权限 |
|------|--------|------|------|
| 管理员 | admin | admin123 | 所有功能权限 |
| 员工 | staff1 | admin123 | 基础操作权限 |

## 📊 数据库结构

系统包含以下主要数据表：

- `users` - 用户表
- `patients` - 病人信息表
- `doctors` - 医生信息表
- `medicines` - 药品信息表
- `registrations` - 挂号记录表
- `payments` - 收费记录表

## 🔗 API接口

### 认证接口
- `POST /api/auth/login` - 用户登录

### 病人管理
- `GET /api/patients` - 获取病人列表
- `POST /api/patients` - 创建病人
- `PUT /api/patients/{id}` - 更新病人信息
- `DELETE /api/patients/{id}` - 删除病人

### 医生管理
- `GET /api/doctors` - 获取医生列表
- `POST /api/doctors` - 创建医生
- `PUT /api/doctors/{id}` - 更新医生信息
- `DELETE /api/doctors/{id}` - 删除医生

### 挂号管理
- `GET /api/registrations` - 获取挂号列表
- `POST /api/registrations` - 创建挂号
- `PUT /api/registrations/{id}/status` - 更新挂号状态

### 收费管理
- `GET /api/payments` - 获取收费记录
- `POST /api/payments` - 创建收费记录
- `PUT /api/payments/{id}/pay` - 处理支付
- `PUT /api/payments/{id}/refund` - 处理退费

## 🔐 权限说明

### 管理员权限 (ADMIN)
- 用户管理（增删改查）
- 医生管理（增删改查）
- 病人管理（增删改查）
- 药品管理（增删改查）
- 挂号管理（增删改查）
- 收费管理（增删改查）
- 系统统计报表

### 员工权限 (STAFF)
- 病人管理（查看、添加、修改）
- 医生信息查看
- 挂号管理（增删改查）
- 收费管理（增删改查）
- 基础统计功能


