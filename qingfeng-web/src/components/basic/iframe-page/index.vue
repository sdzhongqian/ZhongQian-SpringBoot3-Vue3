<template>
  <div class="iframe-box wh-full">
    <Spin :spinning="loading" size="large">
      <iframe class="wh-full" frameborder="0" v-bind="$attrs" :src="src" @load="onFrameLoad"></iframe>
    </Spin>
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { Spin } from 'ant-design-vue';

  defineOptions({
    name: 'IFramePage',
  });

  defineProps({
    src: {
      type: String,
      required: true,
    },
  });

  const loading = ref(true);

  const onFrameLoad = () => {
    loading.value = false;
  };
</script>
<style lang="less" scoped>
  .iframe-box {
    transform: translate(0);

    :deep(div[class^='ant-spin']) {
      @apply wh-full;
    }
  }
</style>
