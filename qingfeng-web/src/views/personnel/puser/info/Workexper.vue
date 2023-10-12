<template>
  <a-form v-bind="formLayout" :model="formRef" @finish="handleFinish">
    <table class="ant-table">
      <thead class="ant-table-thead">
        <tr>
          <th>公司名称<span style="color: red">*</span></th>
          <th>任职职位<span style="color: red">*</span></th>
          <th>开始日期<span style="color: red">*</span></th>
          <th>结束日期<span style="color: red">*</span></th>
          <th>联系人<span style="color: red">*</span></th>
          <th>联系电话<span style="color: red">*</span></th>
          <th>离职理由<span style="color: red">*</span></th>
        </tr>
      </thead>
      <tbody class="ant-table-tbody">
        <tr v-for="(item, index) in formRef.dataList" :key="index">
          <td>
            {{ item.name }}
          </td>
          <td>
            {{ item.position }}
          </td>
          <td>
            {{ item.start_time }}
          </td>
          <td>
            {{ item.end_time }}
          </td>
          <td>
            {{ item.link_user }}
          </td>
          <td>
            {{ item.link_phone }}
          </td>
          <td>
            {{ item.leave_reason }}
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
  import { updateAuth, findRoleAuth, findOrganizeAuth } from '@/api/system/user';
  import { randomNum } from '@/utils/util';
  import { DeleteOutlined } from '@ant-design/icons-vue';
  import dayjs, { Dayjs } from 'dayjs';
  import { message } from 'ant-design-vue';
  import { findWorkexperList, saveWorkexper } from '@/api/personnel/puser';
  import { formatDateTZ } from '@/utils/util';

  defineOptions({
    name: 'Select',
  });

  const title = ref('工作经历');
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
  const workexper_data = ref([]);
  onMounted(async () => {
    const eduListRes: any = await findWorkexperList({ user_id: props.data.id });
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
