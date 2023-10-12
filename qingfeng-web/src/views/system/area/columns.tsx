import { Tag } from 'ant-design-vue';
import type { TableColumn } from '@/components/core/dynamic-table';

export type TableListItem = API.AreaListResult;
export type TableColumnItem = TableColumn<TableListItem>;


export const baseColumns: TableColumnItem[] = [
  {
    title: '地区名称',
    width: 150,
    dataIndex: 'name',
    align: 'left',
  },
  {
    title: '地区简称',
    width: 150,
    align: 'left',
    dataIndex: 'short_name',
  },
  {
    title: '排序',
    dataIndex: 'order_by',
    hideInSearch: true,
    align: 'center',
    width: 80,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
    hideInSearch: true,
    formItemProps: {
      component: 'Select',
      componentProps: {
        options: [
          {
            label: '启用',
            value: 0,
          },
          {
            label: '禁用',
            value: 1,
          },
        ],
      },
    },
    customRender: ({ record }) => {
      const isEnable = record.status === 0;
      return <Tag color={isEnable ? 'success' : 'red'}>{isEnable ? '启用' : '禁用'}</Tag>;
    },
  },
  {
    title: '创建时间',
    dataIndex: 'create_time',
    width: 120,
    hideInSearch: true,
    formItemProps: {
      component: 'DatePicker',
      componentProps: {
        class: 'w-full',
      },
    },
  },
];
