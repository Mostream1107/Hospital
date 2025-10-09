package com.example.hospital.service;

import com.example.hospital.dto.PageResponse;
import com.example.hospital.entity.Medicine;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 药品服务接口
 */
public interface MedicineService {
    
    /**
     * 创建药品
     */
    Medicine createMedicine(Medicine medicine);
    
    /**
     * 根据ID获取药品
     */
    Medicine getMedicineById(Long id);
    
    /**
     * 更新药品信息
     */
    Medicine updateMedicine(Long id, Medicine medicine);
    
    /**
     * 删除药品
     */
    void deleteMedicine(Long id);
    
    /**
     * 分页获取药品列表
     */
    PageResponse<Medicine> getMedicines(Pageable pageable);
    
    /**
     * 根据分类分页获取药品列表
     */
    PageResponse<Medicine> getMedicinesByCategory(String category, Pageable pageable);
    
    /**
     * 根据名称搜索药品
     */
    List<Medicine> searchMedicinesByName(String name);
    
    /**
     * 根据药品编码查找药品
     */
    Medicine getMedicineByCode(String code);
    
    /**
     * 检查药品编码是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 获取库存不足的药品
     */
    List<Medicine> getLowStockMedicines(Integer threshold);
    
    /**
     * 更新药品库存
     */
    Medicine updateStock(Long id, Integer quantity);
    
    /**
     * 初始化常用药品数据
     */
    Map<String, Integer> initializeCommonMedicines();
}













