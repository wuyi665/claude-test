package com.allinpay.checkout.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    @NotNull(message = "appid不能为空")
    private String appid;
    @NotNull(message = "cusid不能为空")
    private String cusid;
    @NotNull(message = "amount不能为空")
    private String amount;
    @NotNull(message = "bizseq不能为空")
    private String bizseq;
    private String status;
}
