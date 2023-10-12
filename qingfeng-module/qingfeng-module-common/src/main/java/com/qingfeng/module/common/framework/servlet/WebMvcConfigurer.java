package com.qingfeng.module.common.framework.servlet;

import com.qingfeng.module.common.utils.ParaUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 拦截器
 * Created by anxingtao on 2018-8-19.
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:" + ParaUtil.localName);
        super.addResourceHandlers(registry);
    }


}
