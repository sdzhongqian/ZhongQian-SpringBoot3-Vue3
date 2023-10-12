package com.qingfeng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.common.utils.DateTimeUtil;
import com.qingfeng.module.common.utils.GuidUtil;
import com.qingfeng.module.common.utils.PageData;
import com.qingfeng.module.common.utils.Verify;
import com.qingfeng.system.entity.OrganizeRole;
import com.qingfeng.system.entity.Post;
import com.qingfeng.system.entity.PostRole;
import com.qingfeng.system.entity.Role;
import com.qingfeng.system.mapper.PostMapper;
import com.qingfeng.system.service.IOrganizeRoleService;
import com.qingfeng.system.service.IPostRoleService;
import com.qingfeng.system.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName PostServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description PostServiceImpl接口
 * @createTime 2022/1/19 0019 22:55
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {

    @Autowired
    private IPostRoleService postRoleService;


    /**
     * @ProjectName PostServiceImpl
     * @author Administrator
     * @version 1.0.0
     * @Description 查询数据分页列表
     * @createTime 2022/1/19 0019 22:55
     */
    public IPage<Post> findListPage(Post post, QueryRequest request){
        Page<Post> page = new Page<>(request.getPage(), request.getLimit());
        return this.baseMapper.findListPage(page, post);
    }

    /**
     * @ProjectName PostServiceImpl
     * @author Administrator
     * @version 1.0.0
     * @Description 查询数据列表
     * @createTime 2022/1/19 0019 22:55
     */
    public List<Post> findList(Post post){
        return this.baseMapper.findList(post);
    }

    /**
     * 查询岗位权限信息
     * @param pd
     * @return
     */
    public List<Role> findPostRoleList(PageData pd){
        return this.baseMapper.findPostRoleList(pd);
    }

    /**
     * 更新权限
     * @param pd
     */
    public void updateAuth(PageData pd){
        String time = DateTimeUtil.getDateTimeStr();
        String[] role_ids = pd.get("role_ids").toString().split(",");
        //处理数据权限
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        String create_user = userParams.split(":")[1];
        //删除用户角色表。
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("post_id",pd.get("id"));
        postRoleService.remove(wrapper);
        if(Verify.verifyIsNotNull(pd.get("role_ids"))){
            String post_id = pd.get("id").toString();
            List<PostRole> list = new ArrayList<PostRole>();
            //执行保存
            for (int i = 0; i < role_ids.length; i++) {
                PostRole postRole = new PostRole();
                //主键id
                postRole.setId(GuidUtil.getUuid());
                postRole.setRole_id(role_ids[i]);
                postRole.setPost_id(post_id);
                postRole.setCreate_time(time);
                postRole.setCreate_user(create_user);
                list.add(postRole);
            }
            postRoleService.saveBatch(list);
        }
    }

}