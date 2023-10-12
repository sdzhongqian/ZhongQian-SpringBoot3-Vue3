<template>
  <DraggableModal
    title="员工信息编辑"
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
        <schema-form ref="dynamicForm" v-bind="formProps" @submit="confirm"> </schema-form>
      </a-tab-pane>
      <a-tab-pane key="2">
        <template #tab>
          <span>
            <apple-outlined />
            完善信息
          </span>
        </template>
        <schema-form ref="puserForm" v-bind="formPuserProps" @submit="confirmPuser"> </schema-form>
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
  import { schemas, eduSchemas, puserSchemas } from './formSchemas';
  import type { SchemaFormProps, SchemaFormInstance } from '@/components/core/schema-form';
  import { SchemaForm } from '@/components/core/schema-form';
  import { useI18n } from '@/hooks/useI18n';
  import { saveOrUpdate, getUserInfo } from '@/api/system/user';
  import { findPuserInfo, savePuser } from '@/api/personnel/puser';
  import { DraggableModal } from '@/components/core/draggable-modal';
  import Education from './expand/Education.vue';
  import Workexper from './expand/Workexper.vue';
  import Photo from './expand/Photo.vue';
  defineOptions({
    name: 'EditUser',
  });
  const { t } = useI18n();
  /**
   * @description 基础表单
   */
  const dynamicForm = ref<SchemaFormInstance>();

  const formProps: SchemaFormProps = {
    schemas,
    labelWidth: 120,
    actionColOptions: { span: 24 },
    submitButtonOptions: {
      text: t('common.okText'),
    },
    resetButtonOptions: {
      text: t('common.cancelText'),
    },
    resetFunc: ()=>emits('cancel')
  };
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
      findUser();
    },
  );

  onMounted(() => {
    findUser();
  });

  const findUser = async () => {
    if (props.data.id) {
      const res = await getUserInfo(props.data.id);
      const data = res.data;
      if (!isNaN(parseInt(data.order_by))) {
        data.order_by = parseInt(data.order_by);
      }
      if (!isNaN(parseInt(data.sex))) {
        data.sex = parseInt(data.sex);
      }
      if (data.post_ids) {
        data.post_ids = data.post_ids?.split(',');
      } else {
        data.post_ids = [];
      }
      dynamicForm.value?.setFieldsValue(data);
    }

    dynamicForm.value?.updateSchema([
      {
        field: 'organize_id',
        componentProps: {
          treeDefaultExpandedKeys: props.expandedKeys,
          treeData: props.deptTree,
        },
      },
    ]);
  };

  // 点击提交
  function confirm() {
    dynamicForm.value?.validate().then(async () => {
      let formData: any = dynamicForm.value?.formModel;
      formData.post_ids = formData.post_ids.join(',');
      formData.id = props.data.id;
      // formData.organize_id = '';
      const res = await saveOrUpdate(formData);
      if (res.data.success) {
        message.success(res.data.msg);
      } else {
        message.error(res.data.msg);
      }
    });
  }

  defineExpose({});
  const emits = defineEmits(['ok', 'cancel']); //这里暴露父组件自定义的方法
  const onOk = () => {
    emits('ok', '');
  };

  const onCancel = () => {
    emits('cancel');
  };

  const educationForm = ref<SchemaFormInstance>();
  const educationProps: SchemaFormProps = {
    schemas: eduSchemas,
    labelWidth: 120,
    actionColOptions: { span: 24 },
    submitButtonOptions: {
      text: t('common.okText'),
    },
    resetButtonOptions: {
      text: t('common.cancelText'),
    },
  };

  const educationConfirm = (record) => {
    console.log(record);
  };

  const changeTab = (e) => {
    if (e == 1) {
      findUser();
    } else if (e == 2) {
      findPUser();
    }
  };

  //----完善信息----
  const puserForm = ref<SchemaFormInstance>();
  const formPuserProps: SchemaFormProps = {
    schemas: puserSchemas,
    labelWidth: 120,
    actionColOptions: { span: 24 },
    submitButtonOptions: {
      text: t('common.okText'),
    },
    resetButtonOptions: {
      text: t('common.cancelText'),
    },
  };

  const findPUser = async () => {
    if (props.data.id) {
      const res: any = await findPuserInfo({ id: props.data.id });
      puserForm.value?.setFieldsValue(res.data);
    }
  };

  // 点击提交完善信息
  function confirmPuser() {
    puserForm.value?.validate().then(async () => {
      let formData: any = puserForm.value?.formModel;
      formData.id = props.data.id;
      formData.user_id = props.data.id;
      const res = await savePuser(formData);
      console.log(res);
      if (res.success) {
        message.success(res.msg);
      } else {
        message.error(res.msg);
      }
    });
  }
</script>

<style lang="less" scoped>
  .btn-rows {
    button {
      margin-right: 12px;
    }
  }
</style>
