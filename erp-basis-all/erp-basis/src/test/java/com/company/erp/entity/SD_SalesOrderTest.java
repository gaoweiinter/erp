package com.company.erp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class SD_SalesOrderTest {
    
    private SD_SalesOrder salesOrder;
    
    @BeforeEach
    void setUp() throws Throwable {
        salesOrder = new SD_SalesOrder();
    }
    
    @Test
    void testSetAndGetDocumentNumber() throws Throwable {
        // Given
        String documentNumber = "SO-2024-001";
        
        // When
        salesOrder.setDocumentNumber(documentNumber);
        
        // Then
        assertEquals(documentNumber, salesOrder.getDocumentNumber());
    }
    
    @Test
    void testSetAndGetCustomerID() throws Throwable {
        // Given
        Long customerID = 1001L;
        
        // When
        salesOrder.setCustomerID(customerID);
        
        // Then
        assertEquals(customerID, salesOrder.getCustomerID());
    }
    
    @Test
    void testSetAndGetMaterialID() throws Throwable {
        // Given
        Long materialID = 2001L;
        
        // When
        salesOrder.setMaterialID(materialID);
        
        // Then
        assertEquals(materialID, salesOrder.getMaterialID());
    }
    
    @Test
    void testSetAndGetQuantity() throws Throwable {
        // Given
        BigDecimal quantity = new BigDecimal("10.000");
        
        // When
        salesOrder.setQuantity(quantity);
        
        // Then
        assertEquals(quantity, salesOrder.getQuantity());
    }
    
    @Test
    void testSetAndGetAmount() throws Throwable {
        // Given
        BigDecimal amount = new BigDecimal("1000.00");
        
        // When
        salesOrder.setAmount(amount);
        
        // Then
        assertEquals(amount, salesOrder.getAmount());
    }
    
    @Test
    void testSetAndGetCurrencyID() throws Throwable {
        // Given
        Long currencyID = 3001L;
        
        // When
        salesOrder.setCurrencyID(currencyID);
        
        // Then
        assertEquals(currencyID, salesOrder.getCurrencyID());
    }
    
    @Test
    void testChainedSetters() throws Throwable {
        // Given
        String documentNumber = "SO-2024-001";
        Long customerID = 1001L;
        Long materialID = 2001L;
        
        // When
        salesOrder
            .setDocumentNumber(documentNumber)
            .setCustomerID(customerID)
            .setMaterialID(materialID);
        
        // Then
        assertEquals(documentNumber, salesOrder.getDocumentNumber());
        assertEquals(customerID, salesOrder.getCustomerID());
        assertEquals(materialID, salesOrder.getMaterialID());
    }
} 