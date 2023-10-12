import type { FormSchema } from '@/components/core/schema-form';

let login_password = "";
let confirm_password = async (_rule, value: string) => {
  if (value === "") {
    return Promise.reject("必填项不能为空");
  } else if (value !== login_password) {
    return Promise.reject("两次密码输入不一致!");
  } else {
    return Promise.resolve();
  }
};

export const schemas: FormSchema<API.UserEditPwdParams>[] = [
  {
    field: 'old_password',
    component: 'InputPassword',
    label: '原密码',
    rules: [{ required: true ,max: 50, message: "请输入原密码且不多于50个字符" }],
  },
  {
    field: 'login_password',
    component: 'InputPassword',
    label: '登录密码',
    rules: [{ required: true, type: 'string', max: 50, message: '请输入密码且不多于50个字符' }],
    componentProps: {
      /** 类似于 vue 中的 watch */
      request: {
       watchFields: ['login_password'],
       options: {
         immediate: true,
       },
       callback: async ({ formModel }) => {
        login_password = formModel['login_password'];
       },
     },
   },
  },
  {
    field: 'confirm_password',
    component: 'InputPassword',
    label: '登录密码',
    rules: [{ required: true, validator: confirm_password, trigger: "change" },{ min: 0, max: 50, message: "长度不得大于50个字符" }],
  },
];
