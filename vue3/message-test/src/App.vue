<script setup>
import { RouterView } from 'vue-router'
import { useCounterStore } from './stores/counter'
import { onMounted } from 'vue'
const st = useCounterStore()
onMounted(() => {
  console.log(st.count)
})
</script>

<template>
  <RouterView />
</template>

<style scoped></style>
