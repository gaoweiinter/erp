package com.company.erp.mm.common;

public class MMConstants {
    // 采购订单状态常量
    public static final int STATUS_CREATED = 1;   // 已创建
    public static final int STATUS_CANCELED = 2;  // 已取消
    public static final int STATUS_APPROVED = 3;  // 已审批
    
    // 采购订单类型常量
    public static final String TYPE_STANDARD = "ZNOR";  // 标准采购订单
    public static final String TYPE_SERVICE = "ZSER";   // 服务采购订单
}