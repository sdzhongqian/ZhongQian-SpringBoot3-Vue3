package com.qingfeng.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qingfeng.module.base.entity.UploadFile;
import com.qingfeng.module.base.mapper.UploadMapper;
import com.qingfeng.module.base.service.IUploadService;
import com.qingfeng.module.common.utils.PageData;
import com.qingfeng.module.common.utils.Verify;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ProjectName UploadServiceImpl
 * @author qingfeng
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021/4/3 0003 20:29
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UploadServiceImpl extends ServiceImpl<UploadMapper, UploadFile> implements IUploadService {


    public List<UploadFile> findFileList(List<String> objIds, String source){
        QueryWrapper queryWrapper = new QueryWrapper();
        if(Verify.verifyIsNotNull(objIds)){
            queryWrapper.in("obj_id",objIds);
        }else{
            queryWrapper.isNotNull("obj_id");
        }
        queryWrapper.eq("source",source);
        queryWrapper.orderByAsc(Arrays.asList(new String[] {"create_time"}));
        return this.baseMapper.selectList(queryWrapper);
    }


    public PageData arrayGroup(List<UploadFile> fileList){
        PageData resPd = new PageData();
        fileList.stream().forEach(item->{
            if(Verify.verifyIsNotNull(resPd.get(item.getObj_id()))){
                List<UploadFile> nlist = (List<UploadFile>)resPd.get(item.getObj_id());
                nlist.add(item);
                resPd.put(item.getObj_id(),nlist);
            }else{
                List<UploadFile> nlist = new ArrayList<>();
                nlist.add(item);
                resPd.put(item.getObj_id(),nlist);
            }
        });
        return resPd;
    }


    /**
     * 根据obj分组查询最新的一条-形成列表
     * @param obj_id
     * @param source
     * @return
     */
    public List<UploadFile> findFileGroupInfo(String obj_id, String source) {
        PageData pd = new PageData();
        if(Verify.verifyIsNotNull(obj_id)){
            pd.put("obj_id",obj_id);
        }
        pd.put("source",source);
        return this.baseMapper.findFileList(pd);
    }

}