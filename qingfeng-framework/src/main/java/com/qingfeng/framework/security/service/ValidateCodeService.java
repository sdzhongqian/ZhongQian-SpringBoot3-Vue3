package com.qingfeng.framework.security.service;


import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingfeng.framework.security.entity.MyConstant;
import com.qingfeng.module.common.framework.exception.BizException;
import com.qingfeng.module.common.framework.properties.AuthProperties;
import com.qingfeng.module.common.framework.properties.ValidateCodeProperties;
import com.qingfeng.module.common.framework.redis.RedisService;
import com.qingfeng.module.common.utils.Json;
import com.qingfeng.module.common.utils.MyResponse;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ProjectName ValidateCodeService
 * @author Administrator
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021/4/3 0003 19:13
 */
@Service
public class ValidateCodeService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private AuthProperties properties;

    /**
     * @title create
     * @description 成验证码
     * @author Administrator
     * @updateTime 2021/4/3 0003 19:13
     */
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, BizException {
        String key = request.getParameter("key");
        if (StringUtils.isBlank(key)) {
            throw new BizException("验证码key不能为空");
        }
        ValidateCodeProperties code = properties.getCode();
        setHeader(response, code.getType());

        Captcha captcha = createCaptcha(code);
        redisService.set(MyConstant.CODE_PREFIX + key, StringUtils.lowerCase(captcha.text()), code.getTime());
        captcha.out(response.getOutputStream());
    }

    /**
     * 校验验证码
     * @param key   前端上送 key
     * @param value 前端上送待校验值
     */
    public void check(HttpServletResponse response,String key, String value) throws Exception {
        Object codeInRedis = redisService.get(MyConstant.CODE_PREFIX + key);

        Json json = new Json();
        json.setSuccess(false);
        json.setCode(500);
        if (StringUtils.isBlank(value)) {
            response.setStatus(500);
            json.setMsg("请输入验证码");
            MyResponse.writeJson(response,json);
        }
        if (codeInRedis == null) {
            response.setStatus(500);
            json.setMsg("验证码已过期");
            MyResponse.writeJson(response,json);
        }
        if (!StringUtils.equalsIgnoreCase(value, String.valueOf(codeInRedis))) {
            response.setStatus(500);
            json.setMsg("验证码不正确");
            MyResponse.writeJson(response,json);
        }
    }

    /**
     * @title createCaptcha
     * @description 生成验证码
     * @author Administrator
     * @updateTime 2021/4/3 0003 19:14
     */
    private Captcha createCaptcha(ValidateCodeProperties code) {
        Captcha captcha = null;
        if (StringUtils.equalsIgnoreCase(code.getType(), MyConstant.GIF)) {
            captcha = new GifCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        } else {
            captcha = new SpecCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        }
        captcha.setCharType(code.getCharType());
        return captcha;
    }

    /**
     * @title setHeader
     * @description 设置头部信息
     * @author Administrator
     * @updateTime 2021/4/3 0003 19:14
     */
    private void setHeader(HttpServletResponse response, String type) {
        if (StringUtils.equalsIgnoreCase(type, MyConstant.GIF)) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }
}