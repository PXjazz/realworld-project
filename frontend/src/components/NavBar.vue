<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>

<template>
  <nav class="navbar navbar-light">
    <div class="container">
      <RouterLink class="navbar-brand" to="/">conduit</RouterLink>
      <ul class="nav navbar-nav pull-xs-right">
        <li class="nav-item">
          <RouterLink class="nav-link" active-class="active" to="/">Home</RouterLink>
        </li>
        <template v-if="userStore.isLoggedIn">
          <li class="nav-item">
            <RouterLink class="nav-link" active-class="active" to="/editor">
              <i class="ion-compose"></i>&nbsp;New Article
            </RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" active-class="active" to="/settings">
              <i class="ion-gear-a"></i>&nbsp;Settings
            </RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink
              class="nav-link"
              active-class="active"
              :to="`/profile/${userStore.currentUser?.username}`"
            >
              {{ userStore.currentUser?.username }}
            </RouterLink>
          </li>
        </template>
        <template v-else>
          <li class="nav-item">
            <RouterLink class="nav-link" active-class="active" to="/login">Sign in</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" active-class="active" to="/register">Sign up</RouterLink>
          </li>
        </template>
      </ul>
    </div>
  </nav>
</template>
