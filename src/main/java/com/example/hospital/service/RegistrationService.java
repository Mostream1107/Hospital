package com.example.hospital.service;

import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Registration;
import com.example.hospital.entity.RegistrationStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 挂号服务接口
 */
public interface RegistrationService {
    
    /**
     * 创建挂号
     */
    Registration createRegistration(Registration registration);
    
    /**
     * 根据ID获取挂号
     */
    Registration getRegistrationById(Long id);
    
    /**
     * 更新挂号信息
     */
    Registration updateRegistration(Long id, Registration registration);
    
    /**
     * 删除挂号
     */
    void deleteRegistration(Long id);
    
    /**
     * 分页获取挂号列表
     */
    PageResponse<Registration> getRegistrations(Pageable pageable);
    
    /**
     * 根据病人ID获取挂号列表
     */
    List<Registration> getRegistrationsByPatientId(Long patientId);
    
    /**
     * 根据医生ID获取挂号列表
     */
    List<Registration> getRegistrationsByDoctorId(Long doctorId);
    
    /**
     * 更新挂号状态
     */
    Registration updateRegistrationStatus(Long id, RegistrationStatus status);
    
    /**
     * 获取今日挂号统计
     */
    List<Object[]> getTodayRegistrationStatistics();
}














