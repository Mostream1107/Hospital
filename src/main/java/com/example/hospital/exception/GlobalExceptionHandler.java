package com.example.hospital.exception;

import com.example.hospital.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理静态资源找不到异常 - 避免favicon.ico等图标请求污染日志
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFoundException(NoResourceFoundException ex) {
        String resourcePath = ex.getResourcePath();
        
        // 对于favicon.ico等常见图标文件，静默处理，不记录日志
        if (isStaticIconResource(resourcePath)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // 其他静态资源找不到则记录警告日志
        log.warn("静态资源找不到: {}", resourcePath);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    /**
     * 判断是否为静态图标资源
     */
    private boolean isStaticIconResource(String resourcePath) {
        if (resourcePath == null) return false;
        
        return "favicon.ico".equals(resourcePath) || 
               resourcePath.endsWith(".ico") || 
               resourcePath.endsWith(".png") || 
               resourcePath.endsWith(".jpg") || 
               resourcePath.endsWith(".jpeg") || 
               resourcePath.endsWith(".gif") || 
               resourcePath.endsWith(".svg") ||
               resourcePath.startsWith("apple-touch-icon") ||
               resourcePath.startsWith("android-chrome");
    }

    /**
     * 处理方法参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("参数验证失败: {}", ex.getMessage());
        
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        
        String message = "输入信息有误，请检查以下字段：" + String.join(", ", fieldErrors.keySet());
        
        return ResponseEntity.badRequest()
            .body(ApiResponse.error(message, fieldErrors));
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleBindException(BindException ex) {
        log.error("数据绑定失败: {}", ex.getMessage());
        
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        
        String message = "输入信息有误，请检查以下字段：" + String.join(", ", fieldErrors.keySet());
        
        return ResponseEntity.badRequest()
            .body(ApiResponse.error(message, fieldErrors));
    }

    /**
     * 处理业务逻辑异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("业务逻辑异常: {}", ex.getMessage());
        
        return ResponseEntity.badRequest()
            .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("系统异常: ", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("系统内部错误，请稍后重试"));
    }
}
