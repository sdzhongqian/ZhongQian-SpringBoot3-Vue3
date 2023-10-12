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
 * @ProjectName Education
 * @author Administrator
 * @version 1.0.0
 * @Description 字典信息
 * @createTime 2022/1/19 0019 22:54
 */
@Data
@TableName("personnel_education")
public class Education implements Serializable {

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
    * 用户ID
    */
    @TableField("user_id")
    private String user_id;
    /**
    * 学校
    */
    @TableField("school")
    private String school;
    /**
    * 专业
    */
    @TableField("special")
    private String special;
    /**
    * 学历
    */
    @TableField("education")
    private String education;
    /**
    * 学位
    */
    @TableField("degree")
    private String degree;
    /**
    * 入学日期
    */
    @TableField("start_time")
    private String start_time;
    /**
    * 毕业日期
    */
    @TableField("end_time")
    private String end_time;
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
    private String ids;

    @TableField(exist = false)
    private String education_name;
    @TableField(exist = false)
    private String degree_name;

}