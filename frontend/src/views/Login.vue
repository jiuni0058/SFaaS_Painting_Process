<template>
  <div class="login-container">
    <div class="login-box">
      <img src="/src/assets/images/tetoever_logo.png" alt="Tetoever Logo" style="width:275px; margin-bottom:5px;" />
      <h1 class="logo">도장 모니터링</h1>
      <p class="welcome-text">로그인 후 서비스를 이용하세요.</p>

        <!-- 로딩 오버레이 -->
        <div v-if="isLoading">
          <div></div>
          <p>메일 전송 중입니다...</p>
        </div>
      
      <form @submit.prevent="checkBool">
        <input 
          :type="inputType"
          v-model="username" 
          :placeholder="id" 
          required
        />
        <input 
          type="password" 
          v-model="password" 
          :placeholder="pwd"
        />
        <button type="submit">{{ loginBtn }}</button>
      </form>

      <div class="links">
        <a href="signup">회원가입</a>
        <a href="#" @click="isBool">{{ findPwd }}</a>
      </div>
    </div>
    <div>
      <!-- <button @click="endCamera">카메라 끄기</button>
      <button @click="startCamera">카메라 켜기</button> -->
      <!-- <video ref="video" autoplay playsinline width="640" height="480"></video> -->
    </div>
  </div>


</template>

<script setup>
import { ref, onMounted  } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const route = useRoute();
onMounted(() => {
  sessionStorage.clear(); // 세션스토리지 전체 삭제
  if (route.query.run === 'true') {
    isBool(); // 홈 화면에서 실행할 함수
  }
});

const URL = '/api/hr/login';
const URL_PASSWORD = '/api/hr';
const username = ref('');
const password = ref('');
const router = useRouter();
const bool = ref(false);
const inputType = ref('text');
const id = ref('아이디');
const pwd = ref('비밀번호');
const loginBtn = ref('로그인');
const findPwd = ref('비밀번호 찾기');
const certVal = ref('F'); //F 는 인증 번호, S는 인증 확인


const isBool = () => {
  console.log(bool.value)
  if(!bool.value){
    pwd.value = "인증번호 입력";
    username.value = '';
    password.value = '';
    inputType.value = "email";
    loginBtn.value = "인증번호 전송";
    id.value = "이메일 입력"
    findPwd.value = "로그인"
    certVal.value = "F";
    bool.value = true;
  }else{
    bool.value = false;
    username.value = '';
    password.value = '';
    pwd.value = "비밀번호 입력";
    inputType.value = "text";
    loginBtn.value = "로그인";
    id.value = "아이디 & 이메일"
    findPwd.value = "비밀번호 찾기"
  }
  
}



const checkBool = () => {
  if(!bool.value) {
    login();
  }else{
    if(certVal.value === "F"){
      handleSignup();
    }else{
      checkIn();
    }
    
  }
}

const MAIL_SERVER = '/api/mail/send';
const MAIL_SERVER_CERT = '/api/mail/verify';
const isLoading = ref(false); //로딩창

const handleSignup = async () => {
  try {
    isLoading.value = true; // 로딩 시작

    const to = encodeURIComponent(username.value);

    const response = await fetch(`${MAIL_SERVER}?to=${to}`, {
      method: "POST",
    });

    if (!response.ok) {
      throw new Error("서버 오류: " + response.status);
    }

    loginBtn.value = "인증번호 확인";
    certVal.value = "S";
  } catch (error) {
    alert("이메일 전송 실패: " + error.message);
  } finally {
    isLoading.value = false; // 로딩 종료
  }
};


