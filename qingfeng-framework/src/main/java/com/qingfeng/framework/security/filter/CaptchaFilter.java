package com.qingfeng.framework.security.filter;

import com.qingfeng.framework.security.service.ValidateCodeService;
import com.qingfeng.module.common.utils.PageData;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 验证码过滤器 todo
 */
public class CaptchaFilter extends OncePerRequestFilter {

    @Resource
    private ValidateCodeService validateCodeService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals("/auth/login")){
            if (validateCodeService == null) {//解决service为null无法注入问题
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                validateCodeService = (ValidateCodeService) factory.getBean("validateCodeService");
            }
            PageData pd = new PageData(request);
            try {
                validateCodeService.check(response,pd.get("captchaId").toString(),pd.get("verifyCode").toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(request,response);
    }

}
