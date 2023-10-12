package com.qingfeng.framework.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingfeng.framework.security.entity.dto.AuthUser;
import com.qingfeng.module.common.utils.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * (Account)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-09 10:12:40
 */
@Mapper
public interface UserDetailMapper extends BaseMapper<AuthUser> {

    /**
     * @name findUserInfo
     * @description 查询用户详情
     * @author anzi
     * @create 2023/9/14 13:57
     **/
    public PageData findUserInfo(PageData pd);

    /**
     * @name findUserOrganizeInfo
     * @description 查询用户组织信息
     * @author anzi
     * @create 2023/9/14 13:57
     **/
    public PageData findUserOrganizeInfo(PageData pd);

    /**
     * @name findUserPermissions
     * @description 查询用户权限集合
     * @author anzi
     * @create 2023/9/14 13:58
     **/
    public List<PageData> findUserPermissions(PageData pd);

}
