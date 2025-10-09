package com.example.hospital.service;

import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Doctor;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 医生服务接口
 */
public interface DoctorService {
    
    /**
     * 创建医生
     */
    Doctor createDoctor(Doctor doctor);
    
    /**
     * 根据ID获取医生
     */
    Doctor getDoctorById(Long id);
    
    /**
     * 更新医生信息
     */
    Doctor updateDoctor(Long id, Doctor doctor);
    
    /**
     * 删除医生
     */
    void deleteDoctor(Long id);
    
    /**
     * 分页获取医生列表
     */
    PageResponse<Doctor> getDoctors(Pageable pageable);
    
    /**
     * 根据科室获取医生列表
     */
    List<Doctor> getDoctorsByDepartment(String department);
    
    /**
     * 根据姓名搜索医生
     */
    List<Doctor> searchDoctorsByName(String name);
    
    /**
     * 获取所有科室列表
     */
    List<String> getAllDepartments();
    
    /**
     * 获取可用的医生列表
     */
    List<Doctor> getAvailableDoctors();
}














