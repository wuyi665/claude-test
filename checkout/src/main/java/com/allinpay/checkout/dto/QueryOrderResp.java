package com.allinpay.checkout.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryOrderResp {
    private String appid;
    private String cusid;
    private String trxcode;
    private String termid;
    private String source;
    private String bizseq;
    private String trxresere;
    private String extresere;
    private String amount;
    private String asinfo;
}
