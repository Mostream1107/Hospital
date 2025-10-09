package com.example.hospital.service.impl;

import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Medicine;
import com.example.hospital.repository.MedicineRepository;
import com.example.hospital.service.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 药品服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MedicineServiceImpl implements MedicineService {
    
    private final MedicineRepository medicineRepository;
    
    @Override
    @Transactional
    public Medicine createMedicine(Medicine medicine) {
        if (medicineRepository.existsByCode(medicine.getCode())) {
            throw new RuntimeException("药品编码已存在");
        }
        
        Medicine savedMedicine = medicineRepository.save(medicine);
        log.info("创建药品成功: {}", savedMedicine.getName());
        return savedMedicine;
    }
    
    @Override
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("药品不存在"));
    }
    
    @Override
    @Transactional
    public Medicine updateMedicine(Long id, Medicine medicine) {
        Medicine existingMedicine = medicineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("药品不存在"));
        
        // 检查药品编码是否被其他药品使用
        if (!existingMedicine.getCode().equals(medicine.getCode()) && 
            medicineRepository.existsByCode(medicine.getCode())) {
            throw new RuntimeException("药品编码已存在");
        }
        
        // 更新字段，确保不为null
        existingMedicine.setName(medicine.getName());
        existingMedicine.setCode(medicine.getCode());
        existingMedicine.setCategory(medicine.getCategory());
        existingMedicine.setSpecification(medicine.getSpecification());
        existingMedicine.setUnit(medicine.getUnit());
        existingMedicine.setPrice(medicine.getPrice());
        existingMedicine.setStock(medicine.getStock());
        existingMedicine.setManufacturer(medicine.getManufacturer());
        existingMedicine.setIndication(medicine.getIndication());
        existingMedicine.setDosage(medicine.getDosage());
        existingMedicine.setSideEffects(medicine.getSideEffects());
        existingMedicine.setContraindications(medicine.getContraindications());
        existingMedicine.setAvailable(medicine.getAvailable() != null ? medicine.getAvailable() : true);
        
        Medicine savedMedicine = medicineRepository.save(existingMedicine);
        log.info("更新药品成功: {}", savedMedicine.getName());
        return savedMedicine;
    }
    
    @Override
    @Transactional
    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("药品不存在"));
        
        medicineRepository.delete(medicine);
        log.info("删除药品成功: {}", medicine.getName());
    }
    
    @Override
    public PageResponse<Medicine> getMedicines(Pageable pageable) {
        Page<Medicine> medicinePage = medicineRepository.findAll(pageable);
        return new PageResponse<>(medicinePage.getContent(), medicinePage);
    }
    
    @Override
    public PageResponse<Medicine> getMedicinesByCategory(String category, Pageable pageable) {
        Page<Medicine> medicinePage = medicineRepository.findByCategory(category, pageable);
        return new PageResponse<>(medicinePage.getContent(), medicinePage);
    }
    
    @Override
    public List<Medicine> searchMedicinesByName(String name) {
        return medicineRepository.findByNameContaining(name);
    }
    
    @Override
    public Medicine getMedicineByCode(String code) {
        return medicineRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("未找到该编码对应的药品"));
    }
    
    @Override
    public boolean existsByCode(String code) {
        return medicineRepository.existsByCode(code);
    }
    
    @Override
    public List<Medicine> getLowStockMedicines(Integer threshold) {
        return medicineRepository.findLowStockMedicines(threshold);
    }
    
    @Override
    @Transactional
    public Medicine updateStock(Long id, Integer quantity) {
        Medicine medicine = medicineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("药品不存在"));
        
        medicine.setStock(quantity);
        Medicine savedMedicine = medicineRepository.save(medicine);
        log.info("更新药品库存成功: {} -> {}", medicine.getName(), quantity);
        return savedMedicine;
    }
    
    @Override
    @Transactional
    public Map<String, Integer> initializeCommonMedicines() {
        List<Medicine> commonMedicines = createCommonMedicinesList();
        int added = 0;
        int skipped = 0;
        
        for (Medicine medicine : commonMedicines) {
            if (!medicineRepository.existsByCode(medicine.getCode())) {
                try {
                    medicineRepository.save(medicine);
                    added++;
                    log.info("添加常用药品: {}", medicine.getName());
                } catch (Exception e) {
                    log.warn("添加药品失败: {} - {}", medicine.getName(), e.getMessage());
                    skipped++;
                }
            } else {
                skipped++;
                log.debug("药品已存在，跳过: {}", medicine.getName());
            }
        }
        
        Map<String, Integer> result = new HashMap<>();
        result.put("added", added);
        result.put("skipped", skipped);
        result.put("total", commonMedicines.size());
        
        log.info("初始化常用药品完成: 添加{}个，跳过{}个，总计{}个", added, skipped, commonMedicines.size());
        return result;
    }
    
    private List<Medicine> createCommonMedicinesList() {
        List<Medicine> medicines = new ArrayList<>();
        
        // 感冒药
        medicines.add(createMedicine("999感冒灵颗粒", "GML001", "感冒药", "10g/袋", "袋", 
            new BigDecimal("15.50"), 200, "华润三九", 
            "用于感冒引起的头痛，发热，鼻塞，流涕，咽痛等症状", 
            "开水冲服，一次1袋，一日3次", 
            "偶见困倦、嗜睡、口渴、虚弱感", 
            "严重肝肾功能不全者禁用"));
            
        medicines.add(createMedicine("连花清瘟胶囊", "LHQW001", "感冒药", "0.35g", "粒", 
            new BigDecimal("28.00"), 150, "石家庄以岭药业", 
            "清瘟解毒，宣肺泄热。用于治疗流行性感冒", 
            "口服，一次4粒，一日3次", 
            "个别患者服药后偶有胃肠不适", 
            "孕妇慎用"));
            
        medicines.add(createMedicine("板蓝根颗粒", "BLG001", "感冒药", "10g/袋", "袋", 
            new BigDecimal("12.80"), 300, "广州白云山", 
            "清热解毒，凉血利咽。用于肺胃热盛所致的咽喉肿痛", 
            "开水冲服，一次1袋，一日3-4次", 
            "偶见轻微胃肠不适", 
            "体虚而无实火热毒者不宜服用"));
            
        // 消炎药
        medicines.add(createMedicine("阿莫西林胶囊", "AMX001", "抗生素", "0.25g", "粒", 
            new BigDecimal("18.50"), 500, "华北制药", 
            "适用于敏感菌所致的各种感染", 
            "口服，成人一次0.5g，每6-8小时1次", 
            "可能出现过敏反应、胃肠道反应", 
            "对青霉素类抗生素过敏者禁用"));
            
        medicines.add(createMedicine("头孢克肟胶囊", "TBK001", "抗生素", "0.1g", "粒", 
            new BigDecimal("25.60"), 400, "齐鲁制药", 
            "适用于敏感细菌所致的感染", 
            "口服，成人一次0.2g，一日2次", 
            "可能出现腹泻、恶心等胃肠道反应", 
            "对头孢菌素类抗生素过敏者禁用"));
            
        medicines.add(createMedicine("罗红霉素胶囊", "LHM001", "抗生素", "0.15g", "粒", 
            new BigDecimal("22.30"), 350, "扬子江药业", 
            "适用于敏感菌所致的感染", 
            "口服，成人一次0.15g，一日2次", 
            "可能出现胃肠道不适、头痛", 
            "对大环内酯类抗生素过敏者禁用"));
            
        // 止痛药
        medicines.add(createMedicine("布洛芬缓释胶囊", "BLF001", "止痛药", "0.3g", "粒", 
            new BigDecimal("16.80"), 400, "中美天津史克", 
            "用于缓解轻至中度疼痛", 
            "口服，一次1粒，必要时每8小时1次", 
            "可能出现胃肠道不适、头晕", 
            "对阿司匹林过敏者禁用"));
            
        medicines.add(createMedicine("对乙酰氨基酚片", "DYXABJF001", "止痛药", "0.5g", "片", 
            new BigDecimal("8.50"), 600, "天津力生制药", 
            "用于普通感冒或流行性感冒引起的发热，也用于缓解轻至中度疼痛", 
            "口服，成人一次0.5-1g，每4-6小时1次", 
            "偶见皮疹、荨麻疹、药热", 
            "严重肝肾功能不全者禁用"));
            
        medicines.add(createMedicine("双氯芬酸钠肠溶片", "SLF001", "止痛药", "25mg", "片", 
            new BigDecimal("14.20"), 300, "北京诺华制药", 
            "用于缓解类风湿关节炎、骨关节炎等引起的疼痛", 
            "口服，一次25mg，一日2-3次", 
            "可能出现胃肠道不适、头痛", 
            "消化性溃疡患者禁用"));
            
        // 维生素
        medicines.add(createMedicine("维生素C片", "WSC001", "维生素", "0.1g", "片", 
            new BigDecimal("6.80"), 800, "华润双鹤药业", 
            "用于预防坏血病，也可用于各种急慢性传染疾病", 
            "口服，成人一次0.1-0.2g，一日3次", 
            "大剂量服用可能出现腹泻、皮疹", 
            "无特殊禁忌"));
            
        medicines.add(createMedicine("复合维生素B片", "FHWSB001", "维生素", "1片", "片", 
            new BigDecimal("12.50"), 500, "上海信谊药厂", 
            "用于预防和治疗B族维生素缺乏症", 
            "口服，成人一次1-3片，一日3次", 
            "偶见胃肠道不适", 
            "无特殊禁忌"));
            
        medicines.add(createMedicine("钙尔奇D600片", "GEQ001", "维生素", "1片", "片", 
            new BigDecimal("45.60"), 200, "辉瑞制药", 
            "用于妊娠和哺乳期妇女、更年期妇女、老年人等的钙补充", 
            "口服，一次1片，一日1-2次", 
            "偶见便秘、胃肠胀气", 
            "高钙血症患者禁用"));
            
        // 心血管药
        medicines.add(createMedicine("硝苯地平缓释片", "XBD001", "心血管药", "20mg", "片", 
            new BigDecimal("28.90"), 300, "拜耳医药", 
            "用于治疗高血压、心绞痛", 
            "口服，一次20mg，一日1次", 
            "可能出现头痛、面部潮红、踝部水肿", 
            "心源性休克患者禁用"));
            
        medicines.add(createMedicine("阿司匹林肠溶片", "ASP001", "心血管药", "100mg", "片", 
            new BigDecimal("15.30"), 400, "拜耳医药", 
            "用于预防心肌梗死复发，预防血栓形成", 
            "口服，一次100mg，一日1次", 
            "可能出现胃肠道出血、皮疹", 
            "活动性消化性溃疡患者禁用"));
            
        medicines.add(createMedicine("卡托普利片", "KTP001", "心血管药", "25mg", "片", 
            new BigDecimal("18.70"), 350, "中美上海施贵宝", 
            "用于治疗高血压、心力衰竭", 
            "口服，一次12.5-25mg，一日2-3次", 
            "可能出现干咳、高钾血症", 
            "双侧肾动脉狭窄患者禁用"));
            
        // 消化系统药
        medicines.add(createMedicine("奥美拉唑肠溶胶囊", "AML001", "消化系统药", "20mg", "粒", 
            new BigDecimal("32.50"), 250, "阿斯利康制药", 
            "适用于胃溃疡、十二指肠溃疡、反流性食管炎", 
            "口服，一次20mg，一日1-2次", 
            "可能出现头痛、腹泻、皮疹", 
            "对本品过敏者禁用"));
            
        medicines.add(createMedicine("多潘立酮片", "DPL001", "消化系统药", "10mg", "片", 
            new BigDecimal("24.80"), 300, "西安杨森制药", 
            "用于消化不良、腹胀、嗳气、恶心、呕吐", 
            "口服，一次10mg，一日3次", 
            "可能出现口干、皮疹、乳房胀痛", 
            "胃肠道出血患者禁用"));
            
        medicines.add(createMedicine("蒙脱石散", "MTS001", "消化系统药", "3g/袋", "袋", 
            new BigDecimal("19.60"), 400, "博福-益普生", 
            "用于成人及儿童急、慢性腹泻", 
            "口服，成人一次1袋，一日3次", 
            "可能出现便秘", 
            "无特殊禁忌"));
            
        // 呼吸系统药
        medicines.add(createMedicine("沙丁胺醇气雾剂", "SDL001", "呼吸系统药", "100μg/揿", "支", 
            new BigDecimal("26.40"), 150, "葛兰素史克", 
            "用于预防和治疗支气管哮喘或喘息性支气管炎", 
            "吸入，一次100-200μg，一日3-4次", 
            "可能出现震颤、心悸、头痛", 
            "对本品过敏者禁用"));
            
        medicines.add(createMedicine("氨溴索口服溶液", "ALX001", "呼吸系统药", "15mg/5ml", "瓶", 
            new BigDecimal("22.80"), 200, "勃林格殷格翰", 
            "适用于痰液粘稠而不易咳出的患者", 
            "口服，成人一次10ml，一日3次", 
            "偶见胃肠道不适", 
            "无特殊禁忌"));
            
        medicines.add(createMedicine("复方甘草片", "FGCP001", "呼吸系统药", "1片", "片", 
            new BigDecimal("8.90"), 500, "太极集团", 
            "用于镇咳祛痰", 
            "口服，一次2-3片，一日3次", 
            "可能出现恶心、呕吐", 
            "无特殊禁忌"));
            
        // 神经系统药
        medicines.add(createMedicine("安定片", "AD001", "神经系统药", "2.5mg", "片", 
            new BigDecimal("12.30"), 200, "上海信谊药厂", 
            "用于焦虑、紧张、激动，也可用于催眠或癫痫", 
            "口服，抗焦虑一次2.5-5mg，一日3次", 
            "可能出现嗜睡、乏力、记忆力减退", 
            "重症肌无力患者禁用"));
            
        medicines.add(createMedicine("谷维素片", "GWS001", "神经系统药", "10mg", "片", 
            new BigDecimal("15.70"), 400, "华润双鹤药业", 
            "用于神经官能症、经前期紧张综合征、更年期综合征", 
            "口服，一次10-20mg，一日3次", 
            "偶见胃部不适、恶心", 
            "无特殊禁忌"));
            
        medicines.add(createMedicine("银杏叶片", "YXY001", "神经系统药", "9.6mg", "片", 
            new BigDecimal("28.50"), 300, "德国威玛舒培", 
            "用于血瘀阻络引起的胸痹心痛、中风、半身不遂", 
            "口服，一次2-3片，一日3次", 
            "偶见胃肠道不适、头痛", 
            "无特殊禁忌"));
            
        // 外用药
        medicines.add(createMedicine("红霉素软膏", "HMM001", "外用药", "10g", "支", 
            new BigDecimal("8.60"), 600, "上海通用药业", 
            "用于脓疱疮等化脓性皮肤病、小面积烧伤、溃疡面的感染", 
            "外用，涂于患处，一日2次", 
            "偶见局部刺激如烧灼感", 
            "对本品过敏者禁用"));
            
        medicines.add(createMedicine("云南白药气雾剂", "YNBY001", "外用药", "60g", "支", 
            new BigDecimal("35.80"), 200, "云南白药集团", 
            "用于跌打损伤、瘀血肿痛、肌肉酸痛及风湿性关节疼痛", 
            "外用，喷于患处，一日数次", 
            "偶见皮肤过敏", 
            "皮肤破溃处禁用"));
            
        medicines.add(createMedicine("碘伏消毒液", "DF001", "外用药", "500ml", "瓶", 
            new BigDecimal("12.90"), 400, "利康制药", 
            "用于皮肤、粘膜的消毒", 
            "外用，直接涂擦或稀释后使用", 
            "偶见局部刺激", 
            "对碘过敏者禁用"));
            
        return medicines;
    }
    
    private Medicine createMedicine(String name, String code, String category, String specification, 
                                   String unit, BigDecimal price, Integer stock, String manufacturer,
                                   String indication, String dosage, String sideEffects, String contraindications) {
        Medicine medicine = new Medicine();
        medicine.setName(name);
        medicine.setCode(code);
        medicine.setCategory(category);
        medicine.setSpecification(specification);
        medicine.setUnit(unit);
        medicine.setPrice(price);
        medicine.setStock(stock);
        medicine.setManufacturer(manufacturer);
        medicine.setIndication(indication);
        medicine.setDosage(dosage);
        medicine.setDosageForm(getDosageFormFromSpecification(specification)); // 根据规格推断剂型
        medicine.setSideEffects(sideEffects);
        medicine.setContraindications(contraindications);
        medicine.setAvailable(true);
        return medicine;
    }
    
    private String getDosageFormFromSpecification(String specification) {
        if (specification == null) return "其他";
        
        String spec = specification.toLowerCase();
        if (spec.contains("片") || spec.contains("tablet")) return "片剂";
        if (spec.contains("胶囊") || spec.contains("capsule")) return "胶囊";
        if (spec.contains("颗粒") || spec.contains("granule")) return "颗粒剂";
        if (spec.contains("袋") || spec.contains("包")) return "颗粒剂";
        if (spec.contains("注射") || spec.contains("injection")) return "注射剂";
        if (spec.contains("软膏") || spec.contains("膏")) return "软膏剂";
        if (spec.contains("滴眼") || spec.contains("眼药")) return "滴眼剂";
        if (spec.contains("口服液") || spec.contains("糖浆")) return "口服液";
        if (spec.contains("喷雾") || spec.contains("spray")) return "喷雾剂";
        
        return "其他";
    }
}













