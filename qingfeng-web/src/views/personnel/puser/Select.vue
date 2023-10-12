<!--
  参数：type:
    0-单用户
    1-单组织
    2-单组织用户
    3-多用户
    4-多组织
    5-多组织用户
  返回数据：
    id、name、type 
    type:1-用户 2-组织
-->
<template>
  <DraggableModal :title="title" width="800px" v-model:visible="$props.visible" @ok="onOk" @cancel="onCancel">
    <SplitPanel>
      <template #left-content>
        <org-tree @selectTree="selectTree" :parent_id="parent_id" ref="treeRef"></org-tree>
      </template>
      <template #right-content>
        <a-card size="small" style="width: 100%;max-height: 280px;overflow: auto;">
          <template #title>选择区域
            <a-checkbox style="padding-left: 20px;" v-if="$props.type == 3 || $props.type == 4 || $props.type == 5"
              @change="onCheckAllChange"> 全选
            </a-checkbox>
          </template>
          <a-radio-group v-if="$props.type == 0 || $props.type == 1 || $props.type == 2" v-model:value="selectData"
            :style="{ padding: '10px' }" @change="onChange">
            <a-radio v-for="(item, i) in list" :key="i" :value="item.id + ':' + item.name + ':' + item.type">
              {{ item.name }}
              <span v-if="item.type == '2' && $props.type == 2">(组织)</span>
              <span v-if="item.type == '1' && $props.type == 2">(用户)</span>
            </a-radio>
          </a-radio-group>
          <a-checkbox-group v-if="$props.type == 3 || $props.type == 4 || $props.type == 5" v-model:value="selectData"
            :style="{ padding: '10px' }" @change="onChange">
            <a-checkbox v-for="(item, i) in list" :key="i" :value="item.id + ':' + item.name + ':' + item.type">
              {{ item.name }}
              <span v-if="item.type == '2' && $props.type == 5">(组织)</span>
              <span v-if="item.type == '1' && $props.type == 5">(用户)</span>
            </a-checkbox>
          </a-checkbox-group>
        </a-card>
        <a-card size="small" style="width: 100%;margin-top: 2px;">
          <a-tag style="margin-top: 5px;" v-if="selectList.length > 0" v-for="(item, index) in selectList" :key="index"
            color="blue">{{ item.name
            }} <a @click="delData(item)" style="color:#ff4d4f">
              <DeleteOutlined />
            </a></a-tag>
          <span v-else style="color:#999">请选择</span>
        </a-card>
      </template>
    </SplitPanel>
  </DraggableModal>
</template>

<script setup lang="tsx">
import { ref, watch, onMounted, defineEmits, defineExpose } from 'vue';
import { DeleteOutlined } from '@ant-design/icons-vue';
import { SplitPanel } from '@/components/basic/split-panel';
import orgTree from "../organize/Tree.vue";
import { DraggableModal } from '@/components/core/draggable-modal';
import { getSelectData } from "@/api/system/user";

defineOptions({
  name: 'Select',
});

const props = defineProps({
  type: {
    type: Number,
    default: 0,
  },
  visible: {
    type: Boolean,
    default: false,
  },
  value: {
    type: Object,
    default: [],
  },
  organize_id: {
    type: String,
    default: '',
  },
})

watch(() => props.value, (newValue, oldValue) => {
  initData();
})


const emits = defineEmits(['ok', 'cancel'])//这里暴露父组件自定义的方法
const list = ref([]);
const title = ref("选择")
const parent_id = ref("")
onMounted(() => {
  if(props.organize_id!=''){
    organize_id.value = props.organize_id;
    parent_id.value = props.organize_id;
  }
  if (props.type == 0 || props.type == 3) {
    title.value = "请选择用户";
  } else if (props.type == 1 || props.type == 4) {
    title.value = "请选择组织";
  } else if (props.type == 2 || props.type == 5) {
    title.value = "请选择组织用户";
  }
  initData();
});


const findData = async () => {
  const res: any = await getSelectData({
    type: props.type,
    organize_id: organize_id.value,
  });
  list.value = res.data;
}

const treeRef = ref()
const deptTree = ref({})
const expandedKeys = ref([])
const organize_id = ref('');
const selectTree = (state) => {
  deptTree.value = state.deptTree;
  expandedKeys.value = state.expandedKeys;
  organize_id.value = state.departmentIds[0];
  findData();
}


const selectData = ref([]);
const selectIds = ref([]);
const sdata = ref([])
const selectList = ref([]);
const onChange = (e) => {
  if (props.type == 0 || props.type == 1 || props.type == 2) {
    const value = e.target.value.split(":");
    const id = value[0];
    const name = value[1];
    const type = value[2];
    selectList.value = [];
    selectList.value.push({ id, name, type });
  } else {
    list.value.map(function (item, index) {
      removeObject(selectList.value, item.id);
      remove(sdata.value, item.id + ":" + item.name + ":" + item.type);
      remove(selectIds.value, item.id);
    });
    e.map(function (item, key, ary) {
      const id = item.split(":")[0];
      const name = item.split(":")[1];
      const type = item.split(":")[2];
      if (selectIds.value.indexOf(id) == -1) {
        selectList.value.push({ id, name, type });
        selectIds.value.push(id);
        sdata.value.push(id + ":" + name + ":" + type);
      }
    });
    selectData.value = sdata.value;
  }
};


const onCheckAllChange = (e) => {
  if (e.target.checked) {
    list.value.map(function (item, index) {
      if (sdata.value.indexOf(item.id + ":" + item.name + ":" + item.type) == -1) {
        sdata.value.push(item.id + ":" + item.name + ":" + item.type);
        if (item.id != "") {
          selectList.value.push({ id: item.id, name: item.name, type: item.type });
        }
      }
    });
  } else {
    list.value.map(function (item, index) {
      removeObject(selectList.value, item.id);
      remove(sdata.value, item.id + ":" + item.name + ":" + item.type);
    });
  }
};

const remove = (that, val) => {
  var index = that.indexOf(val);
  if (index > -1) {
    that.splice(index, 1);
  }
};
const removeObject = (that, val) => {
  that.map(function (item, key, ary) {
    if (item.id == val) {
      that.splice(key, 1);
    }
  });
};

const delData = (item) => {
  removeObject(selectList.value, item.id);
  remove(sdata.value, item.id + ":" + item.name + ":" + item.type);
  remove(selectIds, item.id);
};

const initData = () => {
  const selectValue = props.value;
  selectValue.forEach(item => {
    selectIds.value.push(item.id);
    sdata.value.push(item.id + ":" + item.name + ":" + item.type);
  });
  selectList.value = selectValue;
  selectData.value = sdata.value;
}


defineExpose({
  selectList: selectList.value
})

const onOk = () => {
  emits('ok', selectList.value);
}

const onCancel = () => {
  emits('cancel');
}

</script>

<style scoped></style>
