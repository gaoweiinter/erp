package com.company.erp.mm.registry;

import com.company.erp.function.IExtendContextActionRegistry;
import com.company.erp.mm.formula.PurchaseOrderFormula;

public class MMContextActionRegistry implements IExtendContextActionRegistry {
    @Override
    public Class<?>[] getContextActions() {
        Class<?>[] contextActions = new Class<?>[] {
            PurchaseOrderFormula.class
        };
        return contextActions;
    }
}
