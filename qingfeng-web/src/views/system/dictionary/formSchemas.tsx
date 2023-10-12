import type { FormSchema } from '@/components/core/schema-form/';

export const dicSchemas: FormSchema<API.DictionaryEditParams>[] = [
  {
    field: 'parent_id',
    component: 'TreeSelect',
    label: '上级名称',
    componentProps: {
      getPopupContainer: () => document.body,
    },
    rules: [{ required: true, type: 'string' }],
  },
  {
    field: 'name',
    component: 'Input',
    label: '字典名称',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入字典名称且不多于50个字符' }],
  },
  {
    field: 'short_name',
    component: 'Input',
    label: '字典简称',
    rules: [{ required: false, type: 'string', max: 50, message: '请输入字典简称且不多于50个字符' }],
  },
  {
    field: 'code',
    component: 'Input',
    label: '字典编码',
    rules: [{ required: false, type: 'string', max: 50, message: '请输入字典编码且不多于50个字符' }],
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
    field: 'remark',
    component: 'InputTextArea',
    label: '备注',
    componentProps: {
      autosize: { minRows: 4, maxRows: 8 },
    },
    rules: [{ required: false, type: 'string', max: 500, message: '请输入备注且不多于500个字符' }],
  },
  {
    field: 'status',
    component: 'Input',
    label: '隐藏字段',
    vShow: false,
  },
];

