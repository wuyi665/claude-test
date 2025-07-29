package com.allinpay.checkout.entity;

import com.allinpay.checkout.constants.CheckoutConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResp<T> {
    private String retcode;
    private String retmsg;
    //    private String timestamp;
//    private String randomstr;
//    private String signtype;
//    private String sign;
    private T data;

    public static <T> BaseResp<T> success(T data) {
        BaseResp<T> resp = new BaseResp<>();
        resp.setRetcode(CheckoutConstants.RETCODE_SUCCESS);
        resp.setRetmsg(CheckoutConstants.RETMSG_SUCCESS);
        resp.setData(data);
        return resp;
    }

    public static BaseResp<?> error(String code, String message) {
        BaseResp<Object> resp = new BaseResp<>();
        resp.setRetcode(code);
        resp.setRetmsg(message);
        resp.setData(null);
        return resp;
    }
}
