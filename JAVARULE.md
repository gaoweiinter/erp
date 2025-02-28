# ERP项目Java开发规范

## 1. 模块化架构规范

### 1.1 模块标识规则
- 采用2-4个大写英文字符作为模块标识
- 模块与代码包路径强绑定：`com.company.erp.{模块标识小写}`

### 1.2 核心模块清单
| 模块标识 | 模块名称 | 英文全称 |
|----------|----------|-----------|
| BASIS | 基础模块 | Basis |
| FI | 财务会计 | Finance |
| CO | 管理会计 | Controlling |
| AM | 资产管理 | Asset Management |
| FM | 预算管理 | Fund Management |
| RE | 不动产管理 | Real Estate Management |
| MM | 物料管理 | Material Management |
| QM | 质量管理 | Quality Management |
| WM | 仓储管理 | Warehouse Management |
| SD | 销售和分销 | Sales and Distribution |
| PP | 生产计划 | Production Planning |
| PM | 设备管理 | Plant Maintenance |
| PS | 项目管理 | Project System |
| HCM | 人力资本管理 | Human Capital Management |

### 1.3 分层架构设计
```
erp项目
├── erp-basis-all/          # 基础层
│   └── erp-basis/
│       ├── entity/         # 基础实体定义
│       ├── function/       # 核心功能
│       ├── registry/       # 注册管理
│       ├── formula/        # 基础业务逻辑
│       └── utils/          # 工具类
│
├── erp-supplychain-all/    # 业务层
│   └── erp-supplychain/
│       ├── mm/            # 物料管理
│       │   ├── entity/    # 物料管理实体类
│       │   └── formula/   # 物料管理业务逻辑
│       └── sd/            # 销售分销
│           ├── entity/    # 销售分销实体类
│           └── formula/   # 销售分销业务逻辑
│
└── erp-solution-all/       # 配置层
    └── solutions/
        ├── erp/           # ERP配置
        │   ├── mmconfig/  # 物料管理配置
        │   └── basisconfig/# 基础模块配置
        └── billentity-erp/ # 单据实体配置
```

### 1.4 层级职责
#### 1.4.1 基础层（erp-basis-all）
- 提供基础实体定义
- 提供核心功能支持
- 提供通用工具类
- 定义基础业务规则
- 管理系统注册机制

#### 1.4.2 业务层（erp-supplychain-all）
- 实现具体业务逻辑
- 定义业务实体类
- 物料管理(MM)实现
- 销售分销(SD)实现
- 依赖基础层的功能
- 实现业务流程处理

#### 1.4.3 配置层（erp-solution-all）
- 提供配置定义
- 业务规则配置
- 界面布局配置
- 数据对象配置
- 解决方案定制

## 2. 包结构规范

### 2.1 基本包结构
```
com.company.erp.{module}
├── entity/        # 模块实体类
├── formula/       # 业务逻辑处理类
├── registry/      # 注册器类
└── common/        # 公共组件
```

### 2.2 命名规范
- 包名必须全小写
- 模块包必须遵循 `com.company.erp.{模块标识小写}` 格式
- 实体类必须放在对应业务模块的entity包中（如：`com.company.erp.mm.entity.PurchaseOrder`）

## 3. 类命名规范

### 3.1 实体类（Entity）
- 使用英文名称作为类名
- 必须放在对应业务模块的entity包下
- 示例：`com.company.erp.mm.entity.PurchaseOrder`, `com.company.erp.sd.entity.SalesOrder`

### 3.2 业务逻辑类（Formula）
- 命名规则：`实体名 + Formula`
- 必须继承 `EntityContextAction`
- 示例：`PurchaseOrderFormula`, `MaterialFormula`

### 3.3 注册器类
- 命名规则：`模块标识 + ContextActionRegistry`
- 必须实现 `IExtendContextActionRegistry`
- 示例：`MMContextActionRegistry`, `BasisContextActionRegistry`

