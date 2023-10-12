package com.qingfeng.module.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qingfeng.module.base.entity.UploadFile;
import com.qingfeng.module.common.utils.PageData;

import java.util.List;

/**
 * @ProjectName IUploadService
 * @author qingfeng
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021/4/3 0003 20:29
 */
public interface IUploadService extends IService<UploadFile> {


    List<UploadFile> findFileList(List<String> obj_id, String source);

    PageData arrayGroup(List<UploadFile> fileList);

    List<UploadFile> findFileGroupInfo(String obj_id, String source);

}