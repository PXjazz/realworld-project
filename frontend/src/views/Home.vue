<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { useArticleStore } from '@/stores/article'
import { useAppStore } from '@/stores/app'
import { formatDate } from '@/utils'

const articleStore = useArticleStore()
const appStore = useAppStore()

const activeTab = ref<'global' | 'tag'>('global')
const activeTag = ref('')
const currentPage = ref(1)
const articlesPerPage = 10

async function loadArticles() {
  const params: { limit: number; offset: number; tag?: string } = {
    limit: articlesPerPage,
    offset: (currentPage.value - 1) * articlesPerPage
  }
  if (activeTab.value === 'tag' && activeTag.value) {
    params.tag = activeTag.value
  }
  await articleStore.fetchArticles(params)
}

function selectGlobalFeed() {
  activeTab.value = 'global'
  activeTag.value = ''
  currentPage.value = 1
  loadArticles()
}

function selectTag(tag: string) {
  activeTab.value = 'tag'
  activeTag.value = tag
  currentPage.value = 1
  loadArticles()
}

function goToPage(page: number) {
  currentPage.value = page
  loadArticles()
}

const totalPages = computed(() =>
  Math.ceil(articleStore.articlesCount / articlesPerPage)
)

const pageNumbers = computed(() => {
  const pages: number[] = []
  for (let i = 1; i <= totalPages.value; i++) {
    pages.push(i)
  }
  return pages
})

onMounted(() => {
  loadArticles()
  appStore.fetchTags()
})
</script>

<template>
  <div class="home-page">
    <!-- Banner -->
    <div class="banner">
      <div class="container">
        <h1 class="logo-font">conduit</h1>
        <p>A place to share your knowledge.</p>
      </div>
    </div>

    <div class="container page">
      <div class="row">
        <!-- Main content -->
        <div class="col-md-9">
          <!-- Feed Toggle -->
          <div class="feed-toggle">
            <ul class="nav nav-pills outline-active">
              <li class="nav-item">
                <a
                  class="nav-link"
                  :class="{ active: activeTab === 'global' }"
                  href=""
                  @click.prevent="selectGlobalFeed"
                >Global Feed</a>
              </li>
              <li v-if="activeTab === 'tag' && activeTag" class="nav-item">
                <a
                  class="nav-link active"
                  href=""
                  @click.prevent="selectTag(activeTag)"
                >#{{ activeTag }}</a>
              </li>
            </ul>
          </div>

          <!-- Article List -->
          <div v-if="articleStore.loading" class="article-preview">Loading articles...</div>

          <div v-else-if="articleStore.articles.length === 0" class="article-preview">
            No articles are here... yet.
          </div>

          <template v-else>
            <div v-for="article in articleStore.articles" :key="article.slug" class="article-preview">
              <div class="article-meta">
                <a :href="'/profile/' + article.author.username">
                  <img :src="article.author.image || 'https://api.realworld.io/images/smiley-cyrus.jpeg'" />
                </a>
                <div class="info">
                  <a :href="'/profile/' + article.author.username" class="author">
                    {{ article.author.username }}
                  </a>
                  <span class="date">{{ formatDate(article.createdAt) }}</span>
                </div>
                <button class="btn btn-outline-primary btn-sm pull-xs-right">
                  <i class="ion-heart"></i> {{ article.favoritesCount }}
                </button>
              </div>
              <a :href="'/article/' + article.slug" class="preview-link">
                <h1>{{ article.title }}</h1>
                <p>{{ article.description }}</p>
                <span>Read more...</span>
              </a>
              <ul class="tag-list">
                <li v-for="tag in article.tagList" :key="tag" class="tag-default tag-pill tag-outline">
                  {{ tag }}
                </li>
              </ul>
            </div>
          </template>

          <!-- Pagination -->
          <nav v-if="totalPages > 1">
            <ul class="pagination">
              <li
                v-for="page in pageNumbers"
                :key="page"
                class="page-item"
                :class="{ active: page === currentPage }"
              >
                <a
                  class="page-link"
                  href=""
                  @click.prevent="goToPage(page)"
                >{{ page }}</a>
              </li>
            </ul>
          </nav>
        </div>

        <!-- Sidebar -->
        <div class="col-md-3">
          <div class="sidebar">
            <p>Popular Tags</p>
            <div v-if="appStore.loading" class="tag-default">Loading tags...</div>
            <template v-else>
              <a
                v-for="tag in appStore.tags"
                :key="tag"
                class="tag-default tag-pill"
                href=""
                @click.prevent="selectTag(tag)"
              >{{ tag }}</a>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
