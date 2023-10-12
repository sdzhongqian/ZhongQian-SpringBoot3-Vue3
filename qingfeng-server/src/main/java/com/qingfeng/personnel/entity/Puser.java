package com.qingfeng.personnel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
@TableName("personnel_puser")
public class Puser implements Serializable {

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
     * 用户ID
     */
    @TableField("user_id")
    private String user_id;
    /**
     * 工号
     */
    @TableField("job_num")
    private String job_num;
    /**
     * 入职日期
     */
    @TableField("job_date")
    private String job_date;
    /**
     * 用工类型
     */
    @TableField("job_type")
    private String job_type;
    /**
     * 人员状态
     */
    @TableField("job_status")
    private String job_status;
    /**
     * 学历
     */
    @TableField("education")
    private String education;
    /**
     * 婚姻
     */
    @TableField("marriage")
    private String marriage;
    /**
     * 民族
     */
    @TableField("nation")
    private String nation;
    /**
     * 政治面貌
     */
    @TableField("politics_face")
    private String politics_face;
    /**
     * 入党时间
     */
    @TableField("party_time")
    private String party_time;
    /**
     * 基本工资
     */
    @TableField("basic_salary")
    private String basic_salary;
    /**
     * 岗位工资
     */
    @TableField("post_salary")
    private String post_salary;
    /**
     * 绩效工资
     */
    @TableField("merit_salary")
    private String merit_salary;
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


    /**
     * 教育经历
     */
    @TableField(exist = false)
    private List<Education> educationList;

    /**
     * 证件照信息
     */
    @TableField(exist = false)
    private List<Photo> photoList;

    /**
     * 工作经历
     */
    @TableField(exist = false)
    private List<Workexper> workexperList;

    @TableField(exist = false)
    private String ids;
    @TableField(exist = false)
    private String names;
    @TableField(exist = false)
    private String reason;
    @TableField(exist = false)
    private String organize_id;
    @TableField(exist = false)
    private int status;

    @TableField(exist = false)
    private String education_name;
    @TableField(exist = false)
    private String marriage_name;
    @TableField(exist = false)
    private String nation_name;
    @TableField(exist = false)
    private String politics_face_name;


}