const checkIn = async () => {
  if(password.value.length !== 6){
    alert('인증번호 6자리 입력해주세요!');
  }

  try{
    const certRes = await fetch(MAIL_SERVER_CERT, {
      method: "POST",
      headers:{
        "Content-Type": "application/json", // JSON 전송
      },
      body: JSON.stringify({
        username: username.value,
        password: password.value
      })
    });

    const data = await certRes.json();

    if (data.status === 'fail') {
      throw new Error("인증번호 오류: " + data.message);
    }

    const response = await fetch(URL_PASSWORD + `/${username.value}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json", // JSON 전송
        },
        body: JSON.stringify({
          username: username.value
        })
      }).then(res => res.json())
        .then(data => {
          if (data.status === 'fail') {
            throw new Error("로그인 오류: " + data.message);
          } else {
            alert('인증되었습니다. 비밀번호는 1111로 초기화 되었습니다.');
            isBool();
            username.value = '';
            password.value = '';
          }
        })
        .catch(err => {
          console.error(err.message);
          alert(err.message);
        });
  }
  catch (err){
    alert(err);
  }
}

const login = async () => {
  try {
    console.log(username.value)
    const response = await fetch(URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json", // JSON 전송
      },
      // credentials: "include", // 쿠키 인증 필요 시
      body: JSON.stringify({
        username: username.value,
        password: password.value
      })
    }).then(res => res.json())
      .then(data => {
        if (data.status === 'fail') {
          throw new Error("로그인 오류: " + data.message);
        } else {
          sessionStorage.setItem("accountName", data.message);
          sessionStorage.setItem("accountId", data.id);  
          router.push('/main'); // 로그인 성공 시 이동
        }
      })
      .catch(err => {
        console.error(err.message);
        alert(err.message);
      });
  } catch (error) {
    console.error("로그인 실패:", error);
  }
};



// const video = ref(null);
// const cameraOn = ref(false);

// const endCamera = () => {
//   router.push('/');
// };

// const startCamera = async () => {
//   try {
//     const stream = await navigator.mediaDevices.getUserMedia({ video: true });
//     video.value.srcObject = stream;
//     cameraOn.value = true; // 웹캠 화면 보이도록 상태 변경
//   } catch (err) {
//     console.error("카메라 접근 실패:", err);
//     alert("카메라 접근 권한이 필요합니다.");
//   }
// };
</script>
<style scoped>

/* CSS 리셋 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}

/* 전체 화면 배경 + 중앙 배치 */
.login-container {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  width: 100vw !important;
  height: 100vh !important;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  overflow: hidden;
  margin: 0 !important;
  padding: 0 !important;
  /* 그리드도 함께 사용 */
  display: grid !important;
  place-items: center !important;
}

/* 폼 박스 (SignForm 느낌) */
.login-box {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 50px 40px;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
  width: 420px;
  max-width: 90vw;
  text-align: center;
  position: relative;
  animation: slideInRight 0.8s ease-out;
  margin: 0 auto !important;
  /* 추가 중앙 정렬 */
  justify-self: center;
  align-self: center;
}

/* 상단 그라데이션 라인 */
.login-box::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: linear-gradient(90deg, #667eea, #764ba2);
  border-radius: 20px 20px 0 0;
}

/* 글래스모피즘 배경 */
/* .login-box::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0.05));
  pointer-events: none;
  z-index: -1;
} */

/* 로고 */
.logo {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
  background: linear-gradient(45deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 안내 텍스트 */
.welcome-text {
  font-size: 16px;
  color: #666;
  margin-bottom: 35px;
}

/* input 스타일 */
input {
  width: 100%;
  padding: 18px 22px;
  margin-bottom: 20px;
  border: 2px solid rgba(230, 230, 230, 0.5);
  border-radius: 15px;
  font-size: 16px;
  background: rgba(248, 249, 250, 0.8);
  outline: none;
  transition: all 0.3s ease;
}

input:focus {
  border-color: #667eea;
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.2);
}

input::placeholder {
  color: #adb5bd;
}

/* 버튼 스타일 */
button {
  width: 100%;
  padding: 18px;
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: #fff;
  border: none;
  border-radius: 15px;
  font-size: 17px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 30px rgba(102, 126, 234, 0.4);
}

/* 링크 영역 */
.links {
  margin-top: 30px;
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}

.links a {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.links a:hover {
  color: #764ba2;
  text-decoration: underline;
}

/* 타이머 표시 */
.timer-box {
  display: flex;
  gap: 8px;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #d9534f;
}

/* 애니메이션 */
@keyframes slideInRight {
  0% { 
    opacity: 0; 
    transform: translateX(100px);
  }
  100% { 
    opacity: 1; 
    transform: translateX(0);
  }
}

/* 반응형 */
@media (max-width: 768px) {
  .login-box { 
    padding: 40px 30px; 
    width: 350px; 
    max-width: 95vw;
  }
  .logo { font-size: 30px; }
  input, button { padding: 16px 20px; font-size: 15px; }
}

@media (max-width: 480px) {
  .login-box { 
    padding: 35px 25px; 
    width: 320px; 
    max-width: 95vw;
  }
  .logo { font-size: 28px; }
  input, button { padding: 14px 18px; font-size: 14px; }
}


</style>