## 4. 数据库表命名规范

### 4.1 表名前缀规则
- 全局表前缀：`D1_`（通用字典）
- 系统表前缀：`EGS_`（全局系统表）
- 模块表前缀：`E{模块标识}_`
- 缓存表前缀：`EG{模块标识}_`

### 4.2 表名示例
| 类型 | 表名示例 | 说明 |
|------|-----------|------|
| 全局字典表 | D1_Material | 基础模块通用字典 |
| 全局系统表 | EGS_ConditionRecord | 全局配置表 |
| 模块缓存表 | EGMM_Characteristic | 物料管理模块的全局缓存表 |
| 模块单据表 | EMM_PurchaseOrderHead | 采购订单头表 |
| 项目专用表 | ESD_KLNY_BuyoutSaleOrder | 昆仑能源项目的销售单据表 |

## 5. 代码开发规范

### 5.1 基本原则
- 遵循SOLID原则
- 遵循DRY原则（Don't Repeat Yourself）
- 遵循KISS原则（Keep It Simple, Stupid）
- 遵循YAGNI原则（You Aren't Gonna Need It）

### 5.2 Formula类开发规范
- 必须继承 `EntityContextAction`
- 业务逻辑必须在Formula类中实现
- 每个Formula类专注于单一业务领域

### 5.3 实体类开发规范
- getter/setter方法需要支持链式调用
- 必须处理异常（throws Throwable）
- 字段常量必须定义为public static final

### 5.4 上下文管理规范
- 必须通过ContextActionRegistry注册业务动作
- 每个模块必须有自己的ContextActionRegistry实现

## 6. 技术栈规范

### 6.1 开发环境
- Java 8
- Spring Boot 3
- Maven

### 6.2 依赖管理
- Spring Web
- Lombok
- DRUID
- JDBC
- MyBatis

## 7. 代码质量控制

### 7.1 代码审查要点
- 命名规范符合性
- 异常处理完整性
- 业务逻辑合理性
- 代码可维护性

### 7.2 文档要求
- 类级别的JavaDoc注释
- 关键方法的注释说明
- 复杂业务逻辑的流程说明

## 8. 最佳实践

### 8.1 业务逻辑处理
- 使用Formula模式处理业务逻辑
- 状态管理通过StatusFormula处理
- 文档编号通过DocumentFunction处理

### 8.2 异常处理
- 统一使用throws Throwable
- 异常信息需要包含业务上下文
- 关键操作需要日志记录

### 8.3 性能考虑
- 合理使用缓存表
- 批量操作优化
- 大数据量场景性能优化 

## 9. 分层开发规范

### 9.1 基础层开发规范
- 严格的接口定义
- 高内聚低耦合原则
- 通用功能抽象
- 核心功能实现
- 避免业务逻辑耦合

### 9.2 业务层开发规范
- 遵循基础层接口规范
- 实现具体业务逻辑
- 模块化业务实现
- 业务规则封装
- 保持业务逻辑独立性

### 9.3 配置层开发规范
- 标准配置文件格式
- 模块化配置管理
- 配置文件版本控制
- 解决方案定制规范
- XML配置规范：
  ```xml
  <!-- 配置文件结构示例 -->
  <config>
    <dataObject>
      <!-- 数据对象定义 -->
    </dataObject>
    <form>
      <!-- 表单定义 -->
    </form>
  </config>
  ```

### 9.4 跨层调用规范
- 遵循依赖方向：配置层 → 业务层 → 基础层
- 禁止反向依赖
- 使用标准接口通信
- 避免跨层直接访问
- 保持层级边界清晰

### 9.5 扩展开发规范
#### 9.5.1 基础功能扩展
- 在erp-basis-all中开发
- 提供标准接口
- 更新相关文档
- 保证向后兼容

#### 9.5.2 业务功能扩展
- 在erp-supplychain-all中开发
- 遵循已有模式
- 复用基础功能
- 保持业务隔离

