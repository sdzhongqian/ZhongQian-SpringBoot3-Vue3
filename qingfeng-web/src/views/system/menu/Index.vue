<template>
  <div>
    <DynamicTable header-title="菜单管理" :data-request="loadTableData" :columns="columns">
      <template #toolbar>
        <a-button type="primary" :disabled="!$auth('menu.add')" @click="openMenuModal({})">
          新增
        </a-button>
      </template>
    </DynamicTable>
  </div>
</template>

<script lang="tsx" setup>
  import { ref } from 'vue';
  import { cloneDeep } from 'lodash-es';
  import { baseColumns, type TableListItem, type TableColumnItem } from './columns';
  import { useMenuSchemas } from './formSchemas';
  import type { TreeSelectProps } from 'ant-design-vue';
  import { message } from 'ant-design-vue';
  import {
    getListPage,
    delData,
    updateStatus,
    saveOrUpdate,
    findMenuList,
  } from '@/api/system/menu';
  import { useTable } from '@/components/core/dynamic-table';
  import { useFormModal } from '@/hooks/useModal/useFormModal';
  import { formatMenu2Tree } from '@/core/permission/utils';

  defineOptions({
    name: 'SystemMenu',
  });

  const menuTree = ref<TreeSelectProps['treeData']>([]);
  const [DynamicTable, dynamicTableInstance] = useTable({
    search: false,
    pagination: false,
    size: 'small',
    rowKey: 'id',
    bordered: true,
    scroll: { x: 2000 },
  });
  const [showModal] = useFormModal();

  const loadTableData = async () => {
    const data = await findMenuList({});
    menuTree.value = formatMenu2Tree(
      cloneDeep(data.data).filter((n) => n.type !== 2),
      '1',
    );
    return { list: formatMenu2Tree(cloneDeep(data.data), '1') };
  };

  const openMenuModal = async (record: Partial<TableListItem>) => {
    const [formRef] = await showModal({
      modalProps: {
        title: `${record.id ? '编辑' : '新增'}菜单`,
        width: 700,
        onFinish: async (values) => {
          console.log('新增/编辑菜单', values);
          record.id && (values.menuId = record.id);
          const id = record.id ? record.id : '';
          await saveOrUpdate({ ...values, id });
          dynamicTableInstance.reload();
        },
      },
      formProps: {
        labelWidth: 100,
        schemas: useMenuSchemas(),
      },
    });

    formRef?.updateSchema([
      {
        field: 'parent_id',
        componentProps: {
          treeDefaultExpandedKeys: ['1'].concat(record?.keyPath || []),
          treeData: ref([{ id: '1', name: '根节点', children: menuTree.value }]),
        },
      },
    ]);

    if (record.keepAlive == 'true') {
      record.keepAlive = true;
    } else {
      record.keepAlive = false;
    }
    console.log(record);
    formRef?.setFieldsValue({
      ...record,
      icon: record.icon ?? '',
      parent_id: record.parent_id ?? '1',
    });
  };
  const delRowConfirm = async (record: TableListItem) => {
    const res: any = await delData({ ids: record.id });
    if (res.success) {
      dynamicTableInstance.reload();
    } else {
      message.error(res.msg);
    }
  };

  const columns: TableColumnItem[] = [
    ...baseColumns,
    {
      title: '操作',
      width: 120,
      dataIndex: 'ACTION',
      hideInSearch: true,
      align: 'center',
      fixed: 'right',
      actions: ({ record }) => [
        {
          label: '编辑',
          auth: {
            perm: 'menu.edit',
            effect: 'disable',
          },
          onClick: () => openMenuModal(record),
        },
        {
          label: '删除',
          auth: 'menu.del',
          popConfirm: {
            title: '你确定要删除吗？',
            onConfirm: () => delRowConfirm(record),
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
</script>
