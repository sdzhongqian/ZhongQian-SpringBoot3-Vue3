<template>
  <div>
    <Alert message="关联组织管理" type="info" show-icon>
      <template #description> 用户除了创建时帮的主组织以外,还可以绑定其他兼职组织信息,如果一个用户有多个兼职组织,可以在个人设置中进行组织切换. </template>
    </Alert>
    <DynamicTable size="small" bordered :data-request="loadData" :columns="tableColumns" :editable-type="editableType"
      :on-save="handleSave" :on-cancel="handleCancelSave" row-key="id">
      <template #toolbar>
        <a-button type="primary" @click="openAddModal({})">
          <PlusOutlined /> 新增
        </a-button>
      </template>
    </DynamicTable>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
import { Alert, Card, Select, message } from 'ant-design-vue';
import type { LoadDataParams } from '@/components/core/dynamic-table';
import { columns, tableData } from './columns';
import type {
  OnChangeCallbackParams,
  EditableType,
  OnSave,
  OnCancel,
} from '@/components/core/dynamic-table';
import { useTable } from '@/components/core/dynamic-table';
import { waitTime } from '@/utils/common';
import { useFormModal } from '@/hooks/useModal/index';
import { userOrganizeSchemas } from './formSchemas';
import {
  getMyOrganizeListPage,
  saveOrUpdateUserOrganize,
  delUserOrganize,
} from "@/api/system/user";
import { object } from 'node_modules/vue-types/dist';

defineOptions({
  name: 'EditRowTable',
});

const props = defineProps({
  data: {
    type: Object,
    default: {},
  }
})


const [DynamicTable, dynamicTableInstance] = useTable({ formProps: { autoSubmitOnEnter: true } });
const [showModal] = useFormModal();

const editableType = ref<EditableType>('cell');

const loadData = async (params: LoadDataParams) => {
  const data: any = await getMyOrganizeListPage({
    ...params,
    user_id: props.data.id
  });
  console.log(data)
  return data;
};

const tableColumns = computed<typeof columns>(() => [
  ...columns,
  {
    title: '操作',
    align: 'center',
    width: 200,
    dataIndex: 'ACTION',
    actions: ({ record }) => [
      {
        label: '编辑',
        onClick: () => openAddModal(record),
      },
      {
        label: '删除',
        disabled: record.type == 0,
        popConfirm: {
          title: '你确定要删除吗？',
          onConfirm: () => delRowConfirm(record),
        },
      },
    ],
  },
  {
    title: '操作',
    align: 'center',
    hideInTable: editableType.value === 'cell',
    width: 200,
    dataIndex: 'ACTION',
    actions: ({ record }, action) => {
      const { startEditable, cancelEditable, isEditable, getEditFormModel, validateRow } = action;
      return isEditable(record.id)
        ? [
          {
            label: '保存',
            onClick: async () => {
              const result = await validateRow(record.id);
              message.loading({ content: '保存中...', key: record.id });
              console.log('result', result);
              console.log('保存', getEditFormModel(record.id));
              await waitTime(2000);
              cancelEditable(record.id);
              message.success({ content: '保存成功!', key: record.id, duration: 2 });
            },
          },
          {
            label: '取消',
            onClick: () => {
              cancelEditable(record.id);
            },
          },
        ]
        : [
          {
            label: '编辑',
            onClick: () => {
              startEditable(record.id, record);
            },
          },
        ];
    },
  },
]);

const handleCancelSave: OnCancel = (rowKey, record, originRow) => {
  console.log('handleCancelSave', rowKey, record, originRow);
};

const handleSave: OnSave = async (rowKey, record, originRow) => {
  console.log('handleSave', rowKey, record, originRow);
  const res = await saveOrUpdateUserOrganize({ ...record });
  if (res.success) {
    message.success(res.msg);
  } else {
    message.error(res.msg);
  }
  dynamicTableInstance?.reload();
  // await waitTime(2000);
};


const openAddModal = async (record: any) => {
  const [formRef] = await showModal<any>({
    modalProps: {
      title: `新增兼职组织`,
      width: 800,
      onFinish: async (values) => {
        const id = record.id ? record.id : '';
        const res = await saveOrUpdateUserOrganize({ ...values, id, user_id: props.data.id });
        if (res.success) {
          message.success(res.msg);
        } else {
          message.error(res.msg);
        }
        dynamicTableInstance?.reload();
      },
    },
    formProps: {
      labelWidth: 100,
      schemas: userOrganizeSchemas,
      autoSubmitOnEnter: true,
    },
  });

  formRef?.setFieldsValue(record);
};

const delRowConfirm = async (record: any) => {
  await delUserOrganize(record.id);
  dynamicTableInstance.reload();
};

</script>

<style lang="less" scoped></style>
