public class SD_SalesOrder extends AbstractBillEntity {
    public static final String SD_SalesOrder = "SD_SalesOrder";

    public static final String Opt_BillSave = "BillSave";
    public static final String Opt_BillCancel = "BillCancel";

    public static final String OID = "OID";
    public Long getOID() throws Throwable {
        return value_Long("OID");
    }

    public SD_SalesOrder setOID(Long value) throws Throwable {
        setValue("OID", value);
        return this;
    }

    public static final String DocumentNumber = "DocumentNumber";
    public String getDocumentNumber() throws Throwable {
        return value_String("DocumentNumber");
    }

    public SD_SalesOrder setDocumentNumber(String value) throws Throwable {
        setValue("DocumentNumber", value);
        return this;
    }

    public static final String CustomerID = "CustomerID";
    public Long getCustomerID() throws Throwable {
        return value_Long("CustomerID");
    }

    public SD_SalesOrder setCustomerID(Long value) throws Throwable {
        setValue("CustomerID", value);
        return this;
    }

    public static final String MaterialID = "MaterialID";
    public Long getMaterialID() throws Throwable {
        return value_Long("MaterialID");
    }

    public SD_SalesOrder setMaterialID(Long value) throws Throwable {
        setValue("MaterialID", value);
        return this;
    }

    public static final String Quantity = "Quantity";
    public BigDecimal getQuantity() throws Throwable {
        return value_BigDecimal("Quantity");
    }

    public SD_SalesOrder setQuantity(BigDecimal value) throws Throwable {
        setValue("Quantity", value);
        return this;
    }

    public static final String Amount = "Amount";
    public BigDecimal getAmount() throws Throwable {
        return value_BigDecimal("Amount");
    }

    public SD_SalesOrder setAmount(BigDecimal value) throws Throwable {
        setValue("Amount", value);
        return this;
    }

    public static final String CurrencyID = "CurrencyID";
    public Long getCurrencyID() throws Throwable {
        return value_Long("CurrencyID");
    }

    public SD_SalesOrder setCurrencyID(Long value) throws Throwable {
        setValue("CurrencyID", value);
        return this;
    }
} 