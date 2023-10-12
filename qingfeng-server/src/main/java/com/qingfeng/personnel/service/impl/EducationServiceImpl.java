package com.qingfeng.personnel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.personnel.entity.Education;
import com.qingfeng.personnel.mapper.EducationMapper;
import com.qingfeng.personnel.service.IEducationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProjectName EducationServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description EducationServiceImpl接口
 * @createTime 2022/1/19 0019 22:55
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EducationServiceImpl extends ServiceImpl<EducationMapper, Education> implements IEducationService {



}