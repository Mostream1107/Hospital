package com.example.hospital.service.impl;

import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Patient;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 病人服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {
    
    private final PatientRepository patientRepository;
    
    @Override
    @Transactional
    public Patient createPatient(Patient patient) {
        // 检查身份证号是否为空以及是否已存在
        String idCard = patient.getIdCard();
        if (idCard != null && !idCard.trim().isEmpty() && patientRepository.existsByIdCard(idCard)) {
            throw new RuntimeException("身份证号已存在");
        }
        
        Patient savedPatient = patientRepository.save(patient);
        log.info("创建病人成功: {}", savedPatient.getName());
        return savedPatient;
    }
    
    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("病人不存在"));
    }
    
    @Override
    @Transactional
    public Patient updatePatient(Long id, Patient patient) {
        log.info("开始更新病人信息: ID={}", id);
        
        Patient existingPatient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("病人不存在"));
        
        log.info("当前病人信息: 姓名={}, 身份证号={}", existingPatient.getName(), existingPatient.getIdCard());
        log.info("新病人信息: 姓名={}, 身份证号={}", patient.getName(), patient.getIdCard());
        
        // 检查身份证号是否被其他病人使用
        String existingIdCard = existingPatient.getIdCard();
        String newIdCard = patient.getIdCard();
        
        if (newIdCard != null && !newIdCard.trim().isEmpty()) {
            // 如果新身份证号与现有的不同，检查是否已被其他人使用
            if (existingIdCard == null || !existingIdCard.equals(newIdCard)) {
                if (patientRepository.existsByIdCard(newIdCard)) {
                    throw new RuntimeException("身份证号已存在");
                }
            }
        }
        
        // 更新字段
        existingPatient.setName(patient.getName());
        existingPatient.setGender(patient.getGender());
        existingPatient.setBirthDate(patient.getBirthDate());
        existingPatient.setIdCard(patient.getIdCard());
        existingPatient.setPhone(patient.getPhone());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setEmergencyContact(patient.getEmergencyContact());
        existingPatient.setEmergencyPhone(patient.getEmergencyPhone());
        existingPatient.setMedicalHistory(patient.getMedicalHistory());
        existingPatient.setAllergies(patient.getAllergies());
        
        Patient savedPatient = patientRepository.save(existingPatient);
        log.info("更新病人成功: ID={}, 姓名={}", savedPatient.getId(), savedPatient.getName());
        return savedPatient;
    }
    
    @Override
    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("病人不存在"));
        
        patientRepository.delete(patient);
        log.info("删除病人成功: {}", patient.getName());
    }
    
    @Override
    public PageResponse<Patient> getPatients(Pageable pageable) {
        Page<Patient> patientPage = patientRepository.findAll(pageable);
        return new PageResponse<>(patientPage.getContent(), patientPage);
    }
    
    @Override
    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByNameContaining(name);
    }
    
    @Override
    public List<Patient> searchPatients(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        
        log.info("执行病人综合搜索，关键词: {}", keyword);
        
        // 使用Set去重，避免多个条件匹配同一病人时重复
        Set<Patient> patientSet = new HashSet<>();
        
        // 按姓名搜索（模糊匹配）
        List<Patient> nameMatches = patientRepository.findByNameContaining(keyword);
        patientSet.addAll(nameMatches);
        log.debug("按姓名搜索到 {} 个结果", nameMatches.size());
        
        // 按身份证号搜索（精确匹配和模糊匹配）
        Optional<Patient> idCardExactMatch = patientRepository.findByIdCard(keyword);
        idCardExactMatch.ifPresent(patientSet::add);
        
        // 身份证号模糊匹配（适用于输入部分身份证号的情况）
        List<Patient> idCardMatches = patientRepository.findByIdCardContaining(keyword);
        patientSet.addAll(idCardMatches);
        log.debug("按身份证号搜索到 {} 个结果", idCardMatches.size());
        
        // 按手机号搜索（精确匹配和模糊匹配）
        Optional<Patient> phoneExactMatch = patientRepository.findByPhone(keyword);
        phoneExactMatch.ifPresent(patientSet::add);
        
        // 手机号模糊匹配
        List<Patient> phoneMatches = patientRepository.findByPhoneContaining(keyword);
        patientSet.addAll(phoneMatches);
        log.debug("按手机号搜索到 {} 个结果", phoneMatches.size());
        
        List<Patient> result = new ArrayList<>(patientSet);
        
        // 按创建时间倒序排序，最新的在前面
        result.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
        
        log.info("综合搜索完成，共找到 {} 个匹配的病人", result.size());
        return result;
    }
    
    @Override
    public Patient getPatientByIdCard(String idCard) {
        return patientRepository.findByIdCard(idCard)
            .orElseThrow(() -> new RuntimeException("未找到该身份证号对应的病人"));
    }
    
    @Override
    public boolean existsByIdCard(String idCard) {
        return patientRepository.existsByIdCard(idCard);
    }
}