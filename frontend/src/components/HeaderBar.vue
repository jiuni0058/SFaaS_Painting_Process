<template>
  <header class="header">
    <h1 class="logo">TETOEVER</h1>
    <nav class="nav">
      <div class="menu-left">
        <NavButton text="Main" to="/main" />
        <NavButton text="Detail" to="/detail" />
        <NavButton text="Defect" to="/defect" />
        <NavButton text="Downtime" to="/downtime" />
      </div>
      <div class="menu-right">
        <span class="username">{{ username }}님</span>
        <NavButton text="MyPage" to="/signup" />
        <NavButton text="Logout" to="/" />
        <div class="hamburger" @click="toggleChat">
                <span></span>
                <span></span>
                <span></span>
              </div>
      </div>
    </nav>
     <!-- 채팅창 슬라이드 -->
    <div class="chat-panel" :class="{ open: chatOpen }">
      <h3>정보공유방</h3>
      <div class="chat-messages">
        <div v-for="(msg, index) in messages" :key="index">{{ msg }}</div>
      </div>
      <input v-model="inputMsg" @keyup.enter="sendMessage" placeholder="메시지를 입력하세요" />
      <button @click="sendMessage">전송</button>
    </div>
  </header>
</template>

<script>
import NavButton from './NavButton.vue'
import { ref, onMounted, nextTick, watch } from 'vue'
import { io } from 'socket.io-client'

export default {
  components: { NavButton },
  setup() {
    const username = ref('');
    const chatOpen = ref(false);
    const socket = io('http://192.168.203.68:3000');
    const messages = ref([]);
    const inputMsg = ref('');

    // 예시: 고정 방 이름
    const roomName = 'defaultRoom';

    // 채팅 메시지 컨테이너에 대한 ref
    const chatMessagesRef = ref(null);

    // 스크롤을 맨 아래로 이동시키는 함수
    const scrollToBottom = () => {
      nextTick(() => {
        const chatContainer = document.querySelector('.chat-messages');
        if (chatContainer) {
          chatContainer.scrollTop = chatContainer.scrollHeight;
        }
      });
    };

    onMounted(() => {
      username.value = sessionStorage.getItem("accountName") || '';

      // 방 참여
      socket.emit('joinRoom', { userId: username.value, roomName }, (res) => {
        console.log('방 참여 결과:', res);
      });

      // 서버에서 메시지 수신
      socket.on('message', (msg) => {
        messages.value.push(msg);
        scrollToBottom(); // 새 메시지가 올 때마다 스크롤
      });
    });

    // messages 배열 변화 감지하여 자동 스크롤
    watch(messages, () => {
      scrollToBottom();
    }, { deep: true });

    function toggleChat() {
      chatOpen.value = !chatOpen.value;
      
      // 채팅창이 열릴 때도 스크롤을 맨 아래로
      if (chatOpen.value) {
        scrollToBottom();
      }
    }

    function sendMessage() {
      if (inputMsg.value.trim() === '') return;

      // 서버에 메시지 전송
      socket.emit('chatMessage', { roomName, message: inputMsg.value });

      // 입력창 초기화
      inputMsg.value = '';
      
      // 메시지 전송 후 스크롤을 맨 아래로
      scrollToBottom();
    }

    return { 
      username, 
      chatOpen, 
      toggleChat, 
      messages, 
      inputMsg, 
      sendMessage,
      chatMessagesRef 
    };
  }
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background-color: #f4f7f6; /* 옅은 회색 */
  display: flex;
  align-items: center;
  justify-content: flex-start; /* 왼쪽 정렬 */
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  z-index: 10;
}

