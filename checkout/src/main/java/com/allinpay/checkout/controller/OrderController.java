package com.allinpay.checkout.controller;

import com.allinpay.checkout.entity.BaseResp;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

//订单
@RequestMapping("/api")
public interface OrderController {

    //插入订单
    @PostMapping(path = "/InsertOrder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseResp<?> insertOrder(@RequestParam Map<String, String> request) throws Exception;

    //订单查询数据同步
    @PostMapping(path = "/QueryOrder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseResp<?> queryOrder(@RequestParam Map<String, String> request) throws Exception;

    //交易结果通知
    @PostMapping(path = "/Notify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    BaseResp<?> notify(@RequestParam Map<String, String> request) throws Exception;

}
