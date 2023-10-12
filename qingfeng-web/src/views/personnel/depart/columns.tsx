import { Avatar, Space, Tag } from 'ant-design-vue';
import type { TableColumn } from '@/components/core/dynamic-table';

export type TableListItem = API.UserListResult;
export type TableColumnItem = TableColumn<TableListItem>;

export const baseColumns: TableColumnItem[] = [
  {
    title: '组织名称',
    width: 150,
    dataIndex: 'organize_name',
    align: 'left',
    hideInSearch: true,
  },
  {
    title: '姓名',
    width: 150,
    dataIndex: 'name',
    align: 'left',
  },
  {
    title: '性别',
    dataIndex: 'sex',
    width: 80,
    hideInSearch: true,
    formItemProps: {
      component: 'Select',
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
    customRender: ({ record }) => {
      const isEnable = record.sex === 0;
      return <Tag color={isEnable ? 'success' : 'red'}>{isEnable ? '男' : '女'}</Tag>;
    },
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
            label: '在职',
            value: 0,
          },
          {
            label: '禁用',
            value: 1,
          },
          {
            label: '休眠',
            value: 2,
          },
          {
            label: '离职',
            value: 3,
          },
          {
            label: '黑名单',
            value: 4,
          },
        ],
      },
    },
    customRender: ({ record }) => {
      let tag = <Tag color='success'>在职</Tag>;
      if(record.status === 0){
        tag = <Tag color='success'>在职</Tag>;
      }else if(record.status === 1){
        tag = <Tag color='red'>禁用</Tag>;
      }else if(record.status === 2){
        tag = <Tag color='orange'>休眠</Tag>;
      }else if(record.status === 3){
        tag = <Tag color='red'>离职</Tag>;
      }else if(record.status === 4){
        tag = <Tag color='red'>黑名单</Tag>;
      }
      return tag;
    },
  },
  {
    title: '手机号',
    width: 150,
    align: 'left',
    dataIndex: 'phone',
  },
  {
    title: '电子邮箱',
    width: 150,
    align: 'left',
    dataIndex: 'email',
  },
  {
    title: '排序',
    dataIndex: 'order_by',
    hideInSearch: true,
    align: 'center',
    width: 80,
  },
  {
    title: '创建时间',
    dataIndex: 'create_time',
    width: 160,
    hideInSearch: true,
    formItemProps: {
      component: 'DatePicker',
      componentProps: {
        class: 'w-full',
      },
    },
  },
];
