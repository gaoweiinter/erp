package com.company.erp.mm.formula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.company.erp.function.EntityContextAction;
import com.company.erp.function.IMidContext;
import com.company.erp.function.MidContextTool;
import com.company.erp.function.DocumentFunction;
import com.company.erp.formula.StatusFormula;
import com.company.erp.mm.entity.MM_PurchaseOrder;
import com.company.erp.mm.entity.MM_PurchaseOrderDtl;
import java.math.BigDecimal;
import java.util.List;

public class PurchaseOrderFormula extends EntityContextAction{
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public PurchaseOrderFormula(IMidContext context) throws Throwable{
        super(context);
    }

    /**
     * 生成采购订单
     * 
     * @param purchaseOrder 采购订单实体
     * @throws Throwable
     */
    public void save(PurchaseOrder purchaseOrder) throws Throwable{
        // 1. 验证必填字段
        validateRequiredFields(purchaseOrder);
            
        // 2. 生成单据编号
        if (purchaseOrder.getDocumentNumber() == null || purchaseOrder.getDocumentNumber().isEmpty()) {
            String docNumber = DocumentFunction.generateDocumentNumber(getMidContext(), "MM", "PO");
            purchaseOrder.setDocumentNumber(docNumber);
        }
        // 3. 保存主表数据
        MidContextTool.saveObject(getMidContext(), purchaseOrder);
    }

    /**
     * 验证必填字段
     */
    private void validateRequiredFields(PurchaseOrder purchaseOrder) throws Throwable {
        // 1. 基础字段验证
        if (purchaseOrder.getVendorID() == null) {
            throw new Throwable("供应商不能为空");
        }
        
        // 2. 明细验证
        if (purchaseOrder.getDetails() == null || purchaseOrder.getDetails().isEmpty()) {
            throw new Throwable("订单明细不能为空");
        }
        
        // 3. 明细字段验证
        for (MM_PurchaseOrderDtl detail : purchaseOrder.getDetails()) {
            if (detail.getMaterialID() == null) {
                throw new Throwable("物料不能为空");
            }
            if (detail.getQuantity() == null || detail.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new Throwable("数量必须大于0");
            }
            if (detail.getPrice() == null || detail.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new Throwable("价格不能为负数");
            }
            if (detail.getUnitID() == null) {
                throw new Throwable("单位不能为空");
            }
        }
    }

    /**
     * 取消采购订单
     * 
     * @param purchaseOrder 采购订单实体
     * @throws Throwable
     */
    public void cancel(PurchaseOrder purchaseOrder) throws Throwable {
        logger.info("开始取消采购订单，单据编号：{}", purchaseOrder.getDocumentNumber());
        try {
            // 1. 验证可取消状态
            validateCancelStatus(purchaseOrder);
            
            // 2. 更新单据状态
            StatusFormula statusFormula = new StatusFormula(getMidContext());
            statusFormula.changeStatus(true, "MM_PurchaseOrder", "CancelPurchaseOrder", 
                purchaseOrder.getOID(), 2L, null);
                
            logger.info("采购订单已取消");
        } catch (Throwable e) {
            logger.error("取消采购订单失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 验证可取消状态
     */
    private void validateCancelStatus(PurchaseOrder purchaseOrder) throws Throwable {
        // 检查订单状态是否允许取消
        if (purchaseOrder.getStatus() != 1) { // 1=已保存状态
            throw new Throwable("当前状态不允许取消");
        }
    }

    /**
     * 审核采购订单
     * 
     * @param purchaseOrder 采购订单实体
     * @throws Throwable
     */
    public void approve(MM_PurchaseOrder purchaseOrder) throws Throwable {
        logger.info("开始审核采购订单，单据编号：{}", purchaseOrder.getDocumentNumber());
        try {
            // 1. 验证可审核状态
            validateApproveStatus(purchaseOrder);
            
            // 2. 执行业务检查
            validateBusinessRules(purchaseOrder);
            
            // 3. 更新单据状态
            StatusFormula statusFormula = new StatusFormula(getMidContext());
            statusFormula.changeStatus(true, "MM_PurchaseOrder", "ApprovePurchaseOrder", 
                purchaseOrder.getOID(), 3L, null);
                
            logger.info("采购订单审核完成");
        } catch (Throwable e) {
            logger.error("审核采购订单失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 验证可审核状态
     */
    private void validateApproveStatus(MM_PurchaseOrder purchaseOrder) throws Throwable {
        // 检查订单状态是否允许审核
        if (purchaseOrder.getStatus() != 1) { // 1=已保存状态
            throw new Throwable("当前状态不允许审核");
        }
    }

    /**
     * 验证业务规则
     */
    private void validateBusinessRules(MM_PurchaseOrder purchaseOrder) throws Throwable {
        // 1. 检查供应商有效性
        checkVendorValidity(purchaseOrder);
        
        // 2. 检查预算
        checkBudget(purchaseOrder);
    }
}