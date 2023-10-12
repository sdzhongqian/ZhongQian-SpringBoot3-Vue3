package com.qingfeng.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.system.entity.OrganizeRole;
import com.qingfeng.system.entity.PostRole;
import com.qingfeng.system.mapper.OrganizeRoleMapper;
import com.qingfeng.system.mapper.PostRoleMapper;
import com.qingfeng.system.service.IOrganizeRoleService;
import com.qingfeng.system.service.IPostRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProjectName PostRoleServiceImpl
 * @author Administrator
 * @version 1.0.0
 * @Description PostRoleServiceImpl实现类
 * @createTime 2022/1/20 0020 0:18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PostRoleServiceImpl extends ServiceImpl<PostRoleMapper, PostRole> implements IPostRoleService {


}