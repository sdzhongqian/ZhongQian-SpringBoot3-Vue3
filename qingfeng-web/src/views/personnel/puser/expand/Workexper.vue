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
            <th style="width: 80px">操作</th>
          </tr>
        </thead>
        <tbody class="ant-table-tbody">
          <tr v-for="(item, index) in formRef.dataList" :key="index">
            <td>
              <a-form-item
                label=""
                :name="['dataList', index, 'name']"
                :rules="{
                  required: true,
                  message: '请输入公司名称',
                }"
              >
                <a-input v-model:value="item.name" placeholder="公司名称" />
              </a-form-item>
            </td>
            <td>
              <a-form-item
                label=""
                :name="['dataList', index, 'position']"
                :rules="{
                  required: true,
                  message: '请输入任职职位',
                }"
              >
                <a-input v-model:value="item.position" placeholder="任职职位" />
              </a-form-item>
            </td>
            <td>
              <a-form-item
                label=""
                :name="['dataList', index, 'start_time']"
                :rules="{
                  required: true,
                  message: '请选择开始日期',
                }"
              >
                <a-date-picker
                  v-model:value="item.start_time"
                  type="date"
                  placeholder="请选择开始日期"
                  style="width: 100%"
                />
              </a-form-item>
            </td>
            <td>
              <a-form-item
                label=""
                :name="['dataList', index, 'end_time']"
                :rules="{
                  required: true,
                  message: '请选择结束日期',
                }"
              >
                <a-date-picker
                  v-model:value="item.end_time"
                  type="date"
                  placeholder="请选择结束日期"
                  style="width: 100%"
                />
              </a-form-item>
            </td>
            <td>
              <a-form-item
                label=""
                :name="['dataList', index, 'link_user']"
                :rules="{
                  required: true,
                  message: '请输入联系人',
                }"
              >
                <a-input v-model:value="item.link_user" placeholder="联系人" />
              </a-form-item>
            </td>
            <td>
              <a-form-item
                label=""
                :name="['dataList', index, 'link_phone']"
                :rules="{
                  required: true,
                  message: '请输入联系电话',
                }"
              >
                <a-input v-model:value="item.link_phone" placeholder="联系电话" />
              </a-form-item>
            </td>
            <td>
              <a-form-item
                label=""
                :name="['dataList', index, 'leave_reason']"
                :rules="{
                  required: true,
                  message: '请输入离职理由',
                }"
              >
                <a-input v-model:value="item.leave_reason" placeholder="离职理由" />
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
    import { updateAuth, findRoleAuth, findOrganizeAuth } from '@/api/system/user';
    import { randomNum } from '@/utils/util';
    import { DeleteOutlined } from '@ant-design/icons-vue';
    import dayjs, { Dayjs } from 'dayjs';
    import { message } from "ant-design-vue";
    import { findWorkexperList, saveWorkexper } from '@/api/personnel/puser';
  
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
    const dateFormat = 'YYYY-MM-DD';
    const degree_data = ref([]);
    const workexper_data = ref([]);
    onMounted(async () => {
      const eduListRes: any = await findWorkexperList({ user_id: props.data.id });
      if (eduListRes.data.length > 0) {
        eduListRes.data.forEach((item) => {
          item.start_time = dayjs(item.start_time, dateFormat);
          item.end_time = dayjs(item.end_time, dateFormat);
        });
        formRef.dataList = eduListRes.data;
      } else {
        addLine({});
      }
    });
  
    const addLine = (item) => {
      formRef.dataList.push({
        name: '',
        position: '',
        start_time: '',
        end_time: '',
        link_user: '',
        link_phone: '',
        leave_reason:'',
        key: randomNum(24, 16),
      });
    };
  
    const del = (item) => {
      formRef.dataList = formRef.dataList.filter((data) => data.key != item.key);
    };
  
    const handleFinish = async (values) => {
      const res = await saveWorkexper({ workexperList: values.dataList, id: props.data.id });
      if (res.success) {
        message.success(res.msg);
      } else {
        message.error(res.msg);
      }
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
  