/**
 * @author 23275
 * @version 1.0
 * @since 2026/5/5
 */
package com.atguigu.lease.common.context;

public class LoginUserContext {

    private static final ThreadLocal<LoginUser> userThreadLocal = new ThreadLocal<>();

    public static void setLoginUser(LoginUser loginUser) {
        userThreadLocal.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        return userThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }
}
