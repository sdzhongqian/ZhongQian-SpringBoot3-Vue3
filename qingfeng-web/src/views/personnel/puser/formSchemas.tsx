import type { FormSchema } from '@/components/core/schema-form/';
import { PlusOutlined } from '@ant-design/icons-vue';
import { Storage } from '@/utils/Storage';
import { ACCESS_TOKEN_KEY } from '@/enums/cacheEnum';
import { findList } from '@/api/system/post';
import { findDictionaryList } from "@/api/system/dictionary";

const baseUrl = import.meta.env.VITE_BASE_API;

export const schemas: FormSchema<API.UserEditParams>[] = [
  {
    field: 'organize_id',
    component: 'TreeSelect',
    label: '上级部门',
    componentProps: {
      getPopupContainer: () => document.body,
      disabled:true
    },
    rules: [{ required: true, type: 'string' }],
  },
  {
    field: 'post_ids',
    component: 'Select',
    label: '岗位',
    colProps: {
      span: 12,
    },
    componentProps: {
      mode: 'multiple',
      /** 类似于 vue 中的 watch */
      request: {
        watchFields: ['organize_id'],
        options: {
          immediate: true,
        },
        callback: async ({ formModel }) => {
          console.log('values', formModel);
          const res: any = await findList({ organize_id: formModel['organize_id'] });
          const data = [];
          res.data.forEach((item) => {
            data.push({ value: item.id, label: item.name });
          });
          return data;
        },
      },
    },
    rules: [{ required: true, type: 'array', max: 50, message: '请输入用户名称且不多于50个字符' }],
  },
  {
    field: 'position',
    component: 'Select',
    label: '职位',
    colProps: {
      span: 12,
    },
    componentProps: {
      request: async (values) => {
        const res:any = await findDictionaryList({parent_code:'position'});
        return res.data;
      },
      onChange: (e) => {
        console.log('selected:', e);
      },
    },
    rules: [{ required: true, type: 'string', max: 50, message: '请输入用户名称且不多于50个字符' }],
  },
  {
    field: 'login_name',
    component: 'Input',
    label: '登陆名称',
    colProps: {
      span: 12,
    },
    rules: [
      {
        required: true,
        type: 'string',
        validator: async (_, value) => {
          if (value) {
            if (value.length < 6) {
              return Promise.reject('请输入用户名称且不少于6个字符');
            } else if (value.length > 50) {
              return Promise.reject('请输入用户名称且不多于50个字符');
            }
          }
          return Promise.resolve();
        },
      },
    ],
  },
  {
    field: 'name',
    component: 'Input',
    label: '姓名',
    colProps: {
      span: 12,
    },
    rules: [{ required: true, type: 'string', max: 50, message: '请输入用户名称且不多于50个字符' }],
  },
  {
    field: 'sex',
    component: 'RadioGroup',
    label: '性别',
    defaultValue: 0,
    colProps: {
      span: 12,
    },
    rules: [{ required: true, type: 'number' }],
    componentProps: {
      options: [
        {
          label: '男',
          value: 0,
        },
        {
          label: '女',
          value: 1,
        },
      ],
    },
  },
  {
    field: 'idcard',
    component: 'Input',
    label: '身份证号',
    colProps: {
      span: 12,
    },
    rules: [
      {
        required: false,
        type: 'string',
        validator: async (_, value) => {
          if (value) {
            if (
              !/^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(
                value,
              )
            ) {
              return Promise.reject('请输入合法的身份证号');
            }
          }
          return Promise.resolve();
        },
      },
    ],
  },
  {
    field: 'phone',
    component: 'Input',
    label: '手机号',
    colProps: {
      span: 12,
    },
    rules: [
      {
        required: false,
        type: 'string',
        validator: async (_, value) => {
          if (value) {
            if (!/^1[3456789]\d{9}$/.test(value)) {
              return Promise.reject('手机号格式不正确');
            }
          }
          return Promise.resolve();
        },
      },
    ],
  },
  {
    field: 'email',
    component: 'Input',
    label: '电子邮箱',
    colProps: {
      span: 12,
    },
    rules: [
      {
        required: false,
        type: 'string',
        max: 50,
        message: '请输入电子邮箱且不多于50个字符',

        validator: async (_, value) => {
          if (value) {
            if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
              return Promise.reject('邮箱格式不正确');
            }
          }
          return Promise.resolve();
        },
      },
    ],
  },
  {
    field: 'birth_date',
    component: 'DatePicker',
    label: '出生日期',
    colProps: {
      span: 12,
    },
    componentProps: {
      style: {
        width: '100%',
      },
    },
    rules: [{ required: false, type: 'string' }],
  },
  {
    field: 'motto',
    component: 'Input',
    label: '座右铭',
    colProps: {
      span: 12,
    },
    rules: [{ required: false, type: 'string', max: 50, message: '请输入座右铭且不多于50个字符' }],
  },
  {
    field: 'live_address',
    component: 'Input',
    label: '居住地址',
    colProps: {
      span: 12,
    },
    rules: [
      { required: false, type: 'string', max: 50, message: '请输入居住地址且不多于50个字符' },
    ],
  },
  {
    field: 'birth_address',
    component: 'Input',
    label: '户籍地址',
    colProps: {
      span: 12,
    },
    rules: [
      { required: false, type: 'string', max: 50, message: '请输入户籍地址且不多于50个字符' },
    ],
  },
  {
    field: 'headAddress',
    component: 'Upload',
    label: '头像',
    colProps: {
      span: 8,
    },
    componentProps: {
      action: baseUrl + 'upload/uploadFile',
      listType: 'picture-card',
      maxCount: 1,
      class: 'avatar-uploader',
      headers: {
        Authorization: 'Bearer ' + Storage.get(ACCESS_TOKEN_KEY),
      },
      data: {
        source: 'userHeader',
      },
      /** 类似于 vue 中的 watch */
      request: {
        watchFields: ['headAddress'],
        options: {
          immediate: true,
        },
        callback: async ({ formModel }) => {
          if (formModel['headAddress'] != undefined) {
            formModel['fileIds'] = formModel['headAddress'][0].response.data.id;
            console.log(formModel['fileIds']);
          }
        },
      },
    },
    componentSlots: ({ formModel }) => ({
      default: () =>
        formModel['headAddress']?.length ? (
          ''
        ) : formModel['headImg'] != null ? (
          <Img width="100" src={formModel['headImg']} />
        ) : (
          <div>
            <PlusOutlined />
            <div class="mt-8px">上传</div>
          </div>
        ),
    }),
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
    field: 'headImg',
    component: 'Input',
    label: '隐藏字段',
    vShow: false,
  },
  {
    field: 'fileIds',
    component: 'Input',
    label: '隐藏字段',
    vShow: false,
  },
  {
    field: 'status',
    component: 'InputNumber',
    label: '隐藏字段',
    vShow: false,
  },
];

