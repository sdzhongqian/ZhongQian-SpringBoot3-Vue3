import { Tag } from 'ant-design-vue';
import type { TableColumn } from '@/components/core/dynamic-table';
// import { Avatar, Space, Tag } from 'ant-design-vue';

export type TableListItem = API.MenuListResult;
export type TableColumnItem = TableColumn<TableListItem>;

/**
 * 将对应菜单类型转为字符串字意
 */
const getMenuType = (type) => {
  switch (type) {
    case 0:
      return '目录';
    case 1:
      return '菜单';
    case 2:
      return '按钮';
    case 3:
      return '外链';
    case 4:
      return 'iframe';
    default:
      return '';
  }
};

export const baseColumns: TableColumnItem[] = [
  {
    title: '菜单名称',
    dataIndex: 'title',
    width: 180,
    fixed: 'left',
    ellipsis: true,
  },
  {
    title: '请求路径',
    dataIndex: 'path',
    width: 180,
    fixed: 'left',
    ellipsis: true,
  },
  {
    title: '图标',
    width: 80,
    dataIndex: 'icon',
    align: 'center',
    customRender: ({ record }) => record.icon && <icon-font type={record.icon} size="22" />,
  },
  {
    title: '类型',
    width: 80,
    align: 'center',
    dataIndex: 'type',
    customRender: ({ record }) => getMenuType(record.type),
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
    title: '排序号',
    width: 80,
    align: 'center',
    dataIndex: 'order_by',
  },
  {
    title: '更新时间',
    width: 180,
    align: 'center',
    dataIndex: 'update_time',
  },
];
