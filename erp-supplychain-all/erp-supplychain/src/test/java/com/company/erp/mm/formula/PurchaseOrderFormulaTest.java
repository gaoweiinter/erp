package com.company.erp.mm.formula;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.erp.function.IMidContext;
import com.company.erp.function.MidContextTool;
import com.company.erp.function.DocumentFunction;
import com.company.erp.formula.StatusFormula;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.company.erp.mm.entity.MM_PurchaseOrder;

/**
 * PurchaseOrderFormula测试类
 */
@ExtendWith(MockitoExtension.class)
public class PurchaseOrderFormulaTest {

    @Mock
    private IMidContext context;
    
    @Mock
    private DocumentFunction documentFunction;
    
    @Mock
    private StatusFormula statusFormula;
    
    @InjectMocks
    private PurchaseOrderFormula formula;
    
    private MM_PurchaseOrder validOrder;
    private MM_PurchaseOrder invalidOrder;
    private MM_PurchaseOrder cancelableOrder;
    private MM_PurchaseOrder nonCancelableOrder;
    
    @BeforeEach
    void setUp() throws Throwable {
        // 配置Mock对象
        when(context.getFunction(DocumentFunction.class)).thenReturn(documentFunction);
        when(documentFunction.generateDocumentNumber(any(), eq("MM"), eq("PO"))).thenReturn("PO-TEST-001");
        
        // 创建测试用例
        validOrder = createValidPurchaseOrder();
        invalidOrder = createInvalidPurchaseOrder();
        cancelableOrder = createCancelablePurchaseOrder();
        nonCancelableOrder = createNonCancelablePurchaseOrder();
    }
    
    /**
     * 测试保存有效采购订单
     */
    @Test
    void testSave_ValidOrder_Success() throws Throwable {
        // Given
        // 有效采购订单已在setUp中准备
        
        // When
        formula.save(validOrder);
        
        // Then
        // 验证DocumentFunction的调用
        verify(documentFunction).generateDocumentNumber(any(), eq("MM"), eq("PO"));
        
        // 验证保存对象的调用
        verify(context).saveObject(validOrder);
        
        // 验证订单编号已设置
        assertEquals("PO-TEST-001", validOrder.getDocumentNumber());
    }
    
    /**
     * 测试保存无效采购订单（缺少供应商）
     */
    @Test
    void testSave_MissingVendor_ThrowsException() {
        // Given
        // 无效采购订单（缺少供应商）已在setUp中准备
        
        // When & Then
        Throwable exception = assertThrows(Throwable.class, () -> {
            formula.save(invalidOrder);
        });
        
        // 验证异常消息
        assertEquals("供应商不能为空", exception.getMessage());
        
        // 验证保存对象未被调用
        verify(context, never()).saveObject(any());
    }
    
    /**
     * 测试保存无效采购订单（缺少明细）
     */
    @Test
    void testSave_MissingDetails_ThrowsException() {
        // Given
        MM_PurchaseOrder orderWithoutDetails = createOrderWithoutDetails();
        
        // When & Then
        Throwable exception = assertThrows(Throwable.class, () -> {
            formula.save(orderWithoutDetails);
        });
        
        // 验证异常消息
        assertEquals("订单明细不能为空", exception.getMessage());
        
        // 验证保存对象未被调用
        verify(context, never()).saveObject(any());
    }
    
    /**
     * 测试保存采购订单时已有单据编号
     */
    @Test
    void testSave_WithExistingDocNumber_Success() throws Throwable {
        // Given
        MM_PurchaseOrder orderWithDocNumber = createValidPurchaseOrder();
        orderWithDocNumber.setDocumentNumber("EXISTING-PO-001");
        
        // When
        formula.save(orderWithDocNumber);
        
        // Then
        // 验证DocumentFunction未被调用
        verify(documentFunction, never()).generateDocumentNumber(any(), anyString(), anyString());
        
        // 验证保存对象的调用
        verify(context).saveObject(orderWithDocNumber);
        
        // 验证订单编号未被修改
        assertEquals("EXISTING-PO-001", orderWithDocNumber.getDocumentNumber());
    }
    
