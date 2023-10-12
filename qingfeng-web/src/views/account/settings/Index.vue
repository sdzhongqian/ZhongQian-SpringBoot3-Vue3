<template>
  <div class="page-header-index-wide">
    <SplitPanel>
      <template #left-content>
        <a-menu :selectedKeys="selectedKeys" mode="vertical" @click="onOpenChange">
          <a-menu-item key="1"> 个人信息 </a-menu-item>
          <a-menu-item key="2"> 密码设置 </a-menu-item>
          <a-menu-item key="3"> 组织切换 </a-menu-item>
        </a-menu>
      </template>
      <template #right-content> 
        <EditUser v-if="selectedKeys.indexOf('1')!=-1"></EditUser>
        <EditUserPwd v-if="selectedKeys.indexOf('2')!=-1"></EditUserPwd>
        <SwitchOrganize v-if="selectedKeys.indexOf('3')!=-1"></SwitchOrganize>
      </template>
    </SplitPanel>
  </div>
</template>

<script setup lang="ts">
  import { defineComponent, onMounted, ref, watch } from 'vue';
  import { SplitPanel } from '@/components/basic/split-panel';
  import EditUser from './EditUser/Index.vue';
  import EditUserPwd from './EditUserPwd/Index.vue';
  import SwitchOrganize from './SwitchOrganize/Index.vue';

  const selectedKeys = ref(['1']);

  const onOpenChange = (keys) => {
    console.log(keys);
    selectedKeys.value = [keys.key];
  };
</script>

<style scoped></style>
