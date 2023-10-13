<template>
  <div>
    <SplitPanel :leftWidth="320">
      <template #left-content>
        <org-tree @selectTree="selectTree" ref="treeRef"></org-tree>
      </template>
      <template #right-content>
        <DynamicTable
          header-title="岗位管理"
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
            <a-button type="primary" :disabled="!$auth('post.add')" @click="openAddModal({})">
              <PlusOutlined /> 新增
            </a-button>
            <a-button
              type="danger"
              :disabled="!isCheckRows || !$auth('post.del')"
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
  import { schemas } from './formSchemas';
  import { baseColumns, type TableListItem, type TableColumnItem } from './columns';
  import { SplitPanel } from '@/components/basic/split-panel';
  import { useTable } from '@/components/core/dynamic-table';
  import { useFormModal } from '@/hooks/useModal/index';
  import { getListPage, delData, updateStatus, saveOrUpdate,updateAuth } from '@/api/system/post';
  import orgTree from '../organize/Tree.vue';
  import Auth from './Auth.vue';
  defineOptions({
    name: 'SystemPost',
  });

  const treeRef = ref();
  const deptTree = ref({});
  const expandedKeys = ref([]);
  const organize_id = ref('');
  const selectTree = (state) => {
    deptTree.value = state.deptTree;
    expandedKeys.value = state.expandedKeys;
    organize_id.value = state.departmentIds[0];
    dynamicTableInstance?.reload();
  };
  
  onMounted(() => {});
  const loadTableData = async (params: API.PostQueryParams) => {
    const data: any = await getListPage({
      ...params,
      organize_id: organize_id.value,
    });
    rowSelection.value.selectedRowKeys = [];
    return data;
  };

  const columns: TableColumnItem[] = [
    ...baseColumns,
    {
      title: '操作',
      width: 280,
      dataIndex: 'ACTION',
      align: 'center',
      fixed: 'right',
      actions: ({ record }) => [
        {
          label: '编辑',
          auth: {
            perm: 'post.edit',
            effect: 'disable',
          },
          onClick: () => openAddModal(record),
        },
        {
          label: '删除',
          auth: {
            perm: 'post.del',
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
            perm: 'post.auth',
            effect: 'disable',
          },
          onClick: () => openAuthModal(record),
        },
        {
          label: '禁用',
          ifShow: record.status == 0,
          auth: {
            perm: 'post.status',
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
            perm: 'post.edit',
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

  /**
   * 删除数据
   * @param ids
   * @returns {Promise<void>}
   */
  const delRowConfirm = async (ids: string | string[]) => {
    if (Array.isArray(ids)) {
      Modal.confirm({
        title: '确定要删除所选的数据吗?',
        icon: <ExclamationCircleOutlined />,
        centered: true,
        onOk: async () => {
          await delData({ ids: ids.join(',') });
          dynamicTableInstance?.reload();
        },
      });
    } else {
      await delData({ ids: ids }).finally(dynamicTableInstance?.reload);
    }
  };

  /**
   * 打开新增/编辑弹窗
   * @param record
   * @returns {Promise<void>}
   */
  const openAddModal = async (record: Partial<TableListItem> = {}) => {
    const [formRef] = await showModal<any>({
      modalProps: {
        title: `${record.id ? '编辑' : '新增'}字典`,
        width: 1000,
        onFinish: async (values) => {
          console.log('新增/编辑字典', values);
          const id = record.id ? record.id : '';
          await saveOrUpdate({ ...values, id });
          dynamicTableInstance?.reload();
        },
      },
      formProps: {
        labelWidth: 100,
        schemas: schemas,
        autoSubmitOnEnter: true,
      },
    });

    formRef?.updateSchema([
      {
        field: 'organize_id',
        componentProps: {
          treeDefaultExpandedKeys: expandedKeys.value,
          treeData: deptTree.value,
        },
      },
    ]);
    if (!record.id) {
      record.organize_id = organize_id.value;
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
