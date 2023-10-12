<template>
  <div>
    <SplitPanel :leftWidth="320">
      <template #left-content>
        <org-tree @selectTree="selectTree" ref="treeRef"></org-tree>
      </template>
      <template #right-content>
        <DynamicTable
          header-title="用户管理"
          show-index
          title-tooltip="请不要随意删除用户，避免到影响用户的使用。"
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
            <a-button type="primary" :disabled="!$auth('user.add')" @click="openAddModal({})">
              <PlusOutlined /> 新增
            </a-button>
            <AButton
              color="rgb(19, 194, 194)"
              :disabled="!isCheckRows || !$auth('user.resetPwd')"
              @click="resetPwd(rowSelection.selectedRowKeys)"
            >
              <EditOutlined /> 密码重置
            </AButton>
            <a-button
              type="danger"
              :disabled="!isCheckRows || !$auth('user.del')"
              @click="delRowConfirm(rowSelection.selectedRowKeys)"
            >
              <DeleteOutlined /> 删除
            </a-button>
            <AButton
              color="rgb(19, 194, 194)"
              ghost
              :disabled="!$auth('user.downImportTemp')"
              @click="downImportTemp()"
            >
              <ArrowDownOutlined /> 导入模板下载
            </AButton>
            <AButton
              color="rgb(19, 194, 194)"
              ghost
              :disabled="!$auth('user.importData')"
              @click="importData()"
            >
              <ImportOutlined /> 导入
            </AButton>
            <AButton
              color="rgb(19, 194, 194)"
              ghost
              :disabled="!$auth('user.exportData')"
              @click="exportData()"
            >
              <ExportOutlined /> 导出
            </AButton>
          </template>
          <template #bodyCell="{ column, record }">
            <template v-if="column.dataIndex === 'operation'">
              <AButton
                class="ant-btn"
                @click="openAddModal(record)"
                ghost
                size="small"
                color="rgb(19, 194, 194)"
                >编辑</AButton
              >
              <a-popconfirm
                title="你确定要删除吗？"
                @confirm="delRowConfirm(record.id)"
                ok-text="确定"
                cancel-text="取消"
              >
                <AButton class="ant-btn" ghost size="small" color="rgb(245, 34, 45)">删除</AButton>
              </a-popconfirm>
              <a-dropdown :style="{ marginLeft: '10px' }">
                <a class="ant-dropdown-link" @click.prevent>
                  <span style="color: rgb(24, 144, 255)">更多</span>
                  <DownOutlined />
                </a>
                <template #overlay>
                  <a-menu>
                    <a-menu-item v-if="$auth('user.resetPwd')">
                      <a @click="() => resetPwd(record.id)">密码重置</a>
                    </a-menu-item>
                    <a-menu-item v-if="$auth('user.resetOrganize')">
                      <a @click="() => mangeOrganize(record)">设置组织</a>
                    </a-menu-item>
                    <a-menu-item v-if="$auth('user.assignAuth')">
                      <a @click="() => openAuthModal(record)">权限分配</a>
                    </a-menu-item>
                    <a-menu-item v-if="$auth('user.status') && record.status == 0">
                      <a-popconfirm
                        title="你确定要禁用用户吗？"
                        ok-text="确认"
                        cancel-text="取消"
                        @confirm="editStatus(record, 1)"
                      >
                        <a>禁用用户</a>
                      </a-popconfirm>
                    </a-menu-item>
                    <a-menu-item v-if="$auth('user.status') && record.status != 0">
                      <a-popconfirm
                        title="你确定要启用用户吗？"
                        ok-text="确认"
                        cancel-text="取消"
                        @confirm="editStatus(record, 0)"
                      >
                        <a>启用用户</a>
                      </a-popconfirm>
                    </a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </template>
          </template>
        </DynamicTable>
      </template>
    </SplitPanel>
    <DraggableModal title="关联组织" width="1000px" v-model:visible="orgVisible">
      <template #footer>
        <a-button @click="onCancel" type="primary">关闭</a-button>
      </template>
      <mgorganize :data="userData"></mgorganize>
    </DraggableModal>
    <auth :data="record" :visible="visible" @ok="saveAuth" @cancel="visible = false"></auth>
    <import :data="record" :visible="visibleImport" @ok="dynamicTableInstance?.reload()" @cancel="visibleImport = false"></import>
  </div>
