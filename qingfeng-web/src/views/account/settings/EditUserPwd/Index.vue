<template>
  <div>
    <Alert message="密码设置" type="info" show-icon style="margin-bottom: 12px">
      <template #description>
          需要输入旧密码,密码修改先验证旧密码是否正确。
      </template>
    </Alert>
    <a-card>
      <SchemaForm @submit="handleSubmit"> </SchemaForm>
    </a-card>
  </div>
</template>

<script lang="tsx" setup>
  import { computed, ref, unref } from 'vue';
  import { Alert, message, Select } from 'ant-design-vue';
  import { schemas } from './form-schema';
  import { useForm } from '@/components/core/schema-form';
  import { updateMyPwd } from '@/api/system/user';
  import { useI18n } from '@/hooks/useI18n';
  
  defineOptions({
    name: 'EditUserPwd',
  });

  const { t } = useI18n();

  const [SchemaForm] = useForm({
    labelWidth: 120,
    schemas,
    actionColOptions: {
      span: 24,
    },
    submitButtonOptions: {
      text: t('common.okText'),
    },
  });

  // 点击提交
  const handleSubmit = async (values) => {
    message.error("体验平台不可操作");
    // const res = await updateMyPwd(values);
    // if (res.data.success) {
    //   message.success(res.data.msg);
    // } else {
    //   message.error(res.data.msg);
    // }
  }
</script>

<style lang="less" scoped>
  .btn-rows {
    button {
      margin-right: 12px;
    }
  }
</style>
