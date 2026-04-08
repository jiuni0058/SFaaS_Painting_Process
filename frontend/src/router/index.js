import { createRouter, createWebHistory } from 'vue-router'
import Main from '@/views/Main.vue'
import Detail from '@/views/Detail.vue'
import Settings from '@/views/Settings.vue'
import Downtime from '@/views/Downtime.vue'
import Defect from '@/views/Defect.vue'
import Login from '@/views/Login.vue'
import SignUp from '@/views/SignUpForm.vue'

const routes = [
  { path: '/', component: Login, meta: { showHeader: false } },
  { path: '/signup', component: SignUp, meta: { showHeader: false } },
  { path: '/main', component: Main, meta: { showHeader: true } },
  { path: '/detail', component: Detail, meta: { showHeader: true } },
  { path: '/settings', component: Settings, meta: { showHeader: true } },
  { path: '/downtime', component: Downtime, meta: { showHeader: true } },
  { path: '/defect', component: Defect, meta: { showHeader: true } },

]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 모든 라우트 이동 전에 체크
router.beforeEach((to, from, next) => {
  const accountName = sessionStorage.getItem("accountName");

  // 로그인 화면으로 가는 경우는 허용
  if (to.path === "/" || to.path === "/signup") {
    return next();
  }

  // 세션에 계정 정보 없으면 로그인 화면으로 리다이렉트
  if (!accountName) {
    alert('로그인 정보가 만료되었거나 존재하지 않습니다. 로그인해주세요')
    return next("/");
  }

  // 세션 정보가 있으면 정상적으로 이동
  next();
});

export default router
