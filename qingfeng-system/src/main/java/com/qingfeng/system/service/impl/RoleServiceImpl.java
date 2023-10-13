package com.qingfeng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.common.utils.GuidUtil;
import com.qingfeng.module.common.utils.PageData;
import com.qingfeng.module.common.utils.Verify;
import com.qingfeng.system.entity.Role;
import com.qingfeng.system.entity.RoleMenu;
import com.qingfeng.system.mapper.RoleMapper;
import com.qingfeng.system.service.IRoleMenuService;
import com.qingfeng.system.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName RoleServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description RoleServiceImpl实现类
 * @createTime 2022/1/19 0019 21:55
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IRoleMenuService roleMenuService;

    /**
     * @title findListPage
     * @description 查询数据分页列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:57
     */
    public IPage<Role> findListPage(Role role, QueryRequest request){
        Page<Role> page = new Page<>(request.getPage(), request.getLimit());
        return this.baseMapper.findListPage(page, role);
    }

    /**
     * @title findList
     * @description 查询数据列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:57
     */
    public List<Role> findList(Role role){
        QueryWrapper queryWrapper = new QueryWrapper();
        if(Verify.verifyIsNotNull(role.getName())){
            queryWrapper.like("name",role.getName());
        }
        if(Verify.verifyIsNotNull(role.getShort_name())){
            queryWrapper.like("short_name",role.getShort_name());
        }
        if(Verify.verifyIsNotNull(role.getStatus())){
            queryWrapper.eq("status",role.getStatus());
        }
        return this.list(queryWrapper);
    }


    /**
     * @title findSimpleList
     * @description 查询数据列表
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:36
     */
    public List<Role> findSimpleList(PageData pd){
        return this.baseMapper.findSimpleList(pd);
    }

    /**
     * 新增数据
     * @param role
     */
    @Override
    public void saveData(Role role) {
        this.baseMapper.insert(role);
        String role_id = role.getId();
        String create_user = role.getCreate_user();
        String time = role.getCreate_time();
        List<String> menus = role.getMenus();
        List<RoleMenu> list = new ArrayList<RoleMenu>();
        for (String id:menus) {
            RoleMenu roleMenu = new RoleMenu();
            //主键id
            roleMenu.setType(0);
            roleMenu.setId(GuidUtil.getUuid());
            roleMenu.setMenu_id(id);
            roleMenu.setRole_id(role_id);
            roleMenu.setCreate_user(create_user);
            roleMenu.setCreate_time(time);
            list.add(roleMenu);
        }
        List<String> half_menus = role.getHalf_menus();
        for (String id:half_menus) {
            RoleMenu roleMenu = new RoleMenu();
            //主键id
            roleMenu.setType(1);
            roleMenu.setId(GuidUtil.getUuid());
            roleMenu.setMenu_id(id);
            roleMenu.setRole_id(role_id);
            roleMenu.setCreate_user(create_user);
            roleMenu.setCreate_time(time);
            list.add(roleMenu);
        }
        roleMenuService.saveBatch(list);
    }

    /**
     * 更新数据
     * @param role
     */
    @Override
    public void updateData(Role role) {
        this.baseMapper.updateById(role);
        String role_id = role.getId();
        String create_user = role.getUpdate_user();
        String time = role.getUpdate_time();
        List<String> menus = role.getMenus();
        List<RoleMenu> list = new ArrayList<RoleMenu>();
        for (String id:menus) {
            RoleMenu roleMenu = new RoleMenu();
            //主键id
            roleMenu.setType(0);
            roleMenu.setId(GuidUtil.getUuid());
            roleMenu.setMenu_id(id);
            roleMenu.setRole_id(role_id);
            roleMenu.setCreate_user(create_user);
            roleMenu.setCreate_time(time);
            list.add(roleMenu);
        }
        List<String> half_menus = role.getHalf_menus();
        for (String id:half_menus) {
            RoleMenu roleMenu = new RoleMenu();
            //主键id
            roleMenu.setType(1);
            roleMenu.setId(GuidUtil.getUuid());
            roleMenu.setMenu_id(id);
            roleMenu.setRole_id(role_id);
            roleMenu.setCreate_user(create_user);
            roleMenu.setCreate_time(time);
            list.add(roleMenu);
        }
        //删除
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("role_id",role_id);
        roleMenuService.remove(wrapper);
        //保存
        roleMenuService.saveBatch(list);
    }

    /**
     * 删除数据
     * @param ids
     */
    public void delData(List<String> ids) {
        this.baseMapper.deleteBatchIds(ids);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("role_id",ids);
        roleMenuService.remove(wrapper);
    }


}