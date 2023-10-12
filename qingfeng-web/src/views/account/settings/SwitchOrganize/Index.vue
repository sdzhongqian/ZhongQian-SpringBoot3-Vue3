<template>
  <div class="controller">
    <Alert message="用户组织切换" type="info" show-icon style="margin-bottom: 12px">
      <template #description> 组织切换后,需要重新登录。 </template>
    </Alert>
    <p>当前组织：{{ organize_name }}</p>
    <a-divider style="background: #1890ff" />
    <a-radio-group
      name="organize_id"
      v-for="(item, index) in data"
      v-model:value="current_organize"
      @change="onChange"
      :key="item.id"
    >
      <a-radio
        v-if="item.organize_name != null"
        :value="item.organize_id + ':' + item.organize_name"
      >
        {{ item.organize_name }}
      </a-radio>
    </a-radio-group>
    <a-row>
      <a-col :span="24">
        <div align="right" style="margin-top: 20px">
          <a-button @click="handleSwitchOrganizeOk" type="primary">确定</a-button>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  import { defineComponent, onMounted, ref, nextTick } from 'vue';
  import { message, Alert } from 'ant-design-vue';
  import { findLoginUser, updateSwitchOrganize } from '@/api/system/user';
  import { useUserStore } from '@/store/modules/user';
  import { useKeepAliveStore } from '@/store/modules/keepAlive';
  import { useRouter, useRoute } from 'vue-router';
  import { LOGIN_NAME } from '@/router/constant';

  const userStore = useUserStore();
  const keepAliveStore = useKeepAliveStore();
  const router = useRouter();
  const route = useRoute();

  const current_organize = ref('');
  const organize_id = ref('');
  const organize_name = ref('');
  const data: any = ref([]);

  // 自动请求并暴露内部方法
  onMounted(async () => {
    const res = await findLoginUser({});
    data.value = res.data.organizeList;
    console.log(data.value);
    for (let i = 0; i < data.value.length; i++) {
      if (data.value[i].use_status == '0') {
        current_organize.value = data.value[i].organize_id + ':' + data.value[i].organize_name;
        organize_id.value = data.value[i].organize_id;
        organize_name.value = data.value[i].organize_name;
      }
    }
  });

  const onChange = (e) => {
    const value = e.target.value.split(':');
    organize_id.value = value[0];
    organize_name.value = value[1];
  };

  const handleSwitchOrganizeOk = async () => {
    const res = await updateSwitchOrganize({ organize_id: organize_id.value });
    if (res.data.success) {
      message.success('组织切换成功，请重新登录');
      await userStore.logout();
      keepAliveStore.clear();
      // 移除标签页
      localStorage.clear();
      message.success('成功退出登录');
      await nextTick();
      router.replace({
        name: LOGIN_NAME,
        query: {
          redirect: route.fullPath,
        },
      });
    } else {
      message.error(res.data.msg);
    }
  };
</script>
<style scoped>
  .controller {
    margin: 20px;
  }
</style>
