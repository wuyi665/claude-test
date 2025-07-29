package com.allinpay.checkout.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NotifyReq {
    @ApiModelProperty(value = "通联分配appid")
    private String appid;
    @ApiModelProperty(value = "收银宝商户号")
    private String cusid;
    @ApiModelProperty(value = "接口编号")
    private String trxcode;
    @ApiModelProperty(value = "接口版本编号")
    private String version;
    @ApiModelProperty(value = "时间戳")
    private String timestamp;
    @ApiModelProperty(value = "随机字符串")
    private String randomstr;
    @ApiModelProperty(value = "终端/二维码编码")
    private String termid;
    @ApiModelProperty(value = "请求来源")
    private String source;
    @ApiModelProperty(value = "业务流水号")
    private String bizseq;
    @ApiModelProperty(value = "业务关联内容")
    private String trxresere;
    @ApiModelProperty(value = "业务拓展内容")
    private String extresere;
    @ApiModelProperty(value = "签名方式")
    private String signtype;
    @ApiModelProperty(value = "sign校验码")
    private String sign;
}
