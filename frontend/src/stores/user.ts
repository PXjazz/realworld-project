import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { userApi } from '@/api/user'
import type { User, LoginRequest, RegisterRequest, UpdateUserRequest } from '@/types/user'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const currentUser = ref<User | null>(null)
  const loading = ref(false)

  const token = computed(() => currentUser.value?.token ?? null)
  const isLoggedIn = computed(() => !!currentUser.value)

  function setUser(user: User) {
    currentUser.value = user
    localStorage.setItem('token', user.token)
  }

  function clearUser() {
    currentUser.value = null
    localStorage.removeItem('token')
  }

  async function login(data: LoginRequest) {
    loading.value = true
    try {
      const res = await userApi.login(data)
      setUser(res.data.user)
      router.push('/')
    } finally {
      loading.value = false
    }
  }

  async function register(data: RegisterRequest) {
    loading.value = true
    try {
      const res = await userApi.register(data)
      setUser(res.data.user)
      router.push('/')
    } finally {
      loading.value = false
    }
  }

  async function fetchCurrentUser() {
    try {
      const res = await userApi.getCurrentUser()
      setUser(res.data.user)
    } catch {
      clearUser()
    }
  }

  async function updateUser(data: UpdateUserRequest) {
    const res = await userApi.updateUser(data)
    setUser(res.data.user)
    return res.data.user
  }

  function logout() {
    clearUser()
    router.push('/')
  }

  return { currentUser, token, isLoggedIn, loading, login, register, fetchCurrentUser, updateUser, logout, clearUser }
})
