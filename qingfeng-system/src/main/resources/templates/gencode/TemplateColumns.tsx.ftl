import { Tag } from 'ant-design-vue';
import type { TableColumn } from '@/components/core/dynamic-table';

export type TableListItem = API.${tablePd.bus_name?cap_first}ListResult;
export type TableColumnItem = TableColumn<TableListItem>;


export const baseColumns: TableColumnItem[] = [
<#list fieldList as obj>
<#if obj.field_list == 'Y'>
  <#if obj.show_type == 3 || obj.show_type == 4>
  {
    title: "${obj.field_comment}",
    <#if obj.option_content?contains(";")>
      dataIndex: "${obj.field_name}",
    </#if>
    <#if !obj.option_content?contains(";")>
      dataIndex: "${obj.field_name}_name",
    </#if>
    ellipsis: true,
    <#if obj.field_query != 'Y'>
    hideInSearch: true,
    </#if>
    <#if obj.option_content?contains(";")>
      customRender: (record) => {
      <#list obj.option_content?split(";") as name>
        <#assign param = name?split("/")>
        if(record.value=='${param[0]}'){
        return '${param[1]}';
        }
      </#list>
      },
    </#if>
  },
  <#else>
  {
    title: "${obj.field_comment}",
    dataIndex: "${obj.field_name}",
  <#if obj.field_query != 'Y'>
    hideInSearch: true,
  </#if>
    ellipsis: true,
  },
  </#if>
</#if>
</#list>
  {
    title: '排序',
    dataIndex: 'order_by',
    hideInSearch: true,
    align: 'center',
    width: 80,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
    hideInSearch: true,
    formItemProps: {
      component: 'Select',
      componentProps: {
        options: [
          {
            label: '启用',
            value: 0,
          },
          {
            label: '禁用',
            value: 1,
          },
        ],
      },
    },
    customRender: ({ record }) => {
      const isEnable = record.status === 0;
      return <Tag color={isEnable ? 'success' : 'red'}>{isEnable ? '启用' : '禁用'}</Tag>;
    },
  },
  {
    title: '创建时间',
    dataIndex: 'create_time',
    width: 120,
    hideInSearch: true,
    formItemProps: {
      component: 'DatePicker',
      componentProps: {
        class: 'w-full',
      },
    },
  },
];
