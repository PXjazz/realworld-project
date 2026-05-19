import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useArticleStore = defineStore('article', () => {
  const articles = ref<unknown[]>([])
  const currentArticle = ref<unknown>(null)

  return { articles, currentArticle }
})
