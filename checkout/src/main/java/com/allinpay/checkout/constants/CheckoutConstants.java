package com.allinpay.checkout.constants;

public final class CheckoutConstants {

    //防止实例化
    private CheckoutConstants() {
    }

    // 日期格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 字符编码
    public static final String CHARSET = "UTF-8";

    // 响应码
    public static final String RETCODE_SUCCESS = "0000";
    public static final String RETCODE_FAILURE = "9999";
    public static final String RETMSG_SUCCESS = "处理成功";
    public static final String RETMSG_FAILURE1 = "请求sign校验失败";
    public static final String RETMSG_FAILURE2 = "响应sign校验失败";

    // 订单查询失败
    public static final String RETMSG_QUERY_FAILURE = "订单查询失败，订单不存在";
    public static final String RETMSG_NOTIFY_FAILURE = "交易通知失败";

    //重复通知
    public static final String RETMSG_NOTIFY_REPEAT = "重复通知";

    //插入订单失败
    public static final String RETMSG_INSERT_FAILURE = "插入订单失败";
}
