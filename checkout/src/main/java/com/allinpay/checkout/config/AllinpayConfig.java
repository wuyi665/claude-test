package com.allinpay.checkout.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "allinpay")
public class AllinpayConfig {
    private String appid;
    private String c;
    private String priKey;
    private String pubKey;
}