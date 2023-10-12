<template>
  <div>
    <DynamicTable
      row-key="id"
      show-index
      :row-selection="rowSelection"
      header-title="Quartz任务管理"
      :data-request="loadTableData"
      :columns="columns"
      :scroll="{ x: 1500 }"
      bordered
      size="small"
    >
      <template v-if="isCheckRows" #title>
        <Alert class="w-full" type="info" show-icon>
          <template #message>
            已选 {{ isCheckRows }} 项
            <a-button type="link" @click="rowSelection.selectedRowKeys = []">取消选择</a-button>
          </template>
        </Alert>
      </template>
      <template #toolbar>
        <a-button type="primary" :disabled="!$auth('timTask.add')" @click="openAddModal({})">
          新增
        </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'operation'">
          <AButton @click="openAddModal(record)" ghost size="small" color="rgb(19, 194, 194)"
            >编辑</AButton
          >
          <a-popconfirm
            title="你确定要删除吗？"
            @confirm="delRowConfirm(record)"
            ok-text="确定"
            cancel-text="取消"
          >
            <AButton ghost size="small" color="rgb(245, 34, 45)">删除</AButton>
          </a-popconfirm>
          <AButton @click="handleExecution(record)" ghost size="small" color="rgb(47, 84, 235)"
            >执行</AButton
          >
          <AButton
            v-if="record.trigger_state == 'PAUSED'"
            @click="handleStopOrRestore(record, 'restore')"
            ghost
            size="small"
            color="rgb(19, 194, 194)"
            >恢复</AButton
          >
          <AButton
            v-if="record.trigger_state != 'PAUSED'"
            @click="handleStopOrRestore(record, 'stop')"
            ghost
            size="small"
            color="rgb(47, 84, 235)"
            >停止</AButton
          >
        </template>
      </template>
    </DynamicTable>
  </div>
</template>

<script setup lang="tsx">
  import { ref, reactive, computed, onMounted } from 'vue';
  import { baseColumns, type TableListItem, type TableColumnItem } from './columns';
  import { timTaskSchemas } from './formSchemas';
  import type { TreeDataItem } from 'ant-design-vue/es/tree/Tree';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, Alert, message } from 'ant-design-vue';
  import { getListPage, saveOrUpdate, del, stopOrRestore, execution } from '@/api/quartz/timTask';
  // import { getMenuList } from '@/api/system/menu';
  import { useTable } from '@/components/core/dynamic-table';
  import type { LoadDataParams } from '@/components/core/dynamic-table';
  import { useFormModal } from '@/hooks/useModal/useFormModal';
  import { formatDept2Tree, formatMenu2Tree } from '@/core/permission/utils';

  defineOptions({
    name: 'SystemtimTask',
  });

  const [DynamicTable, dynamicTableInstance] = useTable();

  const [showModal] = useFormModal();

  // 是否勾选了表格行
  const isCheckRows = computed(() => rowSelection.value.selectedRowKeys.length);

  const getCheckedKeys = (checkedList: number[], options: TreeDataItem[], total = []) => {
    return options.reduce<number[]>((prev, curr) => {
      if (curr.children?.length) {
        getCheckedKeys(checkedList, curr.children, total);
      } else {
        if (checkedList.includes(curr.value)) {
          prev.push(curr.value);
        }
      }
      return prev;
    }, total);
  };

  /**
   * @description 打开新增/编辑弹窗
   */
  const openAddModal = async (record: Partial<TableListItem>) => {
    const [formRef] = await showModal<API.TimTaskEditParams>({
      modalProps: {
        title: `${record.job_name ? '编辑' : '新增'}角色`,
        width: '50%',
        onFinish: async (values) => {
          console.log('新增/编辑角色', values);
          const id = record.id ? record.id : '';
          await saveOrUpdate({ ...values, id });
          dynamicTableInstance?.reload();
        },
      },
      formProps: {
        labelWidth: 100,
        schemas: timTaskSchemas,
        autoSubmitOnEnter: true,
      },
    });
    formRef?.setFieldsValue(record);
  };

  const delRowConfirm = async (record: any) => {
    const res = await del(record.job_name, record.job_group).finally(dynamicTableInstance?.reload);
    if (res.data.success) {
      message.success(res.data.msg);
    } else {
      message.error(res.data.msg);
    }
  };

  const rowSelection = ref({
    selectedRowKeys: [] as number[],
    onChange: (selectedRowKeys: number[], selectedRows: TableListItem[]) => {
      console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      rowSelection.value.selectedRowKeys = selectedRowKeys;
    },
  });

  const loadTableData = async (params: API.TimTaskQueryParams) => {
    const data: any = await getListPage(params);
    rowSelection.value.selectedRowKeys = [];
    return data;
  };

  const columns: TableColumnItem[] = [
    ...baseColumns,
    {
      title: '操作',
      width: 260,
      dataIndex: 'operation',
      hideInSearch: true,
      align: 'center',
      fixed: 'right',
    },
  ];

  //执行
  const handleExecution = (record) => {
    const res = execution(record.job_name, record.job_group).finally(dynamicTableInstance?.reload);
    if (res.data.success) {
      message.success(res.data.msg);
    } else {
      message.error(res.data.msg);
    }
  };

  //停止或者恢复
  const handleStopOrRestore = (record, status) => {
    const res = stopOrRestore(record.job_name, record.job_group, status).finally(
      dynamicTableInstance?.reload,
    );
    console.log(res)
    if (res.data.success) {
      message.success(res.data.msg);
    } else {
      message.error(res.data.msg);
    }
  };
</script>
