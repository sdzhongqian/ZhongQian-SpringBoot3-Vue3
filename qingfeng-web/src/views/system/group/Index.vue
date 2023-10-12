<template>
  <div>
    <DynamicTable row-key="id" show-index :row-selection="rowSelection" :scroll="{ x: 1700 }" header-title="角色管理" :data-request="loadTableData"
      :columns="columns" bordered size="small">
      <template v-if="isCheckRows" #title>
        <Alert class="w-full" type="info" show-icon>
          <template #message>
            已选 {{ isCheckRows }} 项
            <a-button type="link" @click="rowSelection.selectedRowKeys = []">取消选择</a-button>
          </template>
        </Alert>
      </template>
      <template #toolbar>
        <a-button type="primary" :disabled="!$auth('group.add')" @click="openMenuModal({})">
          新增
        </a-button>
        <a-button type="danger" :disabled="!isCheckRows || !$auth('group.del')"
          @click="delRowConfirm(rowSelection.selectedRowKeys)">
          <DeleteOutlined /> 删除
        </a-button>
      </template>
    </DynamicTable>
  </div>
</template>

<script setup lang="tsx">
import { ref, reactive, computed, onMounted } from 'vue';
import { baseColumns, type TableListItem, type TableColumnItem } from './columns';
import { groupSchemas } from './formSchemas';
import type { TreeDataItem } from 'ant-design-vue/es/tree/Tree';
import {
  ExclamationCircleOutlined,
} from '@ant-design/icons-vue';
import { Modal, Alert,message } from 'ant-design-vue';
import {
  getListPage,
  delData,
  updateStatus,
  saveOrUpdate,
} from "@/api/system/group";
// import { getMenuList } from '@/api/system/menu';
import { useTable } from '@/components/core/dynamic-table';
import type { LoadDataParams } from '@/components/core/dynamic-table';
import { useFormModal } from '@/hooks/useModal/useFormModal';
import { formatDept2Tree, formatMenu2Tree } from '@/core/permission/utils';

defineOptions({
  name: 'Systemgroup',
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
const openMenuModal = async (record: Partial<TableListItem>) => {
  const [formRef] = await showModal<API.GroupEditParams>({
    modalProps: {
      title: `${record.id ? '编辑' : '新增'}角色`,
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
      schemas: groupSchemas,
      autoSubmitOnEnter: true
    },
  });
  formRef?.setFieldsValue(record);
};

const delRowConfirm = async (userId: number | number[]) => {
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
  selectedRowKeys: [] as number[],
  onChange: (selectedRowKeys: number[], selectedRows: TableListItem[]) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
    rowSelection.value.selectedRowKeys = selectedRowKeys;
  },
});

const loadTableData = async (params: API.GroupQueryParams) => {
  const data: any = await getListPage(params);
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
          perm: 'group.edit',
          effect: 'disable',
        },
        onClick: () => openMenuModal(record),
      },
      {
        label: '删除',
        auth: 'group.del',
        popConfirm: {
          title: '你确定要删除吗？',
          onConfirm: () => delRowConfirm(record.id),
        },
      },
      {
        label: '禁用',
        ifShow:record.status==0,
        auth: {
          perm: 'role.edit',
          effect: 'disable',
        },
        popConfirm: {
          title: '你确定要禁用吗？',
          onConfirm: () => editStatus(record,1),
        },
      },
      {
        label: '启用',
        ifShow:record.status==1,
        auth: {
          perm: 'role.edit',
          effect: 'disable',
        },
        popConfirm: {
          title: '你确定要启用吗？',
          onConfirm: () => editStatus(record,0),
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

</script>
