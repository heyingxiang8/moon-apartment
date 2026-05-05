/**
 * @author 23275
 * @version 1.0
 * @since 2026/5/5
 */
package com.atguigu.lease.common.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {

    private Long userId;
    private String username;
}