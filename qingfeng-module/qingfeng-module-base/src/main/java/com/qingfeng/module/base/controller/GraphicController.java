package com.qingfeng.module.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qingfeng.module.base.entity.Graphic;
import com.qingfeng.module.base.service.IGraphicService;
import com.qingfeng.module.common.base.controller.BaseController;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.common.framework.exception.BizException;
import com.qingfeng.module.common.utils.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @ProjectName GraphicController
 * @author Administrator
 * @version 1.0.0
 * @Description graphic
 * @createTime 2022/6/3 0003 22:53
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/graphic")
public class GraphicController extends BaseController {

    @Autowired
    private IGraphicService graphicService;

    /**
     * @title findListPage
     * @description 查询数据分页列表
     * @author Administrator
     * @updateTime 2022/6/25 0025 21:51
     */
    @GetMapping("/findListPage")
    @PreAuthorize("hasAnyAuthority('graphic:info')")
    public Map<String, Object> findListPage(QueryRequest queryRequest, Graphic graphic) {
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        IPage<Graphic> pages = graphicService.findListPage(graphic, queryRequest);
        for (Graphic g:pages.getRecords()) {
            g.setShow_tpdz(ParaUtil.cloudfile+g.getTpdz());
        }
        Map<String, Object> dataTable = MyUtil.getDataTable(pages);
        return dataTable;
    }

    /**
     * @title save
     * @description 保存方法
     * @author Administrator
     * @updateTime 2022/6/25 0025 21:51
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('graphic:add')")
    public void save(@Valid @RequestBody Graphic graphic, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 创建用户
            String id = GuidUtil.getUuid();
            graphic.setId(id);
            String time = DateTimeUtil.getDateTimeStr();
            graphic.setCreate_time(time);
            graphic.setStatus(1);
            graphic.setRead_num(0);
            //处理数据权限
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            graphic.setCreate_user(authParams.split(":")[1]);
            graphic.setCreate_organize(authParams.split(":")[2]);
            this.graphicService.save(graphic);
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
     * @description 更新方法
     * @author Administrator
     * @updateTime 2022/6/25 0025 21:51
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('graphic:edit')")
    public void update(@Valid @RequestBody Graphic graphic,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            // 更新组织信息
            String time = DateTimeUtil.getDateTimeStr();
            String authParams = SecurityContextHolder.getContext().getAuthentication().getName();
            graphic.setUpdate_time(time);
            graphic.setUpdate_user(authParams.split(":")[1]);
            this.graphicService.updateById(graphic);
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
     * @description 删除方法
     * @author Administrator
     * @updateTime 2022/6/25 0025 21:52
     */
    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAnyAuthority('graphic:del')")
    public void delete(@NotBlank(message = "{required}") @PathVariable String ids, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String[] del_ids = ids.split(StringPool.COMMA);
            this.graphicService.removeByIds(Arrays.asList(del_ids));
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
     * @updateTime 2022/6/25 0025 21:52
     */
    @PostMapping("/updateStatus")
    @PreAuthorize("hasAnyAuthority('graphic:status')")
    public void updateStatus(@Valid @RequestBody Graphic graphic,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            this.graphicService.updateById(graphic);
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
     * @title updateOneStatus
     * @description 更新状态
     * @author Administrator
     * @updateTime 2022/10/15 0015 13:57
     */
    @PostMapping("/updateOneStatus")
    @PreAuthorize("hasAnyAuthority('graphic:status')")
    public void updateOneStatus(@Valid @RequestBody Graphic graphic,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            Graphic grap = new Graphic();
            grap.setStatus(1);
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("type",graphic.getType());
            this.graphicService.update(grap,queryWrapper);

            this.graphicService.updateById(graphic);
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
     * @title updateReadNum
     * @description 更新阅读量
     * @author Administrator
     * @updateTime 2022/6/25 0025 21:53
     */
    @PostMapping("/updateReadNum")
    @PreAuthorize("hasAnyAuthority('graphic:edit')")
    public void updateReadNum(@Valid @RequestBody Graphic graphic,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            Graphic g = graphicService.getById(graphic.getId());
            g.setRead_num(g.getRead_num()+1);
            this.graphicService.updateById(g);
            json.setSuccess(true);
        } catch (Exception e) {
            json.setSuccess(false);
            throw new BizException(e.toString());
        }
        this.writeJson(response,json);
    }

    //=========================================uniapp端接口=================================

    /**
     * @title findList
     * @description 查询分页列表
     * @author Administrator
     * @updateTime 2022/9/28 0028 23:37
     */
    @GetMapping("/findList")
    public MyResponse findList(Graphic graphic) {
        graphic.setStatus(0);
        if(Verify.verifyIsNull(graphic.getLimit())){
            graphic.setLimit(5);
        }
        List<Graphic> list = graphicService.findList(graphic);
        for (Graphic g:list) {
            g.setShow_tpdz(ParaUtil.cloudfile+g.getTpdz());
        }
        return new MyResponse().data(list);
    }

    /**
     * @title findInfo
     * @description 查询详情
     * @author Administrator
     * @updateTime 2022/9/29 0029 21:07
     */
    @GetMapping("/findInfo")
    public MyResponse findInfo(Graphic graphic) {
        Graphic data = graphicService.getById(graphic.getId());
        if(Verify.verifyIsNotNull(data.getTpdz())){
            data.setShow_tpdz(ParaUtil.cloudfile+data.getTpdz());
        }
        return new MyResponse().data(data);
    }

    /**
     * 根据类型查询当前启用的数据
     * @param graphic
     * @return
     */
    @GetMapping("/findInfoForType")
    public MyResponse findInfoForType(Graphic graphic) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type",graphic.getType());
        queryWrapper.eq("status","0");
        queryWrapper.orderByAsc(Arrays.asList(new String[] {"status","order_by","create_time"}));
        queryWrapper.last("limit 1");
        Graphic data = graphicService.getOne(queryWrapper);
        if(Verify.verifyIsNotNull(data.getTpdz())){
            data.setShow_tpdz(ParaUtil.cloudfile+data.getTpdz());
        }
        return new MyResponse().data(data);
    }




}