</template>

<script setup lang="tsx">
  import { ref, reactive, computed, onMounted } from 'vue';
  import {
    PlusOutlined,
    EditOutlined,
    DeleteOutlined,
    ExclamationCircleOutlined,
  } from '@ant-design/icons-vue';
  import { Modal, Alert, message } from 'ant-design-vue';
  import { deptSchemas, resetPwdSchemas } from './formSchemas';
  import { baseColumns, type TableListItem, type TableColumnItem } from './columns';
  import { SplitPanel } from '@/components/basic/split-panel';
  import { useTable } from '@/components/core/dynamic-table';
  import { useFormModal } from '@/hooks/useModal/index';
  import orgTree from '../organize/Tree.vue';
  import { DraggableModal } from '@/components/core/draggable-modal';
  import mgorganize from './organize/Index.vue';
  import { getGuid } from '@/utils/util';
  import axios from 'axios';
  import { Storage } from '@/utils/Storage';
  import { ACCESS_TOKEN_KEY } from '@/enums/cacheEnum';
  import Auth from './Auth.vue';
  import Import from './Import.vue';
  import md5 from 'blueimp-md5';
  import {
    getListPage,
    delData,
    updateStatus,
    saveOrUpdate,
    updatePwd,
    updateAuth,
  } from '@/api/system/user';

  defineOptions({
    name: 'SystemUser',
  });

  onMounted(() => {});

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

  const data_count = ref(0);
  const queryParams = ref({});
  const loadTableData = async (params: API.UserQueryParams) => {
    queryParams.value = params;
    const data: any = await getListPage({
      ...params,
      organize_id: organize_id.value,
    });
    data_count.value = data.pagination.total;
    rowSelection.value.selectedRowKeys = [];
    return data;
  };

  const columns: TableColumnItem[] = [
    ...baseColumns,
    {
      title: '操作',
      width: 160,
      dataIndex: 'operation',
      align: 'center',
      fixed: 'right',
      // actions: ({ record }) => [
      //   {
      //     label: '编辑',
      //     auth: {
      //       perm: 'user.edit',
      //       effect: 'disable',
      //     },
      //     onClick: () => openAddModal(record),
      //   },
      //   {
      //     label: '删除',
      //     auth: 'user.del',
      //     popConfirm: {
      //       title: '你确定要删除吗？',
      //       onConfirm: () => delRowConfirm(record.id),
      //     },
      //   },
      // ],
    },
  ];

  const [DynamicTable, dynamicTableInstance] = useTable({ formProps: { autoSubmitOnEnter: true } });
  const [showModal] = useFormModal();

  const rowSelection = ref({
    selectedRowKeys: [] as number[] | string[],
    onChange: (selectedRowKeys: number[] | string[], selectedRows: TableListItem[]) => {
      console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      rowSelection.value.selectedRowKeys = selectedRowKeys;
    },
  });

  // 是否勾选了表格行
  const isCheckRows = computed(() => rowSelection.value.selectedRowKeys.length);

  const delRowConfirm = async (id: string | string[]) => {
    if (Array.isArray(id)) {
      Modal.confirm({
        title: '确定要删除所选的数据吗?',
        icon: <ExclamationCircleOutlined />,
        centered: true,
        onOk: async () => {
          const res = await delData({ ids: id.join(',') });
          if (res.data.success) {
            message.success(res.data.msg);
          } else {
            message.error(res.data.msg);
          }
          dynamicTableInstance?.reload();
        },
      });
    } else {
      const res = await delData({ ids: id }).finally(dynamicTableInstance?.reload);
      if (res.data.success) {
        message.success(res.data.msg);
      } else {
        message.error(res.data.msg);
      }
      dynamicTableInstance?.reload();
    }
  };

  /**
   * @description 打开用户弹窗
   */
  const openAddModal = async (record: Partial<TableListItem> = {}) => {
    const [formRef] = await showModal<any>({
      modalProps: {
        title: `${record.id ? '编辑' : '新增'}用户`,
        width: 1000,
        onFinish: async (values) => {
          console.log('新增/编辑用户', values);
          values.post_ids = values.post_ids.join(',');
          const id = record.id ? record.id : '';
          const res = await saveOrUpdate({ ...values, id });
          if (res.data.success) {
            message.success(res.data.msg);
          } else {
            message.error(res.data.msg);
          }
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
        field: 'organize_id',
        componentProps: {
          treeDefaultExpandedKeys: expandedKeys.value,
          treeData: deptTree.value,
        },
      },
    ]);
    if (!record.id) {
      record.organize_id = organize_id.value;
      record.order_by = data_count.value + 1;
    } else {
      record.post_ids = record.post_ids?.split(',');
    }
    formRef?.setFieldsValue(record);
  };

  const resetPwd = async (ids) => {
    const [formRef] = await showModal<any>({
      modalProps: {
        title: `密码重置`,
        width: 600,
        onFinish: async (values) => {
          if (Array.isArray(ids)) {
            const res = await updatePwd({ ...values, ids: ids.join(',') });
            if (res.data.success) {
              message.success(res.data.msg);
            } else {
              message.error(res.data.msg);
            }
          } else {
            const res = await updatePwd({ ...values, ids });
            if (res.data.success) {
              message.success(res.data.msg);
            } else {
              message.error(res.data.msg);
            }
          }
          dynamicTableInstance?.reload();
        },
      },
      formProps: {
        labelWidth: 100,
        schemas: resetPwdSchemas,
        autoSubmitOnEnter: true,
      },
    });
  };

  const orgVisible = ref(false);
  const userData = ref({});
  const mangeOrganize = (record) => {
    userData.value = record;
    orgVisible.value = true;
  };

  const onCancel = () => {
    orgVisible.value = false;
  };

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

  const record = ref({});
  const visible = ref(false);
  const openAuthModal = (item) => {
    record.value = item;
    visible.value = true;
  };

  const saveAuth = (id, targetKeys, showAuthData, operaAuthData, organize_id) => {
    updateAuth({
      id: id,
      role_ids: targetKeys.join(','),
      showAuthData: showAuthData.join(','),
      operaAuthData: operaAuthData.join(','),
      organize_id: organize_id,
    }).then((response) => {
      if (response.success) {
        message.success('权限分配成功。');
        visible.value = false;
      } else {
        message.error('权限分配失败。');
      }
    });
  };


  const baseUrl = import.meta.env.VITE_BASE_API;
  //下载导出模板
  const downImportTemp = () => {
    axios
      .get(baseUrl + 'system/user/downloadImportTemplate', {
        params: {},
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
        a.download = '用户导入模板.xlsx';
        a.click();
        // 释放这个临时的对象url
        window.URL.revokeObjectURL(url);
      });
  };
  //导入数据
  const visibleImport = ref(false);
  const importData = (item) => {
    record.value = {};
    visibleImport.value = true;
  };
  //导出数据
  const exportData = () => {
    axios
      .get(baseUrl + 'system/user/export', {
        params: { ...queryParams.value, organize_id: organize_id.value },
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
        a.download = '用户数据_'+getGuid()+'.xlsx';
        a.click();
        // 释放这个临时的对象url
        window.URL.revokeObjectURL(url);
      });
  };

  const visibleEdit = ref(false);
  const openEditModal = (item) => {
    record.value = item;
    visibleEdit.value = true;
  };


</script>

<style scoped>
  .ant-btn {
    margin-right: 10px !important;
  }
</style>
