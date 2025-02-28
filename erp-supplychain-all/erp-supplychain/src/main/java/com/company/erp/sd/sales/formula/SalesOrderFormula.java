package com.company.erp.sd.sales.formula;

import com.company.erp.entity.SD_SalesOrder;
import com.company.erp.formula.StatusFormula;
import com.company.erp.function.DocumentFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

public class SalesOrderFormula extends EntityContextAction {
    private static final Logger logger = LoggerFactory.getLogger(SalesOrderFormula.class);

    public SalesOrderFormula(IMidContext context) throws Throwable {
        super(context);
    }

    /**
     * 保存销售订单
     * 
     * @param salesOrder 销售订单实体
     * @throws Throwable
     */
    public void save(SD_SalesOrder salesOrder) throws Throwable {
        logger.info("开始保存销售订单");
        try {
            // 1. 验证必填字段
            validateRequiredFields(salesOrder);
            
            // 2. 生成单据编号
            if (salesOrder.getDocumentNumber() == null || salesOrder.getDocumentNumber().isEmpty()) {
                String docNumber = DocumentFunction.generateDocumentNumber(getMidContext(), "SD", "SO");
                salesOrder.setDocumentNumber(docNumber);
            }
            
            // 3. 保存主表数据
            MidContextTool.saveObject(getMidContext(), salesOrder);
            
            // 4. 保存明细数据并计算汇总
            saveSalesOrderDetails(salesOrder);
            
            // 5. 更新单据状态
            StatusFormula statusFormula = new StatusFormula(getMidContext());
            statusFormula.changeStatus(true, "SD_SalesOrder", "SaveSalesOrder", 
                salesOrder.getOID(), 1L, null);
                
            logger.info("销售订单保存完成，单据编号：{}", salesOrder.getDocumentNumber());
        } catch (Throwable e) {
            logger.error("保存销售订单失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 取消销售订单
     * 
     * @param salesOrder 销售订单实体
     * @throws Throwable
     */
    public void cancel(SD_SalesOrder salesOrder) throws Throwable {
        logger.info("开始取消销售订单，单据编号：{}", salesOrder.getDocumentNumber());
        try {
            // 1. 验证可取消状态
            validateCancelStatus(salesOrder);
            
            // 2. 更新单据状态
            StatusFormula statusFormula = new StatusFormula(getMidContext());
            statusFormula.changeStatus(true, "SD_SalesOrder", "CancelSalesOrder", 
                salesOrder.getOID(), 2L, null);
                
            logger.info("销售订单已取消");
        } catch (Throwable e) {
            logger.error("取消销售订单失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 审核销售订单
     * 
     * @param salesOrder 销售订单实体
     * @throws Throwable
     */
    public void approve(SD_SalesOrder salesOrder) throws Throwable {
        logger.info("开始审核销售订单，单据编号：{}", salesOrder.getDocumentNumber());
        try {
            // 1. 验证可审核状态
            validateApproveStatus(salesOrder);
            
            // 2. 执行业务检查
            validateBusinessRules(salesOrder);
            
            // 3. 更新单据状态
            StatusFormula statusFormula = new StatusFormula(getMidContext());
            statusFormula.changeStatus(true, "SD_SalesOrder", "ApproveSalesOrder", 
                salesOrder.getOID(), 3L, null);
                
            logger.info("销售订单审核完成");
        } catch (Throwable e) {
            logger.error("审核销售订单失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 验证必填字段
     */
    private void validateRequiredFields(SD_SalesOrder salesOrder) throws Throwable {
        // 1. 基础字段验证
        if (salesOrder.getCustomerID() == null) {
            throw new Throwable("客户不能为空");
        }
        
        // 2. 明细验证
        if (salesOrder.getDetails() == null || salesOrder.getDetails().isEmpty()) {
            throw new Throwable("订单明细不能为空");
        }
        
        // 3. 明细字段验证
        for (SD_SalesOrderDtl detail : salesOrder.getDetails()) {
            if (detail.getMaterialID() == null) {
                throw new Throwable("物料不能为空");
            }
            if (detail.getQuantity() == null || detail.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new Throwable("数量必须大于0");
            }
            if (detail.getPrice() == null || detail.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new Throwable("单价必须大于0");
            }
            if (detail.getUnitID() == null) {
                throw new Throwable("单位不能为空");
            }
        }
    }

    /**
     * 验证可取消状态
     */
    private void validateCancelStatus(SD_SalesOrder salesOrder) throws Throwable {
        // 检查订单状态是否允许取消
        if (salesOrder.getStatus() != 1) { // 1=已保存状态
            throw new Throwable("当前状态不允许取消");
        }
    }

    /**
     * 验证可审核状态
     */
    private void validateApproveStatus(SD_SalesOrder salesOrder) throws Throwable {
        // 检查订单状态是否允许审核
        if (salesOrder.getStatus() != 1) { // 1=已保存状态
            throw new Throwable("当前状态不允许审核");
        }
    }

    /**
     * 验证业务规则
     */
    private void validateBusinessRules(SD_SalesOrder salesOrder) throws Throwable {
        // 1. 检查客户信用
        checkCustomerCredit(salesOrder);
        
        // 2. 检查库存
        checkInventory(salesOrder);
    }

    /**
     * 保存销售订单明细
     */
    private void saveSalesOrderDetails(SD_SalesOrder salesOrder) throws Throwable {
        BigDecimal totalAmount = BigDecimal.ZERO;
        int sequence = 1;
        
        for (SD_SalesOrderDtl detail : salesOrder.getDetails()) {
            // 1. 设置行号
            detail.setSequence(sequence++);
            
            // 2. 设置主表关联
            detail.setSOID(salesOrder.getOID());
            
            // 3. 计算行金额
            BigDecimal netAmount = detail.getQuantity().multiply(detail.getPrice());
            detail.setNetAmount(netAmount);
            
            // 4. 累计总金额
            totalAmount = totalAmount.add(netAmount);
            
            // 5. 保存明细
            MidContextTool.saveObject(getMidContext(), detail);
        }
        
        // 更新主表金额
        salesOrder.setTotalAmount(totalAmount);
        MidContextTool.saveObject(getMidContext(), salesOrder);
    }

    /**
     * 检查客户信用
     */
    private void checkCustomerCredit(SD_SalesOrder salesOrder) throws Throwable {
        // TODO: 实现客户信用检查逻辑
    }

    /**
     * 检查库存
     */
    private void checkInventory(SD_SalesOrder salesOrder) throws Throwable {
        // TODO: 实现库存检查逻辑
    }
} 