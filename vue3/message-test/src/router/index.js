import { createRouter, createWebHistory } from 'vue-router'
import { useCounterStore } from '@/stores/counter'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: () => import('@/views/indexPage.vue') },
    { path: '/login', component: () => import('@/views/loginPage.vue') },
    { path: '/user', component: () => import('@/views/userPage.vue') },
    { path: '/register', component: () => import('@/views/registerPage.vue') },
  ],
})
router.beforeEach((to, from, next) => {
  const counterStore = useCounterStore()
  // 判断用户是否已经登录
  const isLoggedIn = counterStore.count

  if (!isLoggedIn && to.path !== '/login' && to.path !== '/register') {
    next('/login')
  } else {
    next()
  }
})
export default router
