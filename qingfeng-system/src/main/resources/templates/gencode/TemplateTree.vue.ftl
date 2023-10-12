<template>
  <div>
    <div class="flex justify-between">
      <div>${tablePd.menu_name}</div>
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
  import { findList } from "@/api/${tablePd.mod_name}/${tablePd.bus_name}";
  import { formatDept2Tree } from '@/core/permission/utils';

  defineOptions({
    name: '${tablePd.bus_name}Tree',
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
   * 获取列表
   */
  const listLoading = ref(false);
  const fetchList = async () => {
    listLoading.value = true;
    const response = await findList({ ${tablePd.tree_pid}: '' }).finally(() => (listLoading.value = false));
    // state.treeData = formatDept2Tree(response.data, '');
    state.treeData = fommat({
      arrayList: response.data,
      pidStr: ${tablePd.tree_pid},
    });
    state.expandedKeys = [...state.expandedKeys, ...state.treeData.map((n) => n.key)];
    emits('selectTree', state);
  };

  const fommat = ({
      arrayList,
      pidStr = "parent_id",
      idStr = "id",
      childrenStr = "children",
    }) => {
    arrayList.push({
      ${tablePd.tree_name}: "${tablePd.menu_name}",
      id: "1",
      ${tablePd.tree_pid}: "0",
    });
    let listOjb = {}; // 用来储存{key: obj}格式的对象
    let treeList = []; // 用来储存最终树形结构数据的数组
    // 将数据变换成{key: obj}格式，方便下面处理数据
    for (let i = 0; i < arrayList.length; i++) {
      var data = arrayList[i];
      data.title = data.${tablePd.tree_name};
      data.key = data.id;
      if (data.child_num == 0) {
        data.isLeaf = true;
      }
      listOjb[arrayList[i][idStr]] = data;
    }
    // 根据pid来将数据进行格式化
    for (let j = 0; j < arrayList.length; j++) {
      // 判断父级是否存在
      let haveParent = listOjb[arrayList[j][pidStr]];
      if (haveParent) {
        // 如果有没有父级children字段，就创建一个children字段
        !haveParent[childrenStr] && (haveParent[childrenStr] = []);
        // 在父级里插入子项
        haveParent[childrenStr].push(arrayList[j]);
      } else {
        // 如果没有父级直接插入到最外层
        treeList.push(arrayList[j]);
      }
    }
    return treeList;
  };

  defineExpose({
    fetchList
  })

</script>

<style></style>