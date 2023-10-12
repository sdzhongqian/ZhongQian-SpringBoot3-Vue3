declare namespace API {

  /** 获取数据返回结果 */
  type ${tablePd.bus_name?cap_first}QueryParams = {
<#list fieldList as obj>
  <#if obj.field_query == 'Y'>
  <#if obj.field_name == 'type'||obj.field_name == 'order_by'||obj.field_name == 'status'>
    ${obj.field_name}: number;
  </#if>
  <#if obj.field_name != 'type'&&obj.field_name != 'order_by'&&obj.field_name != 'status'>
    ${obj.field_name}: string;
  </#if>
  </#if>
</#list>
    status: number;
    page?: number;
    limit?: number;
  };

  /** 获取数据返回结果 */
  type ${tablePd.bus_name?cap_first}ListResult = {
    id: string;
    type: number;
<#list fieldList as obj>
  <#if obj.field_list == 'Y'>
    ${obj.field_name}: string;
  </#if>
</#list>
    status: number;
    order_by: number;
    create_time: string;
  };

  /** 删除数据的参数 */
  type ${tablePd.bus_name?cap_first}DelParams = {
    ids: string | number;
  };

  /** 更新某个部门需要传的参数 */
  type ${tablePd.bus_name?cap_first}EditParams = {
    id: string;
<#list fieldList as obj>
  <#if obj.field_operat == 'Y'>
    ${obj.field_name}: string;
  </#if>
</#list>
    order_by: number;
    status: number;
  };

  /** 更新状态参数 */
  type ${tablePd.bus_name?cap_first}EditStatusParams = {
    id: string;
    status: number;
  };

  /** 查询详情 */
  type ${tablePd.bus_name?cap_first}InfoResult = {
    id: string;
<#list fieldList as obj>
    ${obj.field_name}: string;
</#list>
    status: number;
    order_by: number;
    create_time: string;
  }

}