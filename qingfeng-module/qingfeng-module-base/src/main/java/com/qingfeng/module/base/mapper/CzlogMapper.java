package com.qingfeng.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingfeng.module.base.entity.Czlog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ProjectName CzlogMapper
 * @author Administrator
 * @version 1.0.0
 * @Description CzlogMapper
 * @createTime 2022/1/19 0019 22:54
 */
public interface CzlogMapper extends BaseMapper<Czlog> {

    //查询数据分页列表
    IPage<Czlog> findListPage(Page page, @Param("obj") Czlog czlog);

    //查询数据列表
    List<Czlog> findList(Czlog czlog);

}