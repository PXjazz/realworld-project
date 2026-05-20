import request from './request'
import type { LoginRequest, RegisterRequest, UpdateUserRequest, UserResponse } from '@/types/user'

export const userApi = {
  login(data: LoginRequest) {
    return request.post<UserResponse>('/users/login', data)
  },

  register(data: RegisterRequest) {
    return request.post<UserResponse>('/users', data)
  },

  getCurrentUser() {
    return request.get<UserResponse>('/user')
  },

  updateUser(data: UpdateUserRequest) {
    return request.put<UserResponse>('/user', data)
  }
}
