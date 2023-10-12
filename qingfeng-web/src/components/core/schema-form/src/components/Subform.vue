<template>
  <div>
    <table class="ant-table">
      <thead class="ant-table-thead">
        <tr>
          <th v-for="(item, index) in titles">{{ item }}</th>
          <th style="width: 80px">操作</th>
        </tr>
      </thead>
      <tbody class="ant-table-tbody">
        <tr v-for="(item, idx) in dataList">
          <td v-for="(schemaItem, index) in item" :key="schemaItem.field">
            <SchemaFormItem
              v-model:form-model="formModel"
              :schema="schemaItem"
              :table-instance="instance"
              @update="update"
            >
              <template v-for="item in Object.keys($slots)" #[item]="data" :key="item">
                <slot :name="item" v-bind="data || {}"></slot>
              </template>
            </SchemaFormItem>
          </td>
          <td>
            <a style="color: red" @click="delLine(item)"><DeleteOutlined /></a>
          </td>
        </tr>
      </tbody>
    </table>
    <a-button @click="addLine">新增</a-button>
  </div>
</template>

<script lang="ts" setup>
  import { defineComponent, ref, watch, onMounted } from 'vue';
  import inputProps from 'ant-design-vue/es/input/inputProps';
  import SchemaFormItem from '../schema-item.vue';
  import { DeleteOutlined } from '@ant-design/icons-vue';
  defineOptions({
    name: 'Subform',
    inheritAttrs: false,
  });

  const content = ref('');

  const props = defineProps({
    ...inputProps(),
    titles: {
      type: Array,
      default: [],
    },
    cusSchemas: {
      type: Array,
      default: [],
    },
    formModel: {
      type: Object,
      default: {},
    },
    field: {
      type: String,
      default: {},
    },
  });

  const dataList: any = ref([]);

  const titles: any = ref([]);
  const cusSchemas: any = ref([]);
  const formModel = ref({});
  const instance: any = ref({});
  const index = ref(0);
  const indexArr:any = ref([]);
  onMounted(() => {
    titles.value = props.titles;
    cusSchemas.value = props.cusSchemas;
    formModel.value = props.formModel;
    addLine();
  });
  watch(
    () => formModel.value,
    (newValue, oldValue) => {
      console.log(newValue);
    },
  );

  watch(
    () => props,
    (newValue, oldValue) => {},
  );

  const emit = defineEmits(['change']);

  const addLine = () => {
    index.value++;
    let data = JSON.parse(JSON.stringify(cusSchemas.value));
    data.forEach((item) => {
      item.field = index.value + '.' + item.field;
      item.index = index.value
    });
    indexArr.value.push(index.value+"")
    dataList.value.push(data);
  };

  const update = (data) => {
    let arr: any = [];
    console.log(indexArr.value)
    for (const [key, val] of Object.entries(data)) {
        console.log("#:"+key)
        console.log(indexArr.value.includes(key))
      if (typeof val == 'object' && indexArr.value.includes(key)) {
        console.log("进来了")
        arr.push(val);
      }
    }
    data[props.field] = arr;
    emit('change', arr);
  };

  const delLine = (item) => {
    let index = dataList.value.indexOf(item);
    if (index > -1) {
      dataList.value.splice(index, 1);
    }
    index = indexArr.value.indexOf(item[0].index+"")
    if (index > -1) {
        indexArr.value.splice(index, 1);
    }
    formModel.value[props.field] = formModel.value[props.field].filter(itx => itx.index !== item.index);
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
