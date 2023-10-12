package com.qingfeng.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingfeng.module.common.utils.PageData;
import com.qingfeng.system.entity.Post;
import com.qingfeng.system.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ProjectName PostMapper
 * @author Administrator
 * @version 1.0.0
 * @Description PostMapper
 * @createTime 2022/1/19 0019 22:54
 */
public interface PostMapper extends BaseMapper<Post> {

    //查询数据分页列表
    IPage<Post> findListPage(Page page, @Param("obj") Post post);

    //查询数据列表
    List<Post> findList(Post post);

    List<Role> findPostRoleList(PageData pd);
}