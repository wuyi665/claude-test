# 通联支付结账系统

基于Spring Boot 2.3.8的RESTful支付订单管理系统，提供完整的订单创建、查询和支付通知处理功能。

## 项目概述

此项目是一个完整的支付结账系统，采用Spring Boot框架开发，集成MyBatis Plus进行数据库操作，支持RSA和国密SM2加密算法，提供完整的订单管理和支付通知处理功能。

## 技术栈

- **框架**: Spring Boot 2.3.8
- **数据库**: MySQL 8.0.22
- **ORM**: MyBatis Plus 3.5.1
- **加密**: RSA + 国密SM2
- **构建工具**: Maven 3.x
- **Java版本**: 1.8+

## 项目结构

```
checkout/
├── src/main/java/com/allinpay/checkout/
│   ├── CheckoutApplication.java          # Spring Boot启动类
│   ├── aspect/
│   │   └── RespAspect.java              # AOP响应处理切面
│   ├── config/
│   │   └── AllinpayConfig.java          # 通联支付配置类
│   ├── constants/
│   │   └── CheckoutConstants.java       # 常量定义
│   ├── controller/
│   │   ├── OrderController.java         # 订单控制器接口
│   │   └── impl/
│   │       └── OrderControllerImpl.java # 订单控制器实现
│   ├── dto/
│   │   ├── NotifyReq.java              # 支付通知请求DTO
│   │   ├── OrderInfo.java              # 订单信息DTO
│   │   ├── QueryOrderReq.java          # 查询订单请求DTO
│   │   └── QueryOrderResp.java         # 查询订单响应DTO
│   ├── entity/
│   │   └── BaseResp.java               # 基础响应实体
│   ├── handler/
│   │   └── ValidationHandler.java      # 参数验证处理器
│   ├── mapper/
│   │   └── OrderMapper.java            # MyBatis数据访问接口
│   ├── service/
│   │   ├── OrderService.java           # 订单服务接口
│   │   └── impl/
│   │       └── OrderServiceImpl.java   # 订单服务实现
│   └── utils/
│       ├── Convert.java                # 转换工具类
│       ├── RSAutil.java               # RSA加密工具
│       ├── RandomStr.java             # 随机字符串工具
│       └── SM2util.java               # 国密SM2加密工具
├── src/main/resources/
│   ├── application.yml                 # 应用配置文件
│   └── mapper/
│       └── OrderMapper.xml            # MyBatis SQL映射文件
└── src/test/java/
    └── com/allinpay/checkout/
        └── CheckoutApplicationTests.java # 单元测试类
```

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.x
- MySQL 5.7+

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE checkout_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 创建订单表：
```sql
USE checkout_db;
CREATE TABLE order_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bizseq VARCHAR(64) NOT NULL COMMENT '业务序列号',
    appid VARCHAR(32) NOT NULL COMMENT '应用ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    cusid VARCHAR(64) NOT NULL COMMENT '客户ID',
    status INT DEFAULT 0 COMMENT '订单状态',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0 COMMENT '逻辑删除标识',
    UNIQUE KEY uk_bizseq (bizseq)
);
```

### 配置文件

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/checkout_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: 您的数据库密码
```

### 运行项目

**使用Maven运行：**
```bash
cd checkout
mvn spring-boot:run
```

**使用Maven包装器（推荐）：**
```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

**打包运行：**
```bash
mvn clean package
java -jar target/checkout-0.0.1-SNAPSHOT.jar
```

服务启动后访问地址：http://localhost:8080

## API接口

### 1. 创建订单
- **接口地址**: `POST /api/InsertOrder`
- **请求参数**: OrderInfo对象
- **功能描述**: 创建新的支付订单

### 2. 查询订单
- **接口地址**: `POST /api/QueryOrder`
- **请求参数**: QueryOrderReq对象
- **响应参数**: QueryOrderResp对象
- **功能描述**: 根据业务序列号查询订单状态

### 3. 支付通知
- **接口地址**: `POST /api/Notify`
- **请求参数**: NotifyReq对象
- **功能描述**: 接收支付平台的异步通知

## 开发指南

### 架构设计

系统采用经典的三层架构：

- **Controller层**: 负责接收HTTP请求，参数验证，调用Service层
- **Service层**: 负责业务逻辑处理，事务管理
- **Mapper层**: 负责数据库操作，SQL映射

### 加密安全

- 支持RSA加密算法进行数据签名验证
- 集成国密SM2算法，满足国产化要求
- 密钥配置在application.yml中，生产环境建议使用环境变量

### 参数验证

- 使用Spring Boot Validation进行请求参数验证
- 自定义ValidationHandler处理参数验证异常
- 统一响应格式处理

### AOP切面

- RespAspect切面统一处理API响应格式
- 自动包装返回结果为标准格式
- 异常统一处理和日志记录

## 测试

### 运行测试
```bash
mvn test
```

### 集成测试
项目包含完整的Spring Boot集成测试，测试应用程序上下文加载和基本功能。

## 构建和部署

### 编译项目
```bash
mvn clean compile
```

### 打包项目
```bash
mvn clean package
```

### 生产部署
```bash
# 后台运行
nohup java -jar target/checkout-0.0.1-SNAPSHOT.jar > checkout.log 2>&1 &

# 使用Spring Profile
java -jar target/checkout-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 配置说明

### 关键配置项

- `allinpay.appid`: 通联支付应用ID
- `allinpay.priKey`: RSA私钥
- `allinpay.pubKey`: RSA公钥
- `mybatis-plus.configuration.log-impl`: SQL日志输出
- `server.port`: 服务端口号

### 安全注意事项

- 生产环境请更换默认的数据库密码
- RSA密钥对请妥善保管，不要提交到版本控制系统
- 建议使用HTTPS协议部署到生产环境
- 敏感配置信息建议使用环境变量或配置中心管理

## 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证

此项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

如有问题或建议，请通过以下方式联系：

- 邮箱: developer@allinpay.com
- 项目Issues: 在GitHub项目页面提交Issue

---

**注意**: 本项目仅用于学习和演示目的，生产环境使用前请进行充分的安全审计和测试。