import type { FormSchema } from '@/components/core/schema-form/';
import { PlusOutlined } from '@ant-design/icons-vue';
import { Radio, Button } from 'ant-design-vue';
import { Storage } from '@/utils/Storage';
import { ACCESS_TOKEN_KEY } from '@/enums/cacheEnum';
const baseUrl = import.meta.env.VITE_BASE_API;

export const graphicSchemas: FormSchema<API.GraphicEditParams>[] = [
  {
    field: 'title',
    component: 'Input',
    label: '标题',
    rules: [{ required: true, type: 'string', max: 120, message: '请输入标题且不多于120个字符' }],
  },
  {
    field: 'publish_user',
    component: 'Input',
    label: '发布人',
    colProps: {
      span: 12,
    },
    rules: [{ required: false, type: 'string', max: 50, message: '请输入发布人且不多于50个字符' }],
  },
  {
    field: 'publish_time',
    component: 'Input',
    label: '发布时间',
    colProps: {
      span: 12,
    },
    rules: [
      {
        required: false,
        type: 'string',
        max: 50,
        message: '请选择发布时间且不多于50个字符',
        trigger: 'blur',
      },
    ],
  },
  {
    field: 'fmAddress',
    component: 'Upload',
    label: '封面地址',
    colProps: {
      span: 8,
    },
    componentProps: {
      action: baseUrl + 'upload/uploadLocalFile',
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
        watchFields: ['fmAddress'],
        options: {
          immediate: true,
        },
        callback: async ({ formModel }) => {
          if (formModel['fmAddress'] != undefined) {
            formModel['tpdz'] = formModel['fmAddress'][0].response.data.file_path;
          }
        },
      },
    },
    componentSlots: ({ formModel }) => ({
      default: () =>
        formModel['fmAddress']?.length ? (
          ''
        ) : formModel['show_tpdz'] != null ? (
          <Img width="100" src={formModel['show_tpdz']} />
        ) : (
          <div>
            <PlusOutlined />
            <div class="mt-8px">上传</div>
          </div>
        ),
    }),
  },
  {
    field: 'intro',
    component: 'InputTextArea',
    label: '简介',
    componentProps: {
      autosize: { minRows: 4, maxRows: 8 },
    },
    rules: [{ required: false, type: 'string', max: 500, message: '请输入简介且不多于500个字符' }],
  },
  {
    field: 'content',
    component: 'Tinymce',
    label: '内容',
    componentProps: ({ formModel }) => {
      return {
        content: formModel['content'],
        placeholder: '请输入',
        onChange: (e) => {
          formModel['content'] = e;
        },
      };
    },
    rules: [{ required: false, type: 'string', max: 500, message: '请输入简介且不多于500个字符' }],
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
    field: 'show_tpdz',
    component: 'Input',
    label: '隐藏字段',
    vShow: false,
  },
  {
    field: 'tpdz',
    component: 'Input',
    label: '隐藏字段',
    vShow: false,
  },
  {
    field: 'status',
    component: 'Input',
    label: '隐藏字段',
    vShow: false,
  },
];
