package com.qingfeng.personnel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingfeng.module.common.base.controller.BaseController;
import com.qingfeng.module.common.utils.MyResponse;
import com.qingfeng.personnel.entity.Precord;
import com.qingfeng.personnel.service.IPrecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @ProjectName PrecordController
 * @author Administrator
 * @version 1.0.0
 * @Description 员工变动记录
 * @createTime 2022/1/19 0019 22:52
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/personnel/precord")
public class PrecordController extends BaseController {

    @Autowired
    private IPrecordService precordService;

    /**
     * @title list
     * @description 查询数据列表
     * @author Administrator
     * @updateTime 2022/1/19 0019 22:53
     */
    @GetMapping("/list")
    public MyResponse list(Precord precord) throws IOException  {
        List<Precord> list = precordService.list(new LambdaQueryWrapper<Precord>().like(Precord::getUser_ids,precord.getUser_ids()).orderByAsc(Precord::getCreate_time));
        return new MyResponse().data(list);
    }

}
