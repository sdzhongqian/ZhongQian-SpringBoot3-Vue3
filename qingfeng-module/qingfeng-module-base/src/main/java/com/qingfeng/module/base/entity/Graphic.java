package com.qingfeng.module.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName Graphic
 * @author Administrator
 * @version 1.0.0
 * @Description 图文信息
 * @createTime 2022/6/25 0025 21:50
 */
@Data
@TableName("common_graphic")
public class Graphic implements Serializable {

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
    private int type;
    /**
     * 标题
     */
    @TableField("title")
    private String title;
    /**
     * 简介
     */
    @TableField("intro")
    private String intro;
    /**
     * 内容
     */
    @TableField("content")
    private String content;
    /**
     * 图片地址
     */
    @TableField("tpdz")
    private String tpdz;
    /**
     * 发布人
     */
    @TableField("publish_user")
    private String publish_user;
    /**
     * 发布时间
     */
    @TableField("publish_time")
    private String publish_time;
    /**
     * 阅读次数
     */
    @TableField("read_num")
    private int read_num;
    /**
     * 状态
     */
    @TableField("status")
    private int status;
    /**
     * 排序
     */
    @TableField("order_by")
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
    private String show_tpdz;



    @TableField(exist = false)
    private int limit;

}
