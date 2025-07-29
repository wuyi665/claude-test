package com.allinpay.checkout.service.impl;

import com.allinpay.checkout.constants.CheckoutConstants;
import com.allinpay.checkout.dto.NotifyReq;
import com.allinpay.checkout.dto.OrderInfo;
import com.allinpay.checkout.dto.QueryOrderReq;
import com.allinpay.checkout.dto.QueryOrderResp;
import com.allinpay.checkout.mapper.OrderMapper;
import com.allinpay.checkout.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;


    @Override
    public boolean insertOrder(OrderInfo order) {
        order.setStatus("0");
        if (orderMapper.insertOrder(order) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public QueryOrderResp queryOrder(QueryOrderReq request) throws Exception {
        OrderInfo order = orderMapper.queryOrderByBizseq(request.getBizseq());
        if (order == null) {
            throw new Exception("订单不存在");
        }
        return QueryOrderResp.builder()
                .appid(order.getAppid())
                .cusid(order.getCusid())
                .bizseq(order.getBizseq())
                .amount(order.getAmount())
                .build();
    }

    @Override
    public boolean notify(NotifyReq request) throws Exception {
        //查询订单状态，若为1表示已完成此时重复通知，若为0则表示未完成继续修改订单状态为1
        String status = orderMapper.queryOrderStatus(request.getBizseq());
        if (status.equals("1")) {
            throw new Exception(CheckoutConstants.RETMSG_NOTIFY_REPEAT);
        }
        if (orderMapper.updateOrderStatus(request.getBizseq(), "1") > 0) {
            return true;
        }
        return false;
    }

}
