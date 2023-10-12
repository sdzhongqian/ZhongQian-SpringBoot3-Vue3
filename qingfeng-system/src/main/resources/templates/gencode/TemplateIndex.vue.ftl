<#assign isContainStatus = 'false'>
<#assign isContainType = 'false'>
<#list fieldList as obj>
  <#if obj.field_name == 'status' && (tablePd.status_type == '0' || tablePd.status_type == '1')>
    <#assign isContainStatus = 'true'>
  </#if>
  <#if obj.field_name == 'type'>
    <#assign isContainType = 'true'>
  </#if>
</#list>
<template>
  <#if tablePd.temp_type == 1>
  <SplitPanel>
    <template #left-content>
      <${tablePd.bus_name}-tree @selectTree="selectTree" ref="treeRef"></${tablePd.bus_name}-tree>
    </template>
    <template #right-content>
  </#if>
      <DynamicTable
          header-title="${tablePd.menu_name}"
          show-index
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
          <a-button type="primary" :disabled="!$auth('${tablePd.bus_name}.add')" @click="openAddModal({})">
            <PlusOutlined /> 新增
          </a-button>
          <a-button
                  type="danger"
                  :disabled="!isCheckRows || !$auth('${tablePd.bus_name}.del')"
                  @click="delRowConfirm(rowSelection.selectedRowKeys)"
          >
            <DeleteOutlined /> 删除
          </a-button>
        </template>
      </DynamicTable>
  <#if tablePd.temp_type == 1>
    </template>
  </SplitPanel>
  </#if>
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
  <#if tablePd.temp_type == 1>
  import ${tablePd.bus_name}Tree from './Tree.vue';
  </#if>
  import { getListPage, delData, updateStatus, saveOrUpdate } from '@/api/${tablePd.mod_name}/${tablePd.bus_name}';

  defineOptions({
    name: '${tablePd.mod_name?cap_first}${tablePd.bus_name?cap_first}',
  });

  onMounted(() => {});
  <#if tablePd.temp_type == 1>
  const treeState = reactive({
    expandedKeys: [],
    treeIds: [],
    treeData: [],
  });

  const treeRef = ref();
  const selectTree = (data) => {
    treeState.expandedKeys = data.expandedKeys;
    treeState.treeData = data.treeData;
    treeState.treeIds = data.treeIds;
    dynamicTableInstance?.reload();
  };
  </#if>
  const loadTableData = async (params: API.${tablePd.bus_name?cap_first}QueryParams) => {
    const data: any = await getListPage({
      ...params,
      <#if tablePd.temp_type == 1>
      parent_id: treeState.treeIds[0],
      </#if>
    });
    rowSelection.value.selectedRowKeys = [];
    return data;
  };

  const columns: TableColumnItem[] = [
    ...baseColumns,
    {
      title: '操作',
      width: 160,
      dataIndex: 'ACTION',
      align: 'center',
      fixed: 'right',
      actions: ({ record }) => [
        {
          label: '编辑',
          auth: {
            perm: '${tablePd.bus_name}.edit',
            effect: 'disable',
          },
          onClick: () => openAddModal(record),
        },
        {
          label: '删除',
          auth: '${tablePd.bus_name}.del',
          popConfirm: {
            title: '你确定要删除吗？',
            onConfirm: () => delRowConfirm(record.id),
          },
        },
        {
          label: '禁用',
          ifShow: record.status == 0,
          auth: {
            perm: '${tablePd.bus_name}.edit',
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
            perm: '${tablePd.bus_name}.edit',
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
      console.log(`selectedRowKeys: ${'$'}{selectedRowKeys}`, 'selectedRows: ', selectedRows);
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
          <#if tablePd.temp_type == 1>
          treeRef.value.fetchList();
          </#if>
        },
      });
    } else {
      await delData({ ids: ids }).finally(dynamicTableInstance?.reload);
      <#if tablePd.temp_type == 1>
      treeRef.value.fetchList();
      </#if>
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
        title: `${'$'}{record.id ? '编辑' : '新增'}字典`,
        width: 700,
        onFinish: async (values) => {
          console.log('新增/编辑字典', values);
          const id = record.id ? record.id : '';
          await saveOrUpdate({ ...values, id });
          dynamicTableInstance?.reload();
          <#if tablePd.temp_type == 1>
          treeRef.value.fetchList();
          </#if>
        },
      },
      formProps: {
        labelWidth: 100,
        schemas: schemas,
        autoSubmitOnEnter: true,
      },
    });

    <#if tablePd.temp_type == 1>
    formRef?.updateSchema([
      {
        field: 'parent_id',
        componentProps: {
          treeDefaultExpandedKeys: treeState.expandedKeys,
          treeData: treeState.treeData,
        },
      },
    ]);
    </#if>

    formRef?.setFieldsValue(record);
  };
</script>
