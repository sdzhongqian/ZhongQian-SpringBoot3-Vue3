<template>
  <a-form v-bind="formLayout">
    <a-row>
      <a-col :span="12">
        <a-form-item label="工号" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
          {{ formRef.job_num }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="入职日期" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
          {{ formRef.job_date }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="12">
        <a-form-item label="学历" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.education_name }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="婚姻" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.marriage_name }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="12">
        <a-form-item label="民族" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.nation_name }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="政治面貌" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.politics_face_name }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="12">
        <a-form-item label="入党时间" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.party_time }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="基本工资" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.basic_salary }}
        </a-form-item>
      </a-col>
    </a-row>
    <a-divider style="border-color: rgb(0 0 0 / 10%)"  />
    <a-row>
      <a-col :span="12">
        <a-form-item label="岗位工资" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.post_salary }}
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="绩效工资" :label-col="{ span: 6 }" :wrapper-col="{ span: 20 }">
            {{ formRef.merit_salary }}
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
  import { findPhotoList, savePhoto } from '@/api/personnel/puser';
  import { uploadLocalFile } from '@/api/common/upload';
  import { findDictionaryList } from '@/api/system/dictionary';
  import { formatDateTZ } from '@/utils/util';
  import { findPuserInfo } from '@/api/personnel/puser';

  defineOptions({
    name: 'Select',
  });

  const title = ref('证件照信息');
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
    const res: any = await findPuserInfo({ id: props.data.id });
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
