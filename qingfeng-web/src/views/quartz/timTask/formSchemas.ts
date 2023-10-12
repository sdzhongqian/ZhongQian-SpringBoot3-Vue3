import type { FormSchema } from '@/components/core/schema-form/';

export const timTaskSchemas: FormSchema<API.TimTaskEditParams>[] = [
  {
    field: 'job_name',
    component: 'Input',
    label: '任务名称',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入任务名称且不多于50个字符' }],
  },
  {
    field: 'job_group',
    component: 'Input',
    label: '任务分组',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入任务分组且不多于50个字符' }],
  },
  {
    field: 'job_class_name',
    component: 'Input',
    label: '执行类',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入执行类且不多于50个字符' }],
  },
  {
    field: 'cron_expression',
    component: 'Input',
    label: '执行时间',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入执行时间且不多于50个字符' }],
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
];
