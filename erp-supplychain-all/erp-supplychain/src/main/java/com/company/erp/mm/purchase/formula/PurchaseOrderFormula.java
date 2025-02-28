package com.company.erp.mm.purchase.formula;

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