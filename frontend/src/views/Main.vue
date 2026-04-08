<template>
  <div class="page-container">
    <iframe :src="smwpUrl" frameborder="0" class="smwp-iframe"></iframe>
  </div>
</template>

<script>
export default {
  name: 'Main',
  data() {
    return {
      smwpUrl: 'http://192.168.203.84:11005/?Pro=project_250918_19#main'
    }
  }
}
</script>

<style scoped>
.page-container {
  height: calc(100vh - 50px); /* 헤더가 50px이라고 가정 */
  width: 100vw;
  display: flex;
  flex-direction: column;
  margin: 0;
  padding: 0;
  overflow: hidden;
}

.smwp-iframe {
  width: 100%;
  height: 100%;
  border: none;
  flex: 1; /* 남은 공간을 모두 차지 */
}
</style>