package com.allinpay.checkout.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayOrderResp {
    private String appid;
    private String cusid;
    private String bizseq;
    private String amount;
    private String status;
    private String payOrderId; // 支付订单号
    private String payUrl; // 支付页面URL（如支付宝、微信等第三方支付页面）
    private String qrCode; // 二维码支付码（如需要）
    private String payStatus; // 支付状态：SUCCESS-成功，PROCESSING-处理中，FAILED-失败
    private String payMsg; // 支付消息
    private String timestamp; // 响应时间戳
}