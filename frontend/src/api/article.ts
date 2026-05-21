import request from './request'
import type { MultipleArticlesResponse } from '@/types'

export interface ArticleQueryParams {
  tag?: string
  author?: string
  favorited?: string
  limit?: number
  offset?: number
}

export const articleApi = {
  getArticles(params?: ArticleQueryParams) {
    return request.get<MultipleArticlesResponse>('/articles', { params })
  }
}
