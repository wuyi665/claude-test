package com.allinpay.checkout.service;

import com.allinpay.checkout.dto.NotifyReq;
import com.allinpay.checkout.dto.OrderInfo;
import com.allinpay.checkout.dto.PayOrderReq;
import com.allinpay.checkout.dto.PayOrderResp;
import com.allinpay.checkout.dto.QueryOrderReq;
import com.allinpay.checkout.dto.QueryOrderResp;

public interface OrderService {
    //插入订单
    boolean insertOrder(OrderInfo order);

    //根据订单号查询订单
    QueryOrderResp queryOrder(QueryOrderReq request) throws Exception;

    //交易结果通知
    boolean notify(NotifyReq request) throws Exception;

    //订单支付
    PayOrderResp payOrder(PayOrderReq request) throws Exception;

}