    /**
     * 测试取消可取消状态的采购订单
     */
    @Test
    void testCancel_CancelableOrder_Success() throws Throwable {
        // Given
        // 可取消订单已在setUp中准备
        // 配置StatusFormula的模拟行为
        doNothing().when(statusFormula).changeStatus(eq(true), eq("MM_PurchaseOrder"), 
            eq("CancelPurchaseOrder"), anyLong(), eq(2L), isNull());
        
        when(context.getFunction(StatusFormula.class)).thenReturn(statusFormula);
        
        // When
        formula.cancel(cancelableOrder);
        
        // Then
        // 验证状态更新被调用
        verify(statusFormula).changeStatus(eq(true), eq("MM_PurchaseOrder"), 
            eq("CancelPurchaseOrder"), eq(cancelableOrder.getOID()), eq(2L), isNull());
    }
    
    /**
     * 测试取消不可取消状态的采购订单
     */
    @Test
    void testCancel_NonCancelableOrder_ThrowsException() {
        // Given
        // 不可取消订单已在setUp中准备
        
        // When & Then
        Throwable exception = assertThrows(Throwable.class, () -> {
            formula.cancel(nonCancelableOrder);
        });
        
        // 验证异常消息
        assertEquals("当前状态不允许取消", exception.getMessage());
        
        // 验证状态更新未被调用
        verify(statusFormula, never()).changeStatus(anyBoolean(), anyString(), anyString(), anyLong(), anyLong(), any());
    }
    
    /**
     * 创建有效的测试采购订单
     */
    private MM_PurchaseOrder createValidPurchaseOrder() {
        MM_PurchaseOrder order = new PurchaseOrder();
        order.setVendorID(1001L);
        
        // List<MM_PurchaseOrderDtl> details = new ArrayList<>();
        // PurchaseOrderDtl detail = new PurchaseOrderDtl();
        // detail.setMaterialID(2001L);
        // detail.setQuantity(new BigDecimal("10.0"));
        // detail.setPrice(new BigDecimal("100.0"));
        // details.add(detail);
        
        // order.setDetails(details);
        return order;
    }
    
    /**
     * 创建无效的测试采购订单（缺少供应商）
     */
    private MM_PurchaseOrder createInvalidPurchaseOrder() {
        MM_PurchaseOrder order = new PurchaseOrder();
        // 不设置供应商ID
        
        // List<PurchaseOrderDtl> details = new ArrayList<>();
        // PurchaseOrderDtl detail = new PurchaseOrderDtl();
        // detail.setMaterialID(2001L);
        // detail.setQuantity(new BigDecimal("10.0"));
        // detail.setPrice(new BigDecimal("100.0"));
        // details.add(detail);
        
        // order.setDetails(details);
        return order;
    }
    
    /**
     * 创建没有明细的采购订单
     */
    private MM_PurchaseOrder createOrderWithoutDetails() {
        MM_PurchaseOrder order = new PurchaseOrder();
        order.setVendorID(1001L);
        // 不添加明细
        return order;
    }
    
    /**
     * 创建可取消状态的采购订单
     */
    private MM_PurchaseOrder createCancelablePurchaseOrder() {
        MM_PurchaseOrder order = createValidPurchaseOrder();
        order.setOID(3001L);
        order.setDocumentNumber("PO-CANCEL-001");
        order.setStatus(1); // 已保存状态
        return order;
    }
    
    /**
     * 创建不可取消状态的采购订单
     */
    private MM_PurchaseOrder createNonCancelablePurchaseOrder() {
        MM_PurchaseOrder order = createValidPurchaseOrder();
        order.setOID(3002L);
        order.setDocumentNumber("PO-NON-CANCEL-001");
        order.setStatus(2); // 非可取消状态
        return order;
    }
}