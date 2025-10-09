package com.example.hospital.service;

import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Patient;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 病人服务接口
 */
public interface PatientService {
    
    /**
     * 创建病人
     */
    Patient createPatient(Patient patient);
    
    /**
     * 根据ID获取病人
     */
    Patient getPatientById(Long id);
    
    /**
     * 更新病人信息
     */
    Patient updatePatient(Long id, Patient patient);
    
    /**
     * 删除病人
     */
    void deletePatient(Long id);
    
    /**
     * 分页获取病人列表
     */
    PageResponse<Patient> getPatients(Pageable pageable);
    
    /**
     * 根据姓名搜索病人
     */
    List<Patient> searchPatientsByName(String name);
    
    /**
     * 综合搜索病人（支持姓名、身份证号、手机号）
     */
    List<Patient> searchPatients(String keyword);
    
    /**
     * 根据身份证号查找病人
     */
    Patient getPatientByIdCard(String idCard);
    
    /**
     * 检查身份证号是否存在
     */
    boolean existsByIdCard(String idCard);
}