#### 9.5.3 解决方案扩展
- 在erp-solution-all中配置
- 遵循配置规范
- 避免重复配置
- 维护配置文档

## 10. 模块间通信规范

### 10.1 通信原则
- 使用标准接口
- 避免直接依赖
- 遵循最小知道原则
- 使用事件机制解耦

### 10.2 数据流转规范
```
配置层(erp-solution-all)
      ↓ 配置解析
业务层(erp-supplychain-all)
      ↓ 功能调用
基础层(erp-basis-all)
```

### 10.3 接口规范
- 定义清晰的接口契约
- 使用标准的数据传输对象
- 处理异常传递
- 文档化接口行为

### 10.4 事件处理规范
- 使用事件总线机制
- 定义标准事件接口
- 实现异步处理
- 保证事件可追踪 

## 11. XML配置规范

### 11.1 DataObject配置规范

#### 11.1.1 基本结构
```xml
<DataObject Key="模块_实体" Caption="实体名称" PrimaryTableKey="E模块_实体Head" PrimaryType="Entity" Version="1">
    <TableCollection>
        <!-- 主表定义 -->
        <Table Key="E模块_实体Head" Caption="实体头表" IndexPrefix="E模块_实体Head">
            <!-- 系统字段 -->
            <Column Key="OID" Caption="对象标识" DataType="Long" />
            <Column Key="SOID" Caption="主对象标识" DataType="Long" />
            <Column Key="POID" Caption="父对象标识" DataType="Long" />
            <!-- 业务字段 -->
            <Column Key="字段Key" Caption="字段名称" DataType="数据类型" Length="长度" DefaultValue="默认值"/>
        </Table>
        <!-- 明细表定义 -->
        <Table Key="E模块_实体Dtl" TableMode="Detail" Caption="实体明细" IndexPrefix="E模块_实体Dtl">
            <!-- 明细表字段 -->
        </Table>
    </TableCollection>
</DataObject>
```

#### 11.1.2 命名规范
- Key属性：`模块_实体`（例如：MM_PurchaseOrder）
- 表Key：`E模块_实体Head/Dtl`（例如：EMM_PurchaseOrderHead）
- 字段Key：采用PascalCase命名法

#### 11.1.3 数据类型规范
- 字符串：DataType="Varchar" Length="长度"
- 整数：DataType="Integer"
- 长整数：DataType="Long"
- 数量小数：DataType="Numeric" Precision="16" Scale="3"
- 金额小数：DataType="Numeric" Precision="16" Scale="2"
- 日期时间：DataType="DateTime"

#### 11.1.4 系统字段规范
必须包含以下系统字段：
```xml
<Column Key="OID" Caption="对象标识" DataType="Long" />
<Column Key="SOID" Caption="主对象标识" DataType="Long" />
<Column Key="POID" Caption="父对象标识" DataType="Long" />
<Column Key="VERID" Caption="对象版本" DataType="Integer" />
<Column Key="Status" Caption="状态" DataType="Integer" />
<Column Key="Creator" Caption="制单人" DataType="Long" />
<Column Key="CreateTime" Caption="制单时间" DataType="DateTime" />
<Column Key="Modifier" Caption="修改人" DataType="Long" />
<Column Key="ModifyTime" Caption="修改时间" DataType="DateTime" />
```

### 11.2 Form配置规范

#### 11.2.1 基本结构
```xml
<Form Key="模块_实体" Caption="表单标题" FormType="Entity">
    <DataSource RefObjectKey="模块_实体" />
    <OperationCollection>
        <!-- 操作定义 -->
    </OperationCollection>
    <Body>
        <Block>
            <!-- 布局定义 -->
        </Block>
        <OnLoad>
            <!-- 加载脚本 -->
        </OnLoad>
    </Body>
    <MacroCollection>
        <!-- 宏定义 -->
    </MacroCollection>
</Form>
```

