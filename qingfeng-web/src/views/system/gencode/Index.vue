<template>
  <div>
    <DynamicTable
      row-key="id"
      show-index
      :row-selection="rowSelection"
      header-title="代码生成器"
      :data-request="loadTableData"
      :columns="columns"
      :scroll="{ x: 1700 }"
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
        <a-button type="primary" :disabled="!$auth('gencode.import')" @click="importTable({})">
          导入
        </a-button>
        <a-button
          type="danger"
          :disabled="!isCheckRows || !$auth('gencode.del')"
          @click="delRowConfirm(rowSelection.selectedRowKeys)"
        >
          <DeleteOutlined /> 删除
        </a-button>
      </template>
    </DynamicTable>
    <SelectTable
      :data="record"
      :visible="visibleST"
      @ok="handleSTOk"
      @cancel="visibleST = false"
    ></SelectTable>
    <Edit
      :data="record"
      :visible="visibleEdit"
      @ok="handleEditOk"
      @cancel="visibleEdit = false"
    ></Edit>
    <ViewCode :data="record" :visible="visibleVC" @cancel="visibleVC = false"></ViewCode>
  </div>
</template>

<script setup lang="tsx">
  import { ref, reactive, computed, onMounted } from 'vue';
  import { baseColumns, type TableListItem, type TableColumnItem } from './columns';
  import type { TreeDataItem } from 'ant-design-vue/es/tree/Tree';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, Alert, message } from 'ant-design-vue';
  import { getListPage, delData, save, update, gencode } from '@/api/system/gencode';
  import { useTable } from '@/components/core/dynamic-table';
  import { useFormModal } from '@/hooks/useModal/useFormModal';
  import SelectTable from './SelectTable.vue';
  import Edit from './Edit.vue';
  import ViewCode from './ViewCode.vue';
  import axios from 'axios';
  import { Storage } from '@/utils/Storage';
  import { ACCESS_TOKEN_KEY } from '@/enums/cacheEnum';

  defineOptions({
    name: 'Systemgencode',
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

  const loadTableData = async (params: any) => {
    const data: any = await getListPage(params);
    rowSelection.value.selectedRowKeys = [];
    return data;
  };

  const columns: TableColumnItem[] = [
    ...baseColumns,
    {
      title: '操作',
      width: 300,
      dataIndex: 'ACTION',
      hideInSearch: true,
      align: 'center',
      fixed: 'right',
      actions: ({ record }) => [
        {
          label: '预览',
          auth: {
            perm: 'gencode.viewCode',
            effect: 'disable',
          },
          onClick: () => viewCode(record),
        },
        {
          label: '生成',
          auth: {
            perm: 'gencode.gencode',
            effect: 'disable',
          },
          onClick: () => handleGencode(record),
        },
        {
          label: '编辑',
          auth: {
            perm: 'gencode.edit',
            effect: 'disable',
          },
          onClick: () => openEditModal(record),
        },
        {
          label: '删除',
          auth: 'gencode.del',
          popConfirm: {
            title: '你确定要删除吗？',
            onConfirm: () => delRowConfirm(record.id),
          },
        },
      ],
    },
  ];

  const record = ref({});
  const visibleST = ref(false);

  //导入
  const importTable = (item) => {
    record.value = item;
    visibleST.value = true;
  };
  const handleSTOk = (table_names) => {
    save({ table_names: table_names.join(',') }).then((response) => {
      if (response.success) {
        dynamicTableInstance?.reload();
        visibleST.value = false;
      } else {
        message.error(response.msg);
      }
    });
  };

  //编辑
  const visibleEdit = ref(false);
  const openEditModal = (item) => {
    record.value = item;
    console.log(item);
    visibleEdit.value = true;
  };
  const handleEditOk = (record) => {
    update(record).then((response) => {
      if (response.success) {
        dynamicTableInstance?.reload();
        visibleEdit.value = false;
      } else {
        message.error(response.msg);
      }
    });
  };

  //预览
  const visibleVC = ref(false);
  const viewCode = (item) => {
    gencode(item.id).then((response) => {
      if (response.success) {
        console.log(item);
        record.value = item;
        visibleVC.value = true;
      } else {
        message.error('代码生成失败。');
      }
    });
  };

  const baseUrl = import.meta.env.VITE_BASE_API;
  //生成
  const handleGencode = (record) => {
    gencode(record.id).then((response) => {
      if (response.success) {
        if (record.gen_type == 0) {
          axios
            .get(baseUrl + 'system/gencode/downloadCode', {
              params: { id: record.id },
              responseType: 'blob', //首先设置responseType字段格式为 blob
              headers: {
                Authorization: 'Bearer ' + Storage.get(ACCESS_TOKEN_KEY),
              },
            })
            .then((res: any) => {
              let blob = new Blob([res.data]); // 为blob设置文件类型
              let url = window.URL.createObjectURL(blob); // 创建一个临时的url指向blob对象
              let a = document.createElement('a');
              a.href = url;
              a.download = record.table_comment+'模块.zip';
              a.click();
              // 释放这个临时的对象url
              window.URL.revokeObjectURL(url);
            });
        } else {
          message.success('代码生成完成。');
        }
      } else {
        message.error('代码生成失败。');
      }
    });
  };
</script>
