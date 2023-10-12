<template>
  <DraggableModal :title="title" width="1000px" v-model:visible="$props.visible">
    <template #footer>
      <a-button @click="onCancel">取消</a-button>
    </template>
    <a-form v-bind="formLayout" align="left">
      <a-upload
        :max-count="1"
        v-model:file-list="fileList"
        name="file"
        action="/api/upload/uploadLocalFile"
        accept=".xlsx"
        :headers="headers"
        @change="handleChange"
      >
        <a-button>
          <upload-outlined></upload-outlined>
          导入用户Excel
        </a-button>
      </a-upload>
      <a-card align="left" v-show="errorData.length > 0">
        失败信息（{{ errorData.length }}）：
        <a-table :pagination="false" :dataSource="errorData" :columns="columns" />
        <div style="margin: 10px auto; color: rgb(0 0 0 / 48%)">
          失败原因：请检查必输项、身份证号、手机号、电子邮箱是否重复！
        </div>
      </a-card>
    </a-form>
  </DraggableModal>
</template>

<script setup lang="tsx">
  import { ref, watch, onMounted, defineEmits, defineExpose } from 'vue';
  import { DraggableModal } from '@/components/core/draggable-modal';
  import { Form, message } from "ant-design-vue";
  import { Storage } from '@/utils/Storage';
  import { ACCESS_TOKEN_KEY } from '@/enums/cacheEnum';
  import { saveImportExcel } from "@/api/system/user";
  defineOptions({
    name: 'Select',
  });
  const title = ref('用户导入');
  const props = defineProps({
    visible: {
      type: Boolean,
      default: false,
    },
    data: {
      type: Object,
      default: {},
    },
  });

  watch(
    () => props.data,
    (newValue, oldValue) => {},
  );

  // 自动请求并暴露内部方法
  onMounted(() => {});

  defineExpose({});
  const emits = defineEmits(['ok','cancel']); //这里暴露父组件自定义的方法
  const onCancel = () => {
    emits('cancel');
  };

  const columns = [
      {
        title: "所属组织",
        dataIndex: "organize_name",
        sorter: true,
        ellipsis: true,
      },
      {
        title: "登录账号",
        dataIndex: "login_name",
        ellipsis: true,
      },
      {
        title: "身份证号",
        dataIndex: "idcard",
        ellipsis: true,
      },
      {
        title: "手机号",
        dataIndex: "phone",
        ellipsis: true,
      },
      {
        title: "邮箱",
        dataIndex: "email",
        ellipsis: true,
      },
      {
        title: "异常原因",
        dataIndex: "content",
        ellipsis: true,
      },
    ];

  const formLayout = {
    labelCol: {
      xs: { span: 24 },
      sm: { span: 6 },
    },
    wrapperCol: {
      xs: { span: 24 },
      sm: { span: 14 },
    },
  };
  const fileList = ref([]);
  const errorData = ref([]);

  //文档资料
  const handleChange = (info) => {
    if (info.file.status !== 'uploading') {
      console.log(info.file, info.fileList);
    }
    if (info.file.status === 'done') {
      let params = {
        file_path: info.file.response.data.file_path,
      };
      saveImportExcel(params).then((response) => {
        if (response.success) {
          message.success(`用户导入成功`);
          emits('cancel');
          emits('ok',"");
        } else {
          message.error(`用户导入失败`);
          errorData.value = response.data;
        }
      });
    } else if (info.file.status === 'error') {
      message.error(`${info.file.name} file upload failed.`);
    }
  };

  const headers = ref({
    Authorization: 'Bearer ' + Storage.get(ACCESS_TOKEN_KEY),
  });
</script>

<style scoped></style>