#### 11.2.2 操作定义规范
```xml
<OperationCollection>
    <Operation Key="操作Key" Caption="操作名称">
        <Action>
            <![CDATA[宏名称;]]>
        </Action>
    </Operation>
</OperationCollection>
```

#### 11.2.3 布局规范
- 使用FlexFlowLayoutPanel作为根布局
- 使用GridLayoutPanel进行表单布局
- 使用SplitPanel进行区域分割
- 使用Grid展示明细数据

#### 11.2.4 控件规范
```xml
<!-- 文本框 -->
<TextEditor Key="字段Key" Caption="显示名" BuddyKey="标签Key">
    <DataBinding TableKey="表Key" ColumnKey="字段Key" Required="true/false"/>
</TextEditor>

<!-- 下拉框 -->
<Dict Key="字段Key" Caption="显示名" BuddyKey="标签Key" ItemKey="字典类型">
    <DataBinding TableKey="表Key" ColumnKey="字段Key" Required="true/false"/>
    <ItemFilter ItemKey="过滤条件"/>
</Dict>

<!-- 表格列 -->
<GridColumn Key="字段Key" Caption="显示名" Width="宽度" />
```

#### 11.2.5 宏定义规范
```xml
<MacroCollection>
    <Macro Key="宏Key">
        <![CDATA[com.company.erp.模块.业务.formula.实体Formula.方法名(); ]]>
    </Macro>
</MacroCollection>
```

### 11.3 配置文件组织规范

#### 11.3.1 目录结构
```
solutions/
└── erp/
    └── 模块config/
        ├── DataObject/    # 数据对象定义
        │   └── 模块_实体.xml
        └── Form/          # 表单定义
            └── Bill/      # 单据表单
                └── 模块_实体.xml
```

#### 11.3.2 文件命名规范
- DataObject文件：`模块_实体.xml`
- Form文件：`模块_实体.xml`

#### 11.3.3 引用规范
- 实体间引用使用refObject属性
- 表单引用数据对象使用RefObjectKey属性
- 字典引用使用ItemKey属性

### 11.4 性能优化规范

#### 11.4.1 数据对象优化
- 合理设置字段长度
- 必要时添加索引定义
- 大字段使用单独的表存储

#### 11.4.2 表单优化
- 合理使用延迟加载
- 避免过多的动态计算
- 适当使用缓存机制

#### 11.4.3 查询优化
- 添加必要的查询条件
- 使用合适的索引
- 避免大数据量查询 

## 12. 业务模块开发规范

### 12.1 业务模块目录结构
```
com.company.erp.{module}/
├── entity/            # 模块实体类目录
│   ├── PurchaseOrder.java
│   └── Material.java
├── registry/          # 模块注册器
├── {business}/        # 业务分类目录
│   └── formula/       # 业务公式目录
└── common/            # 模块公共组件
```

### 12.2 业务公式（Formula）开发规范

#### 12.2.1 基本结构
```java
public class 业务Formula extends EntityContextAction {
    private static final Logger logger = LoggerFactory.getLogger(业务Formula.class);

    public 业务Formula(IMidContext context) throws Throwable {
        super(context);
    }

    // 业务方法实现
}
```

#### 12.2.2 业务方法规范
- 必须包含日志记录
- 必须进行参数验证
- 必须处理异常
- 必须记录关键操作
- 必须维护单据状态

#### 12.2.3 状态管理规范
```java
// 状态变更示例
StatusFormula statusFormula = new StatusFormula(getMidContext());
statusFormula.changeStatus(
    true,                   // 是否更新文档
    "业务对象类型",          // 如：SD_SalesOrder
    "业务事务代码",          // 如：SaveSalesOrder
    objectID,              // 对象ID
    statusProfileID,       // 状态配置ID
    statusInfo             // 状态信息
);
```

#### 12.2.4 单据编号规范
```java
// 生成单据编号
String docNumber = DocumentFunction.generateDocumentNumber(
    getMidContext(),
    "模块代码",     // 如：SD
    "单据类型"      // 如：SO
);
```

