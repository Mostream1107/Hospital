package com.example.hospital.repository;

import com.example.hospital.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 药品仓库接口
 */
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    /**
     * 根据药品名称查找
     */
    List<Medicine> findByNameContaining(String name);
    
    /**
     * 根据分类分页查找药品
     */
    Page<Medicine> findByCategory(String category, Pageable pageable);
    
    /**
     * 根据药品编码查找
     */
    Optional<Medicine> findByCode(String code);
    
    /**
     * 根据可用状态查找
     */
    List<Medicine> findByAvailable(Boolean available);
    
    /**
     * 根据生产厂家查找
     */
    List<Medicine> findByManufacturer(String manufacturer);
    
    /**
     * 根据价格范围查找
     */
    List<Medicine> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * 根据库存数量查找
     */
    List<Medicine> findByStockGreaterThan(Integer stock);
    
    /**
     * 根据库存数量查找（小于等于指定数量）
     */
    List<Medicine> findByStockLessThanEqual(Integer stock);
    
    /**
     * 检查药品编码是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 根据适应症查找药品
     */
    @Query("SELECT m FROM Medicine m WHERE m.indication IS NOT NULL AND m.indication LIKE %:indication%")
    List<Medicine> findByIndicationContaining(@Param("indication") String indication);
    
    /**
     * 根据可用状态和库存查找药品
     */
    @Query("SELECT m FROM Medicine m WHERE m.available = :available AND m.stock > :minStock")
    List<Medicine> findByAvailableAndStockGreaterThan(@Param("available") Boolean available, @Param("minStock") Integer minStock);
    
    /**
     * 获取库存不足的药品（库存小于等于指定数量且可用）
     */
    @Query("SELECT m FROM Medicine m WHERE m.available = true AND m.stock <= :threshold ORDER BY m.stock ASC")
    List<Medicine> findLowStockMedicines(@Param("threshold") Integer threshold);
    
    /**
     * 根据药品名称或编码查找
     */
    @Query("SELECT m FROM Medicine m WHERE m.name LIKE %:keyword% OR m.code LIKE %:keyword%")
    List<Medicine> findByNameOrCodeContaining(@Param("keyword") String keyword);
    
    /**
     * 获取所有生产厂家列表
     */
    @Query("SELECT DISTINCT m.manufacturer FROM Medicine m WHERE m.manufacturer IS NOT NULL ORDER BY m.manufacturer")
    List<String> findAllManufacturers();
}














