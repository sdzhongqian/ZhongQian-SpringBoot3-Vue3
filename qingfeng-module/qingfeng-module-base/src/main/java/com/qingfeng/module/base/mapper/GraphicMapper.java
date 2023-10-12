package com.qingfeng.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qingfeng.module.base.entity.Graphic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ProjectName GraphicMapper
 * @author Administrator
 * @version 1.0.0
 * @Description GraphicMapper
 * @createTime 2022/6/25 0025 21:53
 */
public interface GraphicMapper extends BaseMapper<Graphic> {

    //查询数据分页列表
    IPage<Graphic> findListPage(Page page, @Param("obj") Graphic graphic);

    //查询数据列表
    List<Graphic> findList(Graphic graphic);

}