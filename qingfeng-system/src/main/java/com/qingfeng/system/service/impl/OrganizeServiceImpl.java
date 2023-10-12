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
import com.qingfeng.system.entity.Organize;
import com.qingfeng.system.entity.OrganizeRole;
import com.qingfeng.system.entity.Role;
import com.qingfeng.system.mapper.OrganizeMapper;
import com.qingfeng.system.service.IOrganizeRoleService;
import com.qingfeng.system.service.IOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName OrganizeServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description 组织service实现类
 * @createTime 2022/1/19 0019 21:41
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OrganizeServiceImpl extends ServiceImpl<OrganizeMapper, Organize> implements IOrganizeService {

    @Autowired
    private IOrganizeRoleService organizeRoleService;

    /**
     * @title findListPage
     * @description 查询数据分页列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:41
     */
    public IPage<Organize> findListPage(Organize organize, QueryRequest request){
        Page<Organize> page = new Page<>(request.getPage(), request.getLimit());
        return this.baseMapper.findListPage(page, organize);
    }

    /**
     * @title findList
     * @description 查询列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:41
     */
    public List<Organize> findList(Organize organize){
        return this.baseMapper.findList(organize);
    }


    /**
     * @title findOrganizeRoleList
     * @description 查询组织角色列表
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:35
     */
    public List<Role> findOrganizeRoleList(PageData pd){
        return this.baseMapper.findOrganizeRoleList(pd);
    }

    /**
     * @title findTreeTableList
     * @description 查询数据权限
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:35
     */
    public List<PageData> findTreeTableList(PageData pd){
        return this.baseMapper.findTreeTableList(pd);
    }

    /**
     * @title updateAuth
     * @description 更新权限
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:35
     */
    public void updateAuth(PageData pd){
        String time = DateTimeUtil.getDateTimeStr();
        String[] role_ids = pd.get("role_ids").toString().split(",");
        //处理数据权限
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        String create_user = userParams.split(":")[1];
        //删除用户角色表。
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("organize_id",pd.get("id"));
        organizeRoleService.remove(wrapper);
        if(Verify.verifyIsNotNull(pd.get("role_ids"))){
            String organize_id = pd.get("id").toString();
            List<OrganizeRole> list = new ArrayList<OrganizeRole>();
            //执行保存
            for (int i = 0; i < role_ids.length; i++) {
                OrganizeRole organizeRole = new OrganizeRole();
                //主键id
                organizeRole.setId(GuidUtil.getUuid());
                organizeRole.setRole_id(role_ids[i]);
                organizeRole.setOrganize_id(organize_id);
                organizeRole.setCreate_time(time);
                organizeRole.setCreate_user(create_user);
                list.add(organizeRole);
            }
            organizeRoleService.saveBatch(list);
        }
    }


    /**
     * @title findOrganizeList
     * @description 查询组织列表-流程候选组
     * @author Administrator
     * @updateTime 2021/9/11 0011 10:13
     */
    public List<PageData> findOrganizeList(PageData pd){
        return this.baseMapper.findOrganizeList(pd);
    }

    public List<PageData> findSelectList(Organize organize){
        return this.baseMapper.findSelectList(organize);
    }
}