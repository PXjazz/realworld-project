import request from './request'
import type { Profile, MultipleArticlesResponse } from '@/types'

export interface ProfileResponse {
  profile: Profile
}

export const profileApi = {
  getProfile(username: string) {
    return request.get<ProfileResponse>(`/profiles/${username}`)
  },

  follow(username: string) {
    return request.post<ProfileResponse>(`/profiles/${username}/follow`)
  },

  unfollow(username: string) {
    return request.delete<ProfileResponse>(`/profiles/${username}/follow`)
  },

  getUserArticles(author: string, params?: { limit?: number; offset?: number }) {
    return request.get<MultipleArticlesResponse>('/articles', { params: { author, ...params } })
  },

  getFavoritedArticles(favorited: string, params?: { limit?: number; offset?: number }) {
    return request.get<MultipleArticlesResponse>('/articles', { params: { favorited, ...params } })
  }
}