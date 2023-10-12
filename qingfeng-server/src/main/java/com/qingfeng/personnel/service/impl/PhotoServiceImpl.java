package com.qingfeng.personnel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.personnel.entity.Photo;
import com.qingfeng.personnel.mapper.PhotoMapper;
import com.qingfeng.personnel.service.IPhotoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProjectName PhotoServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description PhotoServiceImpl接口
 * @createTime 2022/1/19 0019 22:55
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements IPhotoService {


}