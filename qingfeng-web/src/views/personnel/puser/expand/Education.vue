<template>
  <a-form v-bind="formLayout" :model="formRef" @finish="handleFinish">
    <table class="ant-table">
      <thead class="ant-table-thead">
        <tr>
          <th>学校<span style="color: red">*</span></th>
          <th>专业<span style="color: red">*</span></th>
          <th style="width: 120px">学历<span style="color: red">*</span></th>
          <th style="width: 120px">学位<span style="color: red">*</span></th>
          <th>入学日期<span style="color: red">*</span></th>
          <th>毕业日期<span style="color: red">*</span></th>
          <th style="width: 80px">操作</th>
        </tr>
      </thead>
      <tbody class="ant-table-tbody">
        <tr v-for="(item, index) in formRef.dataList" :key="index">
          <td>
            <a-form-item
              label=""
              :name="['dataList', index, 'school']"
              :rules="{
                required: true,
                message: '请输入学校',
              }"
            >
              <a-input v-model:value="item.school" placeholder="学校" />
            </a-form-item>
          </td>
          <td>
            <a-form-item
              label=""
              :name="['dataList', index, 'special']"
              :rules="{
                required: true,
                message: '请输入专业',
              }"
            >
              <a-input v-model:value="item.special" placeholder="专业" />
            </a-form-item>
          </td>
          <td>
            <a-form-item
              label=""
              :name="['dataList', index, 'education']"
              :rules="{
                required: true,
                message: '请选择学历',
              }"
            >
              <a-select
                show-search
                v-model:value="item.education"
                placeholder="请选择学历"
                :filter-option="filterOption"
                :options="education_data"
              >
              </a-select>
            </a-form-item>
          </td>
          <td>
            <a-form-item
              label=""
              :name="['dataList', index, 'degree']"
              :rules="{
                required: true,
                message: '请选择学位',
              }"
            >
              <a-select
                show-search
                v-model:value="item.degree"
                placeholder="请选择学位"
                :filter-option="filterOption"
                :options="degree_data"
              >
              </a-select>
            </a-form-item>
          </td>
          <td>
            <a-form-item
              label=""
              :name="['dataList', index, 'start_time']"
              :rules="{
                required: true,
                message: '请选择入学日期',
              }"
            >
              <a-date-picker
                v-model:value="item.start_time"
                type="date"
                placeholder="请选择入学日期"
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
                message: '请选择毕业日期',
              }"
            >
              <a-date-picker
                v-model:value="item.end_time"
                type="date"
                placeholder="请选择毕业日期"
                style="width: 100%"
              />
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
          <td colspan="7">
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
  import { findDictionaryList } from '@/api/system/dictionary';
  import { DeleteOutlined } from '@ant-design/icons-vue';
  import dayjs, { Dayjs } from 'dayjs';
  import { message } from "ant-design-vue";
  import { findEducationList, saveEducation } from '@/api/personnel/puser';

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
  const dateFormat = 'YYYY-MM-DD';
  const degree_data = ref([]);
  const education_data = ref([]);
  onMounted(async () => {
    console.log(props.data);
    const eduListRes: any = await findEducationList({ user_id: props.data.id });
    if (eduListRes.data.length > 0) {
      eduListRes.data.forEach((item) => {
        item.start_time = dayjs(item.start_time, dateFormat);
        item.end_time = dayjs(item.end_time, dateFormat);
      });
      formRef.dataList = eduListRes.data;
    } else {
      addLine({});
    }

    const eduRes: any = await findDictionaryList({ parent_code: 'education' });
    education_data.value = eduRes.data;
    const deRes: any = await findDictionaryList({ parent_code: 'degree' });
    degree_data.value = deRes.data;
  });

  const addLine = (item) => {
    formRef.dataList.push({
      school: '',
      special: '',
      education: '',
      degree: '',
      start_time: '',
      end_time: '',
      key: randomNum(24, 16),
    });
  };

  const del = (item) => {
    formRef.dataList = formRef.dataList.filter((data) => data.key != item.key);
  };

  const handleFinish = async (values) => {
    console.log(values);
    const res = await saveEducation({ educationList: values.dataList, id: props.data.id });
    console.log(res)
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
