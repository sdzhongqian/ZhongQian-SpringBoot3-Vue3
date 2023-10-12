package com.qingfeng.module.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingfeng.module.base.entity.Czlog;
import com.qingfeng.module.common.base.entity.QueryRequest;

import java.util.List;

/**
 * @ProjectName ICzlogService
 * @author Administrator
 * @version 1.0.0
 * @Description ICzlogService接口
 * @createTime 2022/1/19 0019 22:55
 */
public interface ICzlogService extends IService<Czlog> {

    //查询数据分页列表
    IPage<Czlog> findListPage(Czlog czlog, QueryRequest request);

    //查询数据列表
    List<Czlog> findList(Czlog czlog);

}