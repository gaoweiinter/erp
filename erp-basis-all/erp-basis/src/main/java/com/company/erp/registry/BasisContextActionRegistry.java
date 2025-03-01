package com.company.erp.registry;

import com.company.erp.basis.documentNumber.DocumentNumber;
import com.company.erp.basis.documentNumber.DocumentNumberFormula;
import com.company.erp.function.DocumentFunction;

public class BasisContextActionRegistry implements IExtendContextActionRegistry{
    
    @override
    public Class<?>[] getContextActions(){
        Class<?>[] contextActions = new Class<?>[] {
            DocumentNumber.class,
            DocumentNumberFormula.class,
            DocumentFunction.class
        };
        return contextActions;
    }
}
