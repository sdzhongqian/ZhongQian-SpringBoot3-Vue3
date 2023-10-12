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
import com.qingfeng.system.entity.Area;
import com.qingfeng.system.entity.UserOrganize;
import com.qingfeng.system.service.IAreaService;
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
 * @ProjectName AreaController
 * @author Administrator
 * @version 1.0.0
 * @Description 地区信息
 * @createTime 2022/1/19 0019 23:41
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/system/area")
public class AreaController extends BaseController {

    @Autowired
    private IAreaService areaService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserOrganizeService userOrganizeService;

    /**
     * @title listPage
     * @description 查询数据分页列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 23:41
     */
    @GetMapping("/listPage")
    @PreAuthorize("hasAnyAuthority('area:info')")
    @RedisCache(key = "cache:system:area", fieldKey = "listPage", expired = 60)
    public Map<String, Object> listPage(QueryRequest queryRequest, Area area) {
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
        area.setAuth_user(user_id);
        area.setAuth_organize_ids(auth_organize_ids);
        Map<String, Object> dataTable = MyUtil.getDataTable(areaService.findListPage(area, queryRequest));
        return dataTable;
    }

    /**
     * @title save
     * @description 保存数据
     * @author Administrator
     * @updateTime 2022/1/19 0019 23:41
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('area:add')")
    @RedisEvict(key = "cache:system:area", fieldKey = "")
    @OperationAnnotation(content="新增地区信息",sysType=0,opType=1,action="新增地区信息")
    public void save(@Valid @RequestBody Area area, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 创建用户
            String id = GuidUtil.getUuid();
            area.setId(id);
            String time = DateTimeUtil.getDateTimeStr();
            area.setCreate_time(time);
            area.setStatus(0);
            area.setType(1);
            //处理数据权限
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            area.setCreate_user(authParams.split(":")[1]);
            area.setCreate_organize(authParams.split(":")[2]);

            Area parea = areaService.getById(area.getParent_id());
            area.setArea_cascade(parea.getArea_cascade()+id+"_");
            int level_num = Integer.parseInt(parea.getLevel_num())+1;
            area.setLevel_num(level_num+"");
            this.areaService.save(area);
            json.setSuccess(true);
            json.setMsg("地区信息新增成功");
        } catch (Exception e) {
            json.setSuccess(false);
            String message = "地区信息新增失败";
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
     * @updateTime 2022/1/19 0019 23:41
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('area:edit')")
    @RedisEvict(key = "cache:system:area", fieldKey = "")
    @OperationAnnotation(content="修改地区信息",sysType=0,opType=3,action="修改地区信息")
    public void update(@Valid @RequestBody Area area,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 更新组织信息
            String time = DateTimeUtil.getDateTimeStr();
            area.setUpdate_time(time);
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            area.setUpdate_user(authParams.split(":")[1]);
            this.areaService.updateById(area);
            json.setSuccess(true);
            json.setMsg("地区信息修改成功");
        } catch (Exception e) {
            json.setSuccess(false);
            String message = "地区信息修改失败";
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
     * @updateTime 2022/1/19 0019 23:41
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAnyAuthority('area:del')")
    @RedisEvict(key = "cache:system:area", fieldKey = "")
    @OperationAnnotation(content="删除地区信息",sysType=0,opType=2,action="删除地区信息")
    public void delete(@NotBlank(message = "{required}") @PathVariable String ids, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String[] del_ids = ids.split(StringPool.COMMA);
            long count = areaService.count(new LambdaQueryWrapper<Area>().in(Area::getParent_id,del_ids));
            if(count==0){
                this.areaService.removeByIds(Arrays.asList(del_ids));
                json.setSuccess(true);
                json.setMsg("地区信息删除成功");
            }else{
                json.setSuccess(false);
                json.setMsg("请先删除子级数据");
            }
        } catch (Exception e) {
            String message = "地区信息删除失败";
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
     * @updateTime 2022/1/19 0019 23:41
     */
    @PostMapping("/updateStatus")
    @PreAuthorize("hasAnyAuthority('area:status')")
    @RedisEvict(key = "cache:system:area", fieldKey = "")
    @OperationAnnotation(content="修改地区状态信息",sysType=0,opType=3,action="修改地区状态信息")
    public void updateStatus(@Valid @RequestBody Area area,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            this.areaService.updateById(area);
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
     * @title findList
     * @description 查询数据列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 23:42
     */
    @GetMapping("/list")
    @RedisCache(key = "cache:system:area", fieldKey = "list", expired = 60)
    public MyResponse list(Area area) throws IOException  {
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
        area.setAuth_user(user_id);
        area.setAuth_organize_ids(auth_organize_ids);
        List<Area> list = areaService.findList(area);
        return new MyResponse().data(list);
    }

}
