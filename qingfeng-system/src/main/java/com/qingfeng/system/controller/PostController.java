package com.qingfeng.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qingfeng.module.base.configure.aspect.OperationAnnotation;
import com.qingfeng.module.common.base.controller.BaseController;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.common.framework.exception.BizException;
import com.qingfeng.system.entity.Post;
import com.qingfeng.system.entity.Role;
import com.qingfeng.system.entity.UserOrganize;
import com.qingfeng.system.service.IPostService;
import com.qingfeng.system.service.IRoleService;
import com.qingfeng.system.service.IUserOrganizeService;
import com.qingfeng.system.service.IUserService;
import com.qingfeng.module.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName PostController
 * @author Administrator
 * @version 1.0.0
 * @Description 岗位管理
 * @createTime 2022/1/19 0019 22:52
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/system/post")
public class PostController extends BaseController {

    @Autowired
    private IPostService postService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserOrganizeService userOrganizeService;

    /**
     * @title listPage
     * @description 查询数据列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 22:52
     */
    @GetMapping("/listPage")
    @PreAuthorize("hasAnyAuthority('post:info')")
    public Map<String, Object> listPage(QueryRequest queryRequest, Post post) {
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
        post.setAuth_user(user_id);
        post.setAuth_organize_ids(auth_organize_ids);
        Map<String, Object> dataTable = MyUtil.getDataTable(postService.findListPage(post, queryRequest));
        return dataTable;
    }

    /**
     * @title save
     * @description 保存数据
     * @author Administrator
     * @updateTime 2022/1/19 0019 22:53
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('post:add')")
    public void save(@Valid @RequestBody Post post,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 创建用户
            String id = GuidUtil.getUuid();
            post.setId(id);
            String time = DateTimeUtil.getDateTimeStr();
            post.setCreate_time(time);
            post.setStatus(0);
            post.setType(1);
            //处理数据权限
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            post.setCreate_user(authParams.split(":")[1]);
            post.setCreate_organize(authParams.split(":")[2]);

            this.postService.save(post);
            json.setSuccess(true);
            json.setMsg("新增信息成功");
        } catch (Exception e) {
            String message = "新增信息失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * @title update
     * @description 更新数据
     * @author Administrator
     * @updateTime 2022/1/19 0019 22:53
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('post:edit')")
    public void update(@Valid @RequestBody Post post,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 更新组织信息
            String time = DateTimeUtil.getDateTimeStr();
            post.setUpdate_time(time);
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            post.setUpdate_user(authParams.split(":")[1]);
            this.postService.updateById(post);
            json.setSuccess(true);
            json.setMsg("修改信息成功");
        } catch (Exception e) {
            String message = "修改信息失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * @title delete
     * @description 删除数据
     * @author Administrator
     * @updateTime 2022/1/19 0019 22:53
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAnyAuthority('post:del')")
    public void delete(@NotBlank(message = "{required}") @PathVariable String ids,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String[] del_ids = ids.split(StringPool.COMMA);
            this.postService.removeByIds(Arrays.asList(del_ids));
            json.setSuccess(true);
            json.setMsg("删除信息成功");
        } catch (Exception e) {
            String message = "删除信息失败";
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
     * @updateTime 2022/1/19 0019 22:53
     */
    @PostMapping("/updateStatus")
    @PreAuthorize("hasAnyAuthority('post:status')")
    public void updateStatus(@Valid @RequestBody Post post,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            this.postService.updateById(post);
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
     * @title list
     * @description 查询数据列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 22:53
     */
    @GetMapping("/list")
    public MyResponse list(Post post) throws IOException  {
        //处理数据权限
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        String user_id = userParams.split(":")[1];
        PageData pd = new PageData();
        pd.put("user_id",user_id);
        PageData orgPd = userService.findUserOrganizeInfo(pd);
        List<String> auth_organize_ids = new ArrayList<String>();
        if(Verify.verifyIsNotNull(orgPd)){
            if(Verify.verifyIsNotNull(orgPd.get("authOrgIds"))){
                auth_organize_ids = Arrays.asList(orgPd.get("authOrgIds").toString().split(","));
            }
        }
        post.setAuth_user(user_id);
        post.setAuth_organize_ids(auth_organize_ids);
        List<Post> list = postService.findList(post);
        return new MyResponse().data(list);
    }


    /**
     * @title findRoleAuth
     * @description 查询角色权限
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:07
     */
    @PostMapping("/findRoleAuth")
    public MyResponse findRoleAuth(@RequestBody PageData pd) throws BizException {
        pd.put("post_id",pd.get("id"));
        List<Role> roleLs = roleService.findSimpleList(pd);
        List<Role> myRoleLs = postService.findPostRoleList(pd);
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
            postService.updateAuth(pd);
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

}
