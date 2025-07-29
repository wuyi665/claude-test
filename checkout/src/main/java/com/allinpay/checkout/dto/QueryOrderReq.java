package com.allinpay.checkout.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class QueryOrderReq {
    @NotNull(message = "appid不能为空")
    private String appid;
    @NotNull(message = "cusid不能为空")
    private String cusid;
    @NotNull(message = "trxcode不能为空")
    private String trxcode;
    @NotNull(message = "version不能为空")
    private String version;
    @NotNull(message = "timestamp不能为空")
    private String timestamp;
    @NotNull(message = "randomstr不能为空")
    private String randomstr;
    @NotNull(message = "termid不能为空")
    private String termid;
    private String source;
    @NotNull(message = "bizseq不能为空")
    private String bizseq;
    @NotNull(message = "trxresere不能为空")
    private String trxresere;
    @NotNull(message = "extresere不能为空")
    private String extresere;
    @NotNull(message = "signtype不能为空")
    private String signtype;
    @NotNull(message = "sign不能为空")
    private String sign;
}
