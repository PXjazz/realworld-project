import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/Home.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Register.vue')
    },
    {
      path: '/article/:slug',
      name: 'article',
      component: () => import('@/views/Article.vue')
    },
    {
      path: '/editor',
      name: 'editor',
      component: () => import('@/views/Editor.vue')
    },
    {
      path: '/editor/:slug',
      name: 'editor-edit',
      component: () => import('@/views/Editor.vue')
    },
    {
      path: '/settings',
      name: 'settings',
      component: () => import('@/views/Settings.vue')
    },
    {
      path: '/profile/:username',
      name: 'profile',
      component: () => import('@/views/Profile.vue')
    }
  ]
})

export default router
