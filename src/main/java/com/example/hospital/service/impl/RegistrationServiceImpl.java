package com.example.hospital.service.impl;

import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Registration;
import com.example.hospital.entity.RegistrationStatus;
import com.example.hospital.repository.RegistrationRepository;
import com.example.hospital.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 挂号服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    
    private final RegistrationRepository registrationRepository;
    
    @Override
    @Transactional
    public Registration createRegistration(Registration registration) {
        Registration savedRegistration = registrationRepository.save(registration);
        log.info("创建挂号成功: 病人ID={}, 医生ID={}", 
                savedRegistration.getPatient().getId(), 
                savedRegistration.getDoctor().getId());
        return savedRegistration;
    }
    
    @Override
    public Registration getRegistrationById(Long id) {
        return registrationRepository.findByIdWithPatientAndDoctor(id)
            .orElseThrow(() -> new RuntimeException("挂号记录不存在"));
    }
    
    @Override
    @Transactional
    public Registration updateRegistration(Long id, Registration registration) {
        Registration existingRegistration = registrationRepository.findByIdWithPatientAndDoctor(id)
            .orElseThrow(() -> new RuntimeException("挂号记录不存在"));
        
        // 更新病人和医生信息（如果提供了的话）
        if (registration.getPatient() != null && registration.getPatient().getId() != null) {
            existingRegistration.setPatient(registration.getPatient());
        }
        if (registration.getDoctor() != null && registration.getDoctor().getId() != null) {
            existingRegistration.setDoctor(registration.getDoctor());
        }
        
        existingRegistration.setAppointmentTime(registration.getAppointmentTime());
        existingRegistration.setRegistrationFee(registration.getRegistrationFee());
        existingRegistration.setStatus(registration.getStatus());
        existingRegistration.setSymptoms(registration.getSymptoms());
        existingRegistration.setDiagnosis(registration.getDiagnosis());
        existingRegistration.setTreatment(registration.getTreatment());
        existingRegistration.setPrescription(registration.getPrescription());
        existingRegistration.setNotes(registration.getNotes());
        
        Registration savedRegistration = registrationRepository.save(existingRegistration);
        log.info("更新挂号成功: ID={}", savedRegistration.getId());
        return savedRegistration;
    }
    
    @Override
    @Transactional
    public void deleteRegistration(Long id) {
        Registration registration = registrationRepository.findByIdWithPatientAndDoctor(id)
            .orElseThrow(() -> new RuntimeException("挂号记录不存在"));
        
        registrationRepository.delete(registration);
        log.info("删除挂号成功: ID={}", id);
    }
    
    @Override
    public PageResponse<Registration> getRegistrations(Pageable pageable) {
        Page<Registration> registrationPage = registrationRepository.findAllWithPatientAndDoctor(pageable);
        return new PageResponse<>(registrationPage.getContent(), registrationPage);
    }
    
    @Override
    public List<Registration> getRegistrationsByPatientId(Long patientId) {
        return registrationRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }
    
    @Override
    public List<Registration> getRegistrationsByDoctorId(Long doctorId) {
        return registrationRepository.findByDoctorIdOrderByAppointmentTimeAsc(doctorId);
    }
    
    @Override
    @Transactional
    public Registration updateRegistrationStatus(Long id, RegistrationStatus status) {
        Registration registration = registrationRepository.findByIdWithPatientAndDoctor(id)
            .orElseThrow(() -> new RuntimeException("挂号记录不存在"));
        
        registration.setStatus(status);
        Registration savedRegistration = registrationRepository.save(registration);
        log.info("更新挂号状态成功: ID={}, 新状态={}", id, status);
        return savedRegistration;
    }
    
    @Override
    public List<Object[]> getTodayRegistrationStatistics() {
        return registrationRepository.getTodayRegistrationStatistics();
    }
}
