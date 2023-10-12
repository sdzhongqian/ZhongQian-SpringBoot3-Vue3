import type { TableColumn } from '@/components/core/dynamic-table';
import { Avatar, Space, Tag } from 'ant-design-vue';
import { formatDate } from "@/utils/util";

export type TableListItem = API.GroupListResult;
export type TableColumnItem = TableColumn<TableListItem>;

export const baseColumns: TableColumnItem[] = [
  {
    title: '任务名称',
    width: 200,
    align: 'left',
    dataIndex: 'job_name',
  },
  {
    title: '任务描述',
    align: 'left',
    width: 200,
    hideInSearch: true,
    dataIndex: 'description',
  },
  {
    title: '执行表达式',
    align: 'left',
    width: 160,
    hideInSearch: true,
    dataIndex: 'cron_expression',
  },
  {
    title: '状态',
    dataIndex: 'trigger_state',
    width: 80,
    hideInSearch: true,
    formItemProps: {
      component: 'Select',
      componentProps: {
        options: [
          {
            label: '执行中',
            value: 'Y',
          },
          {
            label: '已停止',
            value: 'N',
          },
        ],
      },
    },
    customRender: ({ record }) => {
      return <Tag color={record.trigger_state === 'Y' ? 'success' : 'red'}>{record.trigger_state === 'Y' ? '执行中' : '已停止' }</Tag>;
    },
  },
  {
    title: '排序',
    dataIndex: 'order_by',
    hideInSearch: true,
    align: 'center',
    width: 80,
  },
  {
    title: '执行开始时间',
    dataIndex: 'trigger_time',
    align: 'center',
    width: 160,
    hideInSearch: true,
    customRender: ({ record }) => {
      return formatDate(record.trigger_time);
    },
  },
];