### 12.3 业务对象处理规范

#### 12.3.1 主表处理
- 必须验证必填字段
- 必须生成单据编号
- 必须记录创建/修改信息
- 必须维护版本信息

#### 12.3.2 明细表处理
- 必须维护行号
- 必须关联主表ID
- 必须验证业务规则
- 必须计算汇总数据

#### 12.3.3 数据验证规范
```java
private void validateRequiredFields(业务Entity entity) throws Throwable {
    // 1. 基础字段验证
    if (entity.getField() == null) {
        throw new Throwable("字段不能为空");
    }
    
    // 2. 业务规则验证
    if (entity.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
        throw new Throwable("金额必须大于0");
    }
    
    // 3. 关联数据验证
    if (!validateReference(entity.getRefID())) {
        throw new Throwable("关联数据无效");
    }
}
```

### 12.4 业务流程规范

#### 12.4.1 保存流程
1. 记录开始日志
2. 验证必填字段
3. 生成单据编号
4. 保存主表数据
5. 保存明细数据
6. 更新单据状态
7. 记录完成日志

#### 12.4.2 取消流程
1. 记录开始日志
2. 验证可取消状态
3. 更新单据状态
4. 记录完成日志

#### 12.4.3 审核流程
1. 记录开始日志
2. 验证可审核状态
3. 执行业务检查
4. 更新单据状态
5. 记录完成日志 

### 12.5 模块注册器规范

#### 12.5.1 基本结构
```java
public class 模块ContextActionRegistry implements IExtendContextActionRegistry {
    @Override
    public Class<?>[] getContextActions() {
        Class<?>[] contextActions = new Class<?>[]{
            业务Formula.class,
            // ... 其他Formula类
        };
        return contextActions;
    }
}
```

#### 12.5.2 注册规范
- 每个模块必须有一个对应的ContextActionRegistry
- 命名规则：`模块标识ContextActionRegistry`
- 必须实现`IExtendContextActionRegistry`接口
- 必须注册模块内所有Formula类

#### 12.5.3 注册流程
1. 在模块的registry包中创建注册器类
2. 实现getContextActions方法
3. 返回所有Formula类的Class对象数组
4. 确保所有新增的Formula类都被注册

#### 12.5.4 维护规范
- 新增Formula类时必须同步更新注册器
- 删除Formula类时必须同步更新注册器
- 确保注册的Formula类都存在且可用
- 避免重复注册相同的Formula类 

#### 12.5.5 SPI服务注册规范

##### 12.5.5.1 目录结构
```
resources/
└── META-INF/
    └── services/
        └── com.company.erp.function.IExtendContextActionRegistry
```

##### 12.5.5.2 注册文件规范
- 文件名必须是接口的全限定名
- 每行一个实现类的全限定名
- 不允许有空行
- 不允许以#开头的注释行
- 示例：
```
com.company.erp.mm.registry.MMContextActionRegistry
com.company.erp.sd.registry.SDContextActionRegistry
```

##### 12.5.5.3 注册流程
1. 在resources/META-INF/services/目录下创建接口全限定名文件
2. 在文件中添加实现类的全限定名
3. 确保实现类存在且实现了对应接口
4. 确保类名完全匹配且大小写正确

##### 12.5.5.4 维护规范
- 新增模块时必须添加对应的注册器
- 删除模块时必须移除对应的注册器
- 确保注册器类名正确
- 避免重复注册相同的注册器 
```

## 13. 单元测试规范

### 13.1 测试目录结构
```
项目根目录/
├── src/
│   ├── main/
│   │   └── java/
│   └── test/
│       └── java/
│           └── com/
│               └── company/
│                   └── erp/
│                       ├── entity/
│                       ├── formula/
│                       └── function/
```

### 13.2 测试类命名规范
- 测试类名必须以Test结尾
- 测试类必须与被测试类在相同包路径下
- 示例：`SalesOrderFormulaTest.java` 测试 `SalesOrderFormula.java`

### 13.3 测试方法规范

#### 13.3.1 命名规范
```java
public class SalesOrderFormulaTest {
    // 命名格式：test{方法名}_{测试场景}
    @Test
    public void testSave_ValidOrder_Success() {}
    
