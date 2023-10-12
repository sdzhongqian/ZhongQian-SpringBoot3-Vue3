package com.qingfeng.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.common.utils.PageData;
import com.qingfeng.system.entity.Post;
import com.qingfeng.system.entity.Role;

import java.util.List;

/**
 * @ProjectName IPostService
 * @author Administrator
 * @version 1.0.0
 * @Description IPostService接口
 * @createTime 2022/1/19 0019 22:55
 */
public interface IPostService extends IService<Post> {

    //查询数据分页列表
    IPage<Post> findListPage(Post post, QueryRequest request);

    //查询数据列表
    List<Post> findList(Post post);

    List<Role> findPostRoleList(PageData pd);

    //更新权限
    void updateAuth(PageData pd);

}