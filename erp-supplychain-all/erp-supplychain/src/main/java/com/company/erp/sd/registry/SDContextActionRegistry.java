package com.company.erp.sd.registry;

import com.company.erp.function.IExtendContextActionRegistry;
import com.company.erp.sd.formula.SalesOrderFormula;
import com.company.erp.sd.formula.SalesInvoiceFormula;

public class SDContextActionRegistry implements IExtendContextActionRegistry {
    @Override
    public Class<?>[] getContextActions() {
        Class<?>[] contextActions = new Class<?>[] {
            SalesOrderFormula.class,
            SalesInvoiceFormula.class
            // 其他SD模块的Formula类在此注册
        };
        return contextActions;
    }
}
