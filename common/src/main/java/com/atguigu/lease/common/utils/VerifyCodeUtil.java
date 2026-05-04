/**
 * @author 23275
 * @version 1.0
 * @since 2026/5/4
 */
package com.atguigu.lease.common.utils;

import java.util.Random;

public class VerifyCodeUtil {
    public static String getVerifyCode(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
