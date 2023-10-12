<template>
  <div>
    <DynamicTable
      header-title="离职人员"
      show-index
      title-tooltip="请不要随意删除用户，避免到影响用户的使用。"
      :data-request="loadTableData"
      :columns="columns"
      :scroll="{ x: 1700 }"
      :row-selection="rowSelection"
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
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'operation'">
          <AButton
            class="ant-btn"
            @click="openInfoModal(record)"
            ghost
            size="small"
            color="rgb(19, 194, 194)"
            >详情</AButton
          >
          <a-popconfirm
            title="你确定要删除吗？"
            @confirm="delRowConfirm(record.id)"
            ok-text="确定"
            cancel-text="取消"
          >
            <AButton class="ant-btn" ghost size="small" color="rgb(245, 34, 45)">删除</AButton>
          </a-popconfirm>
        </template>
      </template>
    </DynamicTable>
    <info
      :data="record"
      :visible="visibleInfo"
      @cancel="visibleInfo = false"
    ></info>
  </div>
</template>

<script setup lang="tsx">
  import { ref, reactive, computed, onMounted } from 'vue';
  import { PlusOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, Alert, message } from 'ant-design-vue';
  import { baseColumns, type TableListItem, type TableColumnItem } from './columns';
  import { useTable } from '@/components/core/dynamic-table';
  import { useFormModal } from '@/hooks/useModal/index';
  import { getListPage, delData } from '@/api/system/user';
  import Info from '../puser/Info.vue';

  defineOptions({
    name: 'SystemUser',
  });

  onMounted(() => {});


  const data_count = ref(0);
  const loadTableData = async (params: API.UserQueryParams) => {
    const data: any = await getListPage({
      ...params,
      status: 3,
    });
    data_count.value = data.pagination.total;
    rowSelection.value.selectedRowKeys = [];
    return data;
  };

  const columns: TableColumnItem[] = [
    ...baseColumns,
    {
      title: '操作',
      width: 120,
      dataIndex: 'operation',
      align: 'center',
      fixed: 'right',
      hideInSearch: true,
    },
  ];

  const [DynamicTable, dynamicTableInstance] = useTable({ formProps: { autoSubmitOnEnter: true } });
  const [showModal] = useFormModal();

  const rowSelection = ref({
    selectedRowKeys: [] as number[] | string[],
    selectedRowNames: [] as number[] | string[],
    onChange: (selectedRowKeys: number[] | string[], selectedRows: TableListItem[]) => {
      console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      rowSelection.value.selectedRowKeys = selectedRowKeys;
      rowSelection.value.selectedRowNames = Array.from(selectedRows, ({ name }) => name);
    },
  });

  // 是否勾选了表格行
  const isCheckRows = computed(() => rowSelection.value.selectedRowKeys.length);

  const delRowConfirm = async (id: string | string[]) => {
    if (Array.isArray(id)) {
      Modal.confirm({
        title: '确定要删除所选的数据吗?',
        icon: <ExclamationCircleOutlined />,
        centered: true,
        onOk: async () => {
          const res = await delData({ ids: id.join(',') });
          if (res.data.success) {
            message.success(res.data.msg);
          } else {
            message.error(res.data.msg);
          }
          dynamicTableInstance?.reload();
        },
      });
    } else {
      const res = await delData({ ids: id }).finally(dynamicTableInstance?.reload);
      if (res.data.success) {
        message.success(res.data.msg);
      } else {
        message.error(res.data.msg);
      }
      dynamicTableInstance?.reload();
    }
  };

  const record = ref({});
  const visibleInfo = ref(false);
  const openInfoModal = (item) => {
    record.value = item;
    visibleInfo.value = true;
  };


</script>

<style scoped>
  .ant-btn {
    margin-right: 10px !important;
  }
</style>
