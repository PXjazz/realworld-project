import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(null)
  const currentUser = ref<unknown>(null)

  return { token, currentUser }
})
