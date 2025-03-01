package com.company.erp.entity.material;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_material_purchase_order")
public class PurchaseOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_no", unique = true, nullable = false, length = 32)
    private String orderNo; // 单据编号（业务唯一号）
    
    @Column(name = "material_code", nullable = false, length = 20)
    private String materialCode; // 物料编号
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity; // 采购数量
    
    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount; // 订单金额
    
    @Column(name = "supplier_code", nullable = false, length = 20)
    private String supplierCode; // 供应商编码
    
    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.now(); // 单据创建时间

    // JPA需要的默认构造函数
    public PurchaseOrder() {
    }

    // 业务构造函数
    public PurchaseOrder(String orderNo, String materialCode, Integer quantity, 
                        BigDecimal amount, String supplierCode) {
        this.orderNo = orderNo;
        this.materialCode = materialCode;
        this.quantity = quantity;
        this.amount = amount;
        this.supplierCode = supplierCode;
    }

    // Getter/Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
