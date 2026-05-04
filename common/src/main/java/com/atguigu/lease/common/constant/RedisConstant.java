/**
 * @author 23275
 * @version 1.0
 * @since 2026/5/4
 */
package com.atguigu.lease.common.constant;

public class RedisConstant {
    public static final String ADMIN_LOGIN_PREFIX = "admin:login:";
    public static final Integer ADMIN_LOGIN_CAPTCHA_TTL_SEC = 60;
    public static final String APP_LOGIN_PREFIX = "app:login:";
    public static final Integer APP_LOGIN_CODE_RESEND_TIME_SEC = 60;
    public static final Integer APP_LOGIN_CODE_TTL_SEC = 60 * 5;
    public static final Integer APP_LOGIN_CODE_TTL_MINUTES = 5;
}
