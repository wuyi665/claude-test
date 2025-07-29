# CLAUDE.md

此文件为Claude Code (claude.ai/code)在此代码库中工作时提供指导。

## 项目概述

此代码库包含三个项目：
1. **Java Swing桌面版贪吃蛇游戏** (`snake-game-java/`) - 使用MVC架构的功能完整的桌面游戏
2. **HTML5/JavaScript网页版贪吃蛇游戏** (`snake-game.html`) - 基于浏览器的单文件实现
3. **通联支付结账系统** (`checkout/`) - 基于Spring Boot的支付订单管理系统

## 构建和开发命令

### 贪吃蛇游戏 - Java版本
进入`snake-game-java/`目录执行所有Maven命令：

**构建和编译：**
```bash
cd snake-game-java
mvn clean compile
```

**运行游戏：**
```bash
mvn exec:java -Dexec.mainClass="com.snakegame.SnakeGame"
```

**打包为可执行JAR：**
```bash
mvn clean package
java -jar target/snake-game-1.0.0.jar
```

**运行测试：**
```bash
mvn test
```

### 贪吃蛇游戏 - 网页版本
直接在网页浏览器中打开`snake-game.html`即可，无需构建过程。

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

### 贪吃蛇游戏 - Java版本（MVC模式）
Java实现遵循严格的模型-视图-控制器架构：

**程序入口：**
- `SnakeGame.java` - 在EDT上初始化Swing UI的主类

**模型层：**
- `Snake.java` - 核心蛇实体，包含移动、碰撞检测和增长逻辑
- `Food.java` - 食物实体，包含位置管理

**视图层：**
- `GameFrame.java` - 主窗口容器（JFrame）
- `GamePanel.java` - 游戏渲染表面，包含自定义绘制和输入处理

**控制层：**
- `GameController.java` - 游戏状态管理，协调模型和视图之间的交互

### 通联支付结账系统（Spring Boot架构）
基于Spring Boot 2.3.8的RESTful Web服务：

**应用入口：**
- `CheckoutApplication.java` - Spring Boot主启动类，配置MyBatis扫描

**控制层：**
- `OrderController.java` - 订单相关REST API接口定义
- `OrderControllerImpl.java` - 订单控制器实现类

**服务层：**
- `OrderService.java` - 订单业务逻辑接口
- `OrderServiceImpl.java` - 订单服务实现

**数据访问层：**
- `OrderMapper.java` - MyBatis数据访问接口
- `OrderMapper.xml` - SQL映射文件

**配置和工具：**
- `AllinpayConfig.java` - 通联支付配置类
- `RSAutil.java` - RSA加密签名工具
- `SM2util.java` - 国密SM2加密工具
- `ValidationHandler.java` - 请求参数验证处理器
- `RespAspect.java` - AOP响应处理切面

**数据传输对象：**
- `OrderInfo.java` - 订单信息DTO
- `QueryOrderReq.java` - 查询订单请求DTO
- `QueryOrderResp.java` - 查询订单响应DTO
- `NotifyReq.java` - 支付通知请求DTO

### 核心设计模式
**贪吃蛇游戏：**
- **MVC分离**：游戏逻辑、渲染和用户输入之间界限清晰
- **组件化**：每个游戏实体都是自包含的，具有各自的职责
- **状态管理**：在GameController中集中管理游戏状态

**支付系统：**
- **分层架构**：Controller-Service-Mapper三层架构，职责清晰
- **依赖注入**：使用Spring的IoC容器管理组件
- **AOP编程**：使用切面处理响应格式化
- **配置外部化**：使用`@ConfigurationProperties`管理配置

### 开发指南

**通用要求：**
- 全程保持Java 8+兼容性
- 遵循Maven标准目录结构

**贪吃蛇游戏：**
- 配置Maven Shade插件用于独立JAR分发
- 正确使用Swing EDT线程进行UI操作
- 输入验证防止反向移动
- 按类型分离碰撞检测（边界、自身、食物）

**支付系统：**
- 使用Spring Boot DevTools进行热重载开发
- 遵循RESTful API设计规范
- 数据库密码等敏感信息应使用环境变量或配置文件管理
- RSA/SM2加密密钥需要妥善保管
- 所有API接口需要进行参数验证
- 使用MyBatis Plus简化数据库操作
- 事务管理使用Spring的`@Transactional`注解

### 数据库配置
**支付系统需要MySQL数据库：**
- 数据库名：`checkout_db`
- 主要表：`order_info`（订单信息表）
- 字段包括：bizseq（业务序列号）、appid、amount（金额）、cusid（客户ID）、status（状态）
- 默认连接：localhost:3306，用户名root

### API接口
**支付系统提供的主要接口：**
- `POST /api/InsertOrder` - 创建订单
- `POST /api/QueryOrder` - 查询订单
- `POST /api/Notify` - 支付结果通知

### 测试
**贪吃蛇游戏：**
- 配置JUnit 4.13.2用于单元测试
- 测试目录结构遵循Maven约定

**支付系统：**
- 使用Spring Boot Test进行集成测试
- 可使用`@SpringBootTest`注解测试完整应用上下文
- 数据库相关测试建议使用H2内存数据库