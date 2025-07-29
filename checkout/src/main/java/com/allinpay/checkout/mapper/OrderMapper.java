package com.allinpay.checkout.mapper;

import com.allinpay.checkout.dto.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    //插入订单
    int insertOrder(OrderInfo orderInfo);

    //根据订单号查询订单
    OrderInfo queryOrderByBizseq(String bizseq);

    //查询订单交易状态
    String queryOrderStatus(String bizseq);

    //根据通知更新订单交易状态
    int updateOrderStatus(String bizseq, String status);
}
