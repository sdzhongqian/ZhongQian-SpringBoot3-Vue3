<template>
  <SplitPanel>
    <template #left-content>
      <dic-tree @selectTree="selectTree" ref="treeRef"></dic-tree>
    </template>
    <template #right-content>
      <DynamicTable
        header-title="地区管理"
        show-index
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
          <a-button type="primary" :disabled="!$auth('area.add')" @click="openAddModal({})">
            <PlusOutlined /> 新增
          </a-button>
          <a-button
            type="danger"
            :disabled="!isCheckRows || !$auth('area.del')"
            @click="delRowConfirm(rowSelection.selectedRowKeys)"
          >
            <DeleteOutlined /> 删除
          </a-button>
        </template>
      </DynamicTable>
    </template>
  </SplitPanel>
</template>

<script setup lang="tsx">
  import { ref, reactive, computed, onMounted } from 'vue';
  import { PlusOutlined, DeleteOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, Alert, Menu, message } from 'ant-design-vue';
  import { dicSchemas } from './formSchemas';
  import { baseColumns, type TableListItem, type TableColumnItem } from './columns';
  import type { LoadDataParams } from '@/components/core/dynamic-table';
  import type { TreeDataItem } from '@/core/permission/utils';
  import { SplitPanel } from '@/components/basic/split-panel';
  import { useTable } from '@/components/core/dynamic-table';
  import { useFormModal } from '@/hooks/useModal/index';
  import { findChildById } from '@/core/permission/utils';
  import dicTree from './Tree.vue';
  import { getListPage, delData, updateStatus, saveOrUpdate } from '@/api/system/area';

  defineOptions({
    name: 'SystemArea',
  });

  onMounted(() => {});

  const treeState = reactive({
    expandedKeys: [],
    treeIds: [],
    treeData: [],
  });

  const treeRef = ref();
  const selectTree = (data) => {
    console.log(data);
    treeState.expandedKeys = data.expandedKeys;
    treeState.treeData = data.treeData;
    treeState.treeIds = data.treeIds;
    console.log(treeState);
    dynamicTableInstance?.reload();
  };

  const loadTableData = async (params: LoadDataParams) => {
    const data: any = await getListPage({
      ...params,
      parent_id: treeState.treeIds[0],
    });
    rowSelection.value.selectedRowKeys = [];
    return data;
  };

  const columns: TableColumnItem[] = [
    ...baseColumns,
    {
      title: '操作',
      width: 130,
      dataIndex: 'ACTION',
      align: 'center',
      fixed: 'right',
      actions: ({ record }) => [
        {
          label: '编辑',
          auth: {
            perm: 'area.edit',
            effect: 'disable',
          },
          onClick: () => openAddModal(record),
        },
        {
          label: '删除',
          auth: 'area.del',
          popConfirm: {
            title: '你确定要删除吗？',
            onConfirm: () => delRowConfirm(record.id),
          },
        },
        {
          label: '禁用',
          ifShow: record.status == 0,
          auth: {
            perm: 'role.edit',
            effect: 'disable',
          },
          popConfirm: {
            title: '你确定要禁用吗？',
            onConfirm: () => editStatus(record, 1),
          },
        },
        {
          label: '启用',
          ifShow: record.status == 1,
          auth: {
            perm: 'role.edit',
            effect: 'disable',
          },
          popConfirm: {
            title: '你确定要启用吗？',
            onConfirm: () => editStatus(record, 0),
          },
        },
      ],
    },
  ];

  //修改状态
  const editStatus = (record, status) => {
    record.status = status;
    updateStatus(record).then((response) => {
      if (response.success) {
        dynamicTableInstance?.reload();
      } else {
        message.error(response.msg);
      }
    });
  };

  const [DynamicTable, dynamicTableInstance] = useTable({ formProps: { autoSubmitOnEnter: true } });
  const [showModal] = useFormModal();

  const rowSelection = ref({
    selectedRowKeys: [] as number[],
    onChange: (selectedRowKeys: number[], selectedRows: TableListItem[]) => {
      console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      rowSelection.value.selectedRowKeys = selectedRowKeys;
    },
  });

  // 是否勾选了表格行
  const isCheckRows = computed(() => rowSelection.value.selectedRowKeys.length);

  const delRowConfirm = async (userId: number | number[]) => {
    if (Array.isArray(userId)) {
      Modal.confirm({
        title: '确定要删除所选的数据吗?',
        icon: <ExclamationCircleOutlined />,
        centered: true,
        onOk: async () => {
          const res: any = await delData({ ids: userId.join(',') });
          if (res.success) {
            dynamicTableInstance?.reload();
            treeRef.value.fetchList();
          } else {
            message.error(res.msg);
          }
        },
      });
    } else {
      const res: any = await delData({ ids: userId }).finally(dynamicTableInstance?.reload);
      if (res.success) {
        dynamicTableInstance?.reload();
        treeRef.value.fetchList();
      } else {
        message.error(res.msg);
      }
    }
  };

  /**
   * @description 打开用户弹窗
   */
  const openAddModal = async (record: Partial<TableListItem> = {}) => {
    const [formRef] = await showModal<any>({
      modalProps: {
        title: `${record.id ? '编辑' : '新增'}地区`,
        width: 700,
        onFinish: async (values) => {
          console.log('新增/编辑地区', values);
          const id = record.id ? record.id : '';
          await saveOrUpdate({ ...values, id });
          dynamicTableInstance?.reload();
          await treeRef.value.fetchList();
        },
      },
      formProps: {
        labelWidth: 100,
        schemas: dicSchemas,
        autoSubmitOnEnter: true,
      },
    });

    formRef?.updateSchema([
      {
        field: 'parent_id',
        componentProps: {
          treeDefaultExpandedKeys: treeState.expandedKeys,
          treeData: treeState.treeData,
        },
      },
    ]);

    formRef?.setFieldsValue(record);
  };
</script>
