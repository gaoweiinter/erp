public class MM_PurchaseOrder extends AbstractBillEntity{
    public static final String MM_PurchaseOrder = "MM_PurchaseOrder";

    public static final String Opt_BillSave = "BillSave";
    public static final String Opt_BillCancel = "BillCancel";

    public static final String OID = "OID";
    public Long getOID(Long oid) throws Throwable{
        return value_Long("OID"， oid);
    }

    public MM_PurchaseOrder setOID(Long oid, Long value) throws Throwable{
        setValue("OID", oid, value);
        return this;
    }

    public static final String SOID = "SOID";
    public Long getSOID(Long soid) throws Throwable{
        return value_Long("SOID"， soid);
    }

    public MM_PurchaseOrder setSOID(Long soid, Long value) throws Throwable{
        setValue("SOID", soid, value);
        return this;
    }

    public static final String POID = "POID";
    public Long getPOID(Long poid) throws Throwable{
        return value_Long("POID"， poid);
    }

    public MM_PurchaseOrder setPOID(Long poid, Long value) throws Throwable{
        setValue("POID", poid, value);
        return this;
    }

    public static final String ShorText = "ShortText";
    public Long getShortText(Long oid) throws Throwable{
        return value_String("ShortText"， poid);
    }

    public MM_PurchaseOrder setShortText(Long oid, String value) throws Throwable{
        setValue("ShortText", oid, value);
        return this;
    }

    public static final String VendorID = "VendorID";
    public Long getVendorID() throws Throwable{
        return value_Long("VendorID");
    }

    public MM_PurchaseOrder setVendorID(Long value) throws Throwable{
        setValue("VendorID", value);
        return this;
    }

    public static final String DocumentNumber = "DocumentNumber";
    public Long getDocumentNumber() throws Throwable{
        return value_String("DocumentNumber");
    }

    public MM_PurchaseOrder setDocumentNumber(String value) throws Throwable{
        setValue("DocumentNumber", value);
        return this;
    }

    public static final String CurrencyID = "CurrencyID";
    public Long getCurrencyID() throws Throwable{
        return value_Long("CurrencyID");
    }

    public MM_PurchaseOrder setCurrencyID(Long value) throws Throwable{
        setValue("CurrencyID", value);
        return this;
    }

    public static final String PlantID = "PlantID";
    public Long getPlantID(Long oid) throws Throwable{
        return value_Long("PlantID", oid);
    }

    public MM_PurchaseOrder setCurrencyID(Long oid, Long value) throws Throwable{
        setValue("PlantID", oid, value);
        return this;
    }

    public static final String PriceQuantity = "PriceQuantity";
    public Long getPriceQuantity(Long oid) throws Throwable{
        return value_BigDecimal("PriceQuantity", oid);
    }

    public MM_PurchaseOrder setPriceQuantity(Long oid, BigDecimal value) throws Throwable{
        setValue("PriceQuantity", oid, value);
        return this;
    }

    public static final String Price = "Price";
    public Long getPrice(Long oid) throws Throwable{
        return value_BigDecimal("Price", oid);
    }

    public MM_PurchaseOrder setPrice(Long oid, BigDecimal value) throws Throwable{
        setValue("Price", oid, value);
        return this;
    }

    public static final String PriceUnitID = "PriceUnitID";
    public Long getPriceUnitID(Long oid) throws Throwable{
        return value_Long("PriceUnitID", oid);
    }

    public MM_PurchaseOrder setPriceUnitID(Long oid, Long value) throws Throwable{
        setValue("PriceUnitID", oid, value);
        return this;
    }

    public static final String Quantity = "Quantity";
    public BigDecimal getQuantity(Long oid) throws Throwable{
        return value_BigDecimal("Quantity", oid);
    }

    public MM_PurchaseOrder setQuantity(Long oid, BigDecimal value) throws Throwable{
        setValue("Quantity", oid, value);
        return this;
    }

    public static final String UnitID = "UnitID";
    public Long getUnitID(Long oid) throws Throwable{
        return value_Long("UnitID", oid);
    }

    public MM_PurchaseOrder setUnitID(Long oid, Long value) throws Throwable{
        setValue("UnitID", oid, value);
        return this;
    }

    public static final String NetAmount = "NetAmount";
    public BigDecimal getNetAmount(Long oid) throws Throwable{
        return value_BigDecimal("NetAmount", oid);
    }

    public MM_PurchaseOrder setNetAmount(Long oid, BigDecimal value) throws Throwable{
        setValue("NetAmount", oid, value);
        return this;
    }

    public static final String NetAmount = "TaxAmount";
    public BigDecimal getTaxAmount(Long oid) throws Throwable{
        return value_BigDecimal("TaxAmount", oid);
    }

    public MM_PurchaseOrder setTaxAmount(Long oid, BigDecimal value) throws Throwable{
        setValue("TaxAmount", oid, value);
        return this;
    }
}
