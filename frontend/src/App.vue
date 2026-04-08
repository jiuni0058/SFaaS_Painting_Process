<script setup>
import { RouterLink, RouterView, useRoute } from 'vue-router'
// import HelloWorld from './components/HelloWorld.vue'
import HeaderBar from './components/HeaderBar.vue'

const route = useRoute()
</script>

<template>
  
  <main class="main-content">
    <HeaderBar v-if="route.meta.showHeader"/>
    <router-view />
  </main>
  <!-- <RouterView /> -->
</template>

<style scoped>
.main-content {
  margin-top: 60px; /* 헤더 높이만큼 여백 */
  width : 100vw;
  height : calc(100vh - 60px);
}

</style>
