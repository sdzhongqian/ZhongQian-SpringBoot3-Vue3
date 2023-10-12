package com.qingfeng.personnel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.personnel.entity.Workexper;
import com.qingfeng.personnel.mapper.WorkexperMapper;
import com.qingfeng.personnel.service.IWorkexperService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProjectName WorkexperServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description WorkexperServiceImpl接口
 * @createTime 2022/1/19 0019 22:55
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WorkexperServiceImpl extends ServiceImpl<WorkexperMapper, Workexper> implements IWorkexperService {


}