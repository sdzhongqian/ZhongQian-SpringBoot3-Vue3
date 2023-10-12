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
            <a-form-item
              label=""
              :name="['dataList', index, 'idcard']"
              :rules="{
                required: true,
                message: '请选择证件照类型',
              }"
            >
              <a-select
                show-search
                v-model:value="item.idcard"
                placeholder="请选择证件照类型"
                :filter-option="filterOption"
                :options="idcard_data"
              >
              </a-select>
            </a-form-item>
          </td>
          <td>
            <a-form-item
              label=""
              :name="['dataList', index, 'code']"
              :rules="{
                required: false,
                message: '请输入证件编号',
              }"
            >
              <a-input v-model:value="item.code" placeholder="证件编号" />
            </a-form-item>
          </td>
          <td>
            <a-form-item
              label=""
              :name="['dataList', index, 'start_time']"
              :rules="{
                required: false,
                message: '请选择开始时间',
              }"
            >
              <a-date-picker
                v-model:value="item.start_time"
                type="date"
                placeholder="请选择开始时间"
                style="width: 100%"
              />
            </a-form-item>
          </td>
          <td>
            <a-form-item
              label=""
              :name="['dataList', index, 'end_time']"
              :rules="{
                required: false,
                message: '请选择结束时间',
              }"
            >
              <a-date-picker
                v-model:value="item.end_time"
                type="date"
                placeholder="请选择结束时间"
                style="width: 100%"
              />
            </a-form-item>
          </td>
          <td>
            <a-form-item
              label=""
              :name="['dataList', index, 'cert_unit']"
              :rules="{
                required: false,
                message: '请输入认证机构',
              }"
            >
              <a-input v-model:value="item.cert_unit" placeholder="认证机构" />
            </a-form-item>
          </td>
          <td>
            <a-form-item
              label=""
              :name="['dataList', index, 'path']"
              :rules="{
                required: false,
                message: '请上传证件照',
              }"
            >
              <a-upload
                v-model:file-list="item.fileList"
                name="file"
                list-type="picture-card"
                class="avatar-uploader"
                accept="image/*"
                :show-upload-list="false"
                :customRequest="(e) => uploadImage(e, item)"
                :before-upload="beforeUpload"
                @change="(e) => handleChange(e, item)"
              >
                <img v-if="item.imageUrl" width="100" :src="item.imageUrl" alt="avatar" />
                <div v-else>
                  <loading-outlined v-if="loading"></loading-outlined>
                  <plus-outlined v-else></plus-outlined>
                  <div class="ant-upload-text">上传</div>
                </div>
              </a-upload>
            </a-form-item>
          </td>
          <td>
            <a-popconfirm
              placement="top"
              ok-text="确定"
              cancel-text="取消"
              @click.stop=""
              @confirm="del(item)"
            >
              <template #title>
                <p>确定删除吗？</p>
              </template>
              <span style="color: red; cursor: pointer"><DeleteOutlined />删除</span>
            </a-popconfirm>
            <div style="margin-top: 10px"
              ><a v-if="item.path != ''" @click="download(item)"><DownloadOutlined />下载</a></div
            >
            <!-- <a @click="copyLine(item, citem, sitem)"
                              ><copy-outlined
                            /></a> -->
          </td>
        </tr>
        <tr>
          <td colspan="8">
            <a-button style="width: 100%" type="dashed" @click="addLine(item)"
              ><plus-outlined />添加</a-button
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
          <a-button type="primary" html-type="submit">确定</a-button>
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
  const dateFormat = 'YYYY-MM-DD';
  const degree_data = ref([]);
  const photo_data = ref([]);
  const idcard_data = ref([]);
  onMounted(async () => {
    const eduListRes: any = await findPhotoList({ user_id: props.data.id });
    if (eduListRes.data.length > 0) {
      eduListRes.data.forEach((item) => {
        item.start_time = dayjs(item.start_time, dateFormat);
        item.end_time = dayjs(item.end_time, dateFormat);
      });
      formRef.dataList = eduListRes.data;
    } else {
      addLine({});
    }

    const res: any = await findDictionaryList({ parent_code: 'idcard' });
    idcard_data.value = res.data;
  });

  const addLine = (item) => {
    formRef.dataList.push({
      idcard: '',
      code: '',
      start_time: '',
      end_time: '',
      cert_unit: '',
      path: '',
      key: randomNum(24, 16),
    });
  };

  const del = (item) => {
    formRef.dataList = formRef.dataList.filter((data) => data.key != item.key);
  };

  const handleFinish = async (values) => {
    const res = await savePhoto({ photoList: values.dataList, id: props.data.id });
    if (res.success) {
      message.success(res.msg);
    } else {
      message.error(res.msg);
    }
  };

  //附件上传
  const loading = ref<boolean>(false);
  const beforeUpload = (file) => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      message.error('请上传正确的图片格式【jepg/png】');
    }
    const isLt2M = file.size / 1024 / 1024 < 10;

    if (!isLt2M) {
      message.error('上传图片大小不能大于10MB');
    }
    return isJpgOrPng && isLt2M;
  };
  // 上传图片
  const uploadImage = (file, item) => {
    if (file.file.status === 'uploading') {
      loading.value = true;
      return;
    }
    const formData = new FormData();
    formData.append('file', file.file);
    formData.append('source', 'photo');
    uploadLocalFile(formData).then((res) => {
      if (res.success) {
        loading.value = false;
        item.imageUrl = res.data.show_path;
        item.path = res.data.file_path;
      }
    });
  };

  const handleChange = (info, item) => {
    if (info.file.status === 'uploading') {
      loading.value = true;
      return;
    }
    if (info.file.status === 'done') {
      // Get this url from response in real world.
      getBase64(info.file.originFileObj, (imageUrl) => {
        loading.value = false;
        item.imageUrl = imageUrl;
      });
    }
  };

  function getBase64(img: Blob, callback: (base64Url: string) => void) {
    const reader = new FileReader();
    reader.addEventListener('load', () => callback(reader.result as string));
    reader.readAsDataURL(img);
  }

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
