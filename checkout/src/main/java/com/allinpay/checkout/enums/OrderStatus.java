package com.allinpay.checkout.enums;

/**
 * 订单状态枚举
 */
public enum OrderStatus {
    PENDING("0", "待支付"),
    COMPLETED("1", "已完成"),
    FAILED("2", "支付失败"),
    CANCELLED("3", "已取消");

    private final String code;
    private final String description;

    OrderStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus fromCode(String code) {
        for (OrderStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid order status code: " + code);
    }
}