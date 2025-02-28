package com.company.erp.sd.invoice.formula;

import com.company.erp.entity.SD_SalesInvoice;
import com.company.erp.formula.StatusFormula;
import com.company.erp.function.DocumentFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalesInvoiceFormula extends EntityContextAction {
    private static final Logger logger = LoggerFactory.getLogger(SalesInvoiceFormula.class);

    public SalesInvoiceFormula(IMidContext context) throws Throwable {
        super(context);
    }

    /**
     * 保存销售发票
     * 
     * @param salesInvoice 销售发票实体
     * @throws Throwable
     */
    public void save(SD_SalesInvoice salesInvoice) throws Throwable {
        logger.info("开始保存销售发票");
        try {
            // 1. 验证必填字段
            validateRequiredFields(salesInvoice);
            
            // 2. 生成单据编号
            if (salesInvoice.getDocumentNumber() == null || salesInvoice.getDocumentNumber().isEmpty()) {
                String docNumber = DocumentFunction.generateDocumentNumber(getMidContext(), "SD", "SI");
                salesInvoice.setDocumentNumber(docNumber);
            }
            
            // 3. 保存主表数据
            MidContextTool.saveObject(getMidContext(), salesInvoice);
            
            // 4. 更新单据状态
            StatusFormula statusFormula = new StatusFormula(getMidContext());
            statusFormula.changeStatus(true, "SD_SalesInvoice", "SaveSalesInvoice", 
                salesInvoice.getOID(), 1L, null);
                
            logger.info("销售发票保存完成，单据编号：{}", salesInvoice.getDocumentNumber());
        } catch (Throwable e) {
            logger.error("保存销售发票失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 取消销售发票
     * 
     * @param salesInvoice 销售发票实体
     * @throws Throwable
     */
    public void cancel(SD_SalesInvoice salesInvoice) throws Throwable {
        logger.info("开始取消销售发票，单据编号：{}", salesInvoice.getDocumentNumber());
        try {
            // 1. 验证可取消状态
            validateCancelStatus(salesInvoice);
            
            // 2. 更新单据状态
            StatusFormula statusFormula = new StatusFormula(getMidContext());
            statusFormula.changeStatus(true, "SD_SalesInvoice", "CancelSalesInvoice", 
                salesInvoice.getOID(), 2L, null);
                
            logger.info("销售发票已取消");
        } catch (Throwable e) {
            logger.error("取消销售发票失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 验证必填字段
     */
    private void validateRequiredFields(SD_SalesInvoice salesInvoice) throws Throwable {
        if (salesInvoice.getSalesOrderNumber() == null || salesInvoice.getSalesOrderNumber().isEmpty()) {
            throw new Throwable("销售订单编号不能为空");
        }
    }

    /**
     * 验证可取消状态
     */
    private void validateCancelStatus(SD_SalesInvoice salesInvoice) throws Throwable {
        if (salesInvoice.getStatus() != 1) { // 1=已保存状态
            throw new Throwable("当前状态不允许取消");
        }
    }
} 