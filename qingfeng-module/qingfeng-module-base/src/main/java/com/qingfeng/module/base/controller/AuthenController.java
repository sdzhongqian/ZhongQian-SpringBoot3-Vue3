package com.qingfeng.module.base.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qingfeng.module.base.entity.Authen;
import com.qingfeng.module.base.service.IAuthenService;
import com.qingfeng.module.common.base.controller.BaseController;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.common.framework.exception.BizException;
import com.qingfeng.module.common.utils.*;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName AuthenController
 * @author Administrator
 * @version 1.0.0
 * @Description 认证授权信息
 * @createTime 2022/1/19 0019 22:52
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/common/authen")
public class AuthenController extends BaseController {

    @Autowired
    private IAuthenService authenService;

    /**
     * @title listPage
     * @description 查询数据列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 22:52
     */
    @GetMapping("/listPage")
    @PreAuthorize("hasAnyAuthority('authen:info')")
    public void listPage(QueryRequest queryRequest, Authen authen,HttpServletResponse response) throws IOException {
        Map<String, Object> dataTable = MyUtil.getDataTable(authenService.findListPage(authen, queryRequest));
        this.writeJson(response,dataTable);
    }

    /**
     * @title save
     * @description 保存数据
     * @author Administrator
     * @updateTime 2022/1/19 0019 22:53
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('authen:add')")
    public void save(@Valid @RequestBody Authen authen, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 创建用户
            String id = GuidUtil.getUuid();
            authen.setId(id);
            String time = DateTimeUtil.getDateTimeStr();
            authen.setCreate_time(time);
            authen.setStatus("0");
            authen.setType("1");
            //处理数据权限
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            authen.setCreate_user(authParams.split(":")[1]);
            authen.setCreate_organize(authParams.split(":")[2]);

            this.authenService.save(authen);
            json.setSuccess(true);
            json.setMsg("新增信息成功");
        } catch (Exception e) {
            String message = "新增信息失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            e.printStackTrace();
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
    @PreAuthorize("hasAnyAuthority('authen:edit')")
    public void update(@Valid @RequestBody Authen authen,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 更新组织信息
            String time = DateTimeUtil.getDateTimeStr();
            authen.setUpdate_time(time);
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            authen.setUpdate_user(authParams.split(":")[1]);
            this.authenService.updateById(authen);
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
    @PreAuthorize("hasAnyAuthority('authen:del')")
    public void delete(@NotBlank(message = "{required}") @PathVariable String ids, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String[] del_ids = ids.split(StringPool.COMMA);
            this.authenService.removeByIds(Arrays.asList(del_ids));
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
    @PreAuthorize("hasAnyAuthority('authen:status')")
    public void updateStatus(@Valid @RequestBody Authen authen,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            this.authenService.updateById(authen);
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
    public void list(Authen authen,HttpServletResponse response) throws IOException  {
        List<Authen> list = authenService.findList(authen);
        this.writeJson(response,list);
    }

    /**
     * @title createApp
     * @description createApp
     * @author Administrator
     * @updateTime 2022/4/5 0005 22:48 
     */
    @RequestMapping(value = "/createApp", method = RequestMethod.GET)
    public void createApp(HttpServletRequest request, HttpServletResponse response) throws IOException  {
        PageData pd = new PageData(request);
        String appId = AppUtils.getAppId();
        String appSecret = AppUtils.getAppSecret(appId);
        pd.put("appId",appId);
        pd.put("appSecret",appSecret);
        Json json = new Json();
        json.setData(pd);
        json.setSuccess(true);
        json.setMsg("操作成功。");
        this.writeJson(response,json);
    }


}
