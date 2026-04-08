<template>
  <div class="side-bar">
    <ul>
      <li v-for="(item, index) in menuItems" :key="index">
        <a href="#" @click.prevent="handleClick(item)">{{ item.icon }} {{ item.label }}</a>
      </li>
    </ul>
  </div>
</template>
<script>
export default {
  name: 'SideBar',
  props: {
    menuItems: {
      type: Array,
      required: true
    }
  },
  methods: {
    handleClick(item) {
      this.$emit('menu-clicked', item); // 부모에게 클릭된 메뉴 전달
    }
  }
}
</script>

<style scoped>
.side-bar {
  position: fixed;
  top: 60px; /* 네비게이션 바 아래에서 시작 */
  left: 0;
  width: 200px;
  height: calc(100vh - 60px); /* 네비게이션 바 높이를 제외한 전체 높이 */
  background-color: white;
  color: black;
  /* 그림자 효과를 다시 추가하여 입체감을 줍니다. */
  box-shadow: 2px 0 5px rgba(0,0,0,0.05); 
  z-index: 1000;
  /* 전체적인 전환 효과를 부드럽게 합니다. */
  transition: all 0.3s ease; 
}

.side-bar ul {
  list-style: none;
  padding: 10px 0; /* 상하 패딩을 추가하여 메뉴 간의 여유 공간을 확보합니다. */
  margin: 0;
}

.side-bar li {
  /* li 태그 자체에는 패딩을 제거하고 a 태그에서 처리합니다. */
  padding: 0;
}

.side-bar a {
  color: #333; /* 완전한 검정색보다 부드러운 색상을 사용합니다. */
  text-decoration: none;
  display: block;
  padding: 18px 25px; /* 내부 여백을 조정하여 클릭 영역을 넓힙니다. */
  transition: all 0.3s ease; /* 모든 효과에 부드러운 전환을 적용합니다. */
  font-weight: 500; /* 폰트 두께를 약간 추가합니다. */
  border-left: 3px solid transparent; /* 호버 시 나타날 테두리 공간을 미리 확보합니다. */
}

/* 마우스를 올렸을 때의 스타일 */
.side-bar a:hover {
  background-color: #f0f5ff; /* 은은한 푸른 계열의 배경색으로 변경합니다. */
  color: #0056b3; /* 텍스트 색상을 강조합니다. */
  border-left: 3px solid #0056b3; /* 왼쪽에 세로 바를 표시하여 활성화된 느낌을 줍니다. */
}
</style>