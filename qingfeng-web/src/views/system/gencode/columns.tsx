import type { TableColumn } from '@/components/core/dynamic-table';
import { Avatar, Space, Tag } from 'ant-design-vue';

export type TableListItem = API.GroupListResult;
export type TableColumnItem = TableColumn<TableListItem>;

export const baseColumns: TableColumnItem[] = [
  {
    title: '表名称',
    width: 200,
    align: 'left',
    dataIndex: 'table_name',
  },
  {
    title: '表描述',
    width: 200,
    align: 'left',
    dataIndex: 'table_comment',
  },
  {
    title: '模板类型',
    dataIndex: 'temp_type',
    width: 120,
    hideInSearch: true,
    formItemProps: {
      component: 'Select',
      componentProps: {
        options: [
          {
            label: '单表',
            value: 0,
          },
          {
            label: '树表',
            value: 1,
          },
        ],
      },
    },
    customRender: ({ record }) => {
      const isEnable = record.temp_type === 0;
      return <Tag color={isEnable ? 'success' : 'blue'}>{isEnable ? '单表' : '树表'}</Tag>;
    },
  },
  {
    title: '生成包路径',
    dataIndex: 'pack_path',
    align: 'center',
    hideInSearch: true,
  },
  {
    title: '模块名称',
    dataIndex: 'mod_name',
    align: 'center',
    hideInSearch: true,
  },
  {
    title: '业务名称',
    dataIndex: 'bus_name',
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
