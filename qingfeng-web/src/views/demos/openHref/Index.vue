<!-- 菜单管理 -->
<template>
  <div>
    菜单管理
    <a-button @click="open()" type="primary"> 当前页打开百度 </a-button>
    <a-button @click="openTarget()" type="primary"> 新页面打开百度 </a-button>
  </div>
</template>

<script setup lang="ts">
  const open = () => {
    window.location.href = 'http://www.baidu.com';
  };
  const openTarget = () => {
    window.open('http://www.baidu.com');
  };
</script>
<style scoped>
  /* @import url(); 引入css类 */
</style>
