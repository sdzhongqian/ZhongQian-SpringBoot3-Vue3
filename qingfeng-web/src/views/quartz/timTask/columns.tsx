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
    title: '任务分组',
    width: 200,
    align: 'left',
    dataIndex: 'job_group',
  },
  {
    title: '描述',
    align: 'left',
    width: 200,
    hideInSearch: true,
    dataIndex: 'description',
  },
  {
    title: '执行类',
    align: 'left',
    width: 260,
    hideInSearch: true,
    dataIndex: 'job_class_name',
  },
  {
    title: '执行表达式',
    align: 'left',
    width: 160,
    hideInSearch: true,
    dataIndex: 'cron_expression',
  },
  {
    title: '开始时间',
    dataIndex: 'start_time',
    align: 'center',
    width: 160,
    hideInSearch: true,
    customRender: ({ record }) => {
      return formatDate(record.start_time);
    },
  },
];
