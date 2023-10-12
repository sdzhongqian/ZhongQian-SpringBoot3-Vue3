package com.qingfeng.framework.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.framework.security.entity.dto.AuthUser;
import com.qingfeng.framework.security.mapper.UserDetailMapper;
import com.qingfeng.framework.security.service.UserDetailService;
import com.qingfeng.module.common.utils.PageData;
import com.qingfeng.module.common.utils.Verify;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (Account)表服务实现类
 *
 * @author makejava
 * @since 2023-08-09 10:12:45
 */
@Service
public class UserDetailServiceImpl extends ServiceImpl<UserDetailMapper, AuthUser> implements UserDetailService {

    @Autowired
    private UserDetailMapper userManager;


    /**
     *实现loadUserByUsername方法
     *  1. 从数据库查询用户信息（登录功能）
     *  2. 如果查到，就再去查询对应的权限信息（授权功能）
     *  3. 封装成UserDetails对象返回（这要求再去创建一个UserDetails的实现类）
     *  注意：由于实现通过用户名/邮箱登录，因此这里传进来的username参数实际上有可能是邮箱
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PageData pd = new PageData();
        if(username.contains(":")){
            pd.put("login_name",username.split(":")[0]);
        }else{
            pd.put("login_name",username);
        }
        PageData uPd = userManager.findUserInfo(pd);
        if(Verify.verifyIsNotNull(uPd)){
            if(uPd.get("status").equals("2")){
                throw new UsernameNotFoundException("账号已休眠，请联系管理员");
            }else{
                if(uPd.get("status").equals("0")){
                    //查询当前用户组织
                    pd.put("user_id",uPd.get("id"));
                    PageData orgPd = userManager.findUserOrganizeInfo(pd);
                    String organize_id = "";
                    if(Verify.verifyIsNotNull(orgPd)){
                        organize_id = orgPd.get("organize_id")+"";
                    }
                    //登录成功
                    pd.put("user_id",uPd.get("id"));
                    List<PageData> userPermissions = userManager.findUserPermissions(pd);
                    String permissions = userPermissions.stream().map(perms->perms.get("perms").toString()).collect(Collectors.joining(","));
//                    System.out.println("===================================查询出来数据权限==============================");
//                    System.out.println(permissions);
                    boolean notLocked = false;
                    if (StringUtils.equals("0", uPd.get("status").toString()))
                        notLocked = true;

                    List<GrantedAuthority> grantedAuthorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
                    AuthUser authUser = new AuthUser(uPd.get("login_name").toString()+":"+uPd.get("id").toString()+":"+organize_id, uPd.get("login_password").toString(), true, true, true, notLocked,
                            grantedAuthorityList);
                    return transToAuthUser(authUser,uPd);
                }else if(uPd.getString("status").equals("1")){
                    throw new UsernameNotFoundException("账号已禁用，请联系管理员");
                }else if(uPd.getString("status").equals("2")){
                    throw new UsernameNotFoundException("账号已休眠，请联系管理员");
                }
            }
            return null;
        }else{
            throw new UsernameNotFoundException("登录名称不存在，请重新输入。");
        }
    }

    /**
     * @title transToAuthUser
     * @description 用户信息转换
     * @author Administrator
     * @updateTime 2021/4/3 0003 19:13
     * @throws
     */
    private AuthUser transToAuthUser(AuthUser authUser, PageData uPd) {
        authUser.setId(uPd.getString("id"));
        authUser.setType(uPd.getString("type"));
        authUser.setLogin_name(uPd.getString("login_name"));
        authUser.setLogin_password(uPd.getString("login_password"));
        authUser.setName(uPd.getString("name"));
        authUser.setSex(uPd.getString("sex"));
        authUser.setPhone(uPd.getString("phone"));
        authUser.setEmail(uPd.getString("email"));
        authUser.setBirth_date(uPd.getString("birth_date"));
        authUser.setLive_address(uPd.getString("live_address"));
        authUser.setBirth_address(uPd.getString("birth_address"));
        authUser.setHead_address(uPd.getString("head_address"));
        authUser.setMotto(uPd.getString("motto"));
        authUser.setStatus(uPd.getString("status"));
        authUser.setOrder_by(uPd.getString("order_by"));
        authUser.setLast_login_time(uPd.getString("last_login_time"));
        authUser.setBrowser(uPd.getString("browser"));
        authUser.setOs(uPd.getString("os"));
        authUser.setIpaddr(uPd.getString("ipaddr"));
        authUser.setIprealaddr(uPd.getString("iprealaddr"));
        authUser.setStatus(uPd.getString("status"));
        return authUser;
    }



}

