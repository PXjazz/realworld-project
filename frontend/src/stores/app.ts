import { defineStore } from 'pinia'
import { ref } from 'vue'
import { tagApi } from '@/api/tag'

export const useAppStore = defineStore('app', () => {
  const tags = ref<string[]>([])
  const loading = ref(false)

  async function fetchTags() {
    loading.value = true
    try {
      const res = await tagApi.getTags()
      tags.value = res.data.tags
    } finally {
      loading.value = false
    }
  }

  return { tags, loading, fetchTags }
})
