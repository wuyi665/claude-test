package com.allinpay.checkout.controller.impl;

import com.allinpay.checkout.config.AllinpayConfig;
import com.allinpay.checkout.constants.CheckoutConstants;
import com.allinpay.checkout.controller.OrderController;
import com.allinpay.checkout.dto.NotifyReq;
import com.allinpay.checkout.dto.OrderInfo;
import com.allinpay.checkout.dto.PayOrderReq;
import com.allinpay.checkout.dto.PayOrderResp;
import com.allinpay.checkout.dto.QueryOrderReq;
import com.allinpay.checkout.dto.QueryOrderResp;
import com.allinpay.checkout.entity.BaseResp;
import com.allinpay.checkout.service.OrderService;
import com.allinpay.checkout.utils.Convert;
import com.allinpay.checkout.utils.RSAutil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class OrderControllerImpl implements OrderController {

    @Autowired
    private AllinpayConfig allinpayConfig;
    @Autowired
    private OrderService orderService;

    @Override
    public BaseResp<?> insertOrder(Map<String, String> request) throws Exception {
        try {
            log.info("插入订单");
            //验证签名是否失效
            if (!RSAutil.verifySign(request, allinpayConfig.getPubKey())) {
                log.info("验签失败");
                return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, CheckoutConstants.RETMSG_FAILURE1);
            }
            log.info("验签成功");
            
            OrderInfo orderInfo = Convert.mapToBean(request, OrderInfo.class);
            boolean result = orderService.insertOrder(orderInfo);
            if (result) {
                return BaseResp.success(orderInfo.getBizseq());
            } else {
                return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, "插入订单失败");
            }
        } catch (RuntimeException e) {
            log.error("插入订单业务异常", e);
            return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, e.getMessage());
        } catch (Exception e) {
            log.error("插入订单异常", e);
            return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, "系统异常：" + e.getMessage());
        }
    }

    @Override
    public BaseResp<?> queryOrder(Map<String, String> request) throws Exception {
        try {
            log.info("查询订单");
            //验证签名是否失效
            if (!RSAutil.verifySign(request, allinpayConfig.getPubKey())) {
                log.info("验签失败");
                return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, CheckoutConstants.RETMSG_FAILURE1);
            }
            log.info("验签成功");
            QueryOrderReq queryOrderReq = Convert.mapToBean(request, QueryOrderReq.class);
            QueryOrderResp result = orderService.queryOrder(queryOrderReq);
            return BaseResp.success(result);
        } catch (Exception e) {
            log.error("查询订单异常", e);
            return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, e.getMessage());
        }
    }

    @Override
    public BaseResp<?> notify(Map<String, String> request) throws Exception {
        try {
            log.info("交易通知");
            //验证签名是否失效
            if (!RSAutil.verifySign(request, allinpayConfig.getPubKey())) {
                log.info("验签失败");
                return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, CheckoutConstants.RETMSG_FAILURE1);
            }
            log.info("验签成功");
            NotifyReq notifyReq = Convert.mapToBean(request, NotifyReq.class);
            boolean result = orderService.notify(notifyReq);
            if (result) {
                return BaseResp.success("通知处理成功");
            } else {
                return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, "通知处理失败");
            }
        } catch (Exception e) {
            log.error("交易通知异常", e);
            return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, e.getMessage());
        }
    }

    @Override
    public BaseResp<?> payOrder(Map<String, String> request) throws Exception {
        try {
            log.info("订单支付");
            //验证签名是否失效
            if (!RSAutil.verifySign(request, allinpayConfig.getPubKey())) {
                log.info("验签失败");
                return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, CheckoutConstants.RETMSG_FAILURE1);
            }
            log.info("验签成功");
            PayOrderReq payOrderReq = Convert.mapToBean(request, PayOrderReq.class);
            PayOrderResp result = orderService.payOrder(payOrderReq);
            return BaseResp.success(result);
        } catch (Exception e) {
            log.error("订单支付异常", e);
            return BaseResp.error(CheckoutConstants.RETCODE_FAILURE, e.getMessage());
        }
    }
}