let login_password = '';
let confirm_password = async (_rule, value: string) => {
  if (value === '') {
    return Promise.reject('必填项不能为空');
  } else if (value !== login_password) {
    return Promise.reject('两次密码输入不一致!');
  } else {
    return Promise.resolve();
  }
};

export const resetPwdSchemas: FormSchema<API.UserEditPwdParams>[] = [
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
          console.log(formModel['login_password']);
          login_password = formModel['login_password'];
        },
      },
    },
  },
  {
    field: 'confirm_password',
    component: 'InputPassword',
    label: '登录密码',
    rules: [
      { required: true, validator: confirm_password, trigger: 'change' },
      { min: 0, max: 50, message: '长度不得大于50个字符' },
    ],
  },
];

export const eduSchemas: FormSchema<API.EducationEditParams>[] = [
  {
    field: 'educationList',
    component: 'Subform',
    componentProps: ({ formModel }) => {
      return {
        formModel:formModel,
        content: formModel['content'],
        titles:['学校','专业','学历','学位','入学日期','毕业日期'],
        field:"educationList",
        cusSchemas:[
          {
            field: 'school',
            component: 'Input',
            label: '学校',
            isSub:true,
            rules: [{ required: true, type: 'string', max: 120, message: '请输入学校且不多于120个字符' }],
          },
          {
            field: 'special',
            component: 'Input',
            label: '专业',
            isSub:true,
            rules: [{ required: true, type: 'string', max: 120, message: '请输入专业且不多于50个字符' }],
          },
          {
            field: 'education',
            component: 'Select',
            label: '学历',
            isSub:true,
            componentProps: {
              request: async (values) => {
                const res:any = await findDictionaryList({parent_code:'education'});
                return res.data;
              },
              onChange: (e) => {
                console.log('selected:', e);
              },
            },
            rules: [{ required: true, type: 'string', max: 50, message: '请选择学历' }],
          },
          {
            field: 'degree',
            component: 'Select',
            label: '学位',
            isSub:true,
            componentProps: {
              request: async (values) => {
                const res:any = await findDictionaryList({parent_code:'degree'});
                return res.data;
              },
              onChange: (e) => {
                console.log('selected:', e);
              },
            },
            rules: [{ required: true, type: 'string', max: 50, message: '请选择学位' }],
          },
          {
            field: 'start_time',
            component: 'DatePicker',
            label: '入学日期',
            isSub:true,
            componentProps: {
              valueFormat: 'YYYY-MM-DD',
            },
            rules: [{ required: true, type: 'string', max: 50, message: '请选择入学日期' }],
          },
          {
            field: 'end_time',
            component: 'DatePicker',
            label: '毕业日期',
            isSub:true,
            componentProps: {
              valueFormat: 'YYYY-MM-DD',
            },
            rules: [{ required: true, type: 'string', max: 50, message: '请选择毕业日期' }],
          }
        ],
        placeholder: '请输入',
        onChange: (e) => {
          // console.log(formModel)
        },
      };
    }
  }
];


