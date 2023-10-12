package com.qingfeng.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qingfeng.module.common.base.annotation.validator.IsMobile;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0.0
 * @ProjectName qingfeng
 * @Description TODO
 * @createTime 2022年01月18日 22:02:00
 */
@Data
@TableName("system_user")
public class SystemUser implements Serializable {

    private static final long serialVersionUID = -4352868070794165001L;

    /**
     * 用户 ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 类型
     */
    @TableField(value = "type")
    @Range(min = 0, max = 9999, message = "类型取值范围：[0-9999]")
    private int type;

    /**
     * 用户名
     */
    @TableField("login_name")
    @NotBlank(message = "登陆名称不能为空")
    @Length(min = 4, max = 50,message = "登陆名称必须大于4个字符小于50字符")
    private String login_name;

    /**
     * 密码
     */
    @TableField("login_password")
    private String login_password;

    /**
     * 姓名
     */
    @TableField("name")
    @NotBlank(message = "姓名不能为空")
    @Length(max = 50,message = "姓名不得大于50个字符")
    private String name;

    /**
     * 身份证号
     */
    @TableField("idcard")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", message = "请输入合法的身份证号")
    private String idcard;

    /**
     * 性别
     */
    @TableField("sex")
    @NotNull(message = "性别不能为空")
//    @Pattern(regexp = "^$|^([0-1])$", message = "性别参数只能为0或1")
    @Range(min = 0, max = 1, message = "性别参数只能为0或1")
    private int sex;

    /**
     * 邮箱
     */
    @TableField("email")
    @Length(max = 50,message = "邮箱不得大于50个字符")
    @Email(message = "请输入合法的电子邮箱")
    private String email;

    /**
     * 联系电话
     */
    @TableField("phone")
    @IsMobile(message = "请输入合法的手机号")
    private String phone;

    /**
     * 出生日期
     */
    @TableField("birth_date")
    @Length(max = 50,message = "出生日期不得大于50个字符")
    private String birth_date;
    /**
     * 居住地
     */
    @TableField("live_address")
    @Length(max = 50,message = "居住地不得大于50个字符")
    private String live_address;
    /**
     * 籍贯地址
     */
    @TableField("birth_address")
    @Length(max = 50,message = "籍贯地址不得大于50个字符")
    private String birth_address;
    /**
     * 座右铭
     */
    @TableField("motto")
    @Length(max = 50,message = "座右铭不得大于50个字符")
    private String motto;
    /**
     * 状态
     */
    @TableField("status")
    @Range(min = -1, max = 10, message = "状态参数只能为0或1")
    private int status;
    /**
     * 排序
     */
    @TableField("order_by")
    @Range(min = 0, max = 9999, message = "排序必须在0到9999之间")
    private int order_by;
    /**
     * 是否初始密码
     */
    @TableField("init_password")
    private String init_password;

    /**
     * 密码输入错误次数
     */
    @TableField("pwd_error_num")
    private String pwd_error_num;
    /**
     * 密码输入错误时间
     */
    @TableField("pwd_error_time")
    private String pwd_error_time;
    /**
     * 备注
     */
    @TableField("remark")
    @Length(max = 500,message = "备注不得大于500个字符")
    private String remark;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private String create_time;
    /**
     * 创建人
     */
    @TableField("create_user")
    private String create_user;
    /**
     * 创建组织
     */
    @TableField("create_organize")
    private String create_organize;
    /**
     * 修改人
     */
    @TableField("update_user")
    private String update_user;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private String update_time;
    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private String last_login_time;

    /**
     * 岗位管理
     */
    @TableField("post_ids")
    private String post_ids;
    /**
     * 职位管理
     */
    @TableField("position")
    private String position;


    @TableField(exist = false)
    private List<String> auth_organize_ids;

    @TableField(exist = false)
    private String auth_user;

    @TableField(exist = false)
    private String organize_id;
    @TableField(exist = false)
    private String organize_name;
    //辅助添加
    @TableField(exist = false)
    private String fileIds;
    @TableField(exist = false)
    private String ids;
    @TableField(exist = false)
    private String headImg;

    @TableField(exist = false)
    private String position_name;

    @TableField(exist = false)
    private List<Post> postList;

    @TableField(exist = false)
    private String post_name;

    @TableField(exist = false)
    private int xh;
    @TableField(exist = false)
    private String status_name;
    @TableField(exist = false)
    private String content;

}
