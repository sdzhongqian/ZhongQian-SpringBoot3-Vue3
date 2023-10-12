import { debounce } from 'lodash-es';
import { PlusOutlined } from '@ant-design/icons-vue';
import { Tag, Image } from 'ant-design-vue';
import type { TableColumn } from '@/components/core/dynamic-table';

import {
  fetchStatusMapData,
  getClothesByGender,
} from '@/views/demos/tables/search-table/columns';


export const tableData = Array.from({ length: 30 }).map((_, i) => {
  const gender = ~~(Math.random() * 2);
  return {
    id: "",
    type: 0,
    organize_name: "",
    position: "",
    order_by: 0,
  };
});

// 数据项类型
export type ListItemType = (typeof tableData)[number];
// 使用TableColumn<ListItemType> 将会限制dataIndex的类型，但换来的是dataIndex有类型提示
export const columns: TableColumn<ListItemType>[] = [
  {
    title: '组织类型',
    dataIndex: 'type',
    width: 180,
    editable: false,
    hideInSearch: true,
    customRender: ({ record }) => {
      const isEnable = record.type === 0;
      return <Tag color={isEnable ? 'success' : 'red'}>{isEnable ? '主组织' : '兼职组织'}</Tag>;
    },
    formItemProps: {
      component: 'Select',
      componentProps: ({ formInstance, formModel, tableInstance }) => ({
        options: [
          {
            label: '兼职组织',
            value: 1,
          },
          {
            label: '主组织',
            value: 0,
          },
        ]
      }),
    },
  },
  {
    title: '组织名称',
    align: 'left',
    dataIndex: 'organize_name',
    sorter: true,
    // defaultEditable: true,
    editable: false,
  },
  {
    title: '担任职务',
    align: 'left',
    dataIndex: 'position',
    hideInSearch: true,
    /** 可编辑行表单配置 */
    editFormItemProps: {
      component: 'Input',
      rules: [{ required: true, message: '请输入担任职务！' }],
    },
    customRender: ({ record }) => {
      const isEnable = record.position === null;
      return <span color={isEnable ? '#666' : 'success'}>{isEnable ? '未设置' : record.position}</span>;
    },
  },
  {
    title: '排序',
    align: 'left',
    dataIndex: 'order_by',
    hideInSearch: true,
    editFormItemProps: {
      component: 'InputNumber',
      rules: [{ required: true, message: '请输入排序！' }],
    },
  }
];
