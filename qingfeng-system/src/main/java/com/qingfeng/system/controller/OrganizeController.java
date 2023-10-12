package com.qingfeng.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qingfeng.module.base.configure.aspect.OperationAnnotation;
import com.qingfeng.module.base.configure.aspect.RedisCache;
import com.qingfeng.module.base.configure.aspect.RedisEvict;
import com.qingfeng.module.common.base.controller.BaseController;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.common.framework.exception.BizException;
import com.qingfeng.module.common.utils.*;
import com.qingfeng.system.entity.Organize;
import com.qingfeng.system.entity.Role;
import com.qingfeng.system.entity.SystemUser;
import com.qingfeng.system.entity.UserOrganize;
import com.qingfeng.system.service.IOrganizeService;
import com.qingfeng.system.service.IRoleService;
import com.qingfeng.system.service.IUserOrganizeService;
import com.qingfeng.system.service.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName OrganizeController
 * @author Administrator
 * @version 1.0.0
 * @Description 组织信息
 * @createTime 2022/1/19 0019 21:39
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/system/organize")
public class OrganizeController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IOrganizeService organizeService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserOrganizeService userOrganizeService;


    /**
     * @title listPage
     * @description 查询数据分页列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:39
     */
    @GetMapping("/listPage")
    @PreAuthorize("hasAnyAuthority('organize:info')")
    @RedisCache(key = "cache:system:organize", fieldKey = "listPage", expired = 60)
    public Map<String, Object> listPage(QueryRequest queryRequest, Organize organize) {
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
        organize.setAuth_user(user_id);
        organize.setAuth_organize_ids(auth_organize_ids);
        Map<String, Object> dataTable = MyUtil.getDataTable(organizeService.findListPage(organize, queryRequest));
        return dataTable;
    }

    /**
     * @title save
     * @description 保存方法
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:39
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('organize:add')")
    @RedisEvict(key = "cache:system:organize", fieldKey = "")
    @OperationAnnotation(content="新增组织信息",sysType=0,opType=1,action="新增组织信息")
    public void save(@Valid @RequestBody Organize organize, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 创建用户
            String id = GuidUtil.getUuid();
            organize.setId(id);
            String time = DateTimeUtil.getDateTimeStr();
            organize.setCreate_time(time);
            organize.setStatus(0);
            organize.setType(1);
            //处理数据权限
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            organize.setCreate_user(authParams.split(":")[1]);
            organize.setCreate_organize(authParams.split(":")[2]);

            Organize porg = organizeService.getById(organize.getParent_id());
            porg.setOrg_cascade(porg.getOrg_cascade()+id+"_");
            int level_num = porg.getLevel_num()+1;
            porg.setLevel_num(level_num);

            this.organizeService.save(organize);
            json.setSuccess(true);
            json.setMsg("组织信息新增成功");
        } catch (Exception e) {
            String message = "组织信息新增失败";
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
     * @updateTime 2022/1/19 0019 21:39
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('organize:edit')")
    @RedisEvict(key = "cache:system:organize", fieldKey = "")
    @OperationAnnotation(content="更新组织信息",sysType=0,opType=3,action="更新组织信息")
    public void update(@Valid @RequestBody Organize organize,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 更新组织信息
            String time = DateTimeUtil.getDateTimeStr();
            organize.setUpdate_time(time);
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            organize.setUpdate_user(authParams.split(":")[1]);
            this.organizeService.updateById(organize);
            json.setSuccess(true);
            json.setMsg("组织信息修改成功");
        } catch (Exception e) {
            String message = "组织信息修改失败";
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
     * @updateTime 2022/1/19 0019 21:39
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAnyAuthority('organize:del')")
    @RedisEvict(key = "cache:system:organize", fieldKey = "")
    @OperationAnnotation(content="删除组织信息",sysType=0,opType=2,action="删除组织信息")
    public void delete(@NotBlank(message = "{required}") @PathVariable String ids, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String[] del_ids = ids.split(StringPool.COMMA);
            long count = organizeService.count(new LambdaQueryWrapper<Organize>().in(Organize::getParent_id,del_ids));
            if(count==0){
                this.organizeService.removeByIds(Arrays.asList(del_ids));
                json.setSuccess(true);
                json.setMsg("组织信息删除成功");
            }else{
                json.setSuccess(false);
                json.setMsg("请先删除子级数据");
            }
        } catch (Exception e) {
            String message = "组织信息删除失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * @title updateStatus
     * @description 更新状态
     * @author Administrator
     * @updateTime 2022/1/19 0019 21:39
     */
    @PostMapping("/updateStatus")
    @PreAuthorize("hasAnyAuthority('organize:status')")
    @RedisEvict(key = "cache:system:organize", fieldKey = "")
    @OperationAnnotation(content="更新组织状态信息",sysType=0,opType=3,action="更新组织状态信息")
    public void updateStatus(@Valid @RequestBody Organize organize,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            this.organizeService.updateById(organize);
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
     * @title findRoleAuth
     * @description 查询角色权限
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:07
     */
    @PostMapping("/findRoleAuth")
    public MyResponse findRoleAuth(@RequestBody PageData pd) throws BizException {
        pd.put("organize_id",pd.get("id"));
        List<Role> roleLs = roleService.findSimpleList(pd);
        List<Role> myRoleLs = organizeService.findOrganizeRoleList(pd);
        pd.put("roleLs",roleLs);
        pd.put("myRoleLs",myRoleLs);
        return new MyResponse().data(pd);
    }

    /**
     * @title updateAuth
     * @description 更新权限
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:07
     */
    @PostMapping("/updateAuth")
    @OperationAnnotation(content="更新组织权限信息",sysType=0,opType=3,action="更新组织权限信息")
    public void updateAuth(@RequestBody PageData pd,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            organizeService.updateAuth(pd);
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
     * @title getTreeList
     * @description 查询树形列表
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:07
     */
    @GetMapping("/list")
    @RedisCache(key = "cache:system:organize", fieldKey = "list", expired = 60)
    public MyResponse list(Organize organize) throws IOException  {
        //处理数据权限
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        String user_id = userParams.split(":")[1];
//        String organize_id = userParams.split(":")[2];
        PageData pd = new PageData();
        pd.put("user_id",user_id);
        PageData orgPd = userService.findUserOrganizeInfo(pd);
        List<String> auth_organize_ids = new ArrayList<String>();
        if(Verify.verifyIsNotNull(orgPd)){
            if(Verify.verifyIsNotNull(orgPd.get("authOrgIds"))){
                auth_organize_ids = Arrays.asList(orgPd.get("authOrgIds").toString().split(","));
            }
        }
        organize.setAuth_user(user_id);
        organize.setAuth_organize_ids(auth_organize_ids);
        //获取用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",user_id);
        SystemUser user = userService.getOne(queryWrapper);
        if(user.getType()!=0){//管理员
            organize.setOrg_cascade(orgPd.get("org_cascade").toString());
        }
        List<Organize> list = organizeService.findList(organize);
        return new MyResponse().data(list);
    }


}
