<template>
  <div>
    <Alert message="个人信息编辑" type="info" show-icon style="margin-bottom: 12px" />
    <a-card>
      <schema-form ref="dynamicForm" v-bind="formProps" @submit="confirm"> </schema-form>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { ref, onMounted } from 'vue';
  import { Alert, message } from 'ant-design-vue';
  import { schemas } from './form-schema';
  import type { SchemaFormProps, SchemaFormInstance } from '@/components/core/schema-form';
  import { SchemaForm } from '@/components/core/schema-form';
  import { useI18n } from '@/hooks/useI18n';
  import { updateMyUser, findLoginUser } from '@/api/system/user';
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
  };

  onMounted(async () => {
    const res = await findLoginUser({});
    const data = res.data;
    if(!isNaN(parseInt(data.order_by))){
      data.order_by = parseInt(data.order_by);
    }
    if(!isNaN(parseInt(data.sex))){
      data.sex = parseInt(data.sex);
    }
    dynamicForm.value?.setFieldsValue(res.data);
  });

  // 点击提交
  function confirm() {
    dynamicForm.value?.validate().then(async() => {
      message.error("体验平台不可操作");
      // const res = await updateMyUser(dynamicForm.value?.formModel);
      // if (res.data.success) {
      //   message.success(res.data.msg);
      // } else {
      //   message.error(res.data.msg);
      // }
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
