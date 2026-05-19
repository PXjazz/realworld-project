import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  const tags = ref<string[]>([])

  return { tags }
})
