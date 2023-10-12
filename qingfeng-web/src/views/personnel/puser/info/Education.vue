<template>
    <a-form v-bind="formLayout" :model="formRef" @finish="handleFinish">
      <table class="ant-table">
        <thead class="ant-table-thead">
          <tr>
            <th>学校<span style="color: red">*</span></th>
            <th>专业<span style="color: red">*</span></th>
            <th style="width: 120px">学历<span style="color: red">*</span></th>
            <th style="width: 120px">学位</th>
            <th>入学日期<span style="color: red">*</span></th>
            <th>毕业日期<span style="color: red">*</span></th>
          </tr>
        </thead>
        <tbody class="ant-table-tbody">
          <tr v-for="(item, index) in formRef.dataList" :key="index">
            <td>
              {{ item.school }}
            </td>
            <td>
              {{ item.special }}
            </td>
            <td>
              {{ item.education_name }}
            </td>
            <td>
              {{ item.degree_name }}
            </td>
            <td>
              {{ item.start_time }}
            </td>
            <td>
              {{ item.end_time }}
            </td>
          </tr>
        </tbody>
      </table>
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
    import { DeleteOutlined } from '@ant-design/icons-vue';
    import { findEducationList } from '@/api/personnel/puser';
    import { formatDateTZ } from '@/utils/util';
  
    defineOptions({
      name: 'Select',
    });
  
    const title = ref('教育经历');
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
    const formRef = reactive({
      id: '',
      dataList: [],
    });
    const dateFormat = 'y-m-d';
    const degree_data = ref([]);
    const education_data = ref([]);
    onMounted(async () => {
      const eduListRes: any = await findEducationList({ user_id: props.data.id });
      if (eduListRes.data.length > 0) {
        eduListRes.data.forEach((item) => {
          item.start_time = formatDateTZ(item.start_time, dateFormat);
          item.end_time = formatDateTZ(item.end_time, dateFormat);
        });
        formRef.dataList = eduListRes.data;
      }
    });
  </script>
  
  <style scoped>
    .ant-table {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
      color: #000000d9;
      font-variant: tabular-nums;
      line-height: 1.5715;
      list-style: none;
      font-feature-settings: 'tnum';
      position: relative;
      font-size: 14px;
      background: #fff;
      border-radius: 2px;
      width: 100%;
    }
    .ant-table-thead > tr > th {
      position: relative;
      color: #000000d9;
      font-weight: 500;
      text-align: left;
      background: #fafafa;
      border: 1px solid #f0f0f0;
      transition: background 0.3s ease;
      padding: 10px;
    }
    .ant-table > tr > td {
      position: relative;
      padding: 16px;
      overflow-wrap: break-word;
    }
    .ant-table-tbody > tr > td {
      border: 1px solid #f0f0f0;
      transition: background 0.3s;
      padding: 10px;
    }
    .ant-form-item {
      margin: 0 !important;
    }
  </style>
  