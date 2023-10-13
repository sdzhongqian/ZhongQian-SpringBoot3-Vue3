<template>
  <div>
    <SplitPanel :leftWidth="320">
      <template #left-content>
        <org-tree @selectTree="selectTree" ref="treeRef"></org-tree>
      </template>
      <template #right-content>
        <DynamicTable
          header-title="组织管理"
          show-index
          title-tooltip="请不要随意删除组织，避免到影响其他用户的使用。"
          :data-request="loadTableData"
          :columns="columns"
          :scroll="{ x: 1500 }"
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
            <a-button type="primary" :disabled="!$auth('organize.add')" @click="openAddModal({})">
              <PlusOutlined /> 新增
            </a-button>
            <a-button
              type="danger"
              :disabled="!isCheckRows || !$auth('organize.del')"
              @click="delRowConfirm(rowSelection.selectedRowKeys)"
            >
              <DeleteOutlined /> 删除
            </a-button>
          </template>
        </DynamicTable>
      </template>
    </SplitPanel>
    <auth :data="record" :visible="visible" @ok="saveAuth" @cancel="visible = false"></auth>
  </div>
</template>

<script setup lang="tsx">
  import { ref, reactive, computed, onMounted } from 'vue';
  import { PlusOutlined, DeleteOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, Alert, message } from 'ant-design-vue';
  import { deptSchemas } from './formSchemas';
  import { baseColumns, type TableListItem, type TableColumnItem } from './columns';
  import { SplitPanel } from '@/components/basic/split-panel';
  import { useTable } from '@/components/core/dynamic-table';
  import { useFormModal } from '@/hooks/useModal/index';
  import orgTree from './Tree.vue';
  import Auth from './Auth.vue';
  import { getChildNum } from '@/utils/util';
  import {
    getListPage,
    delData,
    updateStatus,
    saveOrUpdate,
    updateAuth,
  } from '@/api/system/organize';
  import type { UseArrayIncludesComparatorFn } from '@vueuse/core';

  defineOptions({
    name: 'SystemUser',
  });

  onMounted(() => {});

  const treeRef = ref();
  const deptTree = ref([]);
  const expandedKeys = ref([]);
  const parent_id = ref('');
  const selectTree = (state) => {
    deptTree.value = state.deptTree;
    expandedKeys.value = state.expandedKeys;
    parent_id.value = state.departmentIds[0];
    dynamicTableInstance?.reload();
  };

  const loadTableData = async (params: API.OrganizeQueryParams) => {
    const data = await getListPage({
      ...params,
      parent_id: parent_id.value,
    });
    rowSelection.value.selectedRowKeys = [];
    return data;
  };

  const columns: TableColumnItem = [
    ...baseColumns,
    {
      title: '操作',
      width: 220,
      dataIndex: 'ACTION',
      align: 'center',
      fixed: 'right',
      actions: ({ record }) => [
        {
          label: '编辑',
          auth: {
            perm: 'organize.edit',
            effect: 'disable',
          },
          onClick: () => openAddModal(record),
        },
        {
          label: '删除',
          auth: {
            perm: 'organize.del',
            effect: 'disable',
          },
          popConfirm: {
            title: '你确定要删除吗？',
            onConfirm: () => delRowConfirm(record.id),
          },
        },
        {
          label: '权限',
          auth: {
            perm: 'organize.auth',
            effect: 'disable',
          },
          onClick: () => openAuthModal(record),
        },
        {
          label: '禁用',
          ifShow: record.status == 0,
          auth: {
            perm: 'organize.status',
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
            perm: 'organize.status',
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
    selectedRowKeys: [] as string[],
    onChange: (selectedRowKeys: string[], selectedRows: TableListItem[]) => {
      console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      rowSelection.value.selectedRowKeys = selectedRowKeys;
    },
  });

  // 是否勾选了表格行
  const isCheckRows = computed(() => rowSelection.value.selectedRowKeys.length);

  const delRowConfirm = async (userId: string | string[]) => {
    if (Array.isArray(userId)) {
      Modal.confirm({
        title: '确定要删除所选的数据吗?',
        icon: <ExclamationCircleOutlined />,
        centered: true,
        onOk: async () => {
          const res: any = await delData({ ids: userId.join(',') });
          if (res.success) {
            dynamicTableInstance?.reload();
            treeRef.value.fetchDeptList();
          } else {
            message.error(res.msg);
          }
        },
      });
    } else {
      const res: any = await delData({ ids: userId }).finally(dynamicTableInstance?.reload);
      if (res.success) {
        dynamicTableInstance?.reload();
        treeRef.value.fetchDeptList();
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
        title: `${record.id ? '编辑' : '新增'}组织`,
        width: 700,
        onFinish: async (values) => {
          console.log('新增/编辑组织', values);
          const id = record.id ? record.id : '';
          await saveOrUpdate({ ...values, id });
          dynamicTableInstance?.reload();
          await treeRef.value.fetchDeptList();
        },
      },
      formProps: {
        labelWidth: 100,
        schemas: deptSchemas,
        autoSubmitOnEnter: true,
      },
    });

    formRef?.updateSchema([
      {
        field: 'parent_id',
        componentProps: {
          treeDefaultExpandedKeys: expandedKeys.value,
          treeData: deptTree.value,
        },
      },
    ]);
    if (!record.id) {
      record.parent_id = parent_id.value;
      console.log(deptTree.value);
      const res = getChildNum(record.parent_id, deptTree.value);
      record.order_by = Number(res) + 1;
    }

    formRef?.setFieldsValue(record);
  };

  const record = ref({});
  const visible = ref(false);
  const openAuthModal = (item) => {
    record.value = item;
    visible.value = true;
  };

  const saveAuth = (id, targetKeys) => {
    updateAuth({
      id: id,
      role_ids: targetKeys.join(','),
    }).then((response) => {
      if (response.success) {
        message.success('权限分配成功。');
        visible.value = false;
      } else {
        message.error('权限分配失败。');
      }
    });
  };
</script>

<style></style>
