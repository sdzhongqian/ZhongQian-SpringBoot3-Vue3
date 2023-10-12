package com.qingfeng.personnel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.personnel.entity.Puser;
import com.qingfeng.personnel.mapper.PuserMapper;
import com.qingfeng.personnel.service.IPuserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProjectName PuserServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description PuserServiceImpl接口
 * @createTime 2022/1/19 0019 22:55
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PuserServiceImpl extends ServiceImpl<PuserMapper, Puser> implements IPuserService {



}