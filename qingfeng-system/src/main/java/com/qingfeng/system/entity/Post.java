package com.qingfeng.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName Post
 * @author Administrator
 * @version 1.0.0
 * @Description 字典信息
 * @createTime 2022/1/19 0019 22:54
 */
@Data
@TableName("system_post")
public class Post implements Serializable {

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
    * 名称
    */
    @TableField("name")
    private String name;
    /**
    * 组织id
    */
    @TableField("organize_id")
    private String organize_id;
    /**
    * 岗位负责人id
    */
    @TableField("leader_id")
    private String leader_id;
    /**
    * 岗位性质
    */
    @TableField("nature")
    private String nature;
    /**
    * 岗位职责
    */
    @TableField("content")
    private String content;
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
    private String remark;
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
    * 创建时间
    */
    @TableField("create_time")
    private String create_time;
    /**
    * 修改时间
    */
    @TableField("update_time")
    private String update_time;
    /**
    * 修改人
    */
    @TableField("update_user")
    private String update_user;


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
    private String leader_name;
}