<template>
  <DraggableModal
    :title="title"
    width="1200px"
    v-model:visible="$props.visible"
    @ok="onOk"
    @cancel="onCancel"
  >
    <template #footer>
      <a-button @click="onCancel">关闭</a-button>
    </template>
    <a-card
      style="width: 100%"
      :tab-list="tabListNoTitle"
      :active-tab-key="noTitleKey"
      @tabChange="(key) => onTabChange(key, 'noTitleKey')"
    >
      <block v-for="(item, index) of vodeList" :key="index">
        <div v-if="noTitleKey === item.name">
          <div style="white-space: pre-wrap" v-html="item.content"></div>
        </div>
      </block>
      <template #tabBarExtraContent>
        <a @click="downloadCode()">下载</a>
      </template>
    </a-card>
  </DraggableModal>
</template>

<script setup lang="tsx">
  import { ref, reactive, watch, onMounted, defineEmits, defineExpose, computed } from 'vue';
  import { DraggableModal } from '@/components/core/draggable-modal';
  import { getViewCode } from '@/api/system/gencode';

  defineOptions({
    name: 'Select',
  });
  const title = ref('编辑代码生成规则');
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

  const tabListNoTitle = ref([]);
  const key = ref('tab1');
  const noTitleKey = ref('');
  const vodeList = ref([]);

  watch(
    () => props.data,
    (newValue, oldValue) => {
      findViewCode();
    },
  );

  onMounted(() => {
    findViewCode();
  });

  const findViewCode = () => {
    getViewCode(props.data?.id).then((response) => {
      var data = response.data;
      vodeList.value = data;
      for (var i = 0; i < data.length; i++) {
        if (i == 0) {
          noTitleKey.value = data[i].name;
        }
        tabListNoTitle.value.push({ key: data[i].name, tab: data[i].name });
      }
    });
  };

  const onTabChange = (key, type) => {
    noTitleKey.value = key;
  };

  const downloadCode = () => {
    const baseURL = '/api';
    window.location.href = baseURL + '/system/gencode/downloadCode?id=' + props.data?.id;
  };

  defineExpose({});
  const emits = defineEmits(['ok', 'cancel']); //这里暴露父组件自定义的方法
  const onOk = () => {
    emits('ok', '');
  };

  const onCancel = () => {
    emits('cancel');
  };
</script>

<style scoped></style>
