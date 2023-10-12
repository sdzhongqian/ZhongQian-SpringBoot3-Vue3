<template>
  <DraggableModal
    :title="title"
    width="800px"
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
  </DraggableModal>
</template>

<script setup lang="tsx">
  import { ref, watch, onMounted, defineEmits, defineExpose } from 'vue';
  import { DraggableModal } from '@/components/core/draggable-modal';
  import { findRoleAuth } from '@/api/system/organize';
  defineOptions({
    name: 'Select',
  });

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

  const authData: any = ref([]);
  const targetKeys = ref<string[]>([]);
  const selectedKeys = ref<string[]>([]);

  const handleChange = (nextTargetKeys: string[], direction: string, moveKeys: string[]) => {
    //   console.log("targetKeys: ", nextTargetKeys);
    //   console.log("direction: ", direction);
    //   console.log("moveKeys: ", moveKeys);
  };
  const handleSelectChange = (sourceSelectedKeys: string[], targetSelectedKeys: string[]) => {
    //   console.log("sourceSelectedKeys: ", sourceSelectedKeys);
    //   console.log("targetSelectedKeys: ", targetSelectedKeys);
  };
  const handleScroll = (direction: string, e: Event) => {
    //   console.log("direction:", direction);
    //   console.log("target:", e.target);
  };

  // 自动请求并暴露内部方法
  onMounted(() => {
    getRoleAuth();
  });

  const getRoleAuth = () => {
    findRoleAuth({ id: props.data.id }).then((response) => {
      let respData = response.data;
      authData.value = [];
      targetKeys.value = [];
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

  const title = ref('组织授权');

  defineExpose({});
  const emits = defineEmits(['ok', 'cancel']); //这里暴露父组件自定义的方法
  const onOk = () => {
    emits('ok', props.data.id,targetKeys.value);
  };

  const onCancel = () => {
    emits('cancel');
  };
</script>

<style scoped></style>
