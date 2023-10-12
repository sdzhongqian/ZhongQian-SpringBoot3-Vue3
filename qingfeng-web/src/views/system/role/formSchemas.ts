import type { FormSchema } from '@/components/core/schema-form/';

export const roleSchemas: FormSchema<API.RoleEditParams>[] = [
  {
    field: 'name',
    component: 'Input',
    label: '角色名称',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入角色名称且不多于50个字符' }],
  },
  {
    field: 'short_name',
    component: 'Input',
    label: '角色简称',
    rules: [{ required: false, type: 'string', max: 50, message: '请输入角色简称且不多于50个字符' }],
  },
  {
    field: 'menus',
    component: 'Tree',
    label: '菜单权限',
    colProps: {
      span: 24,
    },
    componentProps: {
      checkable: true,
      vModelKey: 'checkedKeys',
      style: {
        height: '350px',
        paddingTop: '5px',
        overflow: 'auto',
        borderRadius: '6px',
        border: '1px solid #dcdfe6',
      },
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
    field: 'status',
    component: 'Input',
    label: '隐藏字段',
    vShow: false,
  },
];
