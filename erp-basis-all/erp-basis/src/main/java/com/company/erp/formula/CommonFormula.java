package main.java.com.company.erp.formula;

public class CommonFormula extends EntityContextAction {
    public static boolean CVFormulaMetaEnabled = false;
    public static boolean CVFormulaBizEnabled= false;

    public static void setCVFormulaEnabled(String cVformulaEnabled){
        if ("all".equalsIgnoreCase(cVformulaEnabled) ||"true".equals(cVformulaEnabled) {
            CVFormulaMetaEnabled = true;
            CVFormulaBizEnabled = true;
        } else if("biz".equals(cVformulaEnabled)) {
            CVFormulaBizEnabled = true;
        }else if("meta".equals(cVformulaEnabled)) {
            CVFormulaMetaEnabled = true;
        }
    }

    
    
}
