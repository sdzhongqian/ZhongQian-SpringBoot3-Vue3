package com.qingfeng.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qingfeng.module.base.configure.aspect.OperationAnnotation;
import com.qingfeng.module.base.configure.aspect.RedisCache;
import com.qingfeng.module.base.configure.aspect.RedisEvict;
import com.qingfeng.module.base.entity.UploadFile;
import com.qingfeng.module.base.service.IUploadService;
import com.qingfeng.module.common.base.controller.BaseController;
import com.qingfeng.module.common.base.entity.QueryRequest;
import com.qingfeng.module.common.framework.exception.BizException;
import com.qingfeng.module.common.utils.*;
import com.qingfeng.system.entity.Dictionary;
import com.qingfeng.system.entity.*;
import com.qingfeng.system.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Administrator
 * @version 1.0.0
 * @ProjectName qingfeng
 * @Description TODO
 * @createTime 2022年01月18日 21:55:00
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IOrganizeService organizeService;
    @Autowired
    public IUploadService uploadService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserOrganizeService userOrganizeService;
    @Autowired
    private IPostService postService;
    @Autowired
    private IDictionaryService dictionaryService;
    @Value("${qingfeng.imitpwd}")
    private String imitpwd;


    /**
     * @title listPage
     * @description 查询数据列表
     * @author Administrator
     * @updateTime 2022/1/18 0018 22:29
     */
    @GetMapping("/listPage")
    @PreAuthorize("hasAnyAuthority('user:info')")
    @RedisCache(key = "cache:system:user", fieldKey = "listPage", expired = 60)
    public Map<String, Object> listPage(QueryRequest queryRequest, SystemUser user) {
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        //处理数据权限
        String user_id = userParams.split(":")[1];
        UserOrganize uoParam = new UserOrganize();
        uoParam.setUser_id(user_id);
        UserOrganize userOrganize = userOrganizeService.findUserOrganizeInfo(uoParam);
        List<String> auth_organize_ids = new ArrayList<String>();
        if(Verify.verifyIsNotNull(userOrganize)){
            if(Verify.verifyIsNotNull(userOrganize.getAuthOrgIds())){
                auth_organize_ids = Arrays.asList(userOrganize.getAuthOrgIds().split(","));
            }
        }
        user.setAuth_user(user_id);
        user.setAuth_organize_ids(auth_organize_ids);

        IPage<SystemUser> page = userService.findListPage(user, queryRequest);
        List<String> uids = page.getRecords().stream().map(SystemUser::getId).collect(Collectors.toList());
        if(uids.size()>0){
            List<UploadFile> upfiles = uploadService.findFileList(uids,"userHeader");
//        Map<String, List<UploadFile>> fileList = upfiles.stream().collect(Collectors.groupingBy(UploadFile::getObj_id));
            Map<String, UploadFile> map = upfiles.stream().collect(Collectors.toMap(UploadFile::getObj_id, o -> o,(oldValue,newValue)->newValue));
            for (SystemUser g:page.getRecords()) {
                UploadFile uploadFile = map.get(g.getId());
                if(Verify.verifyIsNotNull(uploadFile)){
                    g.setHeadImg(ParaUtil.cloudfile+uploadFile.getFile_path());
                }else{
                    g.setHeadImg("");
                }
            }
        }
        Map<String, Object> dataTable = MyUtil.getDataTable(page);
        return dataTable;
    }

    /**
     * @title list
     * @description 查询数据列表
     * @author Administrator
     * @updateTime 2022/1/18 0018 22:44
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('user:info')")
    @RedisCache(key = "cache:system:user", fieldKey = "list", expired = 60)
    public MyResponse list(SystemUser systemUser) {
        List<SystemUser> list = userService.findList(systemUser);
        return new MyResponse().data(list);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyAuthority('user:info')")
    @RedisCache(key = "cache:system:user", fieldKey = "info", expired = 60)
    public MyResponse info(SystemUser systemUser) {
        SystemUser user = userService.findInfo(systemUser);
        //查询头像
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("obj_id",user.getId());
        queryWrapper.eq("source","userHeader");
        queryWrapper.orderByAsc(Arrays.asList(new String[] {"create_time"}));
        queryWrapper.last("limit 1");
        UploadFile uploadFile = uploadService.getOne(queryWrapper);
        if(Verify.verifyIsNotNull(uploadFile)){
            user.setHeadImg(ParaUtil.cloudfile+uploadFile.getFile_path());
        }else{
            user.setHeadImg("");
        }
        //查询岗位
        if(Verify.verifyIsNotNull(user.getPost_ids())){
            String[] post_ids = user.getPost_ids().split(",");
            List<Post> postList = postService.list(new LambdaQueryWrapper<Post>().in(Post::getId,post_ids));
            user.setPostList(postList);
        }
        return new MyResponse().data(user);
    }

    /**
     * @title save
     * @description 保存数据
     * @author Administrator
     * @updateTime 2022/1/18 0018 22:44
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('user:add')")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    @OperationAnnotation(content="新增用户信息",sysType=0,opType=1,action="新增用户信息")
    public MyResponse save(@Valid @RequestBody SystemUser systemUser) throws BizException {
        Json json = new Json();
        try {
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("login_name", systemUser.getLogin_name());
            Collection list = userService.listByMap(columnMap);
            if(list.size()==0){
                this.userService.createUser(systemUser);
                json.setMsg("用户信息新增成功");
                json.setSuccess(true);
            }else{
                json.setSuccess(false);
                json.setMsg("登录名称【"+ systemUser.getLogin_name()+"】已经存在！");
            }
        } catch (Exception e) {
            String message = "用户信息新增失败";
            log.error(message, e);
            throw new BizException(message);
        }
        return new MyResponse().data(json);
    }

    /**
     * @title update
     * @description 更新数据
     * @author Administrator
     * @updateTime 2022/1/18 0018 22:44
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('user:edit')")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    @OperationAnnotation(content="修改用户信息",sysType=0,opType=3,action="修改用户信息")
    public MyResponse update(@Valid @RequestBody SystemUser systemUser) throws BizException {
        Json json = new Json();
        try {
            SystemUser user = userService.getById(systemUser.getId());
            boolean bol = true;
            if(!user.getLogin_name().equals(systemUser.getLogin_name())){
                //判断登录用户名称是否存在
                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("login_name", systemUser.getLogin_name());
                Collection list = userService.listByMap(columnMap);
                if(list.size()>0){
                    json.setSuccess(false);
                    json.setMsg("登录名称【"+ systemUser.getLogin_name()+"】已经存在！");
                    bol = false;
                }
            }
            if(bol){
                json.setSuccess(true);
                json.setMsg("用户信息修改成功");
                this.userService.updateUser(systemUser,0);
            }
        } catch (Exception e) {
            String message = "用户信息修改失败";
            json.setSuccess(false);
            log.error(message, e);
            e.printStackTrace();
            throw new BizException(message);
        }
        return new MyResponse().data(json);
    }

    /**
     * @title del
     * @description 删除数据
     * @author Administrator
     * @updateTime 2022/1/18 0018 22:44
     */
    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasAnyAuthority('user:del')")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    @OperationAnnotation(content="删除用户信息",sysType=0,opType=2,action="删除用户信息")
    public MyResponse del(@NotBlank(message = "{required}") @PathVariable String userIds, HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            String[] ids = userIds.split(StringPool.COMMA);
            this.userService.deleteUsers(ids);
            json.setSuccess(true);
            json.setMsg("用户信息删除成功");
        } catch (Exception e) {
            String message = "用户信息删除失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        return new MyResponse().data(json);
    }

    /**
     * @title deleteUsers
     * @description 更新状态
     * @author Administrator
     * @updateTime 2022/1/18 0018 22:44
     */
    @PostMapping("/updateStatus")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    @OperationAnnotation(content="修改用户状态信息",sysType=0,opType=3,action="修改用户状态信息")
    public void updateStatus(@Valid @RequestBody SystemUser systemUser,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            this.userService.updateById(systemUser);
            json.setSuccess(true);
            json.setMsg("状态修改成功");
        } catch (Exception e) {
            String message = "状态修改失败";
            json.setMsg(message);
            json.setSuccess(false);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }


    /**
     * @title findUserInfo
     * @description 查询用户细腻详情
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:17
     */
    @GetMapping("/findUserInfo")
    public void findUserInfo(@RequestHeader HttpHeaders headers, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        PageData pd = new PageData(request);
        String[] tokens = SecurityContextHolder.getContext().getAuthentication().getName().split(":");
        pd.put("user_id",tokens[1]);
        String organize_id = tokens[2];
        //用户详情
        PageData uPd = userService.findUserInfo(pd);
        PageData rolePd = new PageData();
        //查询当前用户拥有的角色信息
        pd.put("organize_id",organize_id);
        if(Verify.verifyIsNotNull(uPd.getString("post_ids"))){
            pd.put("postList",Arrays.asList(uPd.getString("post_ids").split(",")));
        }else{
            pd.put("postList",new ArrayList<>());
        }
        List<Role> userRoles = userService.findUserRoleList(pd);
        rolePd.put("permissions",userRoles);
        //查询当前组织信息
        PageData orgPd = userService.findUserOrganizeInfo(pd);
        uPd.put("role",rolePd);
        uPd.put("orgPd",orgPd);
        //查询当前用户拥有的权限信息
        List<PageData> list = menuService.findAuthMenuList(pd);
        uPd.put("authList",list);
        //返回用户信息 用于辅助查询（文档下载）
        uPd.put("authName",SecurityContextHolder.getContext().getAuthentication().getName());
        Json json = new Json();
        json.setMsg("获取数据成功。");
        json.setCode(200);
        json.setData(uPd);
        json.setSuccess(true);
        this.writeJson(response,json);
    }


    /**
     * @title updatePwd
     * @description 更新密码
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:18
     */
    @GetMapping("/updatePwd")
    @PreAuthorize("hasAnyAuthority('user:resetPwd')")
    @OperationAnnotation(content="修改用户密码信息",sysType=0,opType=3,action="修改用户密码信息")
    public MyResponse updatePwd(SystemUser user) throws BizException {
        Json json = new Json();
        try {
            userService.updatePwd(user);
            json.setSuccess(true);
            json.setMsg("重置成功");
        } catch (Exception e) {
            String message = "操作失败";
            json.setMsg(message);
            json.setSuccess(false);
            log.error(message, e);
            throw new BizException(message);
        }
        return new MyResponse().data(json);
    }

    /**
     * @title findUserOrganizeListPage
     * @description 查询用户组织分页信息
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:18
     */
    @GetMapping("/findUserOrganizeListPage")
    @PreAuthorize("hasAnyAuthority('user:info')")
    public Map<String, Object> findUserOrganizeListPage(QueryRequest queryRequest,UserOrganize userOrganize) {
        //处理数据权限
        Map<String, Object> dataTable = MyUtil.getDataTable(userOrganizeService.findListPage(userOrganize, queryRequest));
        return dataTable;
    }


    /**
     * @title saveOrUpdateUserOrganize
     * @description 保存或更新用户组织
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:18
     */
    @PostMapping("/saveOrUpdateUserOrganize")
    @OperationAnnotation(content="保存或更新用户组织",sysType=0,opType=3,action="保存或更新用户组织")
    public void saveOrUpdateUserOrganize(@Valid @RequestBody UserOrganize userOrganize,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            if(Verify.verifyIsNotNull(userOrganize.getId())){
                userOrganizeService.updateUserOrganize(userOrganize);
            }else{
                userOrganizeService.saveUserOrganize(userOrganize);
            }
            json.setSuccess(true);
            json.setMsg("权限信息更新成功");
        } catch (Exception e) {
            String message = "权限信息更新失败";
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }


    /**
     * @title delUserOrganize
     * @description 删除用户组织
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:19
     */
    @DeleteMapping("/delUserOrganize/{id}")
    @PreAuthorize("hasAnyAuthority('user:del')")
    @OperationAnnotation(content="删除用户组织",sysType=0,opType=2,action="删除用户组织")
    public void delUserOrganize(@NotBlank(message = "{required}") @PathVariable String id,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            userOrganizeService.removeById(id);
            json.setSuccess(true);
            json.setMsg("用户信息删除成功");
        } catch (Exception e) {
            String message = "用户信息删除失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * @title findRoleAuth
     * @description 查询角色权限
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:19
     */
    @PostMapping("/findRoleAuth")
    public MyResponse findRoleAuth(@RequestBody PageData pd) throws BizException {
        pd.put("user_id",pd.get("id"));
        List<Role> roleLs = roleService.findSimpleList(pd);
        List<Role> myRoleLs = userService.findUserRoleList(pd);
        List<UserOrganize> orgList = userOrganizeService.findUserOrganize(pd);
        pd.put("roleLs",roleLs);
        pd.put("myRoleLs",myRoleLs);
        pd.put("orgList",orgList);
        return new MyResponse().data(pd);
    }

    /**
     * @title findOrganizeAuth
     * @description 查询组织权限
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:19
     */
    @PostMapping("/findOrganizeAuth")
    public MyResponse findOrganizeAuth(@RequestBody PageData pd) throws BizException {
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        //处理数据权限
        String user_id = userParams.split(":")[1];
        String organize_id = userParams.split(":")[2];
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",user_id);
        SystemUser user = userService.getOne(queryWrapper);

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("id",organize_id);
        Organize organize = organizeService.getOne(queryWrapper1);
        if (user.getType()==0) {//管理员
            pd.put("org_cascade", "org");
        } else {
            pd.put("org_cascade", organize.getOrg_cascade());
        }
        List<PageData> list = organizeService.findTreeTableList(pd);
        //查询用户的数据权限数据
        pd.put("user_id", pd.get("id"));
        PageData p = userService.findUserOrganizeInfo(pd);
        if (Verify.verifyIsNotNull(p.get("authOrgIds"))) {
            String[] authOrgIds = p.get("authOrgIds").toString().split(",");
            String[] authParams = p.get("authParams").toString().split(",");
            StringBuilder showAuthData = new StringBuilder();
            StringBuilder operaAuthData = new StringBuilder();
            for (int i = 0; i < authOrgIds.length; i++) {
                showAuthData.append(authOrgIds[i]).append(",");
                if (authParams[i].contains("Y")) {
                    operaAuthData.append(authOrgIds[i]).append(",");
                }
            }
            if (showAuthData.length() > 0) {
                p.put("showAuthData", showAuthData.substring(0, showAuthData.length() - 1));
            } else {
                p.put("showAuthData", "");
            }
            if (operaAuthData.length() > 0) {
                p.put("operaAuthData", operaAuthData.substring(0, operaAuthData.length() - 1));
            } else {
                p.put("operaAuthData", "");
            }
        }else{
            p.put("showAuthData", "");
            p.put("operaAuthData", "");
        }
        pd.put("list",list);
        pd.put("object",p);
        return new MyResponse().data(pd);
    }

    /**
     * @title updateAuth
     * @description 更新权限
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:19
     */
    @PostMapping("/updateAuth")
    @OperationAnnotation(content="更新用户权限",sysType=0,opType=3,action="更新用户权限")
    public void updateAuth(@RequestBody PageData pd,HttpServletResponse response) throws Exception {
        Json json = new Json();
        try {
            userService.updateAuth(pd);
            json.setSuccess(true);
            json.setMsg("权限信息更新成功");
        } catch (Exception e) {
            String message = "权限信息更新失败";
            json.setSuccess(false);
            json.setMsg(message);
            log.error(message, e);
            throw new BizException(message);
        }
        this.writeJson(response,json);
    }

    /**
     * @title findLoginUser
     * @description 查询登录用户
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:20
     */
    @GetMapping("/findLoginUser")
    public MyResponse findLoginUser() {
        PageData pd = new PageData();
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        //处理数据权限
        String user_id = userParams.split(":")[1];
        pd.put("user_id",user_id);
        PageData p = userService.findUserInfo(pd);
        //处理图片
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("obj_id",user_id);
        queryWrapper.orderByDesc("create_time");
        UploadFile file = (UploadFile)uploadService.list(queryWrapper).get(0);
        p.put("pathUrl", ParaUtil.cloudfile);
        if(Verify.verifyIsNotNull(file)){
            p.put("headImg", ParaUtil.cloudfile+file.getFile_path());
        }else{
            p.put("headImg", "");
        }
        //查询用户组织
        pd.put("user_id",p.get("id"));
        List<UserOrganize> organizeList = userOrganizeService.findUserOrganize(pd);
        p.put("organizeList",organizeList);
        return new MyResponse().data(p);
    }


    /**
     * @title updateUser
     * @description 更新用户信息
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:20
     */
    @PostMapping("/updateUser")
    @OperationAnnotation(content="更新用户信息",sysType=0,opType=3,action="更新用户信息")
    public MyResponse updateUser(@Valid @RequestBody SystemUser user) throws BizException {
        Json json = new Json();
        try {
            String time = DateTimeUtil.getDateTimeStr();
            String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
            //处理数据权限
            String user_id = userParams.split(":")[1];
            user.setId(user_id);
            user.setCreate_user(user_id);
            user.setUpdate_time(time);
            this.userService.updateUser(user,1);
            json.setMsg("用户信息修改成功");
            json.setSuccess(true);
        } catch (Exception e) {
            String message = "用户信息修改失败";
            json.setMsg(message);
            json.setSuccess(false);
            log.error(message, e);
            throw new BizException(message);
        }
        return new MyResponse().data(json);
    }

    /**
     * @title updateMyPwd
     * @description 重置密码
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:20
     */
    @PostMapping("/updateMyPwd")
    @OperationAnnotation(content="更新我的密码",sysType=0,opType=3,action="更新我的密码")
    public MyResponse updateMyPwd(@RequestBody PageData pd) throws BizException {
        Json json = new Json();
        try {
            String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
            //处理数据权限
            String user_id = userParams.split(":")[1];
            pd.put("user_id",user_id);
            PageData user = userService.findUserInfo(pd);
            if(passwordEncoder.matches(pd.get("old_password").toString(),user.get("login_password").toString())){
                String time = DateTimeUtil.getDateTimeStr();
                SystemUser systemUser = new SystemUser();
                systemUser.setLogin_password(passwordEncoder.encode(pd.get("login_password").toString()));
                systemUser.setUpdate_time(time);
                systemUser.setId(user_id);
                userService.updateById(systemUser);
                json.setSuccess(true);
                json.setMsg("密码重置成功。");
            }else{
                json.setSuccess(false);
                json.setMsg("旧密码不正确，请重新输入。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            String message = "密码重置失败";
            log.error(message, e);
            throw new BizException(message);
        }
        return new MyResponse().data(json);
    }

    /**
     * @title updateSwitchOrganize
     * @description 组织切换
     * @author qingfeng
     * @updateTime 2021/4/3 0003 21:20
     */
    @PostMapping("/updateSwitchOrganize")
    @OperationAnnotation(content="组织切换",sysType=0,opType=3,action="组织切换")
    public MyResponse updateSwitchOrganize(@RequestBody PageData pd) throws BizException {
        Json json = new Json();
        try {
            String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
            //处理数据权限
            String user_id = userParams.split(":")[1];
            String organize_id = userParams.split(":")[2];
            if(!pd.get("organize_id").equals(organize_id)){
                //将所有的个人信息use_status更新为：1
                PageData param = new PageData();
                param.put("user_id",user_id);
                param.put("type",1);
                param.put("use_status",1);
                param.put("update_time",DateTimeUtil.getDateTimeStr());
                userService.updateUserOrgUseStatus(param);
                param.put("use_status",0);
                param.put("type",0);
                param.put("organize_id",pd.get("organize_id"));
                userService.updateUserOrgUseStatus(param);
            }
            json.setSuccess(true);
            json.setMsg("组织切换成功。");
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg("组织切换成功。");
            String message = "组织切换失败";
            log.error(message, e);
            throw new BizException(message);
        }
        return new MyResponse().data(json);
    }


    @GetMapping("/selectData")
    @PreAuthorize("hasAnyAuthority('user:info')")
    public MyResponse selectData(SystemUser user) {
        List<PageData> list = new ArrayList<>();
        Organize organize = new Organize();
        organize.setParent_id(user.getOrganize_id());
        organize.setName(user.getName());
        organize.setShort_name(user.getLogin_name());
        if(user.getType()==0||user.getType()==3){//查询用户-单选用户+多选用户
            list = userService.findSelectList(user);
        }else if(user.getType()==1||user.getType()==4){//查询组织-单选组织+多选组织
            list = organizeService.findSelectList(organize);
        }else if(user.getType()==2||user.getType()==5){//查询组织用户-单选组织用户+多选组织用户
            List<PageData> ulist = userService.findSelectList(user);
            List<PageData> olist = organizeService.findSelectList(organize);
            list = Stream.of(olist, ulist)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
        return new MyResponse().data(list);
    }

    /**
     * 验证登录密码-用于解锁锁屏
     * @param pd
     * @return
     * @throws BizException
     */
    @PostMapping("/verifyLogin")
    public MyResponse verifyLogin(@RequestBody PageData pd) throws BizException {
        Json json = new Json();
        try {
            String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
            String user_id = userParams.split(":")[1];
            SystemUser user = userService.getById(user_id);
            boolean bol = passwordEncoder.matches(pd.get("password").toString(),user.getLogin_password());
            if(bol){
                json.setMsg("验证成功");
                json.setSuccess(true);
            }else{
                json.setMsg("验证失败");
                json.setSuccess(false);
            }
        } catch (Exception e) {
            String message = "用户信息修改失败";
            json.setMsg(message);
            json.setSuccess(false);
            log.error(message, e);
            throw new BizException(message);
        }
        return new MyResponse().data(json);
    }


    /**
     * @title downloadImportTemplate
     * @description 下载Excel导出模板
     * @author Administrator
     * @updateTime 2022/8/14 0014 15:01
     */
    @GetMapping("/downloadImportTemplate")
    public void downloadImportTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PageData pd = new PageData(request);
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("templates/excelImport/user_import_mb.xlsx");
        FileUtil.downFile(response, in,"用户导入模板.xlsx");
    }

    /**
     * 导出信息
     * @param systemUser
     * @param session
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(SystemUser systemUser,HttpSession session, HttpServletResponse response) throws Exception {
        PageData pd = new PageData();
        List<SystemUser> list = userService.findList(systemUser);
        List<String> ids = list.stream().map(SystemUser::getPosition).collect(Collectors.toList());
        if(ids.size()>0){
            List<Dictionary> dictionaryList = dictionaryService.list(new LambdaQueryWrapper<Dictionary>().in(Dictionary::getId,ids));
            Map<String, Dictionary> map = dictionaryList.stream().collect(Collectors.toMap(Dictionary::getId, o -> o,(oldValue, newValue)->newValue));
            int index = 0;
            for (SystemUser p:list) {
                index++;
                p.setXh(index);
                Dictionary dictionary = map.get(p.getPosition());
                if(Verify.verifyIsNotNull(dictionary)){
                    p.setPosition_name(dictionary.getName());
                }else{
                    p.setPosition_name("");
                }
                //查询岗位
                if(Verify.verifyIsNotNull(p.getPost_ids())){
                    String[] post_ids = p.getPost_ids().split(",");
                    List<Post> postList = postService.list(new LambdaQueryWrapper<Post>().in(Post::getId,post_ids));
                    List<String> postNames = postList.stream().map(Post::getName).collect(Collectors.toList());
                    p.setPost_name(StringUtils.join(postNames, ","));
                }
                if(p.getStatus()==0){
                    p.setStatus_name("在职");
                }else if(p.getStatus()==1){
                    p.setStatus_name("禁用");
                }else if(p.getStatus()==2){
                    p.setStatus_name("休眠");
                }else if(p.getStatus()==3){
                    p.setStatus_name("离职");
                }else if(p.getStatus()==4){
                    p.setStatus_name("黑名单");
                }
            }
        }

        String tempPath = "";
        String toFile = "";
//        tempPath = session.getServletContext().getRealPath("/") + "/templates/excelExport/system_user_mb.xls";
//        toFile = session.getServletContext().getRealPath("/") + "/templates/excelExport/temporary/system_user_mb.xls";

		tempPath = "F:\\泉馨项目\\project\\qx-boot\\qingfeng-system\\src\\main\\resources\\templates\\excelExport/system_user_mb.xls";
		toFile = "F:\\泉馨项目\\project\\qx-boot\\qingfeng-system\\src\\main\\resources\\templates\\excelExport/temporary/system_user_mb.xls";

        InputStream in = new FileInputStream(tempPath);   //得到文档的路径
        //列表数据将存储到指定的excel文件路径，这个路径是在项目编译之后的target目录下
        FileOutputStream out = new FileOutputStream(toFile);
        //这里的context是jxls框架上的context内容
        org.jxls.common.Context context = new org.jxls.common.Context();
        //将列表参数放入context中
        context.putVar("obj", pd);
        context.putVar("list", list);
        //将List<Exam>列表数据按照模板文件中的格式生成到scoreOutput.xls文件中
        JxlsHelper.getInstance().processTemplate(in, out, context);

        FileUtil.downFile(response, toFile, "用户信息" + GuidUtil.getGuid() + ".xls");
        File file = new File(toFile);
        file.delete();
        file.deleteOnExit();
    }



    /**
     * @title saveImportExcel
     * @description 导入用户信息
     * @author Administrator
     * @updateTime 2022/9/24 0024 10:54
     */
    @PostMapping("/saveImportExcel")
    @RedisEvict(key = "cache:system:user", fieldKey = "")
    public void saveImportExcel(@RequestBody PageData pd,HttpSession session,HttpServletResponse response) throws IOException {
        String str = "导入成功";
        InputStream inputStream = null;
        String savePath = ParaUtil.localName;
        File files = new File(savePath+pd.get("file_path"));
        inputStream = new FileInputStream(files);

        Workbook book = new XSSFWorkbook(inputStream);
        Sheet sheet = book.getSheetAt(0);  //示意访问sheet
        int totalRows = sheet.getPhysicalNumberOfRows();
        int totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        String[] objs = new String[totalCells];
        String time = DateTimeUtil.getDateTimeStr();

        //处理数据权限
        String userParams = SecurityContextHolder.getContext().getAuthentication().getName();
        String user_id = userParams.split(":")[1];
        String organize_id = userParams.split(":")[2];
        PageData param = new PageData();
        param.put("user_id",user_id);
        PageData orgPd = userService.findUserOrganizeInfo(param);
        List<String> auth_organize_ids = new ArrayList<String>();
        if(Verify.verifyIsNotNull(orgPd)){
            if(Verify.verifyIsNotNull(orgPd.get("authOrgIds"))){
                auth_organize_ids = Arrays.asList(orgPd.get("authOrgIds").toString().split(","));
            }
        }
        Organize organize = new Organize();
        organize.setAuth_user(user_id);
        organize.setAuth_organize_ids(auth_organize_ids);
        //获取用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",user_id);
        SystemUser user = userService.getOne(queryWrapper);
        if(user.getType()!=0){//管理员
            organize.setOrg_cascade(orgPd.get("org_cascade").toString());
        }
        //查询组织
        List<Organize> organizeList = organizeService.list(new QueryWrapper<>());
        //查询字典-职位
        Dictionary dictionary = new Dictionary();
        dictionary.setParent_code("position");
        List<Dictionary> dictionaryList = dictionaryService.findList(dictionary);
        Map<String, Dictionary> map = dictionaryList.stream().collect(Collectors.toMap(Dictionary::getName, o -> o,(oldValue, newValue)->newValue));
        //查询岗位
        List<Post> postList = postService.list();
        //查询用户信息
        List<SystemUser> userList = userService.list();

        List<SystemUser> list = new ArrayList<SystemUser>();
        List<UserOrganize> uolist = new ArrayList<UserOrganize>();
        List<String> sexList = Arrays.asList(new String[]{"男", "女", "未知"});
        List<SystemUser> errorTitleList = new ArrayList<SystemUser>();
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalCells; j++) {
                Cell xssfCell = null;
                if(Verify.verifyIsNotNull(sheet.getRow(i).getCell(j))){
                    xssfCell = sheet.getRow(i).getCell(j);
                }
                if (totalRows >= 1 && sheet.getRow(0) != null) {
                    if(xssfCell==null){
                        objs[j]="";
                    }else{
                        if(xssfCell.toString().trim().equals("")){
                            objs[j]="";
                        }else{
                            if(xssfCell.getCellType()== CellType.FORMULA){
                                FormulaEvaluator evaluator = book.getCreationHelper().createFormulaEvaluator();
                                double resultScore = evaluator.evaluate(xssfCell).getNumberValue();// 读取计算结果 =SUM(M6:M15)
                                objs[j] = resultScore+"";
                            }else if(xssfCell.getCellType()==CellType.NUMERIC){
                                if (DateUtil.isCellDateFormatted(xssfCell)) {// 处理日期格式、时间格式
                                    SimpleDateFormat sdf = null;
                                    if (xssfCell.getCellStyle().getDataFormat() == HSSFDataFormat
                                            .getBuiltinFormat("h:mm")) {
                                        sdf = new SimpleDateFormat("HH:mm");
                                    } else {// 日期
                                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    }
                                    Date date = xssfCell.getDateCellValue();
                                    objs[j] = sdf.format(date).trim();
                                } else if (xssfCell.getCellStyle().getDataFormat() == 58) {
                                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    double value = xssfCell.getNumericCellValue();
                                    Date date = DateUtil
                                            .getJavaDate(value);
                                    objs[j] = sdf.format(date).trim();
                                } else {
                                    objs[j] = xssfCell.toString().trim();
                                }
                            }else if(xssfCell.getCellType()== CellType.STRING){
                                objs[j] = xssfCell.getRichStringCellValue().toString().trim();
                            }else{
                                objs[j]="";
                            }
                        }
                    }

                }
            }
            if(i!=0){
                if(Verify.verifyIsNotNull(objs[0])){
                    SystemUser systemUser = new SystemUser();
                    String organize_name = objs[0];
                    String post_ids = objs[1];
                    String position = objs[2];
                    String login_name = objs[3];
                    String name = objs[4];
                    String sex = objs[5];
                    String idcard = objs[6];
                    String phone = objs[7];
                    String email = objs[8];
                    String birth_date = objs[9];
                    String live_address = objs[10];
                    String birth_address = objs[11];
                    String motto = objs[12];
                    String remark = objs[13];

                    systemUser.setType(1);

                    String id = GuidUtil.getUuid();
                    systemUser.setId(id);
                    systemUser.setLogin_name(login_name);
                    systemUser.setLogin_password(passwordEncoder.encode(imitpwd));
                    systemUser.setUpdate_time(time);
                    systemUser.setName(name);
                    systemUser.setSex(sexList.indexOf(sex));
                    systemUser.setIdcard(idcard);
                    systemUser.setPhone(phone);
                    systemUser.setEmail(email);
                    systemUser.setBirth_date(birth_date);
                    systemUser.setBirth_address(birth_address);
                    systemUser.setLive_address(live_address);
                    systemUser.setMotto(motto);
                    systemUser.setInit_password("0");
                    systemUser.setStatus(0);
                    systemUser.setRemark(remark);
                    systemUser.setCreate_user(user_id);
                    systemUser.setCreate_organize(organize_id);
                    systemUser.setCreate_time(DateTimeUtil.getDateTimeStr());
                    systemUser.setOrganize_name(organize_name);
                    if(Verify.verifyIsNotNull(map.get(position))){
                        systemUser.setPosition(map.get(position).getId());
                    }

                    if(Verify.verifyIsNull(login_name)){
                        systemUser.setContent("登录名称不可为空。");
                        errorTitleList.add(systemUser);
                        continue;
                    }
                    if(Verify.verifyIsNull(name)){
                        systemUser.setContent("姓名不可为空。");
                        errorTitleList.add(systemUser);
                        continue;
                    }
                    //判断身份号、电子邮箱、手机号是否存在
//                    QueryWrapper<SystemUser> wrapper = new QueryWrapper();
//                    wrapper.and((wq)->{
//                        wq.eq("id_number",systemUser.getId_number()).or().eq("phone",systemUser.getPhone()).or().eq("email",systemUser.getEmail());
//                    });
//                    Long num = userService.count(wrapper);
                    if(Verify.verifyIsNotNull(systemUser.getLogin_name())){
                        List<SystemUser> users = userList.stream().filter(p->p.getLogin_name().equals(login_name)).collect(Collectors.toList());
                        if(users.size()>0){
                            systemUser.setContent("登录账号已存在。");
                            errorTitleList.add(systemUser);
                            continue;
                        }
                    }
                    if(Verify.verifyIsNotNull(systemUser.getIdcard())){
                        List<SystemUser> users = userList.stream().filter(p->Verify.verifyIsNotNull(p.getIdcard())&&p.getIdcard().equals(idcard)).collect(Collectors.toList());
                        if(users.size()>0){
                            systemUser.setContent("身份证号已存在。");
                            errorTitleList.add(systemUser);
                            continue;
                        }
                    }
                    if(Verify.verifyIsNotNull(systemUser.getPhone())){
                        List<SystemUser> users = userList.stream().filter(p->Verify.verifyIsNotNull(p.getPhone())&&p.getPhone().equals(phone)).collect(Collectors.toList());
                        if(users.size()>0){
                            systemUser.setContent("手机号已存在。");
                            errorTitleList.add(systemUser);
                            continue;
                        }
                    }
                    if(Verify.verifyIsNotNull(systemUser.getEmail())){
                        List<SystemUser> users = userList.stream().filter(p->Verify.verifyIsNotNull(p.getEmail())&&p.getEmail().equals(email)).collect(Collectors.toList());
                        if(users.size()>0){
                            systemUser.setContent("电子邮箱已存在。");
                            errorTitleList.add(systemUser);
                            continue;
                        }
                    }
                    //处理用户组织信息
                    List<Organize> organizes = organizeList.stream().filter(item->item.getName().equals(organize_name)).collect(Collectors.toList());
                    if(organizes.size()>0){
                        UserOrganize userOrganize = new UserOrganize();
                        userOrganize.setId(GuidUtil.getUuid());
                        userOrganize.setOrganize_id(organizes.get(0).getId());
                        userOrganize.setUser_id(id);
                        userOrganize.setType(0);
                        userOrganize.setUse_status(0);
                        userOrganize.setCreate_user(user_id);
                        userOrganize.setCreate_time(DateTimeUtil.getDateTimeStr());
                        uolist.add(userOrganize);
                        //处理岗位
                        if(Verify.verifyIsNotNull(post_ids)){
                            List<String> pids = Arrays.asList(post_ids.split(","));
                            List<Post> posts = postList.stream().filter(p->pids.contains(p.getName()) && p.getOrganize_id().equals(organizes.get(0).getId())).collect(Collectors.toList());
                            List<String> postIds = posts.stream().map(Post::getId).collect(Collectors.toList());
                            systemUser.setPost_ids(StringUtils.join(postIds,","));
                        }
                    }else{
                        systemUser.setContent("组织名称不存在。");
                        errorTitleList.add(systemUser);
                    }
                    list.add(systemUser);
                }
            }
        }
        Json json = new Json();
        if(errorTitleList.size()==0){
            this.userService.saveImportList(list);
            this.userService.saveUOImportList(uolist);
            json.setSuccess(true);
            json.setMsg(str);
        }else{
            json.setSuccess(false);
            json.setMsg("导入数据异常");
            json.setData(errorTitleList);
        }
        this.writeJson(response,json);
    }




}
