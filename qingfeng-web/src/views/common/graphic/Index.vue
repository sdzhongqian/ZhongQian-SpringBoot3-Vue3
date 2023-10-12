<template>
  <div>
    <DynamicTable
      row-key="id"
      show-index
      :row-selection="rowSelection"
      :header-title="title"
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
        <a-button type="primary" :disabled="!$auth('graphic.add')" @click="openAddModal({})">
          新增
        </a-button>
        <a-button
          type="danger"
          :disabled="!isCheckRows || !$auth('graphic.del')"
          @click="delRowConfirm(rowSelection.selectedRowKeys)"
        >
          <DeleteOutlined /> 删除
        </a-button>
      </template>
    </DynamicTable>
  </div>
</template>

<script setup lang="tsx">
  import { ref, reactive, computed, onMounted, watch } from 'vue';
  import { baseColumns, type TableListItem, type TableColumnItem } from './columns';
  import { graphicSchemas } from './formSchemas';
  import type { TreeDataItem } from 'ant-design-vue/es/tree/Tree';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, Alert, message } from 'ant-design-vue';
  import { getListPage, delData, updateStatus, saveOrUpdate } from '@/api/common/graphic';
  // import { getMenuList } from '@/api/system/menu';
  import { useTable } from '@/components/core/dynamic-table';
  import type { LoadDataParams } from '@/components/core/dynamic-table';
  import { useFormModal } from '@/hooks/useModal/useFormModal';
  import { formatDept2Tree, formatMenu2Tree } from '@/core/permission/utils';
  import { useRouter } from 'vue-router';
  defineOptions({
    name: 'Systemgraphic',
  });

  const type = ref<string>('');
  const router = useRouter();
  const title = ref<string>("图文信息")

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
    const [formRef] = await showModal<API.GraphicEditParams>({
      modalProps: {
        title: `${record.id ? '编辑' : '新增'}角色`,
        width: '60%',
        onFinish: async (values) => {
          console.log('新增/编辑角色', values);
          const id = record.id ? record.id : '';
          await saveOrUpdate({ ...values, id });
          dynamicTableInstance?.reload();
        },
      },
      formProps: {
        labelWidth: 100,
        schemas: graphicSchemas,
        autoSubmitOnEnter: true,
      },
    });
    formRef?.setFieldsValue(record);
  };

  const delRowConfirm = async (userId: string | string[]) => {
    if (Array.isArray(userId)) {
      Modal.confirm({
        title: '确定要删除所选的数据吗?',
        icon: <ExclamationCircleOutlined />,
        centered: true,
        onOk: async () => {
          await delData({ ids: userId.join(',') });
          dynamicTableInstance?.reload();
        },
      });
    } else {
      await delData({ ids: userId }).finally(dynamicTableInstance?.reload);
    }
  };

  const rowSelection = ref({
    selectedRowKeys: [] as string[],
    onChange: (selectedRowKeys: string[], selectedRows: TableListItem[]) => {
      console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      rowSelection.value.selectedRowKeys = selectedRowKeys;
    },
  });

  const loadTableData = async (params: API.GraphicQueryParams) => {
    const data: any = await getListPage({ ...params, type: type.value });
    rowSelection.value.selectedRowKeys = [];
    return data;
  };

  const columns: TableColumnItem[] = [
    ...baseColumns,
    {
      title: '操作',
      width: 240,
      dataIndex: 'ACTION',
      hideInSearch: true,
      align: 'center',
      fixed: 'right',
      actions: ({ record }) => [
        {
          label: '编辑',
          auth: {
            perm: 'graphic.edit',
            effect: 'disable',
          },
          onClick: () => openAddModal(record),
        },
        {
          label: '删除',
          auth: 'graphic.del',
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

  watch(
    () => router.currentRoute.value.path,
    (toPath) => {
      //要执行的方法
      type.value = toPath.substring(toPath.lastIndexOf('/') + 1);
      dynamicTableInstance?.reload();
      if(type.value=='0'){
        title.value = "新闻信息";
      }else if(type.value=='1'){
        title.value = "通知公告";
      }
    },
    { immediate: true, deep: true },
  );

</script>
