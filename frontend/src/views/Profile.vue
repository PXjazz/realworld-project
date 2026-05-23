<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { profileApi } from '@/api/profile'
import { useUserStore } from '@/stores/user'
import { formatDate } from '@/utils'
import type { Profile, Article } from '@/types'

const route = useRoute()
const userStore = useUserStore()

const profile = ref<Profile | null>(null)
const articles = ref<Article[]>([])
const loading = ref(false)
const articlesLoading = ref(false)
const activeTab = ref<'my-posts' | 'favorited'>('my-posts')
const error = ref('')

const username = computed(() => route.params.username as string)
const isMyProfile = computed(() => userStore.currentUser?.username === username.value)
const isLoggedIn = computed(() => userStore.isLoggedIn)

async function fetchProfile() {
  loading.value = true
  error.value = ''
  try {
    const res = await profileApi.getProfile(username.value)
    profile.value = res.data.profile
  } catch (e) {
    error.value = 'Failed to load profile'
  } finally {
    loading.value = false
  }
}

async function fetchArticles() {
  articlesLoading.value = true
  try {
    const res = activeTab.value === 'my-posts'
      ? await profileApi.getUserArticles(username.value)
      : await profileApi.getFavoritedArticles(username.value)
    articles.value = res.data.articles
  } catch {
    error.value = 'Failed to load articles'
  } finally {
    articlesLoading.value = false
  }
}

async function toggleFollow() {
  if (!isLoggedIn.value) return
  if (!profile.value) return

  try {
    if (profile.value.following) {
      const res = await profileApi.unfollow(username.value)
      profile.value = res.data.profile
    } else {
      const res = await profileApi.follow(username.value)
      profile.value = res.data.profile
    }
  } catch {
    error.value = 'Failed to update follow status'
  }
}

function switchTab(tab: 'my-posts' | 'favorited') {
  activeTab.value = tab
  fetchArticles()
}

onMounted(() => {
  fetchProfile()
  fetchArticles()
})
</script>

<template>
  <div class="profile-page">
    <div v-if="loading" class="container page">Loading...</div>

    <div v-else-if="error" class="container page">{{ error }}</div>

    <template v-else-if="profile">
      <div class="user-info">
        <div class="container">
          <div class="row">
            <div class="col-md-10 col-md-offset-1">
              <img
                :src="profile.image || 'https://api.realworld.io/images/smiley-cyrus.jpg'"
                class="user-img"
              />
              <h4>{{ profile.username }}</h4>
              <p>{{ profile.bio }}</p>
              <button
                v-if="!isMyProfile && isLoggedIn"
                class="btn btn-sm btn-outline-secondary action-btn"
                :class="{ 'btn-secondary': profile.following }"
                @click="toggleFollow"
              >
                <i class="ion-plus-round"></i>
                &nbsp;
                {{ profile.following ? 'Unfollow' : 'Follow' }} {{ profile.username }}
              </button>
              <RouterLink
                v-if="isMyProfile"
                to="/settings"
                class="btn btn-sm btn-outline-secondary action-btn"
              >
                <i class="ion-gear-a"></i>
                &nbsp;Edit Profile Settings
              </RouterLink>
            </div>
          </div>
        </div>
      </div>

      <div class="container">
        <div class="row">
          <div class="col-md-10 col-md-offset-1">
            <div class="posts-toggle">
              <ul class="nav nav-pills outline-active">
                <li class="nav-item">
                  <a
                    class="nav-link"
                    :class="{ active: activeTab === 'my-posts' }"
                    href=""
                    @click.prevent="switchTab('my-posts')"
                  >
                    My Posts
                  </a>
                </li>
                <li class="nav-item">
                  <a
                    class="nav-link"
                    :class="{ active: activeTab === 'favorited' }"
                    href=""
                    @click.prevent="switchTab('favorited')"
                  >
                    Favorited Posts
                  </a>
                </li>
              </ul>
            </div>

            <div v-if="articlesLoading" class="article-preview">Loading articles...</div>

            <div v-else-if="articles.length === 0" class="article-preview">
              No articles are here... yet.
            </div>

            <template v-else>
              <div v-for="article in articles" :key="article.slug" class="article-preview">
                <div class="article-meta">
                  <RouterLink :to="`/profile/${article.author.username}`">
                    <img :src="article.author.image || 'https://api.realworld.io/images/smiley-cyrus.jpg'" />
                  </RouterLink>
                  <div class="info">
                    <RouterLink :to="`/profile/${article.author.username}`" class="author">
                      {{ article.author.username }}
                    </RouterLink>
                    <span class="date">{{ formatDate(article.createdAt) }}</span>
                  </div>
                  <button class="btn btn-outline-primary btn-sm pull-xs-right">
                    <i class="ion-heart"></i> {{ article.favoritesCount }}
                  </button>
                </div>
                <RouterLink :to="`/article/${article.slug}`" class="preview-link">
                  <h1>{{ article.title }}</h1>
                  <p>{{ article.description }}</p>
                  <span>Read more...</span>
                </RouterLink>
              </div>
            </template>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>