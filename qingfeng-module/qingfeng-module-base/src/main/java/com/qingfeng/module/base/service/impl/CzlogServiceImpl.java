package com.qingfeng.module.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.module.base.entity.Czlog;
import com.qingfeng.module.base.mapper.CzlogMapper;
import com.qingfeng.module.base.service.ICzlogService;
import com.qingfeng.module.common.base.entity.QueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ProjectName CzlogServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description CzlogServiceImpl接口
 * @createTime 2022/1/19 0019 22:55
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CzlogServiceImpl extends ServiceImpl<CzlogMapper, Czlog> implements ICzlogService {


    /**
     * @ProjectName CzlogServiceImpl
     * @author Administrator
     * @version 1.0.0
     * @Description 查询数据分页列表
     * @createTime 2022/1/19 0019 22:55
     */
    public IPage<Czlog> findListPage(Czlog czlog, QueryRequest request){
        Page<Czlog> page = new Page<>(request.getPage(), request.getLimit());
        return this.baseMapper.findListPage(page, czlog);
    }

    /**
     * @ProjectName CzlogServiceImpl
     * @author Administrator
     * @version 1.0.0
     * @Description 查询数据列表
     * @createTime 2022/1/19 0019 22:55
     */
    public List<Czlog> findList(Czlog czlog){
        return this.baseMapper.findList(czlog);
    }


}