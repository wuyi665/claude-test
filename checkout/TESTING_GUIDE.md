# Checkout支付系统 Postman测试指南

## 📋 项目确认

**✅ 这是一个完整的Spring Boot项目**
- Spring Boot 2.3.8版本
- 包含Web、Actuator、MyBatis Plus等依赖
- 编译成功，可以正常运行

## 🚀 启动项目

### 方式1：使用Maven命令
```bash
cd "D:\Program Files\Claude Code Project\checkout"
mvn spring-boot:run
```

### 方式2：运行编译后的JAR
```bash
mvn clean package
java -jar target/checkout-0.0.1-SNAPSHOT.jar
```

### 启动成功标志
- 控制台显示：`Started CheckoutApplication in X.XXX seconds`
- 服务监听端口：`http://localhost:8080`

## 🔧 Postman配置

### 1. 导入测试集合
- 打开Postman
- 点击Import → File
- 选择项目根目录下的`postman-collection.json`文件

### 2. 环境变量设置
创建环境变量：
- `baseUrl`: `http://localhost:8080`
- `appid`: `00011114`
- `cusid`: `customer123`

## 📝 测试用例详解

### 🟢 测试1：创建订单（InsertOrder）

**请求配置：**
```
Method: POST
URL: http://localhost:8080/api/InsertOrder
Content-Type: application/x-www-form-urlencoded

Form Data:
- appid: 00011114
- cusid: customer123
- amount: 100.00
- bizseq: ORDER20250729001
```

**预期响应：**
```json
{
  "retcode": "0000",
  "retmsg": "SUCCESS",
  "data": ""
}
```

**测试变化：**
- 数据库`order_info`表新增一条记录
- 订单状态为"0"（待支付）
- 控制台输出创建订单日志

---

### 🟡 测试2：查询订单（QueryOrder）

**⚠️ 注意：此接口需要RSA签名验证**

**请求配置：**
```
Method: POST  
URL: http://localhost:8080/api/QueryOrder
Content-Type: application/x-www-form-urlencoded

Form Data:
- bizseq: ORDER20250729001
- sign: [需要RSA签名]
```

**简化测试方法（临时）：**
可以临时注释掉签名验证代码进行测试，在`OrderControllerImpl.java`第50行：
```java
// if (!RSAutil.verifySign(request, allinpayConfig.getPubKey())) {
//     log.info("验签失败");
//     return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, CheckoutConstants.RETMSG_FAILURE1);
// }
```

**预期响应：**
```json
{
  "retcode": "0000",
  "retmsg": "SUCCESS",
  "data": {
    "appid": "00011114",
    "cusid": "customer123",
    "bizseq": "ORDER20250729001",
    "amount": "100.00",
    "status": "0"
  }
}
```

---

### 🔴 测试3：支付通知（Notify）

**⚠️ 注意：此接口也需要RSA签名验证**

**请求配置：**
```
Method: POST
URL: http://localhost:8080/api/Notify  
Content-Type: application/x-www-form-urlencoded

Form Data:
- bizseq: ORDER20250729001
- sign: [需要RSA签名]
```

**预期响应：**
```json
{
  "retcode": "0000", 
  "retmsg": "SUCCESS",
  "data": true
}
```

**测试变化：**
- 数据库订单状态从"0"更新为"1"（已完成）
- 控制台输出支付通知处理日志

---

### 🟦 测试4：健康检查（Health）

**请求配置：**
```
Method: GET
URL: http://localhost:8080/actuator/health
```

**预期响应：**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

---

### 🟣 测试5：应用指标（Metrics）

**请求配置：**
```
Method: GET
URL: http://localhost:8080/actuator/metrics
```

**预期响应：**
```json
{
  "names": [
    "api.calls.success",
    "api.calls.error", 
    "api.response.time",
    "jvm.memory.used",
    "http.server.requests"
  ]
}
```

## 📊 测试时产生的变化

### 数据库变化
```sql
-- 查看订单表数据
SELECT * FROM order_info;

-- 订单状态变化过程：
-- 1. 创建订单后：status = '0' (待支付)
-- 2. 支付通知后：status = '1' (已完成)
```

### 日志输出
```
2025-07-29 15:50:01.234 [http-nio-8080-exec-1] INFO  [traceId:-] o.a.c.c.s.i.OrderServiceImpl - 创建订单开始，bizseq: ORDER20250729001, amount: 100.00
2025-07-29 15:50:01.256 [http-nio-8080-exec-1] INFO  [traceId:-] o.a.c.c.s.i.OrderServiceImpl - 订单创建完成，bizseq: ORDER20250729001, result: 成功
2025-07-29 15:50:01.278 [http-nio-8080-exec-1] INFO  [traceId:-] o.a.c.c.m.ApiMetricsAspect - API调用成功: OrderControllerImpl.insertOrder 耗时: 45ms
```

### 监控指标变化
- `api.calls.success` 计数器增加
- `api.response.time` 记录响应时间
- 数据库连接池使用情况更新

## ⚠️ 常见问题解决

### 1. 签名验证失败
**临时解决方案：**
注释掉`OrderControllerImpl.java`中的签名验证代码

### 2. 数据库连接失败
**检查配置：**
- MySQL服务是否启动
- 数据库`checkout_db`是否存在
- 用户名密码是否正确

### 3. 端口占用
**解决方案：**
```bash
# 查看端口占用
netstat -ano | findstr 8080
# 结束占用进程
taskkill /f /pid [PID]
```

## 🎯 优化效果验证

通过测试可以验证以下优化效果：

1. **事务管理**：创建订单失败时自动回滚
2. **性能优化**：通知接口只需1次数据库查询
3. **监控功能**：API调用次数和响应时间统计
4. **连接池**：数据库连接复用，提升性能
5. **日志记录**：详细的业务操作日志

## 📈 性能监控

测试过程中可以观察：
- 响应时间：一般在50-200ms之间
- 数据库连接数：通过HikariCP优化管理
- 内存使用：通过JVM指标监控
- API成功率：通过自定义指标统计