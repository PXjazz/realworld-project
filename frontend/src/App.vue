<script setup lang="ts">
import { onMounted } from 'vue'
import NavBar from '@/components/NavBar.vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

onMounted(() => {
  if (localStorage.getItem('token')) {
    userStore.fetchCurrentUser()
  }
})
</script>

<template>
  <NavBar />
  <router-view />
</template>
