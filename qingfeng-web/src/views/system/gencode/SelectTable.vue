<template>
  <DraggableModal
    :title="title"
    width="1000px"
    v-model:visible="$props.visible"
    @ok="onOk"
    @cancel="onCancel"
  >
    <DynamicTable
      row-key="table_name"
      show-index
      :row-selection="rowSelection"
      header-title="数据表"
      :data-request="loadTableData"
      :columns="columns"
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
    </DynamicTable>
  </DraggableModal>
</template>

<script setup lang="tsx">
  import { ref, watch, onMounted, defineEmits, defineExpose, computed } from 'vue';
  import { DraggableModal } from '@/components/core/draggable-modal';
  import { getTableListPage } from '@/api/system/gencode';
  import { useTable } from '@/components/core/dynamic-table';
  import { formatDate } from '@/utils/util';

  defineOptions({
    name: 'Select',
  });
  const title = ref('导入数据表');
  const props = defineProps({
    visible: {
      type: Boolean,
      default: false,
    },
    data: {
      type: Object,
      default: {},
    },
  });

  watch(
    () => props.data,
    (newValue, oldValue) => {},
  );

  const [DynamicTable, dynamicTableInstance] = useTable();

  // 是否勾选了表格行
  const isCheckRows = computed(() => rowSelection.value.selectedRowKeys.length);

  const rowSelection = ref({
    selectedRowKeys: [] as number[],
    onChange: (selectedRowKeys: number[], selectedRows: any[]) => {
      console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      rowSelection.value.selectedRowKeys = selectedRowKeys;
    },
  });

  const loadTableData = async (params: any) => {
    const data: any = await getTableListPage(params);
    rowSelection.value.selectedRowKeys = [];
    return data;
  };

  const columns = [
    { title: '表名称', dataIndex: 'table_name' },
    {
      title: '表描述',
      dataIndex: 'table_comment',
    },
    {
      title: '创建时间',
      dataIndex: 'create_time',
      hideInSearch: true,
      customRender: ({ record }) => {
        if (record.create_time == '' || record.create_time == null) {
          return '';
        } else {
          return formatDate(record.create_time);
        }
      },
    },
    {
      title: '更新时间',
      dataIndex: 'update_time',
      hideInSearch: true,
      customRender: ({ record }) => {
        if (record.create_time == '' || record.create_time == null) {
          return '';
        } else {
          return formatDate(record.create_time);
        }
      },
    },
  ];

  // 自动请求并暴露内部方法
  onMounted(() => {});

  defineExpose({});
  const emits = defineEmits(['ok', 'cancel']); //这里暴露父组件自定义的方法
  const onOk = () => {
    emits('ok', rowSelection.value.selectedRowKeys);
  };

  const onCancel = () => {
    emits('cancel');
  };
</script>

<style scoped></style>
