import { defineStore } from 'pinia'
import { ref } from 'vue'
import { articleApi, type ArticleQueryParams } from '@/api/article'
import type { Article } from '@/types'

export const useArticleStore = defineStore('article', () => {
  const articles = ref<Article[]>([])
  const articlesCount = ref(0)
  const loading = ref(false)

  async function fetchArticles(params?: ArticleQueryParams) {
    loading.value = true
    try {
      const res = await articleApi.getArticles(params)
      articles.value = res.data.articles
      articlesCount.value = res.data.articlesCount
    } finally {
      loading.value = false
    }
  }

  return { articles, articlesCount, loading, fetchArticles }
})
