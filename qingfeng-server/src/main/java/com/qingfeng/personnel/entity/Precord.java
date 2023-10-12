package com.qingfeng.personnel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName Precord
 * @author Administrator
 * @version 1.0.0
 * @Description 字典信息
 * @createTime 2022/1/19 0019 22:54
 */
@Data
@TableName("personnel_precord")
public class Precord implements Serializable {

    private static final long serialVersionUID = -4352868070794165001L;

    /**
    * 主键id
    */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
    * 类型
    */
    @TableField("type")
    @Range(min = 0, max = 9999, message = "类型取值范围：[0-9999]")
    private int type;
    /**
    * 原因
    */
    @TableField("reason")
    private String reason;
    /**
    * 用户ID集合
    */
    @TableField("user_ids")
    private String user_ids;
    /**
    * 用户名称集合
    */
    @TableField("user_names")
    private String user_names;
    /**
    * 备注
    */
    @TableField("remark")
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