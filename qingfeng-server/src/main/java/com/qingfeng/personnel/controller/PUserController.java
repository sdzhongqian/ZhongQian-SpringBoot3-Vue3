package com.qingfeng.personnel.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qingfeng.module.base.configure.aspect.OperationAnnotation;
import com.qingfeng.module.base.configure.aspect.RedisCache;
import com.qingfeng.module.base.configure.aspect.RedisEvict;
import com.qingfeng.module.common.base.controller.BaseController;
import com.qingfeng.module.common.framework.exception.BizException;
import com.qingfeng.module.common.utils.*;
import com.qingfeng.personnel.entity.*;
import com.qingfeng.personnel.service.*;
import com.qingfeng.system.entity.Dictionary;
import com.qingfeng.system.entity.SystemUser;
import com.qingfeng.system.entity.UserOrganize;
import com.qingfeng.system.service.IDictionaryService;
import com.qingfeng.system.service.IUserOrganizeService;
import com.qingfeng.system.service.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ProjectName EducationController
 * @author Administrator
 * @version 1.0.0
 * @Description 教育经历
 * @createTime 2022/1/19 0019 22:52
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/personnel/puser")
public class PUserController extends BaseController {

    @Autowired
    private IEducationService educationService;
    @Autowired
    private IPhotoService photoService;
    @Autowired
    private IWorkexperService workexperService;
    @Autowired
    private IPuserService puserService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IPrecordService precordService;
    @Autowired
    private IUserOrganizeService userOrganizeService;
    @Autowired
    private IDictionaryService dictionaryService;

    /**
     * 查询教育经历列表
     * @param education
     * @return
     * @throws IOException
     */
    @GetMapping("/findEducationList")
    @RedisCache(key = "cache:system:user", fieldKey = "findEducationList", expired = 60)
    public MyResponse findEducationList(Education education) throws IOException  {
        List<Education> list = educationService.list(new LambdaQueryWrapper<Education>().eq(Education::getUser_id,education.getUser_id()));
        List<String> ids = list.stream().map(Education::getEducation).collect(Collectors.toList());
        List<String> degreeIds = list.stream().map(Education::getDegree).collect(Collectors.toList());
        ids.addAll(degreeIds);
        if(ids.size()>0){
            List<Dictionary> dictionaryList = dictionaryService.list(new LambdaQueryWrapper<Dictionary>().in(Dictionary::getId,ids));
            Map<String, Dictionary> map = dictionaryList.stream().collect(Collectors.toMap(Dictionary::getId, o -> o,(oldValue, newValue)->newValue));
            for (Education p:list) {
                Dictionary dictionary = map.get(p.getEducation());
                if(Verify.verifyIsNotNull(dictionary)){
                    p.setEducation_name(dictionary.getName());
                }else{
                    p.setEducation_name("");
                }
                Dictionary dic2 = map.get(p.getDegree());
                if(Verify.verifyIsNotNull(dic2)){
                    p.setDegree_name(dic2.getName());
                }else{
                    p.setDegree_name("");
                }
            }
        }
        return new MyResponse().data(list);
    }

    /**
     * 查询证件照信息列表
     * @param photo
     * @return
     * @throws IOException
     */
    @GetMapping("/findPhotoList")
    @RedisCache(key = "cache:system:user", fieldKey = "findPhotoList", expired = 60)
    public MyResponse findPhotoList(Photo photo) throws IOException  {
        List<Photo> list = photoService.list(new LambdaQueryWrapper<Photo>().eq(Photo::getUser_id,photo.getUser_id()));
        List<String> ids = list.stream().map(Photo::getIdcard).collect(Collectors.toList());
        if(ids.size()>0){
            List<Dictionary> dictionaryList = dictionaryService.list(new LambdaQueryWrapper<Dictionary>().in(Dictionary::getId,ids));
            Map<String, Dictionary> map = dictionaryList.stream().collect(Collectors.toMap(Dictionary::getId, o -> o,(oldValue, newValue)->newValue));
            for (Photo p:list) {
                if(Verify.verifyIsNotNull(p.getPath())){
                    p.setImageUrl(ParaUtil.cloudfile+p.getPath());
                }
                Dictionary dictionary = map.get(p.getIdcard());
                if(Verify.verifyIsNotNull(dictionary)){
                    p.setIdcard_name(dictionary.getName());
                }else{
                    p.setIdcard_name("");
                }
            }
        }
        return new MyResponse().data(list);
    }


    /**
     * 查询工作经历列表
     * @param workexper
     * @return
     * @throws IOException
     */
    @GetMapping("/findWorkexperList")
    @RedisCache(key = "cache:system:user", fieldKey = "findWorkexperList", expired = 60)
    public MyResponse findWorkexperList(Workexper workexper) throws IOException  {
        List<Workexper> list = workexperService.list(new LambdaQueryWrapper<Workexper>().eq(Workexper::getUser_id,workexper.getUser_id()));
        return new MyResponse().data(list);
    }

