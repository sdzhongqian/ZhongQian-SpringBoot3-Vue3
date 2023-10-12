import type { FormSchema } from '@/components/core/schema-form/';
import { findDictionaryList } from '@/api/system/dictionary';
import { Storage } from '@/utils/Storage';
import { ACCESS_TOKEN_KEY } from '@/enums/cacheEnum';
import { formatStrategyValues } from 'ant-design-vue/es/vc-tree-select/utils/strategyUtil';
const baseUrl = import.meta.env.VITE_BASE_API;

export const schemas: FormSchema<API.PostEditParams>[] = [
  {
    field: 'organize_id',
    component: 'TreeSelect',
    label: '所属部门',
    componentProps: {
      getPopupContainer: () => document.body,
    },
    rules: [{ required: true, type: 'string' }],
  },
  {
    field: 'name',
    component: 'Input',
    label: '名称',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入名称且不多于50个字符' }],
  },
  {
    field: 'leader_name',
    component: 'SelectOrg',
    label: '岗位负责人',
    colProps: {
      span: 12,
    },
    // dynamicDisabled:true,
    rules: [
      { required: false, type: 'string', max: 50, message: '请选择岗位负责人', trigger: 'blur' },
    ],
    componentProps: ({ formModel }) => {
      return {
        type: 0,
        placeholder: '请选择岗位负责人',
        onChange: (e) => {
          if (e.length > 0) {
            const ids = Array.from(e, ({ id }) => id);
            formModel['leader_id'] = ids.join(',');
          }
        },
      };
    },
  },
  {
    field: 'nature',
    component: 'Select',
    label: '岗位性质',
    defaultValue: '0',
    colProps: {
      span: 12,
    },
    rules: [
      { required: false, type: 'string', max: 50, message: '请输入字典名称且不多于50个字符' },
    ],
    componentProps: {
      options: [
        {
          label: '全职',
          value: '0',
          key: '0',
        },
        {
          label: '兼职',
          value: '1',
          key: '1',
        },
        {
          label: '实习',
          value: '2',
          key: '2',
        },
        {
          label: '其他',
          value: '3',
          key: '3',
        },
      ],
    },
  },
  {
    field: 'content',
    component: 'Tinymce',
    componentProps: ({ formModel }) => {
      return {
        content: formModel['content'],
        placeholder: '请输入',
        onChange: (e) => {
          formModel['content'] = e;
        },
      };
    },
    label: '岗位职责',
    rules: [{ required: false, type: 'string', message: '请输入字典名称且不多于50个字符' }],
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
      max: 9999,
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
  {
    field: 'leader_id',
    component: 'Input',
    label: '隐藏字段',
    vShow: false,
  },
];
