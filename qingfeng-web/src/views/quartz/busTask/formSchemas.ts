import type { FormSchema } from '@/components/core/schema-form/';

export const busTaskSchemas: FormSchema<API.BusTaskEditParams>[] = [
  {
    field: 'job_name',
    component: 'Input',
    label: '任务名称',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入任务名称且不多于50个字符' }],
  },
  {
    field: 'notice_user',
    component: 'Input',
    label: '接受人',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入接受人且不多于50个字符' }],
  },
  {
    field: 'cron_expression',
    component: 'Input',
    label: '执行表达式',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入执行表达式且不多于50个字符' }],
  },
  {
    field: 'description',
    component: 'InputTextArea',
    label: '任务描述',
    componentProps: {
      autosize: { minRows: 4, maxRows: 8 },
    },
    rules: [{ required: false, type: 'string', max: 500, message: '请输入任务描述且不多于500个字符' }],
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
];