    /**
     * 查询员工拓展信息
     * @param puser
     * @return
     * @throws IOException
     */
    @GetMapping("/findPuserInfo")
    @RedisCache(key = "cache:system:user", fieldKey = "findPuserInfo", expired = 60)
    public MyResponse findPuserInfo(Puser puser) throws IOException  {
        Puser user = puserService.getById(puser.getId());
        List<String> ids = new ArrayList<>();
        if(Verify.verifyIsNotNull(user.getEducation())){
            ids.add(user.getEducation());
        }
        if(Verify.verifyIsNotNull(user.getMarriage())){
            ids.add(user.getMarriage());
        }
        if(Verify.verifyIsNotNull(user.getNation())){
            ids.add(user.getNation());
        }
        if(Verify.verifyIsNotNull(user.getPolitics_face())){
            ids.add(user.getPolitics_face());
        }
        if(ids.size()>0) {
            List<Dictionary> dictionaryList = dictionaryService.list(new LambdaQueryWrapper<Dictionary>().in(Dictionary::getId, ids));
            Map<String, Dictionary> map = dictionaryList.stream().collect(Collectors.toMap(Dictionary::getId, o -> o, (oldValue, newValue) -> newValue));
            Dictionary dicEdu = map.get(user.getEducation());
            if(Verify.verifyIsNotNull(dicEdu)){
                user.setEducation_name(dicEdu.getName());
            }else{
                user.setEducation_name("");
            }
            Dictionary dicMarr = map.get(user.getMarriage());
            if(Verify.verifyIsNotNull(dicMarr)){
                user.setMarriage_name(dicMarr.getName());
            }else{
                user.setMarriage_name("");
            }
            Dictionary dicNat = map.get(user.getNation());
            if(Verify.verifyIsNotNull(dicNat)){
                user.setNation_name(dicNat.getName());
            }else{
                user.setNation_name("");
            }
            Dictionary dicPol = map.get(user.getPolitics_face());
            if(Verify.verifyIsNotNull(dicPol)){
                user.setPolitics_face_name(dicPol.getName());
            }else{
                user.setPolitics_face_name("");
            }
        }
        return new MyResponse().data(user);
    }


