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
      component: () => import('@/views/Editor.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/editor/:slug',
      name: 'editor-edit',
      component: () => import('@/views/Editor.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/settings',
      name: 'settings',
      component: () => import('@/views/Settings.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/profile/:username',
      name: 'profile',
      component: () => import('@/views/Profile.vue')
    }
  ]
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    return { name: 'login' }
  }
  if ((to.name === 'login' || to.name === 'register') && token) {
    return { name: 'home' }
  }
})

export default router
