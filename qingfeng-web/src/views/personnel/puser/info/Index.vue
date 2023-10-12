<template>
  <a-form v-bind="formLayout">
    <a-row>
      <a-col :span="24">
        <a-form-item label="所属部门" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
          {{ formRef.organize_name }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="12">
        <a-form-item label="岗位" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            <a-tag v-for="item in formRef.postList" color="cyan">{{item.name}}</a-tag>
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="职位" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.position_name }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="12">
        <a-form-item label="登录名称" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.login_name }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="姓名" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.name }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="12">
        <a-form-item label="性别" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            <span v-if="formRef.sex==0">男</span>
            <span v-else-if="formRef.sex==1">女</span>
            <span v-else>其他</span>
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="身份证号" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.idcard }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="12">
        <a-form-item label="手机号" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.phone }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="电子邮箱" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.email }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="12">
        <a-form-item label="出生日期" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.birth_date }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="座右铭" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.motto }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="12">
        <a-form-item label="居住地址" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.live_address }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="户籍地址" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.birth_address }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="24">
        <a-form-item label="头像地址" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
          <img width="100" :src="formRef.headImg" alt="avatar" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="24">
        <a-form-item label="排序" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
            {{ formRef.order_by }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="24">
        <a-form-item label="备注" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
            {{ formRef.remark }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider />
    <a-row>
      <a-col :span="24">
        <div align="right" style="margin: 0 20px">
          <a-button
            style="margin-right: 10px"
            @click="
              () => {
                $emit('cancel');
              }
            "
            >关闭</a-button
          >
        </div>
      </a-col>
    </a-row>
  </a-form>
</template>

<script setup lang="tsx">
  import { ref, watch, onMounted, defineEmits, defineExpose, reactive } from 'vue';
  import { randomNum } from '@/utils/util';
  import { DeleteOutlined, DownloadOutlined } from '@ant-design/icons-vue';
  import dayjs, { Dayjs } from 'dayjs';
  import { message } from 'ant-design-vue';
  import { getUserInfo } from '@/api/system/user';
  defineOptions({
    name: 'Puser',
  });

  const title = ref('员工完善信息');
  const props = defineProps({
    data: {
      type: Object,
      default: {},
    },
  });

  watch(
    () => props.data,
    (newValue, oldValue) => {},
  );

   // 表单字段
   const formRef = ref({
    id: '',
  });
  onMounted(async () => {
    const res: any = await getUserInfo(props.data.id);
    formRef.value = res.data;
  });

</script>

<style scoped>
  .ant-form-item-explain {
    min-height: 4px !important;
  }
  .ant-divider-horizontal {
    margin: -15px 10px 10px 50px !important;
    width: 90% !important;
    min-width: 90% !important;
  }
</style>