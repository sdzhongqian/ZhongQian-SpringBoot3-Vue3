package com.qingfeng.module.quartz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.quartz.entity.Bustask;
import com.qingfeng.module.quartz.mapper.BusTaskMapper;
import com.qingfeng.module.quartz.service.IBusTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProjectName BusTaskServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description 业务任务
 * @createTime 2022/3/19 0019 8:54
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BusTaskServiceImpl extends ServiceImpl<BusTaskMapper, Bustask> implements IBusTaskService {


    public IPage<Bustask> findListPage(Bustask bustask, QueryRequest request){
        Page<Bustask> page = new Page<>(request.getPage(), request.getLimit());
        return this.baseMapper.findListPage(page, bustask);
    }




}