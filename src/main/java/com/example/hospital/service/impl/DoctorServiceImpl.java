package com.example.hospital.service.impl;

import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Doctor;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 医生服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    
    private final DoctorRepository doctorRepository;
    
    @Override
    @Transactional
    public Doctor createDoctor(Doctor doctor) {
        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("创建医生成功: {}", savedDoctor.getName());
        return savedDoctor;
    }
    
    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("医生不存在"));
    }
    
    @Override
    @Transactional
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("医生不存在"));
        
        existingDoctor.setName(doctor.getName());
        existingDoctor.setGender(doctor.getGender());
        existingDoctor.setDepartment(doctor.getDepartment());
        existingDoctor.setTitle(doctor.getTitle());
        existingDoctor.setPhone(doctor.getPhone());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setIntroduction(doctor.getIntroduction());
        existingDoctor.setConsultationFee(doctor.getConsultationFee());
        existingDoctor.setAvailable(doctor.getAvailable());
        
        Doctor savedDoctor = doctorRepository.save(existingDoctor);
        log.info("更新医生成功: {}", savedDoctor.getName());
        return savedDoctor;
    }
    
    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("医生不存在"));
        
        doctorRepository.delete(doctor);
        log.info("删除医生成功: {}", doctor.getName());
    }
    
    @Override
    public PageResponse<Doctor> getDoctors(Pageable pageable) {
        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
        return new PageResponse<>(doctorPage.getContent(), doctorPage);
    }
    
    @Override
    public List<Doctor> getDoctorsByDepartment(String department) {
        return doctorRepository.findByDepartment(department);
    }
    
    @Override
    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepository.findByNameContaining(name);
    }
    
    @Override
    public List<String> getAllDepartments() {
        return doctorRepository.findAllDepartments();
    }
    
    @Override
    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.findByAvailable(true);
    }
}














