import { Tag } from 'ant-design-vue';
import type { TableColumn } from '@/components/core/dynamic-table';

export type TableListItem = API.PostListResult;
export type TableColumnItem = TableColumn<TableListItem>;


export const baseColumns: TableColumnItem[] = [
  {
    title: "名称",
    dataIndex: "name",
    ellipsis: true,
  },
  {
    title: "岗位负责人",
    dataIndex: "leader_name",
    hideInSearch: true,
    ellipsis: true,
  },
  {
    title: "岗位性质",
    dataIndex: "nature",
    hideInSearch: true,
    ellipsis: true,
    customRender: ({ record }) => {
      let tag;
      if(record.nature=='0'){
        tag = <Tag color='green'>全职</Tag>
      }else if(record.nature=='1'){
        tag = <Tag color='blue'>兼职</Tag>
      }else if(record.nature=='2'){
        tag = <Tag color='blue'>实习</Tag>
      }else if(record.nature=='3'){
        tag = <Tag color='gray'>其他</Tag>
      }
      return tag;
    },
  },
  {
    title: "排序",
    dataIndex: "order_by",
    hideInSearch: true,
    width: 120,
    ellipsis: true,
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
    title: '创建时间',
    dataIndex: 'create_time',
    width: 180,
    hideInSearch: true,
    formItemProps: {
      component: 'DatePicker',
      componentProps: {
        class: 'w-full',
      },
    },
  },
];
