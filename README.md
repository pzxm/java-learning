## 一、工作流介绍

### 1.1 什么是工作流

- **工作流（workflow）**：是对工作流程及其个操作步骤之间业务规则的抽象、概括描述
- **工作流建模**：即将工作流程中的工作如何前后组织在一起的逻辑和规则，在计算机中以恰当的模型表达并对其实施计算
- **要解决的问题**：是为实现某个业务目标，利用计算机在多个参与者之间按某种预定规则自动传递文档、信息或者任务

### 1.2 工作流对团队的作用

1. 提高效率，减少等待
2. 规范行为，落实制度
3. 协同内外，快速响应
4. 监控全面，提升执行

### 1.3 工作流引擎技术选型

| JBPM        | Activiti |
| ----------- | -------- |
| Hibernate   | Mybatis  |
| Drools Flow | JBPM4    |
| JPA         | Spring   |
| Message     | RESTFUL  |
| ......      | ......   |

为什么选择`Activiti6.0`？

1. 老牌、程数、稳定、满足`BPMN2.0`规范

2. 用户众多、社区活跃、趋势良好

3. 易于上手基于`spring`、`mybatis`常用互联网技术堆栈

### 1.4 BPMN2.0规范

​	Business Process Model and Notation 是一套业务流程模型与符号建模标准；

精准的执行来描述元素的操作；以XML为载体，以符号可视化业务。

BPMN2.0元素：

- 流对象FlowObjects
- 连接对象ConnectingObject
- 数据Data
- 泳道Swimlanes
- 描述对象Artifacts

## 二、activiti入门案例

### 2.1 开发环境介绍

- JDK1.8
- Idea2019.1.2（actiBPM插件）
- MySQL8.0
- Activiti6.0

## 三、流程引擎配置及服务组件

### 3.1 流程引擎配置

#### 3.1.1 流程引擎配置对象

​	ProcessEngineConfiguration对象代表一个Activiti流程引擎的全部配置，该类提供一系列创建 ProcessEngineConfiguration 实例的静态方法，这些方法用于读取和解析相应的配置文件，并返回 ProcessEngineConfiguration 的实例。

- 配置文件读取

  ~~~java
  // 1. 读取默认配置文件（读取类路径下的activiti.cfg.xml文件）
  createProcessEngineConfigurationFromResourceDefault()
      
  // 2. 读取指定位置配置文件（可以指定bean的名称，bean必须存在）
  createProcessEngineConfigurationFromResource(String resource)
  createProcessEngineConfigurationFromResource(String resource, String beanName)
      
  // 3. 通过输入流读取配置文件（可以指定bean的名称，bean必须存在）
  createProcessEngineConfigurationFromInputStream(InputStream inputStream)
  createProcessEngineConfigurationFromInputStream(InputStream inputStream, String beanName)
  ~~~

#### 3.1.2 数据源配置

​	Activiti 在启动时，会读取数据源配置，用于对数据库进行相应的操作。 

- 支持的数据库：H2、MySQL、Oracle、PostgreSQL、DB2、MSSQL

##### 3.1.2.1 jdbc配置

~~~xml
<bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
    <property name="jdbcDriver" value="com.mysql.cj.jdbc.Driver"/>
    <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/act"/>
    <property name="jdbcUsername" value="root"/>
    <property name="jdbcPassword" value="1995"/>
</bean>
~~~

##### 3.1.2.2 dpcp配置

~~~xml
<!-- 使用DBCP 数据源 -->
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver" />
    <property name="url" value="jdbc:mysql://localhost:3306/act" />
    <property name="username" value="root" />
    <property name="password" value="123456" />
</bean>
<bean id="processEngineConfiguration"
class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
	<property name="dataSource" ref="dataSource" />
</bean>
~~~

##### 3.1.2.3 c3p0配置

~~~xml
<!-- 使用 C3P0 数据源 -->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="com.mysql.jdbc.Driver" />
    <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/act" />
    <property name="user" value="root" />
    <property name="password" value="123456" />
</bean>
<bean id="processEngineConfiguration"
class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
	<property name="dataSource" ref="dataSource" />
</bean
~~~

##### 3.1.2.4 其他数据源配置

​	如果不使用第三方数据源， 直接使用 Activiti 提供的数据源， 那么还可以指定其他一些数 据 库 属 性 。 Activiti 默 认 使 用 的 是 myBatis 的 数 据 连 接 池 ， 因 此ProcessEngineConfiguration 中也提供了一些 MyBatis 的配置：

- jdbcMaxActiveConnections：连接池最大活跃连接数 默认10
- jdbcMaxIdleConnections：连接池最大的空闲连接数。
- jdbcMaxCheckoutTime：连接池创建连接的等待时间 默认值为：20000毫秒
- jdbcMaxWaitTime：最大等待时长 默认值为：20000毫秒

##### 3.1.2.5  数据库策略配置

​	ProcessEngineConfiguration 提供了 databaseSchemaUpdate 属性，该项可以设置流
程引擎启动和关闭时数据库执行的策略。 Activiti 的官方文档中，databaseSchemaUpdate
有以下三个值：

**false**：（默认值） Activiti启动时，会对比数据库表中保存的版本，如果没有表或者版本不匹配时，将在启动时抛出异常。
true： Activiti启动时会对数据库中所有的表进行更新，如果表不存在，则自动创建。（常用）
create-drop： Activiti启动时，会执行数据库表的创建操作。在 Activiti 关闭时，执行数据库表的删除操作。