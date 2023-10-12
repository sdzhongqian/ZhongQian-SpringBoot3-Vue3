import type { FormSchema } from '@/components/core/schema-form/';
import { PlusOutlined } from '@ant-design/icons-vue';
import { Radio, Button } from 'ant-design-vue';
import { Storage } from '@/utils/Storage';
import { ACCESS_TOKEN_KEY } from '@/enums/cacheEnum';

const baseUrl = import.meta.env.VITE_BASE_API

export const userOrganizeSchemas: FormSchema<API.UserOrganizeParams>[] = [
  {
    field: 'type',
    component: 'Input',
    label: '组织类型',
    defaultValue: '兼职组织',
    dynamicDisabled: true,
    rules: [{ required: true, type: 'string', max: 50, message: '请输入组织类型且不多于50个字符' }],
  },
  {
    field: 'organize_name',
    component: 'SelectOrg',
    label: '组织名称',
    // dynamicDisabled:true,
    rules: [{ required: true, type: 'string', max: 50, message: '请选择组织名称且不多于50个字符', trigger: 'blur' }],
    componentProps: ({ formModel }) => {
      return {
        type: 1,
        placeholder: "请选择组织",
        onChange: (e) => {
          if (e.length == 1) {
            formModel['organize_id'] = e[0].id;
          }
        },
      }
    },
  },
  {
    field: 'position',
    component: 'Input',
    label: '担任职务',
    rules: [{ required: false, type: 'string', max: 50, message: '请输入担任职务且不多于50个字符' }],
  },
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
    field: 'organize_id',
    component: 'Input',
    label: '隐藏字段',
    vShow: false
  }
];
