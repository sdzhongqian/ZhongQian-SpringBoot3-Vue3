package com.qingfeng.framework.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingfeng.framework.security.entity.dto.AuthUser;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * 表服务接口
 *
 * @author makejava
 * @since 2023-08-09 10:12:43
 */
public interface UserDetailService extends IService<AuthUser>, UserDetailsService {

}

