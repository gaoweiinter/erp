package com.company.erp.sd.formula;

import com.company.erp.entity.SD_SalesOrder;
import com.company.erp.formula.StatusFormula;
import com.company.erp.function.DocumentFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalesOrderFormulaTest {
    
    @Mock
    private IMidContext context;
    
    @Mock
    private DocumentFunction documentFunction;
    
    @Mock
    private StatusFormula statusFormula;
    
    @InjectMocks
    private SalesOrderFormula formula;
    
    private SD_SalesOrder salesOrder;
    
    @BeforeEach
    void setUp() throws Throwable {
        salesOrder = new SD_SalesOrder()
            .setOID(1L)
            .setCustomerID(1001L)
            .setMaterialID(2001L)
            .setQuantity(new BigDecimal("10.000"))
            .setAmount(new BigDecimal("1000.00"))
            .setCurrencyID(3001L);
            
        when(context.getFunction(DocumentFunction.class)).thenReturn(documentFunction);
        when(documentFunction.generateDocumentNumber(context, "SD", "SO"))
            .thenReturn("SO-2024-001");
    }
    
    @Test
    void testSave_ValidOrder_Success() throws Throwable {
        // Given
        // 使用setUp中创建的有效订单
        
        // When
        formula.save(salesOrder);
        
        // Then
        verify(context, times(1)).saveObject(salesOrder);
        verify(statusFormula, times(1)).changeStatus(
            eq(true),
            eq("SD_SalesOrder"),
            eq("SaveSalesOrder"),
            eq(1L),
            eq(1L),
            isNull()
        );
    }
    
    @Test
    void testSave_NullCustomer_ThrowsException() throws Throwable {
        // Given
        salesOrder.setCustomerID(null);
        
        // When & Then
        Throwable exception = assertThrows(Throwable.class, () -> {
            formula.save(salesOrder);
        });
        assertEquals("客户不能为空", exception.getMessage());
    }
    
    @Test
    void testSave_GenerateDocumentNumber() throws Throwable {
        // Given
        salesOrder.setDocumentNumber(null);
        
        // When
        formula.save(salesOrder);
        
        // Then
        assertEquals("SO-2024-001", salesOrder.getDocumentNumber());
    }
    
    @Test
    void testCancel_ValidStatus_Success() throws Throwable {
        // Given
        salesOrder.setStatus(1); // 已保存状态
        
        // When
        formula.cancel(salesOrder);
        
        // Then
        verify(statusFormula, times(1)).changeStatus(
            eq(true),
            eq("SD_SalesOrder"),
            eq("CancelSalesOrder"),
            eq(1L),
            eq(2L),
            isNull()
        );
    }
    
    @Test
    void testCancel_InvalidStatus_ThrowsException() throws Throwable {
        // Given
        salesOrder.setStatus(2); // 已取消状态
        
        // When & Then
        Throwable exception = assertThrows(Throwable.class, () -> {
            formula.cancel(salesOrder);
        });
        assertEquals("当前状态不允许取消", exception.getMessage());
    }
    
    @Test
    void testApprove_ValidStatus_Success() throws Throwable {
        // Given
        salesOrder.setStatus(1); // 已保存状态
        
        // When
        formula.approve(salesOrder);
        
        // Then
        verify(statusFormula, times(1)).changeStatus(
            eq(true),
            eq("SD_SalesOrder"),
            eq("ApproveSalesOrder"),
            eq(1L),
            eq(3L),
            isNull()
        );
    }
    
    @Test
    void testApprove_InvalidStatus_ThrowsException() throws Throwable {
        // Given
        salesOrder.setStatus(2); // 已取消状态
        
        // When & Then
        Throwable exception = assertThrows(Throwable.class, () -> {
            formula.approve(salesOrder);
        });
        assertEquals("当前状态不允许审核", exception.getMessage());
    }
} 