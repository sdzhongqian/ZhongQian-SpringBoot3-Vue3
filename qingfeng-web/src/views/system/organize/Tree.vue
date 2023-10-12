<template>
  <div>
    <div class="flex justify-between">
      <div>组织架构</div>
      <Space>
        <!-- <Tooltip v-if="$auth('organize.add')" placement="top">
          <template #title>新增部门 </template>
          <PlusOutlined @click="openDeptModal({})" />
        </Tooltip> -->
        <Tooltip placement="top">
          <template #title>刷新 </template>
          <SyncOutlined :spin="deptListLoading" @click="fetchDeptList" />
        </Tooltip>
      </Space>
    </div>
    <Tree v-model:expandedKeys="state.expandedKeys" auto-expand-parent :tree-data="state.deptTree" @select="onTreeSelect">
      <template #title="{ key, title, formData }">
        <Dropdown :trigger="['contextmenu']">
          <span>{{ title }}</span>
          <!-- <template #overlay>
            <Menu>
              <Menu.Item key="1" :disabled="!$auth('organize.edit')" @click="openDeptModal(formData)">
                编辑
                <EditOutlined />
              </Menu.Item>
              <Menu.Item key="2" :disabled="!$auth('organize.del')" @click="delDept(key)">
                删除
                <DeleteOutlined />
              </Menu.Item>
            </Menu>
          </template> -->
        </Dropdown>
      </template>
    </Tree>
  </div>
</template>

<script setup lang="tsx">
import { ref, reactive, computed, onMounted, defineEmits, defineExpose } from 'vue';
import {
  SyncOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  ExclamationCircleOutlined,
  SwapOutlined,
} from '@ant-design/icons-vue';
import { Tree, Dropdown, Space, Tooltip, Modal, Alert, Menu } from 'ant-design-vue';
import type { TreeDataItem } from '@/core/permission/utils';
import { getOrganizeList } from "@/api/system/organize";
import { formatDept2Tree, findChildById } from '@/core/permission/utils';

defineOptions({
  name: 'OrganizeTree',
});

const props = defineProps({
  parent_id: {
    type: String,
    default: '',
  },
})

onMounted(() => {
  fetchDeptList();
});


interface State {
  expandedKeys: (string | number)[];
  departmentIds: string[];
  deptTree: TreeDataItem[];
}

const state = reactive<State>({
  expandedKeys: [],
  departmentIds: [],
  deptTree: [],
});

const emits = defineEmits(['selectTree'])//这里暴露父组件自定义的方法
const onTreeSelect = (selectedKeys: string[]) => {
  state.departmentIds = selectedKeys;
  console.log(state.departmentIds)
  emits('selectTree', state);
};

/**
 * 获取部门列表
 */
const deptListLoading = ref(false);
const fetchDeptList = async () => {
  deptListLoading.value = true;
  const response = await getOrganizeList({ parent_id: props.parent_id }).finally(() => (deptListLoading.value = false));
  let parent_name = '1';
  if(props.parent_id!=''){
    parent_name = props.parent_id;
  }
  state.deptTree = formatDept2Tree(response.data, parent_name);
  // state.deptTree = fommat({
  //   arrayList: response.data,
  //   pidStr: "parent_id",
  // });
  state.expandedKeys = [...state.expandedKeys, ...state.deptTree.map((n) => n.key)];
  emits('selectTree', state);
};

defineExpose({
  fetchDeptList
})

</script>

<style></style>