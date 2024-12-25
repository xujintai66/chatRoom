<script setup>
import { ref, onMounted, defineProps, computed } from 'vue'
import { useCounterStore } from '@/stores/counter'

const store = useCounterStore()
const change1 = ref(null)
const change2 = ref(null)
const change3 = ref(null)
const type = ref(0)

const props = defineProps({
  message: String,
  username: String,
  date: Number,
})

onMounted(() => {
  if (store.username === props.username) {
    type.value = 1
  }
  if (type.value === 1) {
    change1.value.classList.add('isUser')
    change2.value.classList.add('activeAdd')
    change3.value.classList.add('activeMessage')
  }
})

const formattedDate = computed(() => {
  const date = new Date(props.date)
  const month = ('0' + (date.getMonth() + 1)).slice(-2)
  const day = ('0' + date.getDate()).slice(-2)
  const hours = ('0' + date.getHours()).slice(-2)
  const minutes = ('0' + date.getMinutes()).slice(-2)
  const seconds = ('0' + date.getSeconds()).slice(-2)
  return `${month}月${day}日 ${hours}:${minutes}:${seconds}`
})
</script>

<template>
  <div class="MessageContainer" ref="change1">
    <div class="Messageleft" ref="change2">
      <div class="MessageTop">
        <div class="name">{{ props.username }}</div>
        <div class="time">{{ formattedDate }}</div>
      </div>
      <div class="mainMessage" ref="change3">
        {{ props.message }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.isUser {
  display: flex;
  justify-content: flex-end;
}
.MessageContainer {
  margin-bottom: 30px;
  .Messageleft {
    display: flex;
    flex-direction: column;
    .MessageTop {
      display: flex;
      align-items: center;
      .name {
        font-size: 26px;
      }
      .time {
        color: #ccc;
        margin-left: 10px;
        font-size: 14px;
      }
    }
    .mainMessage {
      padding: 15px;
      border: 1px solid #000;
      border-radius: 20px;
      width: fit-content;
      max-width: 300px;
      overflow-wrap: anywhere;
    }
  }
  .activeAdd {
    align-items: flex-end;
  }
  .activeMessage {
    background-color: #99ff00;
  }
}
</style>
