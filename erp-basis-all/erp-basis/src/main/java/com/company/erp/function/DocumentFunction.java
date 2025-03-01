package com.company.erp.function;

public class DocumentFunction extends EntityContextAction{

    @FunctionGetValueScope(FunctionGetValueScopeType.Document)
    public void SaveObject() throws Throwable{
        MidContextTool.saveObject(getMidContext(), getDocument());
    }
}
