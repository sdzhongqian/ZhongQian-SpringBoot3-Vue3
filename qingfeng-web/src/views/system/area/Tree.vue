<template>
  <div>
    <div class="flex justify-between">
      <div>地区信息</div>
      <Space>
        <Tooltip placement="top">
          <template #title>刷新 </template>
          <SyncOutlined :spin="listLoading" @click="fetchList" />
        </Tooltip>
      </Space>
    </div>
    <Tree v-model:expandedKeys="state.expandedKeys" auto-expand-parent :tree-data="state.treeData" @select="onTreeSelect">
      <template #title="{ key, title, formData }">
        <Dropdown :trigger="['contextmenu']">
          <span>{{ title }}</span>
          <!-- <template #overlay>
            <Menu>
              <Menu.Item key="1" :disabled="!$auth('dictionary.edit')" @click="openModal(formData)">
                编辑
                <EditOutlined />
              </Menu.Item>
              <Menu.Item key="2" :disabled="!$auth('dictionary.del')" @click="del(key)">
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
} from '@ant-design/icons-vue';
import { Tree, Dropdown, Space, Tooltip } from 'ant-design-vue';
import type { TreeDataItem } from '@/core/permission/utils';
import { findAreaList } from "@/api/system/area";
import { formatDept2Tree } from '@/core/permission/utils';

defineOptions({
  name: 'areaTree',
});

onMounted(() => {
  fetchList();
});

interface State {
  expandedKeys: (string | number)[];
  treeIds: string[];
  treeData: TreeDataItem[];
}

const state = reactive<State>({
  expandedKeys: [],
  treeIds: [],
  treeData: [],
});

const emits = defineEmits(['selectTree'])//这里暴露父组件自定义的方法
const onTreeSelect = (selectedKeys: string[]) => {
  state.treeIds = selectedKeys;
  emits('selectTree', state);
};

/**
 * 获取部门列表
 */
const listLoading = ref(false);
const fetchList = async () => {
  listLoading.value = true;
  const response = await findAreaList({ parent_id: '' }).finally(() => (listLoading.value = false));
  state.treeData = formatDept2Tree(response.data, '1');
  state.expandedKeys = [...state.expandedKeys, ...state.treeData.map((n) => n.key)];
  emits('selectTree', state);
};

defineExpose({
  fetchList
})

</script>

<style></style>