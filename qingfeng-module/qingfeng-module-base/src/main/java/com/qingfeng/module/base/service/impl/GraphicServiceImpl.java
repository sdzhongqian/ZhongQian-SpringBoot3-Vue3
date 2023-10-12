package com.qingfeng.module.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.module.base.entity.Graphic;
import com.qingfeng.module.base.mapper.GraphicMapper;
import com.qingfeng.module.base.service.IGraphicService;
import com.qingfeng.module.common.base.entity.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ProjectName GraphicServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description GraphicServiceImpl
 * @createTime 2022/6/25 0025 21:54
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GraphicServiceImpl extends ServiceImpl<GraphicMapper, Graphic> implements IGraphicService {

    /**
     * @title findListPage
     * @description 查询数据分页列表
     * @author Administrator
     * @updateTime 2022/6/25 0025 21:54
     */
    public IPage<Graphic> findListPage(Graphic graphic, QueryRequest request){
        Page<Graphic> page = new Page<>(request.getPage(), request.getLimit());
        return this.baseMapper.findListPage(page, graphic);
    }

    /**
     * @title findList
     * @description 查询数据列表
     * @author Administrator
     * @updateTime 2022/6/25 0025 21:55
     */
    public List<Graphic> findList(Graphic graphic){
        return this.baseMapper.findList(graphic);
    }

}