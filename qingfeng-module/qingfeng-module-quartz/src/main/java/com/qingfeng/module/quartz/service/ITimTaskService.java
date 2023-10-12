package com.qingfeng.module.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.quartz.entity.TimTask;

/**
 * @ProjectName ITimTaskService
 * @author Administrator
 * @version 1.0.0
 * @Description ITimTaskService
 * @createTime 2022/3/19 0019 8:53
 */
public interface ITimTaskService extends IService<TimTask> {

    IPage<TimTask> findListPage(TimTask timTask, QueryRequest request);


}