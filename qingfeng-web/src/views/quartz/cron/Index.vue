<template>
  <div>
    <Alert message="扩展antd按钮样式" type="info" show-icon style="margin-bottom: 12px" />
    <a-card style="width: 90%;margin: 0 auto;">
      <cron></cron>
    </a-card>
  </div>
</template>

<script setup lang="ts">
  import { Alert, Space, Card, Divider } from 'ant-design-vue';
  import Cron from '@/components/basic/cron/Index.vue';
  defineOptions({
    name: 'Cron',
  });
</script>
