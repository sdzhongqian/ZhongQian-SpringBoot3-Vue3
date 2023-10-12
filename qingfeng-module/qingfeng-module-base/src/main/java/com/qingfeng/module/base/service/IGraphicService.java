package com.qingfeng.module.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qingfeng.module.base.entity.Graphic;
import com.qingfeng.module.common.base.entity.QueryRequest;

import java.util.List;

/**
 * @ProjectName IGraphicService
 * @author Administrator
 * @version 1.0.0
 * @Description IGraphicService
 * @createTime 2022/6/25 0025 21:54
 */
public interface IGraphicService extends IService<Graphic> {

    //查询数据分页列表
    IPage<Graphic> findListPage(Graphic graphic, QueryRequest request);

    //查询数据列表
    List<Graphic> findList(Graphic graphic);

}