package com.qingfeng.personnel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.personnel.entity.Precord;
import com.qingfeng.personnel.mapper.PrecordMapper;
import com.qingfeng.personnel.service.IPrecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProjectName PrecordServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description PrecordServiceImpl接口
 * @createTime 2022/1/19 0019 22:55
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PrecordServiceImpl extends ServiceImpl<PrecordMapper, Precord> implements IPrecordService {



}