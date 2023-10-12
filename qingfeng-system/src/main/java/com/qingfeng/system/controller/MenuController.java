package com.qingfeng.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qingfeng.module.base.configure.aspect.OperationAnnotation;
import com.qingfeng.module.base.configure.aspect.RedisCache;
import com.qingfeng.module.base.configure.aspect.RedisEvict;
import com.qingfeng.module.common.base.controller.BaseController;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.common.framework.exception.BizException;
import com.qingfeng.module.common.utils.*;
import com.qingfeng.system.entity.Menu;
import com.qingfeng.system.entity.UserOrganize;
import com.qingfeng.system.service.IMenuService;
import com.qingfeng.system.service.IUserOrganizeService;
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
 * @ProjectName MenuController
 * @author Administrator
 * @version 1.0.0
 * @Description 菜单信息
 * @createTime 2022/1/19 0019 22:43
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/system/menu")
public class MenuController extends BaseController {

    @Autowired
    private IMenuService menuService;
    @Autowired
    private IUserOrganizeService userOrganizeService;

    /**
     * @title listPage
     * @description TODO
     * @author Administrator
     * @updateTime 2022/1/19 0019 22:43
     */
    @GetMapping("/listPage")
    @PreAuthorize("hasAnyAuthority('menu:info')")
    @RedisCache(key = "cache:system:menu", fieldKey = "listPage", expired = 60)
    public Map<String, Object> listPage(QueryRequest queryRequest, Menu menu) {
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
        menu.setAuth_user(user_id);
        menu.setAuth_organize_ids(auth_organize_ids);
        Map<String, Object> dataTable = MyUtil.getDataTable(menuService.findListPage(menu, queryRequest));
        return dataTable;
    }

    /**
     * @title save
     * @description 保存数据
     * @author Administrator
     * @updateTime 2022/1/19 0019 22:43
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('menu:add')")
    @RedisEvict(key = "cache:system:menu", fieldKey = "")
    @OperationAnnotation(content="新增菜单信息",sysType=0,opType=1,action="新增菜单信息")
    public void save(@Valid @RequestBody Menu menu, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 创建用户
            String id = GuidUtil.getUuid();
            menu.setId(id);
            String time = DateTimeUtil.getDateTimeStr();
            menu.setCreate_time(time);
            menu.setStatus(0);
            //处理数据权限
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            menu.setCreate_user(authParams.split(":")[1]);
            menu.setCreate_organize(authParams.split(":")[2]);
            menu.setTitle(menu.getName());
            menu.setMenu_cascade(menu.getMenu_cascade()+id+"_");
            int level_num = menu.getLevel_num()+1;
            menu.setLevel_num(level_num);
            this.menuService.save(menu);
            json.setSuccess(true);
            json.setMsg("菜单信息新增成功");
        } catch (Exception e) {
            String message = "菜单信息新增失败";
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
     * @updateTime 2022/1/19 0019 22:43
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('menu:edit')")
    @RedisEvict(key = "cache:system:menu", fieldKey = "")
    @OperationAnnotation(content="编辑菜单信息",sysType=0,opType=3,action="编辑菜单信息")
    public void update(@Valid @RequestBody Menu menu,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 更新组织信息
            String time = DateTimeUtil.getDateTimeStr();
            menu.setUpdate_time(time);
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            menu.setUpdate_user(authParams.split(":")[1]);
            menu.setTitle(menu.getName());
            this.menuService.updateById(menu);
            json.setSuccess(true);
            json.setMsg("菜单信息修改成功");
        } catch (Exception e) {
            String message = "菜单信息修改失败";
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
     * @updateTime 2022/1/19 0019 22:44
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAnyAuthority('menu:del')")
    @RedisEvict(key = "cache:system:menu", fieldKey = "")
    @OperationAnnotation(content="删除菜单信息",sysType=0,opType=2,action="删除菜单信息")
    public void delete(@NotBlank(message = "{required}") @PathVariable String ids, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String[] del_ids = ids.split(StringPool.COMMA);
            long count = menuService.count(new LambdaQueryWrapper<Menu>().in(Menu::getParent_id,del_ids));
            if(count==0){
                this.menuService.removeByIds(Arrays.asList(del_ids));
                json.setSuccess(true);
                json.setMsg("菜单信息删除成功");
            }else{
                json.setSuccess(false);
                json.setMsg("请先删除子级数据");
            }
        } catch (Exception e) {
            String message = "菜单信息删除失败";
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
     * @updateTime 2022/1/19 0019 22:44
     */
    @PostMapping("/updateStatus")
    @PreAuthorize("hasAnyAuthority('menu:status')")
    @RedisEvict(key = "cache:system:menu", fieldKey = "")
    @OperationAnnotation(content="修改菜单状态信息",sysType=0,opType=3,action="修改菜单状态信息")
    public void updateStatus(@Valid @RequestBody Menu menu,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            this.menuService.updateById(menu);
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
     * @title: findList
     * @description: 查询菜单列表
     * @author: qingfeng
     * @date: 2021/3/9 0009 23:05
     */
    @PostMapping("/list")
    @RedisCache(key = "cache:system:menu", fieldKey = "list", expired = 60)
    public MyResponse list(@RequestBody PageData pd) throws IOException {
        if(Verify.verifyIsNotNull(pd.get("types"))){
            List<String> list = Arrays.asList(pd.get("types").toString().split(","));
            pd.put("typeList",list);
        }
        List<PageData> list = menuService.findList(pd);
        return new MyResponse().data(list);
    }


    @GetMapping("/info")
    @RedisCache(key = "cache:system:menu", fieldKey = "info", expired = 60)
    public MyResponse info(Menu menu) throws IOException {
        Menu minfo = menuService.getById(menu.getId());
        return new MyResponse().data(minfo);
    }

}