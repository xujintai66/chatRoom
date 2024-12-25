import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore(
  'counter',
  () => {
    const count = ref(0)
    const users = ref({})
    const username = ref('')
    const Volume = ref(0.5)
    const isLook = ref(0)

    return { count, username, users, Volume, isLook }
  },
  {
    persist: {
      key: 'chat',
      path: ['Volume', 'count'],
    },
  },
)
