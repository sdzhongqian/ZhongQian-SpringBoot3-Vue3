package ${tablePd.pack_path}.${tablePd.mod_name}.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.hibernate.validator.constraints.Range;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName ${tablePd.bus_name?cap_first}
 * @author Administrator
 * @version 1.0.0
 * @Description 字典信息
 * @createTime 2022/1/19 0019 22:54
 */
@Data
@TableName("${tablePd.table_name}")
public class ${tablePd.bus_name?cap_first} implements Serializable {

    private static final long serialVersionUID = -4352868070794165001L;

<#list fieldList as obj>
<#if obj.field_name == 'id'>
    /**
    * 主键id
    */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
</#if>
<#if obj.field_name == 'type'||obj.field_name == 'order_by'||obj.field_name == 'status'>
    /**
    * ${obj.field_comment}
    */
    @TableField("${obj.field_name}")
    <#if obj.field_name == 'type'>
    @Range(min = 0, max = 9999, message = "类型取值范围：[0-9999]")
    </#if>
    <#if obj.field_name == 'order_by'>
    @Range(min = 0, max = 9999, message = "排序必须在0到9999之间")
    </#if>
    <#if obj.field_name == 'status'>
    @Range(min = -1, max = 1, message = "状态参数只能为0或1")
    </#if>
    private int ${obj.field_name};
</#if>
<#if obj.field_name != 'id'&&obj.field_name != 'type'&&obj.field_name != 'order_by'&&obj.field_name != 'status'>
    /**
    * ${obj.field_comment}
    */
    @TableField("${obj.field_name}")
    private String ${obj.field_name};
</#if>
</#list>

<#if tablePd.temp_type == 1>
    /**
    * 父级id
    */
    @TableField("parent_id")
    private String parent_id;

    @TableField(exist = false)
    private String parent_name;
</#if>

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