import { SHOW_CHILD } from 'ant-design-vue/es/vc-cascader';
import type { FormSchema } from '@/components/core/schema-form/';
import IconsSelect from '@/components/basic/icons-select/index.vue';
import { asyncRoutes } from '@/router/asyncModules';
import { formarPermsToCascader, str2tree } from '@/core/permission';
import { findMenuInfo } from '@/api/system/menu';

/** 菜单类型 0: 目录 | 1: 菜单 | 2: 按钮 | 3: 外链 | 4: iframe */
const isDir = (type: API.MenuListResult['type']) => type === 0;
const isMenu = (type: API.MenuListResult['type']) => type === 1;
const isButton = (type: API.MenuListResult['type']) => type === 2;
const isLink = (type: API.MenuListResult['type']) => type === 3;
const isIframe = (type: API.MenuListResult['type']) => type === 4;

const filterType = async (id) =>{
  let type = 0;
  if(id!='1'){
    const data:any = await findMenuInfo({ id:id });
    if(data!=null){
      type = data.data.type;
    }
  }
  if(type==0){
    return [
      {
        label: '目录',
        value: 0,
      },
      {
        label: '菜单',
        value: 1,
      },
      {
        label: '按钮',
        value: 2,
      },
      {
        label: '外链',
        value: 3,
      },
      {
        label: 'iframe',
        value: 4,
      },
    ];
  }else if(type==1){
    return [
      {
        label: '按钮',
        value: 2,
      },
    ];
  }
  return [
    {
      label: '无需添加',
      value: -1,
    },
  ];
}

export const useMenuSchemas = (): FormSchema<API.MenuEditParams>[] => [
  {
    field: 'parent_id',
    component: 'TreeSelect',
    label: '上级节点',
    componentProps: {
      fieldNames: {
        label: 'name',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
    rules: [{ required: true, type: 'string' }],
  },
  {
    field: 'type',
    component: 'RadioGroup',
    label: '菜单类型',
    defaultValue: 0,
    rules: [{ required: true, type: 'number' }],
    componentProps: {
       /** 类似于 vue 中的 watch */
       request: {
        watchFields: ['parent_id'],
        options: {
          immediate: true,
        },
        callback: async ({ formModel }) => {
          const op = filterType(formModel['parent_id']);
          if((await op).length==1){
            formModel['type']=2;
          }else if(formModel['type']==2){
            formModel['type']=0;
          }
          return op;
        },
      },
    },
  },
  {
    field: 'name',
    component: 'Input',
    label: ({ formModel }) => (isDir(formModel['type']) ? '目录名称' : (isButton(formModel['type']) ? '按钮名称' : '菜单名称')),
    rules: [{ required: true, type: 'string', max: 50, message: '请输入目录/按钮/菜单名称且不多于50个字符' }],
  },
  {
    field: 'path',
    component: 'Input',
    label: '路由地址',
    vIf: ({ formModel }) => isDir(formModel['type'])||isMenu(formModel['type'])||isLink(formModel['type'])||isIframe(formModel['type']),
    rules: [{ required: false, type: 'string', max: 120, message: '请输入路由地址且不多于120个字符' }],
  },
  {
    field: 'redirect',
    component: 'Input',
    label: '重定向地址',
    vIf: ({ formModel }) => isDir(formModel['type']),
    rules: [{ required: false, type: 'string', max: 120, message: '请输入重定向地址且不多于120个字符' }],
  },
  {
    field: 'component',
    component: 'Input',
    label: '组件路径',
    vIf: ({ formModel }) => isMenu(formModel['type']),
    rules: [{ required: false, type: 'string', max: 120, message: '请输入组件路径且不多于120个字符' }],
  },
  {
    field: 'permission',
    component: 'Input',
    label: '权限标识',
    vIf: ({ formModel }) => isMenu(formModel['type'])||isButton(formModel['type']),
    rules: [{ required: false, type: 'string', max: 50, message: '请输入权限标识且不多于50个字符' }],
  },
  {
    field: 'icon',
    component: () => IconsSelect,
    label: '节点图标',
    vIf: ({ formModel }) => isDir(formModel['type'])||isMenu(formModel['type'])||isLink(formModel['type'])||isIframe(formModel['type']),
  },
  {
    field: 'keepAlive',
    component: 'Switch',
    label: '是否缓存',
    defaultValue: true,
    vIf: ({ formModel }) => isMenu(formModel['type']),
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
