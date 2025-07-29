package com.allinpay.checkout.service.impl;

import com.allinpay.checkout.constants.CheckoutConstants;
import com.allinpay.checkout.dto.NotifyReq;
import com.allinpay.checkout.dto.OrderInfo;
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

}