    /**
     * 保存教育经历
     * @param pUser
     * @param response
     * @throws Exception
     */
    @PostMapping("/saveEducation")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    @OperationAnnotation(content="保存教育经历",sysType=0,opType=1,action="保存教育经历")
    public void save(@Valid @RequestBody Puser pUser, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
            String user_id = userParams.split(":")[1];
            String organize_id = userParams.split(":")[2];

            String id = pUser.getId();
            List<Education> educationList = pUser.getEducationList();
            int index = 0;
            for (Education item:educationList) {
                index++;
                item.setId(GuidUtil.getUuid());
                item.setType(10);
                item.setUser_id(id);
                item.setStatus(0);
                item.setOrder_by(index+1);
                item.setCreate_time(DateTimeUtil.getDateTimeStr());
                item.setCreate_user(user_id);
                item.setCreate_organize(organize_id);
            }
            educationService.remove(new LambdaQueryWrapper<Education>().eq(Education::getUser_id,id));
            this.educationService.saveBatch(educationList);
            json.setSuccess(true);
            json.setMsg("新增信息成功");
        } catch (Exception e) {
            String message = "新增信息失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }


    /**
     * 保存证件照信息
     * @param pUser
     * @param response
     * @throws Exception
     */
    @PostMapping("/savePhoto")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    @OperationAnnotation(content="保存证件照信息",sysType=0,opType=1,action="保存证件照信息")
    public void savePhoto(@Valid @RequestBody Puser pUser, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
            String user_id = userParams.split(":")[1];
            String organize_id = userParams.split(":")[2];

            String id = pUser.getId();
            List<Photo> photoList = pUser.getPhotoList();
            int index = 0;
            for (Photo item:photoList) {
                index++;
                item.setId(GuidUtil.getUuid());
                item.setType(0);
                item.setUser_id(id);
                item.setStatus(0);
                item.setOrder_by(index+1);
                item.setCreate_time(DateTimeUtil.getDateTimeStr());
                item.setCreate_user(user_id);
                item.setCreate_organize(organize_id);
            }
            photoService.remove(new LambdaQueryWrapper<Photo>().eq(Photo::getUser_id,id));
            this.photoService.saveBatch(photoList);
            json.setSuccess(true);
            json.setMsg("新增信息成功");
        } catch (Exception e) {
            String message = "新增信息失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }


    /**
     * 保存工作经历
     * @param pUser
     * @param response
     * @throws Exception
     */
    @PostMapping("/saveWorkexper")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    @OperationAnnotation(content="保存工作经历",sysType=0,opType=1,action="保存工作经历")
    public void saveWorkexper(@Valid @RequestBody Puser pUser, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
            String user_id = userParams.split(":")[1];
            String organize_id = userParams.split(":")[2];

            String id = pUser.getId();
            List<Workexper> workexperList = pUser.getWorkexperList();
            int index = 0;
            for (Workexper item:workexperList) {
                index++;
                item.setId(GuidUtil.getUuid());
                item.setType(0);
                item.setUser_id(id);
                item.setStatus(0);
                item.setOrder_by(index+1);
                item.setCreate_time(DateTimeUtil.getDateTimeStr());
                item.setCreate_user(user_id);
                item.setCreate_organize(organize_id);
            }
            workexperService.remove(new LambdaQueryWrapper<Workexper>().eq(Workexper::getUser_id,id));
            this.workexperService.saveBatch(workexperList);
            json.setSuccess(true);
            json.setMsg("保存成功");
        } catch (Exception e) {
            String message = "保存失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * 保存员工完善信息
     * @param puser
     * @param response
     * @throws Exception
     */
    @PostMapping("/savePuser")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    @OperationAnnotation(content="保存员工完善信息",sysType=0,opType=1,action="保存员工完善信息")
    public void savePuser(@Valid @RequestBody Puser puser, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
            String user_id = userParams.split(":")[1];
            String organize_id = userParams.split(":")[2];
            Puser user = puserService.getById(puser.getUser_id());
            if(Verify.verifyIsNotNull(user)){
                puser.setUpdate_time(DateTimeUtil.getDateTimeStr());
                puser.setUpdate_user(user_id);
                puserService.updateById(puser);
            }else{
                puser.setId(puser.getUser_id());
                puser.setCreate_time(DateTimeUtil.getDateTimeStr());
                puser.setCreate_user(user_id);
                puser.setCreate_organize(organize_id);
                puserService.save(puser);
            }
            json.setSuccess(true);
            json.setMsg("保存成功");
        } catch (Exception e) {
            String message = "保存失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * 组织切换
     * @param puser
     * @param response
     * @throws Exception
     */
    @PostMapping("/updateOrganize")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    @OperationAnnotation(content="组织切换-员工调动",sysType=0,opType=1,action="组织切换-员工调动")
    public void updateOrganize(@Valid @RequestBody Puser puser, HttpServletResponse response) throws Exception {
        Json json = new Json();
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        String create_user = userParams.split(":")[1];
        String create_organize = userParams.split(":")[2];
        try {
            String organize_id = puser.getOrganize_id();
            String ids = puser.getIds();
            String names = puser.getNames();
            String reason = puser.getReason();
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.in("user_id",ids.split(","));
            wrapper.in("use_status",0);
            UserOrganize userOrganize = new UserOrganize();
            userOrganize.setOrganize_id(organize_id);
            userOrganizeService.update(userOrganize,wrapper);
            Precord precord = new Precord();
            precord.setId(GuidUtil.getUuid());
            precord.setType(0);
            precord.setUser_ids(ids);
            precord.setUser_names(names);
            precord.setReason(reason);
            precord.setCreate_time(DateTimeUtil.getDateTimeStr());
            precord.setCreate_user(create_user);
            precord.setCreate_organize(create_organize);
            precordService.save(precord);
            json.setSuccess(true);
            json.setMsg("保存成功");
        } catch (Exception e) {
            String message = "保存失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * 修改状态
     * @param puser
     * @param response
     * @throws Exception
     */
    @PostMapping("/updateStatus")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    @OperationAnnotation(content="修改状态",sysType=0,opType=1,action="修改状态")
    public void updateStatus(@Valid @RequestBody Puser puser, HttpServletResponse response) throws Exception {
        Json json = new Json();
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        String create_user = userParams.split(":")[1];
        String create_organize = userParams.split(":")[2];
        try {
            int status = puser.getStatus();
            String ids = puser.getIds();
            String names = puser.getNames();
            String reason = puser.getReason();
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.in("id",ids.split(","));
            SystemUser systemUser = new SystemUser();
            systemUser.setStatus(status);
            userService.update(systemUser,wrapper);
            Precord precord = new Precord();
            precord.setId(GuidUtil.getUuid());
            precord.setType(status);
            precord.setUser_ids(ids);
            precord.setUser_names(names);
            precord.setReason(reason);
            precord.setCreate_time(DateTimeUtil.getDateTimeStr());
            precord.setCreate_user(create_user);
            precord.setCreate_organize(create_organize);
            precordService.save(precord);
            json.setSuccess(true);
            json.setMsg("操作成功");
        } catch (Exception e) {
            String message = "操作失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }



}
