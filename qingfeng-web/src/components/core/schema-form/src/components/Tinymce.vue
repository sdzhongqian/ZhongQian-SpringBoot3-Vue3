<template>
  <div>
    <editor v-model="content"></editor>
  </div>
</template>

<script lang="ts" setup>
  import Editor from '@/components/tinymce/Index.vue';
  import { defineComponent, ref,watch,onMounted } from 'vue';
  import inputProps from 'ant-design-vue/es/input/inputProps';
  import { useI18n } from '@/hooks/useI18n';

  defineOptions({
    name: 'Tincymce',
    inheritAttrs: false,
  });

  const content = ref('');

  const props = defineProps({
    ...inputProps(),
  });

  onMounted(() => {
    console.log('----props')
    console.log(props.value)
    content.value = props.value;
});
watch(
    () => props.value,
    (newValue, oldValue) => {
      emit('change', newValue);
      content.value = newValue;
    },
  );

  const emit = defineEmits(['change']);

  watch(
    () => content.value,
    (newValue, oldValue) => {
      emit('change', newValue);
    },
  );


</script>

<style></style>
