package com.allinpay.checkout.service.impl;

import com.allinpay.checkout.constants.CheckoutConstants;
import com.allinpay.checkout.dto.NotifyReq;
import com.allinpay.checkout.dto.OrderInfo;
import com.allinpay.checkout.dto.PayOrderReq;
import com.allinpay.checkout.dto.PayOrderResp;
import com.allinpay.checkout.dto.QueryOrderReq;
import com.allinpay.checkout.dto.QueryOrderResp;
import com.allinpay.checkout.enums.OrderStatus;
import com.allinpay.checkout.mapper.OrderMapper;
import com.allinpay.checkout.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOrder(OrderInfo order) {
        log.info("创建订单开始，bizseq: {}, amount: {}", order.getBizseq(), order.getAmount());
        
        // 检查订单是否已存在
        OrderInfo existingOrder = orderMapper.queryOrderByBizseq(order.getBizseq());
        if (existingOrder != null) {
            log.warn("订单已存在，bizseq: {}", order.getBizseq());
            throw new RuntimeException("订单已存在");
        }
        
        order.setStatus(OrderStatus.PENDING.getCode());
        int result = orderMapper.insertOrder(order);
        
        log.info("订单创建完成，bizseq: {}, result: {}", order.getBizseq(), result > 0 ? "成功" : "失败");
        return result > 0;
    }

    @Override
    public QueryOrderResp queryOrder(QueryOrderReq request) throws Exception {
        log.info("查询订单开始，bizseq: {}", request.getBizseq());
        
        OrderInfo order = orderMapper.queryOrderByBizseq(request.getBizseq());
        if (order == null) {
            log.warn("订单不存在，bizseq: {}", request.getBizseq());
            throw new Exception("订单不存在");
        }
        
        log.info("订单查询成功，bizseq: {}, status: {}", request.getBizseq(), order.getStatus());
        return QueryOrderResp.builder()
                .appid(order.getAppid())
                .cusid(order.getCusid())
                .bizseq(order.getBizseq())
                .amount(order.getAmount())
                .status(order.getStatus())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean notify(NotifyReq request) throws Exception {
        log.info("支付通知处理开始，bizseq: {}", request.getBizseq());
        
        // 优化：一次查询获取完整订单信息，减少数据库交互
        OrderInfo order = orderMapper.queryOrderByBizseq(request.getBizseq());
        if (order == null) {
            log.error("通知处理失败：订单不存在，bizseq: {}", request.getBizseq());
            throw new Exception("订单不存在");
        }
        
        // 检查订单状态，防止重复通知
        if (OrderStatus.COMPLETED.getCode().equals(order.getStatus())) {
            log.warn("重复支付通知，订单已完成，bizseq: {}", request.getBizseq());
            throw new Exception(CheckoutConstants.RETMSG_NOTIFY_REPEAT);
        }
        
        // 更新订单状态为已完成
        int result = orderMapper.updateOrderStatus(request.getBizseq(), OrderStatus.COMPLETED.getCode());
        
        boolean success = result > 0;
        log.info("支付通知处理完成，bizseq: {}, result: {}", request.getBizseq(), success ? "成功" : "失败");
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayOrderResp payOrder(PayOrderReq request) throws Exception {
        log.info("订单支付处理开始，bizseq: {}, paytype: {}", request.getBizseq(), request.getPaytype());
        
        // 查询订单是否存在
        OrderInfo order = orderMapper.queryOrderByBizseq(request.getBizseq());
        if (order == null) {
            log.error("支付失败：订单不存在，bizseq: {}", request.getBizseq());
            throw new Exception("订单不存在");
        }
        
        // 检查订单状态，只有待支付状态的订单才能发起支付
        if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
            log.warn("支付失败：订单状态不正确，bizseq: {}, status: {}", request.getBizseq(), order.getStatus());
            throw new Exception("订单状态不正确，无法支付");
        }
        
        // 验证订单信息一致性
        if (!order.getAppid().equals(request.getAppid()) || !order.getCusid().equals(request.getCusid())) {
            log.error("支付失败：订单信息不匹配，bizseq: {}", request.getBizseq());
            throw new Exception("订单信息不匹配");
        }
        
        // 更新订单状态为支付中
        int result = orderMapper.updateOrderStatus(request.getBizseq(), OrderStatus.PROCESSING.getCode());
        if (result <= 0) {
            log.error("支付失败：更新订单状态失败，bizseq: {}", request.getBizseq());
            throw new Exception("更新订单状态失败");
        }
        
        // 模拟调用第三方支付接口，生成支付信息
        String payOrderId = generatePayOrderId(request.getBizseq());
        String payUrl = generatePayUrl(request.getPaytype(), payOrderId);
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        log.info("订单支付处理完成，bizseq: {}, payOrderId: {}", request.getBizseq(), payOrderId);
        
        return PayOrderResp.builder()
                .appid(order.getAppid())
                .cusid(order.getCusid())
                .bizseq(order.getBizseq())
                .amount(order.getAmount())
                .status(OrderStatus.PROCESSING.getCode())
                .payOrderId(payOrderId)
                .payUrl(payUrl)
                .payStatus("PROCESSING")
                .payMsg("支付订单创建成功，请在支付页面完成支付")
                .timestamp(timestamp)
                .build();
    }

    /**
     * 生成支付订单号
     */
    private String generatePayOrderId(String bizseq) {
        return "PAY_" + bizseq + "_" + System.currentTimeMillis();
    }

    /**
     * 根据支付类型生成支付URL
     */
    private String generatePayUrl(String paytype, String payOrderId) {
        // 这里模拟不同支付方式的URL生成
        switch (paytype.toLowerCase()) {
            case "alipay":
                return "https://openapi.alipay.com/gateway.do?payOrderId=" + payOrderId;
            case "wechat":
                return "https://api.mch.weixin.qq.com/pay/unifiedorder?payOrderId=" + payOrderId;
            case "unionpay":
                return "https://gateway.95516.com/gateway/api/frontTransReq.do?payOrderId=" + payOrderId;
            default:
                return "https://pay.example.com/pay?payOrderId=" + payOrderId;
        }
    }

}
