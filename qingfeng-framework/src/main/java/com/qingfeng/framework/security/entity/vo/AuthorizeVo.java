package com.qingfeng.framework.security.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * 登录验证成功的用户信息响应
 */
@Data
public class AuthorizeVo {
    String username;
    String token;
    Date expire;
}
