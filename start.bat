@echo off
chcp 65001
echo ========================================
echo    医院门诊管理系统启动脚本
echo ========================================
echo.

echo [1] 检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 未找到Java环境，请确保已安装JDK 17或更高版本
    pause
    exit /b 1
)
echo ✅ Java环境检查通过

echo.
echo [2] 检查Maven环境...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 未找到Maven环境，请确保已安装Maven 3.6或更高版本
    pause
    exit /b 1
)
echo ✅ Maven环境检查通过

echo.
echo [3] 编译项目...
mvn clean compile
if %errorlevel% neq 0 (
    echo ❌ 项目编译失败，请检查代码
    pause
    exit /b 1
)
echo ✅ 项目编译成功

echo.
echo [4] 启动项目...
echo 📝 请确保MySQL数据库已启动，并按README.md配置数据库连接
echo 🌐 项目启动后访问: http://localhost:8080
echo 👤 默认管理员账号: admin / admin123
echo.
echo 正在启动服务器...
mvn spring-boot:run

pause














