package com.qingfeng.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName Area
 * @author Administrator
 * @version 1.0.0
 * @Description 地区信息
 * @createTime 2022/1/19 0019 23:36
 */
@Data
@TableName("system_area")
public class Area implements Serializable {

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
     * 级联字段
     */
    @TableField("area_cascade")
    private String area_cascade;

    /**
     * 名称
     */
    @TableField("name")
    @NotBlank(message = "名称不能为空")
    @Length(max = 120,message = "名称不得大于120个字符")
    private String name;
    /**
     * 简称
     */
    @TableField("short_name")
    @Length(max = 120,message = "简称不得大于120个字符")
    private String short_name;
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
    @Length(max = 50,message = "等级不得大于50个字符")
    private String level_num;
    /**
     * 英文名称
     */
    @TableField("en_name")
    @Length(max = 1200,message = "英文名称不得大于1200个字符")
    private String en_name;
    /**
     * 英文简称
     */
    @TableField("en_short_name")
    @Length(max = 1200,message = "英文简称不得大于1200个字符")
    private String en_short_name;
    /**
     * 经度
     */
    @TableField("lng")
    @Length(max = 50,message = "经度不得大于50个字符")
    private String lng;
    /**
     * 维度
     */
    @TableField("lat")
    @Length(max = 50,message = "维度不得大于50个字符")
    private String lat;
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
    private String parent_name;

    @TableField(exist = false)
    private String child_num;

}