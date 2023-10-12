package com.qingfeng.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qingfeng.module.base.configure.aspect.OperationAnnotation;
import com.qingfeng.module.base.configure.aspect.RedisCache;
import com.qingfeng.module.base.configure.aspect.RedisEvict;
import com.qingfeng.module.common.base.controller.BaseController;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.common.framework.exception.BizException;
import com.qingfeng.module.common.utils.*;
import com.qingfeng.system.entity.Role;
import com.qingfeng.system.entity.RoleMenu;
import com.qingfeng.system.entity.UserOrganize;
import com.qingfeng.system.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName RoleController
 * @author Administrator
 * @version 1.0.0
 * @Description 角色信息
 * @createTime 2022/1/19 0019 21:56
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrganizeService organizeService;
    @Autowired
    private IRoleMenuService roleMenuService;
    @Autowired
    private IUserOrganizeService userOrganizeService;

    /**
     * @title listPage
     * @description 查询数据分页列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:56
     */
    @GetMapping("/listPage")
    @PreAuthorize("hasAnyAuthority('role:info')")
    @RedisCache(key = "cache:system:role", fieldKey = "listPage", expired = 60)
    public Map<String, Object> listPage(QueryRequest queryRequest, Role role) {
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        //处理数据权限
        String user_id = userParams.split(":")[1];
        UserOrganize uoParam = new UserOrganize();
        uoParam.setUser_id(user_id);
        UserOrganize userOrganize = userOrganizeService.findUserOrganizeInfo(uoParam);
        List<String> auth_organize_ids = new ArrayList<String>();
        if(Verify.verifyIsNotNull(userOrganize)){
            if(Verify.verifyIsNotNull(userOrganize.getAuthOrgIds())){
                auth_organize_ids = Arrays.asList(userOrganize.getAuthOrgIds().split(","));
            }
        }
        role.setAuth_user(user_id);
        role.setAuth_organize_ids(auth_organize_ids);
        Map<String, Object> dataTable = MyUtil.getDataTable(roleService.findListPage(role, queryRequest));
        return dataTable;
    }

    /**
     * @title save
     * @description 保存方法
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:56
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('role:add')")
    @RedisEvict(key = "cache:system:role", fieldKey = "")
    @OperationAnnotation(content="保存角色信息",sysType=0,opType=1,action="保存角色信息")
    public void save(@Valid @RequestBody Role role, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 创建用户
            String id = GuidUtil.getUuid();
            role.setId(id);
            String time = DateTimeUtil.getDateTimeStr();
            role.setCreate_time(time);
            role.setStatus(0);
            role.setType(1);
            //处理数据权限
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            role.setCreate_user(authParams.split(":")[1]);
            role.setCreate_organize(authParams.split(":")[2]);
            this.roleService.saveData(role);
            json.setSuccess(true);
            json.setMsg("角色信息新增成功");
        } catch (Exception e) {
            String message = "角色信息新增失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * @title update
     * @description 更新方法
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:56
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('role:edit')")
    @RedisEvict(key = "cache:system:role", fieldKey = "")
    @OperationAnnotation(content="更新角色信息",sysType=0,opType=3,action="更新角色信息")
    public void update(@Valid @RequestBody Role role,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 更新组织信息
            String time = DateTimeUtil.getDateTimeStr();
            role.setUpdate_time(time);
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            role.setUpdate_user(authParams.split(":")[1]);
            this.roleService.updateData(role);
            json.setSuccess(true);
            json.setMsg("角色信息修改成功");
        } catch (Exception e) {
            String message = "角色信息修改失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * @title delete
     * @description 删除方法
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:56
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAnyAuthority('role:del')")
    @RedisEvict(key = "cache:system:role", fieldKey = "")
    @OperationAnnotation(content="删除角色信息",sysType=0,opType=2,action="删除角色信息")
    public void delete(@NotBlank(message = "{required}") @PathVariable String ids, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String[] del_ids = ids.split(StringPool.COMMA);
            this.roleService.delData(Arrays.asList(del_ids));
            json.setSuccess(true);
            json.setMsg("角色信息删除成功");
        } catch (Exception e) {
            String message = "角色信息删除失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * @title 更新状态
     * @description 更新状态
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:57
     */
    @PostMapping("/updateStatus")
    @PreAuthorize("hasAnyAuthority('role:status')")
    @RedisEvict(key = "cache:system:role", fieldKey = "")
    @OperationAnnotation(content="更新角色状态信息",sysType=0,opType=3,action="更新角色状态信息")
    public void updateStatus(@Valid @RequestBody Role role,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            this.roleService.updateById(role);
            json.setSuccess(true);
            json.setMsg("状态修改成功");
        } catch (Exception e) {
            String message = "状态修改失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }


    /**
     * @title updateAuth
     * @description 更新权限
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:09
     */
    @PostMapping("/updateAuth")
    @OperationAnnotation(content="更新权限信息",sysType=0,opType=3,action="更新权限信息")
    public void updateAuth(@RequestBody PageData pd,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
            String create_user = userParams.split(":")[1];

            String role_id = pd.get("role_id").toString();
            String time = DateTimeUtil.getDateTimeStr();
            String[] ids = pd.get("ids").toString().split(",");
//            pd.put("menu_ids", Arrays.asList(ids));
            roleMenuService.delRoleMenu(pd);
            List<RoleMenu> list = new ArrayList<RoleMenu>();
            for (int i = 0; i < ids.length; i++) {
                RoleMenu roleMenu = new RoleMenu();
                //主键id
                roleMenu.setType("0");
                roleMenu.setId(GuidUtil.getUuid());
                roleMenu.setMenu_id(ids[i]);
                roleMenu.setRole_id(role_id);
                roleMenu.setCreate_user(create_user);
                roleMenu.setCreate_time(time);
                list.add(roleMenu);
            }
            String[] halfIds = pd.get("halfIds").toString().split(",");
            for (int i = 0; i < halfIds.length; i++) {
                RoleMenu roleMenu = new RoleMenu();
                //主键id
                roleMenu.setType("1");
                roleMenu.setId(GuidUtil.getUuid());
                roleMenu.setMenu_id(halfIds[i]);
                roleMenu.setRole_id(role_id);
                roleMenu.setCreate_user(create_user);
                roleMenu.setCreate_time(time);
                list.add(roleMenu);
            }
            roleMenuService.saveBatch(list);
            json.setSuccess(true);
            json.setMsg("权限信息更新成功");
        } catch (Exception e) {
            String message = "权限信息更新失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * @title findRoleMenuList
     * @description 查询角色菜单信息
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:09
     */
    @PostMapping("/findRoleMenuList")
    public MyResponse findRoleMenuList(@RequestBody PageData pd) throws BizException {
        List<PageData> roleMenuList = roleMenuService.findRoleMenuList(pd);
        return new MyResponse().data(roleMenuList);
    }


}
