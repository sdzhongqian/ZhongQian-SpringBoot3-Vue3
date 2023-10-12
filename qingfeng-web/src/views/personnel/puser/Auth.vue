<template>
  <DraggableModal
    :title="title"
    width="1000px"
    v-model:visible="$props.visible"
    @ok="onOk"
    @cancel="onCancel"
  >
    <a-transfer
      :list-style="{
        width: '420px',
        height: '420px',
        margin: '0 auto',
      }"
      v-model:target-keys="targetKeys"
      v-model:selected-keys="selectedKeys"
      :data-source="authData"
      :titles="['角色', '授权角色']"
      :render="(item) => item.title"
      @change="handleChange"
      @selectChange="handleSelectChange"
      @scroll="handleScroll"
    />
    <a-divider>数据权限分配</a-divider>
    <a-select
      style="width: 100%"
      v-model:value="currentOrganize"
      placeholder="请选择"
      @change="selectOrgChange"
    >
      <a-select-option v-for="org in orgData" :key="org.id" :value="org.organize_id">
        {{ org.organize_name }}
      </a-select-option>
    </a-select>
    <a-table
      :columns="columns"
      :data-source="data"
      v-if="data.length"
      defaultExpandAllRows
      bordered
    >
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'showAuth'">
          <a-checkbox
            :value="record.id"
            @change="showAuthChange"
            :checked="showAuthData.indexOf(record.id) != -1"
          ></a-checkbox>
        </template>
        <template v-else-if="column.dataIndex === 'operaAuth'">
          <a-checkbox
            :value="record.id"
            @change="operaAuthChange"
            :checked="operaAuthData.indexOf(record.id) != -1"
          ></a-checkbox>
        </template>
      </template>
    </a-table>
  </DraggableModal>
</template>

<script setup lang="tsx">
  import { ref, watch, onMounted, defineEmits, defineExpose } from 'vue';
  import { DraggableModal } from '@/components/core/draggable-modal';
  import { updateAuth, findRoleAuth, findOrganizeAuth } from '@/api/system/user';
  defineOptions({
    name: 'Select',
  });
  const title = ref("权限分配");
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
    (newValue, oldValue) => {
      getRoleAuth();
    },
  );

  const columns = [
    { title: '名称', dataIndex: 'title' },
    {
      title: '查看权限',
      dataIndex: 'showAuth',
    },
    {
      title: '操作权限（编辑/删除）',
      dataIndex: 'operaAuth',
    },
  ];

  const authData:any = ref([]);
  const targetKeys = ref<string[]>([]);
  const selectedKeys = ref<string[]>([]);

  const data = ref([]);
  const showAuth = ref([]);
  const operaAuth = ref([]);
  const currentOrganize = ref('');
  const orgData = ref([]);
  const showAuthData = ref([]);
  const operaAuthData = ref([]);

  const handleChange = (nextTargetKeys: string[], direction: string, moveKeys: string[]) => {};
  const handleSelectChange = (sourceSelectedKeys: string[], targetSelectedKeys: string[]) => {};
  const handleScroll = (direction: string, e: Event) => {};

  // 自动请求并暴露内部方法
  onMounted(() => {
    getRoleAuth();
  });

  const getRoleAuth = () => {
    findRoleAuth({ id: props.data.id }).then((response) => {
      let respData:any = response.data;
      authData.value = [];
      targetKeys.value = [];
      orgData.value = respData.orgList;
      if (orgData.value.length > 0) {
        currentOrganize.value = orgData.value[0].organize_id;
        selectOrgChange(currentOrganize.value);
      }

      for (let i = 0; i < respData.roleLs.length; i++) {
        respData.roleLs[i].key = respData.roleLs[i].id;
        respData.roleLs[i].title = respData.roleLs[i].name;
        respData.roleLs[i].description = respData.roleLs[i].name;
        authData.value.push({
          key: respData.roleLs[i].id,
          title: respData.roleLs[i].name,
          description: respData.roleLs[i].name,
        });
      }
      for (let i = 0; i < respData.myRoleLs.length; i++) {
        authData.value.push({
          key: respData.myRoleLs[i].id,
          title: respData.myRoleLs[i].name,
          description: respData.myRoleLs[i].name,
        });
        targetKeys.value.push(respData.myRoleLs[i].id);
      }
    });
  };

  const selectOrgChange = (value) => {
    showAuthData.value = [];
    operaAuthData.value = [];
    findOrganizeAuth({ id: props.data.id, organize_id: value }).then((response) => {
      const treeData = fommat({
        arrayList: response.data.list,
        pidStr: 'pid',
      });
      data.value = treeData;
      const object = response.data.object;
      console.log(object)
      if (object.showAuthData != '' && object.showAuthData != null) {
        showAuthData.value = object.showAuthData.split(',');
      }
      if (object.operaAuthData != '' && object.operaAuthData != null) {
        operaAuthData.value = object.operaAuthData.split(',');
      }
    });
  };

  const showAuthChange = (e) => {
    var value = e.target.value;
    if (e.target.checked) {
      let isexist = showAuthData.value.find((c) => c == value);
      if (!isexist) {
        showAuthData.value.push(value);
      }
    } else {
      remove(showAuthData.value, value);
    }
    console.log(showAuthData.value);
  };
  const operaAuthChange = (e) => {
    var value = e.target.value;
    if (e.target.checked) {
      let isexist = operaAuthData.value.find((c) => c == value);
      if (!isexist) {
        operaAuthData.value.push(value);
      }
    } else {
      remove(operaAuthData.value, value);
    }
  };

  const remove = (that, val) => {
    var index = that.indexOf(val);
    if (index > -1) {
      that.splice(index, 1);
    }
  };

  const fommat = ({ arrayList, pidStr = 'parent_id', idStr = 'id', childrenStr = 'children' }) => {
    let listOjb = {}; // 用来储存{key: obj}格式的对象
    let treeList = []; // 用来储存最终树形结构数据的数组
    // 将数据变换成{key: obj}格式，方便下面处理数据
    for (let i = 0; i < arrayList.length; i++) {
      var data = arrayList[i];
      data.key = data.id;
      data.show_key = data.id + ':' + data.org_cascade;
      data.opera_key = data.id + ':' + data.org_cascade;
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

  defineExpose({});
  const emits = defineEmits(['ok', 'cancel']); //这里暴露父组件自定义的方法
  const onOk = () => {
    emits('ok', props.data.id, targetKeys.value,showAuthData.value,operaAuthData.value,currentOrganize.value);
  };

  const onCancel = () => {
    emits('cancel');
  };
</script>

<style scoped></style>
