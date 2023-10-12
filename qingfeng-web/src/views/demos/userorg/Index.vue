<template>
  <div>
    <Alert message="选择组织用户信息案例"
      description="参数(type)：0-单用户、1-单组织、2-单用户或组织、3-多用户、4-多组织、5-多用户或组织；| 返回参数：id-用户/组织ID、name-用户/组织名称、type-类型(1-用户，2-组织)"
      type="info" show-icon style="margin-bottom: 12px" />
    <a-card>
      <div style="margin: 10px auto;">
        <span v-if="type == 0">单用户</span>
        <span v-if="type == 1">多用户</span>
        <span v-if="type == 2">单组织</span>
        <span v-if="type == 3">多组织</span>
        <span v-if="type == 4">单组织用户</span>
        <span v-if="type == 5">多组织用户</span>：
        <a-tag color="blue" v-for="(item, index) in list">{{ item.name }}</a-tag>
      </div>
      <a-button @click="showPreview(0)" style="margin-right: 5px;">选择单用户</a-button>
      <a-button @click="showPreview(3)" style="margin-right: 5px;">选择多用户</a-button>
      <a-button @click="showPreview(1)" style="margin-right: 5px;">选择单组织</a-button>
      <a-button @click="showPreview(4)" style="margin-right: 5px;">选择多组织</a-button>
      <a-button @click="showPreview(2)" style="margin-right: 5px;">选择单组织用户</a-button>
      <a-button @click="showPreview(5)" style="margin-right: 5px;">选择多组织用户</a-button>

      <a-button @click="clearData()">清除</a-button>

      <org-user-select :value="list" :visible="visible" :type="type" @ok="selectData"
        @cancel="visible = false"></org-user-select>
    </a-card>

  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue';
import { Alert, Descriptions, Image } from 'ant-design-vue';
import OrgUserSelect from '@/views/system/user/Select.vue';

defineOptions({
  name: 'CustomIcon'
});

const visible = ref(false);
const type = ref(0);
const list = ref([]);

const showPreview = (e) => {
  type.value = e;
  visible.value = true;
};

const selectData = (data) => {
  list.value = data;
  visible.value = false;
};

const clearData = () => {
  list.value = [];
}
</script>
