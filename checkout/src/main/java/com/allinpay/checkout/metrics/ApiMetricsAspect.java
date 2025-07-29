package com.allinpay.checkout.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * API性能监控切面
 */
@Slf4j
@Aspect
@Component
public class ApiMetricsAspect {

    @Autowired
    private MeterRegistry meterRegistry;

    @Around("execution(* com.allinpay.checkout.controller.impl.*.*(..))")
    public Object monitorApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String apiName = className + "." + methodName;
        
        long startTime = System.currentTimeMillis();
        Timer.Sample sample = Timer.start(meterRegistry);
        
        try {
            Object result = joinPoint.proceed();
            
            // 记录成功调用
            meterRegistry.counter("api.calls.success", "method", apiName).increment();
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("API调用成功: {} 耗时: {}ms", apiName, duration);
            
            return result;
        } catch (Exception e) {
            // 记录失败调用
            meterRegistry.counter("api.calls.error", "method", apiName, "error", e.getClass().getSimpleName()).increment();
            
            long duration = System.currentTimeMillis() - startTime;
            log.error("API调用失败: {} 耗时: {}ms 错误: {}", apiName, duration, e.getMessage());
            
            throw e;
        } finally {
            // 记录响应时间
            sample.stop(Timer.builder("api.response.time")
                    .description("API响应时间")
                    .tag("method", apiName)
                    .register(meterRegistry));
        }
    }
}