package com.qingfeng.module.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingfeng.module.base.entity.UploadFile;
import com.qingfeng.module.common.utils.PageData;

import java.util.List;

/**
 * @ProjectName UploadMapper
 * @author qingfeng
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021/4/3 0003 20:29
 */
public interface UploadMapper extends BaseMapper<UploadFile> {

    List<UploadFile> findFileList(PageData pd);

}