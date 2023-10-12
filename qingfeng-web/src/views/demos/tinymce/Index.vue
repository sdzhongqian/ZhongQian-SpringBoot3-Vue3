<template>
    <div>
      <editor v-model="content"></editor>
    </div>
</template>

<script lang="ts">
import Editor from "@/components/tinymce/Index.vue";
import { defineComponent,ref } from "vue";

export default defineComponent({
  components: {
    Editor,
  },
  setup() {
    const content = ref("");
    return {
      content,
    };
  },
});
</script>

<style>
</style>