    @Test
    public void testSave_InvalidOrder_ThrowsException() {}
    
    @Test
    public void testCancel_ValidStatus_Success() {}
}
```

#### 13.3.2 测试方法结构
```java
@Test
public void testMethodName() {
    // 1. 准备测试数据
    // Given
    
    // 2. 执行测试方法
    // When
    
    // 3. 验证测试结果
    // Then
    
    // 4. 清理测试数据（如果需要）
    // Cleanup
}
```

### 13.4 测试框架规范

#### 13.4.1 必要依赖
```xml
<dependencies>
    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.8.0</version>
        <scope>test</scope>
    </dependency>
    
    <!-- Mockito -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>3.12.4</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### 13.4.2 测试注解使用
```java
@ExtendWith(MockitoExtension.class)
public class FormulaTest {
    @Mock
    private IMidContext context;
    
    @InjectMocks
    private TestFormula formula;
    
    @BeforeEach
    void setUp() {
        // 测试初始化
    }
    
    @AfterEach
    void tearDown() {
        // 测试清理
    }
}
```

### 13.5 测试覆盖要求

#### 13.5.1 必须测试场景
1. 业务方法正常流程
2. 参数验证异常
3. 业务规则验证
4. 状态转换验证
5. 数据保存验证

#### 13.5.2 覆盖率要求
- 类覆盖率：100%
- 方法覆盖率：≥85%
- 行覆盖率：≥80%
- 分支覆盖率：≥75%

### 13.6 测试数据管理

#### 13.6.1 测试数据准备
```java
public class TestDataBuilder {
    public static SD_SalesOrder createValidSalesOrder() {
        SD_SalesOrder order = new SD_SalesOrder();
        // 设置测试数据
        return order;
    }
    
    public static SD_SalesOrder createInvalidSalesOrder() {
        // 创建无效订单数据
        return order;
    }
}
```

#### 13.6.2 Mock对象使用规范
```java
// 1. 设置Mock行为
when(context.getFunction(DocumentFunction.class))
    .thenReturn(documentFunction);
    
// 2. 验证Mock调用
verify(context, times(1)).saveObject(any(SD_SalesOrder.class));
```

### 13.7 异常测试规范

#### 13.7.1 预期异常测试
```java
@Test
void testSave_InvalidData_ThrowsException() {
    // Given
    SD_SalesOrder invalidOrder = TestDataBuilder.createInvalidSalesOrder();
    
    // When & Then
    assertThrows(Throwable.class, () -> {
        formula.save(invalidOrder);
    });
}
```

#### 13.7.2 异常消息验证
```java
@Test
void testSave_NullCustomer_ThrowsException() {
    // Given
    SD_SalesOrder order = TestDataBuilder.createOrderWithoutCustomer();
    
    // When
    Throwable exception = assertThrows(Throwable.class, () -> {
        formula.save(order);
    });
    
    // Then
    assertEquals("客户不能为空", exception.getMessage());
}
```

### 13.8 测试代码质量规范

#### 13.8.1 测试代码组织
1. 相关测试方法分组
2. 使用内部类组织复杂测试
3. 共享测试数据准备
4. 合理使用测试工具类

#### 13.8.2 测试可维护性
1. 测试代码必须有注释
2. 使用有意义的测试数据
3. 避免测试代码重复
4. 保持测试简单明了

#### 13.8.3 测试执行效率
1. 单元测试执行时间不超过100ms
2. 避免不必要的数据库操作
3. 合理使用测试夹具
4. 并行测试支持 