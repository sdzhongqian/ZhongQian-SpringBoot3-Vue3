<template>
    <DraggableModal
      title="员工信息"
      width="1000px"
      v-model:visible="$props.visible"
      :destroyOnClose="true"
      @ok="onOk"
      @cancel="onCancel"
    >
      <template #footer> </template>
      <a-tabs v-model:activeKey="activeKey" @change="changeTab">
        <a-tab-pane key="1">
          <template #tab>
            <span>
              <apple-outlined />
              基本信息
            </span>
          </template>
          <index :data="props.data" @cancel="onCancel"> </index>
        </a-tab-pane>
        <a-tab-pane key="2">
          <template #tab>
            <span>
              <apple-outlined />
              完善信息
            </span>
          </template>
          <puser :data="props.data" @cancel="onCancel"> </puser>
        </a-tab-pane>
        <a-tab-pane key="3">
          <template #tab>
            <span>
              <android-outlined />
              教育经历
            </span>
          </template>
          <!-- <schema-form ref="educationForm" v-bind="educationProps" @submit="educationConfirm">
          </schema-form> -->
          <education :data="props.data" @cancel="onCancel"></education>
        </a-tab-pane>
        <a-tab-pane key="4">
          <template #tab>
            <span>
              <android-outlined />
              工作经历
            </span>
          </template>
          <workexper :data="props.data" @cancel="onCancel"></workexper>
        </a-tab-pane>
        <a-tab-pane key="5">
          <template #tab>
            <span>
              <android-outlined />
              证书信息
            </span>
          </template>
          <photo :data="props.data" @cancel="onCancel"></photo>
        </a-tab-pane>
      </a-tabs>
    </DraggableModal>
  </template>
  
  <script lang="ts" setup>
    import { ref, onMounted, watch } from 'vue';
    import { Alert, message } from 'ant-design-vue';
    import { useI18n } from '@/hooks/useI18n';
    import { DraggableModal } from '@/components/core/draggable-modal';
    import Index from './info/Index.vue';
    import Puser from './info/Puser.vue';
    import Education from './info/Education.vue';
    import Workexper from './info/Workexper.vue';
    import Photo from './info/Photo.vue';
    defineOptions({
      name: 'InfoUser',
    });
    const { t } = useI18n();
  
    const activeKey = ref('1');
    const props = defineProps({
      visible: {
        type: Boolean,
        default: false,
      },
      data: {
        type: Object,
        default: {},
      },
      deptTree: {
        type: Array,
        default: [],
      },
      expandedKeys: {
        type: Array,
        default: [],
      },
    });
  
    watch(
      () => props.data,
      (newValue, oldValue) => {
      },
    );
  
    onMounted(() => {
    });
  
    defineExpose({});
    const emits = defineEmits(['ok', 'cancel']); //这里暴露父组件自定义的方法
    const onOk = () => {
      emits('ok', '');
    };
  
    const onCancel = () => {
      emits('cancel');
    };
  </script>
  
  <style lang="less" scoped>
    .btn-rows {
      button {
        margin-right: 12px;
      }
    }
  </style>
  