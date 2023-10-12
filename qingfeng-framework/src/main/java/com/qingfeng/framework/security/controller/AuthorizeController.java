package com.qingfeng.framework.security.controller;

import com.qingfeng.framework.security.service.ValidateCodeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 用于验证相关的Controller，包含用户的注册、重置密码等操作
 */
@RestController
@RequestMapping("/auth")
@Validated
public class AuthorizeController {

    @Resource
    private ValidateCodeService validateCodeService;


    /**
     * @title captcha
     * @description 获取验证码
     * @author Administrator
     * @updateTime 2021/4/3 0003 19:10
     * @throws
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        validateCodeService.create(request, response);
    }


}
