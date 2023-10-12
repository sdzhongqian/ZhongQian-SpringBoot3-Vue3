import type { FormSchema } from '@/components/core/schema-form/';
import { findDictionaryList } from "@/api/system/dictionary";
import { Storage } from '@/utils/Storage';
import { ACCESS_TOKEN_KEY } from '@/enums/cacheEnum';
const baseUrl = import.meta.env.VITE_BASE_API;

export const schemas: FormSchema<API.${tablePd.bus_name?cap_first}EditParams>[] = [
  <#if tablePd.temp_type == 1>
  {
    field: '${tablePd.tree_pid}',
    component: 'TreeSelect',
    label: '${tablePd.tree_name}',
    componentProps: {
      getPopupContainer: () => document.body,
    },
    rules: [{ required: true, type: 'string' }],
  },
  </#if>
<#list fieldList as obj>
<#if obj.field_operat == 'Y'>
  {
    <#if obj.show_type != 8>
    field: '${obj.field_name}',
    </#if>
    <#if obj.show_type == 1>
    component: 'Input',
    </#if>
    <#if obj.show_type == 2>
    component: 'InputTextArea',
    componentProps: {
      autosize: { minRows: 4, maxRows: 8 },
    },
    </#if>
    <#if obj.show_type == 3||obj.show_type == 4||obj.show_type == 5>
    <#if obj.show_type == 3>
    component: 'Select',
    </#if>
    <#if obj.show_type == 4>
    component: 'RadioGroup',
    </#if>
    <#if obj.show_type == 5>
    component: 'CheckboxGroup',
    </#if>
    componentProps: {
    <#if obj.option_content?contains(";")>
      options: [
       <#list obj.option_content?split(";") as name>
        <#assign param = name?split("/")>
        {
          label: '${param[1]}',
          value: '${param[0]}',
          key: '${param[0]}',
        },
        </#list>
      ],
    </#if>
    <#if !obj.option_content?contains(";")>
      request: async (values) => {
        const res:any = await findDictionaryList({parent_code:'${obj.option_content}'});
        return res.data;
      },
    </#if>
    },
    </#if>
    <#if obj.show_type == 6>
    component: 'Tinymce',
    componentProps: ({ formModel }) => {
      return {
        ${obj.field_name}: formModel['${obj.field_name}'],
        placeholder: '请输入',
        onChange: (e) => {
          formModel['${obj.field_name}'] = e;
        },
      };
    },
    </#if>
    <#if obj.show_type == 7>
    component: 'DatePicker',
    </#if>
    <#if obj.show_type == 8>
      component: 'upload_${obj.field_name}',
      componentProps: {
        action: baseUrl + 'upload/uploadFile',
        headers: {
          Authorization: 'Bearer ' + Storage.get(ACCESS_TOKEN_KEY),
        },
      },
      data: {
        source: '${tablePd.bus_name}_${obj.field_name}',
      },
      /** 类似于 vue 中的 watch */
      request: {
        watchFields: ['upload_${obj.field_name}'],
        options: {
          immediate: true,
        },
        callback: async ({ formModel }) => {
          if (formModel['upload_${obj.field_name}'] != undefined) {
            formModel['${obj.field_name}'] = Array.from(formModel['upload_${obj.field_name}'],({ id }) => id);
          }
        },
      },
      componentSlots: {
        default: () => (
          <Button>
            <UploadOutlined /> 上传
          </Button>
        ),
      },
    },
    </#if>
    label: '${obj.field_comment}',
    <#if obj.verify_rule=='required'>
    rules: [{ required: true, type: 'string', max: 50, message: '请输入${obj.field_comment}且不多于50个字符' }],
    </#if>
    <#if obj.verify_rule!='required'>
    rules: [{ required: false, type: 'string', max: 50, message: '请输入${obj.field_comment}且不多于50个字符' }],
    </#if>
  },
</#if>
</#list>
  {
    field: 'order_by',
    component: 'InputNumber',
    label: '排序号',
    defaultValue: 1,
    componentProps: {
      style: {
        width: '100%',
      },
      max: 9999
    },
    rules: [{ required: false, type: 'number', max: 9999, message: '请输入正确的排序号' }],
  },
  {
    field: 'remark',
    component: 'InputTextArea',
    label: '备注',
    componentProps: {
      autosize: { minRows: 4, maxRows: 8 },
    },
    rules: [{ required: false, type: 'string', max: 500, message: '请输入备注且不多于500个字符' }],
  },
  <#list fieldList as obj>
  <#if obj.field_operat == 'Y'>
  <#if obj.show_type == 8>
  {
    field: '${obj.field_name}',
    component: 'Input',
    label: '隐藏字段',
    vShow: false,
  },
  </#if>
  </#if>
  </#list>
];

