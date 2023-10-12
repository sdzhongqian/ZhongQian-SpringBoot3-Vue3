package com.qingfeng.module.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName Czlog
 * @author Administrator
 * @version 1.0.0
 * @Description 字典信息
 * @createTime 2022/1/19 0019 22:54
 */
@Data
@TableName("common_czlog")
public class Czlog implements Serializable {

    private static final long serialVersionUID = -4352868070794165001L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 标题
     */
    @TableField("title")
    private String title;
    /**
     * 动作
     */
    @TableField("action")
    private String action;
    /**
     * 系统类型
     */
    @TableField("sysType")
    private String sysType;
    /**
     * 操作类型
     */
    @TableField("opType")
    private String opType;
    /**
     * 请求时间
     */
    @TableField("reqtime")
    private String reqtime;
    /**
     * 请求路径
     */
    @TableField("reqpath")
    private String reqpath;
    /**
     * 操作类
     */
    @TableField("opera_class")
    private String opera_class;
    /**
     * 操作方法
     */
    @TableField("opera_method")
    private String opera_method;
    /**
     * 类型
     */
    @TableField("type")
    private String type;
    /**
     * 操作内容
     */
    @TableField("param")
    private String param;
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 名称
     */
    @TableField("user_name")
    private String user_name;
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


    @TableField(exist = false)
    private String start_time;

    @TableField(exist = false)
    private String end_time;

    @TableField(exist = false)
    private List<String> auth_organize_ids;

    @TableField(exist = false)
    private String auth_user;

    @TableField(exist = false)
    private String ids;

    @TableField(exist = false)
    private String child_num;
}