.logo {
  font-size: 24px;
  font-weight: 700;
  /* 로그인 페이지와 일관된 그라데이션 텍스트 효과 */
  background: linear-gradient(45deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-right: 40px;
}

.nav {
  display: flex;
  align-items: center;
  width: 100%;      /* 전체 헤더 폭을 사용 */
}

.menu-left {
  display: flex;
  gap: 15px;
}

.menu-right {
  display: flex;
  gap: 15px;
  margin-left: auto;
  align-items: center;
}

/* 기존 스타일은 그대로 두고, 채팅 관련 부분만 교체하세요 */

/* 햄버거 메뉴 개선 */
.hamburger {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 25px;
  height: 18px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.hamburger:hover {
  transform: scale(1.1);
}

.hamburger span {
  display: block;
  height: 3px;
  background-color: black;
  border-radius: 2px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 햄버거 애니메이션 (활성화 시) */
.hamburger.active span:nth-child(1) {
  transform: rotate(45deg) translate(5px, 5px);
}

.hamburger.active span:nth-child(2) {
  opacity: 0;
}

.hamburger.active span:nth-child(3) {
  transform: rotate(-45deg) translate(7px, -6px);
}

/* 채팅 패널 전체 디자인 */
.chat-panel {
  position: fixed;
  top: 60px;
  right: -350px; /* 너비를 조금 늘림 */
  width: 350px;
  height: calc(100vh - 60px);
  background: linear-gradient(145deg, #ffffff 0%, #f8fafc 100%);
  box-shadow: -10px 0 30px rgba(0, 0, 0, 0.15);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  z-index: 20;
  border-left: 1px solid rgba(102, 126, 234, 0.1);
}

.chat-panel.open {
  right: 0;
}

/* 채팅 헤더 */
.chat-panel h3 {
  margin: 0;
  padding: 20px 25px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 1.2rem;
  font-weight: 600;
  text-align: center;
  box-shadow: 0 2px 10px rgba(102, 126, 234, 0.2);
  position: relative;
}

.chat-panel h3::before {
  content: '💬';
  margin-right: 8px;
  font-size: 1.1rem;
}

.chat-panel h3::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 3px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
}

/* 채팅 메시지 영역 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f8fafc;
  display: flex;
  flex-direction: column;
  gap: 12px;
  scroll-behavior: smooth;
}

/* 스크롤바 스타일링 */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: transparent;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 10px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #5a6fd8, #6a4190);
}

/* 개별 메시지 스타일 */
.chat-messages > div {
  background: white;
  padding: 12px 16px;
  border-radius: 15px 15px 15px 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  border-left: 3px solid #667eea;
  font-size: 14px;
  line-height: 1.4;
  color: #374151;
  position: relative;
  animation: slideInMessage 0.3s ease-out;
  max-width: 85%;
  word-wrap: break-word;
}

/* 메시지 애니메이션 */
@keyframes slideInMessage {
  from {
    opacity: 0;
    transform: translateY(10px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* 메시지 입력 영역 */
.chat-panel input {
  border: none;
  padding: 15px 20px;
  font-size: 14px;
  border-radius: 25px;
  margin: 0 15px 10px;
  background: white;
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  outline: none;
  color: #374151;
}

.chat-panel input:focus {
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.1), 
              0 0 0 3px rgba(102, 126, 234, 0.1);
  transform: translateY(-2px);
}

.chat-panel input::placeholder {
  color: #9ca3af;
}

/* 전송 버튼 */
.chat-panel button {
  margin: 0 15px 20px;
  padding: 12px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.chat-panel button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
  background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%);
}

.chat-panel button:active {
  transform: translateY(0);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

/* 빈 채팅방 표시 */
.chat-messages:empty::before {
  content: '대화를 시작해보세요! 👋';
  display: block;
  text-align: center;
  color: #9ca3af;
  font-style: italic;
  margin-top: 50px;
  opacity: 0.8;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .chat-panel {
    width: 300px;
    right: -300px;
  }
  
  .chat-panel h3 {
    padding: 15px 20px;
    font-size: 1.1rem;
  }
  
  .chat-messages {
    padding: 15px;
  }
}
</style>
