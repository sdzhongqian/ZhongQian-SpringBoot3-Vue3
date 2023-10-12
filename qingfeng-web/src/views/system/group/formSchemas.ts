import type { FormSchema } from '@/components/core/schema-form/';

export const groupSchemas: FormSchema<API.GroupEditParams>[] = [
  {
    field: 'name',
    component: 'Input',
    label: '组名称',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入角色名称且不多于50个字符' }],
  },
  {
    field: 'short_name',
    component: 'Input',
    label: '组简称',
    rules: [{ required: false, type: 'string', max: 50, message: '请输入角色简称且不多于50个字符' }],
  },
  {
    field: 'user_names',
    component: 'SelectOrg',
    label: '组用户',
    // dynamicDisabled:true,
    rules: [{ required: true, type: 'string', max: 50, message: '请选择组织名称且不多于50个字符', trigger: 'blur' }],
    componentProps: ({ formModel }) => {
      return {
        type: 3,
        placeholder: "请选择组用户",
        onChange: (e) => {
          if (e.length > 0) {
            const ids = Array.from(e,({ id }) => id);
            formModel['user_ids'] = ids.join(',');
          }
        },
      }
    },
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
    field: 'user_ids',
    component: 'Input',
    label: '隐藏字段',
    vShow: false
  },
  {
    field: 'status',
    component: 'Input',
    label: '隐藏字段',
    vShow: false,
  },
];
