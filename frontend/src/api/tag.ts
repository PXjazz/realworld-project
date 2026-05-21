import request from './request'
import type { TagsResponse } from '@/types'

export const tagApi = {
  getTags() {
    return request.get<TagsResponse>('/tags')
  }
}
