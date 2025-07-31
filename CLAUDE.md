# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

此代码库主要包含：
**通联支付结账系统** (`checkout/`) - 基于Spring Boot 2.3.8的RESTful支付订单管理系统，集成通联支付接口，支持多种支付方式和完整的订单生命周期管理。

**注意：** 代码库中可能包含其他第三方项目目录，在开发时应专注于`checkout/`目录下的支付系统。

## 构建和开发命令

### 通联支付结账系统
进入`checkout/`目录执行所有Maven命令：

**启动开发服务器：**
```bash
cd checkout
mvn spring-boot:run
```

**构建和编译：**
```bash
mvn clean compile
```

**运行测试：**
```bash
mvn test
```

**打包为可执行JAR：**
```bash
mvn clean package
java -jar target/checkout-0.0.1-SNAPSHOT.jar
```

**使用Maven包装器（推荐）：**
```bash
./mvnw spring-boot:run    # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

## 架构和代码结构

### 通联支付结账系统（Spring Boot架构）
基于Spring Boot 2.3.8的RESTful Web服务：

**应用入口：**
- `CheckoutApplication.java` - Spring Boot主启动类

**控制层：**
- `OrderController.java` - 订单相关REST API接口定义
- `OrderControllerImpl.java` - 订单控制器实现类

**服务层：**
- `OrderService.java` - 订单业务逻辑接口
- `OrderServiceImpl.java` - 订单服务实现

**数据访问层：**
- `OrderMapper.java` - MyBatis Plus数据访问接口
- `OrderMapper.xml` - SQL映射文件

**配置和工具：**
- `AllinpayConfig.java` - 通联支付配置类
- `RSAutil.java` - RSA加密签名工具
- `SM2util.java` - 国密SM2加密工具
- `Convert.java` - 数据转换工具（新增）
- `RandomStr.java` - 随机字符串生成工具（新增）
- `ValidationHandler.java` - 请求参数验证处理器
- `RespAspect.java` - AOP响应处理切面
- `ApiMetricsAspect.java` - API监控指标切面

**数据传输对象：**
- `OrderInfo.java` - 订单信息DTO
- `QueryOrderReq.java` - 查询订单请求DTO
- `QueryOrderResp.java` - 查询订单响应DTO
- `PayOrderReq.java` - 支付订单请求DTO（新增）
- `PayOrderResp.java` - 支付订单响应DTO（新增）
- `NotifyReq.java` - 支付通知请求DTO

**枚举和常量：**
- `OrderStatus.java` - 订单状态枚举
- `CheckoutConstants.java` - 系统常量定义

### 核心设计模式
**支付系统：**
- **分层架构**：Controller-Service-Mapper三层架构，职责清晰
- **依赖注入**：使用Spring的IoC容器管理组件
- **AOP编程**：使用切面处理响应格式化和API监控
- **配置外部化**：使用`@ConfigurationProperties`管理配置
- **监控集成**：集成Prometheus和Actuator进行系统监控
- **连接池优化**：使用HikariCP优化数据库连接性能

### 开发指南

**通用要求：**
- 全程保持Java 8+兼容性
- 遵循Maven标准目录结构

**支付系统：**
- 使用Spring Boot DevTools进行热重载开发
- 遵循RESTful API设计规范
- 数据库密码等敏感信息应使用环境变量或配置文件管理
- RSA/SM2加密密钥需要妥善保管
- 所有API接口需要进行参数验证
- 使用MyBatis Plus简化数据库操作
- 事务管理使用Spring的`@Transactional`注解
- 集成Actuator和Prometheus进行系统监控
- 使用HikariCP连接池优化数据库性能
- 日志记录采用滚动策略，支持按大小和时间分割

### 数据库配置
**支付系统需要MySQL数据库：**
- 数据库名：`checkout_db`
- 主要表：`order_info`（订单信息表）
- 字段包括：bizseq（业务序列号）、appid、amount（金额）、cusid（客户ID）、status（状态）、paytype（支付方式）
- 默认连接：localhost:3306，用户名root
- 连接池：使用HikariCP，最小连接数5，最大连接数20

### API接口
**支付系统提供的主要接口：**
- `POST /api/InsertOrder` - 创建订单
- `POST /api/QueryOrder` - 查询订单
- `POST /api/PayOrder` - 订单支付（新增）
- `POST /api/Notify` - 支付结果通知

### 测试
**支付系统：**
- 使用Spring Boot Test进行集成测试
- 可使用`@SpringBootTest`注解测试完整应用上下文
- 数据库相关测试建议使用H2内存数据库
- 测试配置文件：`application-test.yml`
- 测试数据初始化：`data-test.sql` 和 `schema-test.sql`