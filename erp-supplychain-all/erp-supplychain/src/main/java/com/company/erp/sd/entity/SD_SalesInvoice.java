package com.company.erp.entity;

public class SD_SalesInvoice extends AbstractBillEntity {
    public static final String SD_SalesInvoice = "SD_SalesInvoice";
    
    public static final String Opt_BillSave = "BillSave";
    public static final String Opt_BillCancel = "BillCancel";

    public static final String OID = "OID";
    public Long getOID() throws Throwable {
        return value_Long("OID");
    }

    public SD_SalesInvoice setOID(Long value) throws Throwable {
        setValue("OID", value);
        return this;
    }

    public static final String DocumentNumber = "DocumentNumber";
    public String getDocumentNumber() throws Throwable {
        return value_String("DocumentNumber");
    }

    public SD_SalesInvoice setDocumentNumber(String value) throws Throwable {
        setValue("DocumentNumber", value);
        return this;
    }

    public static final String SalesOrderNumber = "SalesOrderNumber";
    public String getSalesOrderNumber() throws Throwable {
        return value_String("SalesOrderNumber");
    }

    public SD_SalesInvoice setSalesOrderNumber(String value) throws Throwable {
        setValue("SalesOrderNumber", value);
        return this;
    }
} 