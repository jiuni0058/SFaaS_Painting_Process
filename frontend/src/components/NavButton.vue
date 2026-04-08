<template>
  <router-link :to="to" class="nav-button">{{ text }}</router-link>
</template>

<script>
export default {
  props: {
    text: String,
    to: String
  }
}
</script>

<style scoped>
.nav-button {
  padding: 8px 12px;
  border-radius: 6px;
  text-decoration: none;
  color: #333;
  transition: background 0.2s;
}
.nav-button:hover {
  background: #f0f0f0;
}
</style>
