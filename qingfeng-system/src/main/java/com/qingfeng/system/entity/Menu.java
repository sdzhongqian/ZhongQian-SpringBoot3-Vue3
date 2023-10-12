package com.qingfeng.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName Menu
 * @author Administrator
 * @version 1.0.0
 * @Description 菜单信息
 * @createTime 2022/1/19 0019 22:38
 */
@Data
@TableName("system_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 7187628714679791771L;


    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 类型
     */
    @TableField("type")
    @NotNull(message = "类型不能为空")
    @Range(min = 0, max = 4, message = "类型只能为0或1或2或3或4")
    private Integer type;

    /**
     * 名称
     */
    @TableField("name")
    @NotBlank(message = "名称不能为空")
    @Length(max = 50,message = "名称不得大于50个字符")
    private String name;

    /**
     * 请求路径
     */
    @TableField("path")
    @Length(max = 120,message = "请求路径不得大于120个字符")
    private String path;
    /**
     * 重定向路径
     */
    @TableField("redirect")
    @Length(max = 120,message = "重定向路径不得大于120个字符")
    private String redirect;

    /**
     * 组件
     */
    @TableField("component")
    @Length(max = 120,message = "组件地址不得大于120个字符")
    private String component;

    /**
     * 标题
     */
    @TableField("title")
    @Length(max = 50,message = "标题不得大于50个字符")
    private String title;

    /**
     * keepAlive
     */
    @TableField("keepAlive")
    @Length(max = 50,message = "keepAlive不得大于50个字符")
    private String keepAlive;

    /**
     * 图标
     */
    @TableField("icon")
    @Length(max = 50,message = "图标不得大于50个字符")
    private String icon;

    /**
     * 权限标识
     */
    @TableField("permission")
    @Length(max = 50,message = "权限标识不得大于50个字符")
    private String permission;

    /**
     * 菜单级联
     */
    @TableField("menu_cascade")
    private String menu_cascade;

    /**
     * 父级id
     */
    @TableField("parent_id")
    @Length(max = 50,message = "父级id不得大于50个字符")
    private String parent_id;
    /**
     * 等级
     */
    @TableField("level_num")
    @Range(min = 0, max = 9999, message = "等级取值范围：[0-9999]")
    private int level_num;


    /**
     * 状态
     */
    @TableField("status")
    @Range(min = -1, max = 1, message = "状态参数只能为0或1")
    private int status;
    /**
     * 排序
     */
    @TableField("order_by")
    @Range(min = 0, max = 9999, message = "排序必须在0到9999之间")
    private int order_by;

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


    @TableField(exist = false)
    private List<String> auth_organize_ids;

    @TableField(exist = false)
    private String auth_user;

    @TableField(exist = false)
    private String child_num;

}
