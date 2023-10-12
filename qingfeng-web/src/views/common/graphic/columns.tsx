import type { TableColumn } from '@/components/core/dynamic-table';
import { Avatar, Space, Tag } from 'ant-design-vue';

export type TableListItem = API.GraphicListResult;
export type TableColumnItem = TableColumn<TableListItem>;

export const baseColumns: TableColumnItem[] = [
  {
    title: '标题',
    width: 200,
    align: 'left',
    dataIndex: 'title',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 120,
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
    title: '排序',
    dataIndex: 'order_by',
    align: 'center',
    hideInSearch: true,
  },
  {
    title: '创建时间',
    dataIndex: 'create_time',
    align: 'center',
    hideInSearch: true,
  },
];
