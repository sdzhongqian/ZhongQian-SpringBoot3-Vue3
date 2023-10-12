<template>
  <a-row>
    <a-col :span="20"><a-input v-bind="getProps" /></a-col>
    <a-col :span="4"><a-button @click="showPreview">选择</a-button></a-col>
  </a-row>
  <org-user-select
    :value="list"
    :visible="visible"
    :type="stype"
    :organize_id="organize_id"
    @ok="selectData"
    @cancel="visible = false"
  ></org-user-select>
</template>
<script lang="ts" setup>
  import { ref, watchEffect, computed, unref, watch, onMounted } from 'vue';
  import inputProps from 'ant-design-vue/es/input/inputProps';
  import { useI18n } from '@/hooks/useI18n';
  import OrgUserSelect from '@/views/system/user/Select.vue';

  type OptionsItem = { label: string; value: string; disabled?: boolean };

  defineOptions({
    name: 'ApiSelect',
    inheritAttrs: false,
  });

  const props = defineProps({
    ...inputProps(),
    organize_id: {
      type: String,
      default: '',
    },
  });

  const data = ref({});
  const stype = ref(0);
  const organize_id = ref('');
  onMounted(() => {
    console.log('----props');
    console.log(props);
    data.value = props;
    stype.value = props.type;
    organize_id.value = props.organize_id;
  });

  watch(
    () => props,
    (newValue, oldValue) => {
      console.log('----props222');
      console.log(props);
      stype.value = props.type;
      organize_id.value = props.organize_id;
    },
  );

  const emit = defineEmits(['update:value', 'change']);

  const getProps = computed(() => data.value as Recordable);

  const visible = ref(false);
  const list = ref([]);
  const name = ref([]);
  const showPreview = () => {
    visible.value = true;
  };

  const selectData = (data) => {
    list.value = data;
    name.value = Array.from(list.value, ({ name }) => name);
    visible.value = false;
    console.log(name.value.join(','));
    emit('update:value', name.value.join(','));
    emit('change', list.value);
  };
</script>
