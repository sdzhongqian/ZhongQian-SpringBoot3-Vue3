package com.qingfeng.module.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingfeng.module.base.entity.Authen;
import com.qingfeng.module.common.base.entity.QueryRequest;

import java.util.List;

/**
 * @ProjectName IAuthenService
 * @author Administrator
 * @version 1.0.0
 * @Description IAuthenService接口
 * @createTime 2022/1/19 0019 22:55
 */
public interface IAuthenService extends IService<Authen> {

    //查询数据分页列表
    IPage<Authen> findListPage(Authen authen, QueryRequest request);

    //查询数据列表
    List<Authen> findList(Authen authen);

}