export const resetOrgSchemas: FormSchema<API.UserReserOrgParams>[] = [
  {
    field: 'message',
    component: 'Alert',
    label: '调动人员',
    componentProps: {
      message:'',
    },
  },
  {
    field: 'organize_id',
    component: 'TreeSelect',
    label: '调动部门',
    componentProps: {
      getPopupContainer: () => document.body,
    },
    rules: [{ required: true, type: 'string' }],
  },
  {
    field: 'reason',
    component: 'InputTextArea',
    label: '调动原因',
    componentProps: {
      autosize: { minRows: 4, maxRows: 8 },
    },
    rules: [{ required: true, type: 'string', max: 500, message: '请输入调动原因且不多于1200个字符' }],
  },
]


export const resetOperateSchemas: FormSchema<API.UserReserOrgParams>[] = [
  {
    field: 'message',
    component: 'Alert',
    label: '调动原因',
    componentProps: {
      message:'',
    },
  },
  {
    field: 'reason',
    component: 'InputTextArea',
    label: '原因',
    componentProps: {
      autosize: { minRows: 4, maxRows: 8 },
    },
    rules: [{ required: true, type: 'string', max: 500, message: '请输入原因且不多于1200个字符' }],
  },
]


//完善信息
export const puserSchemas: FormSchema<API.PUserEditParams>[] = [
  {
    field: 'job_num',
    component: 'Input',
    label: '工号',
    colProps: {
      span: 12,
    },
    rules: [{ required: true, type: 'string', max: 50, message: '请输入工号称且不多于50个字符' }],
  },
  {
    field: 'job_date',
    component: 'DatePicker',
    label: '入职日期',
    componentProps: {
      valueFormat: 'YYYY-MM-DD',
      style: {
        width: '100%',
      },
    },
    colProps: {
      span: 12,
    },
    rules: [{ required: true, type: 'string', max: 50, message: '请选择入职日期' }],
  },
  {
    field: 'education',
    component: 'Select',
    label: '学历',
    colProps: {
      span: 12,
    },
    componentProps: {
      request: async (values) => {
        const res:any = await findDictionaryList({parent_code:'education'});
        return res.data;
      },
      onChange: (e) => {
        console.log('selected:', e);
      },
    },
    rules: [{ required: false, type: 'string', max: 50, message: '请选择学历' }],
  },
  {
    field: 'marriage',
    component: 'Select',
    label: '婚姻',
    colProps: {
      span: 12,
    },
    componentProps: {
      request: async (values) => {
        const res:any = await findDictionaryList({parent_code:'marriage'});
        return res.data;
      },
      onChange: (e) => {
        console.log('selected:', e);
      },
    },
    rules: [{ required: false, type: 'string', max: 50, message: '请选择婚姻' }],
  },
  {
    field: 'nation',
    component: 'Select',
    label: '民族',
    colProps: {
      span: 12,
    },
    componentProps: {
      request: async (values) => {
        const res:any = await findDictionaryList({parent_code:'nation'});
        return res.data;
      },
      onChange: (e) => {
        console.log('selected:', e);
      },
    },
    rules: [{ required: false, type: 'string', max: 50, message: '请选择民族' }],
  },
  {
    field: 'politics_face',
    component: 'Select',
    label: '政治面貌',
    colProps: {
      span: 12,
    },
    componentProps: {
      request: async (values) => {
        const res:any = await findDictionaryList({parent_code:'politics_face'});
        return res.data;
      },
      onChange: (e) => {
        console.log('selected:', e);
      },
    },
    rules: [{ required: false, type: 'string', max: 50, message: '请选择政治面貌' }],
  },
  {
    field: 'party_time',
    component: 'DatePicker',
    label: '入党时间',
    componentProps: {
      valueFormat: 'YYYY-MM-DD',
      style: {
        width: '100%',
      },
    },
    colProps: {
      span: 12,
    },
    rules: [{ required: false, type: 'string', max: 50, message: '请选择入党时间' }],
  },
  {
    field: 'basic_salary',
    component: 'Input',
    label: '基本工资',
    colProps: {
      span: 12,
    },
    rules: [{ required: false, type: 'string', max: 50, message: '请输入基本工资称且不多于50个字符' }],
  },
  {
    field: 'post_salary',
    component: 'Input',
    label: '岗位工资',
    colProps: {
      span: 12,
    },
    rules: [{ required: false, type: 'string', max: 50, message: '请输入岗位工资称且不多于50个字符' }],
  },
  {
    field: 'merit_salary',
    component: 'Input',
    label: '绩效工资',
    colProps: {
      span: 12,
    },
    rules: [{ required: false, type: 'string', max: 50, message: '请输入绩效工资称且不多于50个字符' }],
  },
]

