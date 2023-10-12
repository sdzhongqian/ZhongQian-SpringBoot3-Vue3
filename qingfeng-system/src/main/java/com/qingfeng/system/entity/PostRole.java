package com.qingfeng.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName PostRole
 * @author Administrator
 * @version 1.0.0
 * @Description 岗位角色信息
 * @createTime 2022/1/20 0020 0:09
 */
@Data
@TableName("system_post_role")
public class PostRole implements Serializable {

    private static final long serialVersionUID = -3166012934498268403L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 岗位id
     */
    @TableField(value = "post_id")
    private String post_id;
    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private String role_id;
    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private String create_user;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private String create_time;
    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private String update_time;

}