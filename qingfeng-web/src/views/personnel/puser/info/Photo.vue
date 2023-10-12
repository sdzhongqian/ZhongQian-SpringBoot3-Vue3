<template>
  <a-form v-bind="formLayout" :model="formRef" @finish="handleFinish">
    <table class="ant-table">
      <thead class="ant-table-thead">
        <tr>
          <th style="width: 140px">证件照名称<span style="color: red">*</span></th>
          <th>证件编号</th>
          <th>开始时间</th>
          <th>结束时间</th>
          <th>认证机构</th>
          <th>证件照地址</th>
          <th style="width: 80px">操作</th>
        </tr>
      </thead>
      <tbody class="ant-table-tbody">
        <tr v-for="(item, index) in formRef.dataList" :key="index">
          <td>
            {{ item.idcard_name }}
          </td>
          <td>
            {{ item.code }}
          </td>
          <td>
            {{ item.start_time }}
          </td>
          <td>
            {{ item.end_time }}
          </td>
          <td>
            {{ item.cert_unit }}
          </td>
          <td>
            <img width="100" :src="item.imageUrl" alt="avatar" />
          </td>
          <td>
            <div
              ><a v-if="item.path != ''" @click="download(item)"><DownloadOutlined />下载</a></div
            >
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
  import { randomNum } from '@/utils/util';
  import { DeleteOutlined, DownloadOutlined } from '@ant-design/icons-vue';
  import dayjs, { Dayjs } from 'dayjs';
  import { message } from 'ant-design-vue';
  import { findPhotoList, savePhoto } from '@/api/personnel/puser';
  import { uploadLocalFile } from '@/api/common/upload';
  import { findDictionaryList } from '@/api/system/dictionary';
  import { formatDateTZ } from '@/utils/util';
  import axios from 'axios';
  import { Storage } from '@/utils/Storage';
  import { ACCESS_TOKEN_KEY } from '@/enums/cacheEnum';
  
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
  const formRef = reactive({
    id: '',
    dataList: [],
  });
  const dateFormat = 'y-m-d';
  const degree_data = ref([]);
  const photo_data = ref([]);
  const idcard_data = ref([]);
  onMounted(async () => {
    const eduListRes: any = await findPhotoList({ user_id: props.data.id });
    if (eduListRes.data.length > 0) {
      eduListRes.data.forEach((item) => {
        item.start_time = formatDateTZ(item.start_time, dateFormat);
        item.end_time = formatDateTZ(item.end_time, dateFormat);
      });
      formRef.dataList = eduListRes.data;
    }
  });


  const baseUrl = import.meta.env.VITE_BASE_API;
  const download = (item) => {
    var params = {
      file_path: item.path,
      name: '证书',
    };
    axios
      .get(
        baseUrl + 'upload/downloadFile',
        {
          params: params,
          responseType: 'blob', //首先设置responseType字段格式为 blob
          headers: {
            Authorization: 'Bearer ' + Storage.get(ACCESS_TOKEN_KEY),
          },
        },
      )
      .then((res: any) => {
        let blob = new Blob([res.data]); // 为blob设置文件类型
        let url = window.URL.createObjectURL(blob); // 创建一个临时的url指向blob对象
        let a = document.createElement('a');
        a.href = url;
        a.download = '证书.png';
        a.click();
        // 释放这个临时的对象url
        window.URL.revokeObjectURL(url);
      });
  };
  
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
