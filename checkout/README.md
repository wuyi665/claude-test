# 通联支付结账系统

基于Spring Boot 2.3.8的RESTful支付订单管理系统，集成了通联支付接口，支持多种支付方式和完整的订单生命周期管理。

## 项目概述

本系统提供了完整的支付订单处理功能，包括订单创建、查询、支付处理和通知回调等核心功能。系统采用Spring Boot框架，集成MyBatis Plus进行数据持久化，并提供了完善的监控和日志记录功能。

## 主要功能

- ✅ **订单管理**: 支持订单创建、查询和状态更新
- ✅ **支付处理**: 集成多种支付方式（支付宝、微信、银联等）
- ✅ **安全加密**: 支持RSA和国密SM2加解密算法
- ✅ **参数验证**: 完整的请求参数验证和错误处理
- ✅ **监控指标**: 集成Prometheus监控和健康检查
- ✅ **连接池**: 使用HikariCP连接池优化数据库性能
- ✅ **日志记录**: 完善的日志记录和滚动策略

## 技术栈

- **框架**: Spring Boot 2.3.8
- **数据库**: MySQL 8.0+ 
- **ORM**: MyBatis Plus 3.5.1
- **连接池**: HikariCP
- **验证**: Spring Boot Validation
- **监控**: Spring Boot Actuator + Micrometer + Prometheus
- **加密**: RSA + SM2国密算法
- **构建工具**: Maven 3.6+
- **Java版本**: JDK 1.8+

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+

### 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE checkout_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. 创建订单表：
```sql
USE checkout_db;
CREATE TABLE order_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bizseq VARCHAR(64) NOT NULL COMMENT '业务序列号',
    appid VARCHAR(32) NOT NULL COMMENT '应用ID',
    cusid VARCHAR(32) NOT NULL COMMENT '客户ID',
    amount DECIMAL(12,2) NOT NULL COMMENT '订单金额',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '订单状态',
    paytype VARCHAR(20) COMMENT '支付方式',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_bizseq (bizseq),
    INDEX idx_appid_cusid (appid, cusid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单信息表';
```

### 启动应用

1. **克隆项目并进入目录**：
```bash
cd checkout
```

2. **使用Maven启动**：
```bash
mvn spring-boot:run
```

3. **或使用Maven包装器（推荐）**：
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

4. **打包运行**：
```bash
mvn clean package
java -jar target/checkout-0.0.1-SNAPSHOT.jar
```

### 验证启动

应用启动后访问：
- **应用端口**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **监控指标**: http://localhost:8080/actuator/metrics
- **Prometheus指标**: http://localhost:8080/actuator/prometheus

## API接口

### 1. 创建订单
```
POST /api/InsertOrder
Content-Type: multipart/form-data

参数:
- appid: 应用ID
- cusid: 客户ID  
- bizseq: 业务序列号
- amount: 订单金额
- timestamp: 时间戳
- randomstr: 随机字符串
- signtype: 签名类型
- sign: 签名
```

### 2. 查询订单
```
POST /api/QueryOrder
Content-Type: multipart/form-data

参数:
- appid: 应用ID
- cusid: 客户ID
- bizseq: 业务序列号
- timestamp: 时间戳
- randomstr: 随机字符串
- signtype: 签名类型
- sign: 签名
```

### 3. 订单支付
```
POST /api/PayOrder
Content-Type: multipart/form-data

参数:
- appid: 应用ID
- cusid: 客户ID
- bizseq: 业务序列号
- paytype: 支付方式 (alipay/wechat/unionpay)
- payacct: 支付账户(可选)
- paypasswd: 支付密码(可选)
- timestamp: 时间戳
- randomstr: 随机字符串
- signtype: 签名类型
- sign: 签名
```

### 4. 支付通知
```
POST /api/Notify
Content-Type: multipart/form-data

参数:
- appid: 应用ID
- cusid: 客户ID
- bizseq: 业务序列号
- status: 支付状态
- timestamp: 时间戳
- randomstr: 随机字符串
- signtype: 签名类型
- sign: 签名
```

## 项目结构

```
src/main/java/com/allinpay/checkout/
├── CheckoutApplication.java           # 应用启动类
├── aspect/                           # AOP切面
│   └── RespAspect.java              # 响应格式化切面
├── config/                          # 配置类
│   └── AllinpayConfig.java         # 通联支付配置
├── constants/                       # 常量定义
│   └── CheckoutConstants.java      # 系统常量
├── controller/                      # 控制层
│   ├── OrderController.java        # 订单接口定义
│   └── impl/
│       └── OrderControllerImpl.java # 订单接口实现
├── dto/                            # 数据传输对象
│   ├── NotifyReq.java             # 通知请求DTO
│   ├── OrderInfo.java             # 订单信息DTO
│   ├── PayOrderReq.java           # 支付请求DTO
│   ├── PayOrderResp.java          # 支付响应DTO
│   ├── QueryOrderReq.java         # 查询请求DTO
│   └── QueryOrderResp.java        # 查询响应DTO
├── entity/                         # 实体类
│   └── BaseResp.java             # 基础响应实体
├── enums/                         # 枚举类
│   └── OrderStatus.java          # 订单状态枚举
├── handler/                       # 处理器
│   └── ValidationHandler.java    # 参数验证处理器
├── mapper/                        # 数据访问层
│   └── OrderMapper.java          # 订单数据访问接口
├── metrics/                       # 监控指标
│   └── ApiMetricsAspect.java     # API监控切面
├── service/                       # 服务层
│   ├── OrderService.java         # 订单服务接口
│   └── impl/
│       └── OrderServiceImpl.java # 订单服务实现
└── utils/                         # 工具类
    ├── Convert.java              # 数据转换工具
    ├── RandomStr.java            # 随机字符串工具
    ├── RSAutil.java             # RSA加密工具
    └── SM2util.java             # SM2国密加密工具
```

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/checkout_db
    username: root
    password: P@Ut97U8
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20
      connection-timeout: 30000
```

### 通联支付配置
```yaml
allinpay:
  appid: 00011114
  c: 1000DBTAPTur
  priKey: [RSA私钥]
  pubKey: [RSA公钥]
```

### 监控配置
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,env
```

## 开发指南

### 代码规范
- 遵循Spring Boot最佳实践
- 使用Lombok减少样板代码
- 统一异常处理和响应格式
- 完善的参数验证和日志记录

### 测试
```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify
```

### 监控和运维
- 集成Spring Boot Actuator提供健康检查
- 支持Prometheus监控指标收集
- 提供完整的日志记录和滚动策略
- 数据库连接池监控

## 安全考虑

- ✅ RSA和SM2双重加密支持
- ✅ 请求参数签名验证
- ✅ 敏感信息加密存储
- ✅ SQL注入防护（MyBatis Plus）
- ✅ 请求参数验证

## 性能优化

- ✅ HikariCP连接池优化
- ✅ Tomcat线程池配置
- ✅ MyBatis Plus查询优化
- ✅ AOP响应处理优化

## 部署说明

### Docker部署（推荐）
```bash
# 构建镜像
docker build -t checkout-app .

# 运行容器
docker run -d -p 8080:8080 --name checkout checkout-app
```

### 传统部署
```bash
# 打包
mvn clean package

# 运行
java -jar target/checkout-0.0.1-SNAPSHOT.jar
```

## 许可证

本项目采用私有许可证，仅供内部使用。

## 贡献

如有问题或建议，请联系开发团队。