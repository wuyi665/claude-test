package com.allinpay.checkout.aspect;

import com.allinpay.checkout.config.AllinpayConfig;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class RespAspect {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    @Autowired
    private AllinpayConfig allinpayConfig;

//       @Around("execution(* com.allinpay.checkout.controller.*.*(..))")
//    public void enhanceResponse(ProceedingJoinPoint joinPoint) throws Throwable {
//        //获取执行结果
//        Object result = joinPoint.proceed();
//        if (result instanceof BaseResp) {
//            BaseResp response = (BaseResp) result;
//            // 设置默认成功状态
//            if (response.getRetcode() == null || response.getRetcode().isEmpty()) {
//                response.setRetcode(CheckoutConstants.RETCODE_SUCCESS);
//            }
//            if (response.getRetmsg() == null || response.getRetmsg().isEmpty()) {
//                response.setRetmsg(CheckoutConstants.RETMSG_SUCCESS);
//            }
//            if (response.getRetcode().equals(CheckoutConstants.RETCODE_SUCCESS)) {
//                // 设置时间戳
//                response.setTimestamp(LocalDateTime.now().format(FORMATTER));
//                // 设置随机字符串
//                response.setRandomstr(RandomStr.generateAlphanumericSecure());
//                // 设置签名方式
//                response.setSigntype("RSA");
//                // 设置签名
//                response.setSign(RSAutil.generateSign(Convert.convertToMap(response), allinpayConfig.getPriKey()));
//            }
//
//        }
//    }
}
