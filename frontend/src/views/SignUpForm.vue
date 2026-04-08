<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="logo">도장 모니터링</h1>
      <p class="welcome-text">회원가입 정보를 입력하세요.</p>

      <!-- 로딩 오버레이 -->
        <div v-if="isLoading">
          <div></div>
          <p>메일 전송 중입니다...</p>
        </div>

      <form @submit.prevent="signup">
        <input 
          type="text" 
          v-model="id" 
          placeholder="아이디" 
          :readonly="isEditMode"
          required
        />
        <input 
          type="email" 
          v-model="email" 
          placeholder="이메일" 
          required
        />
        <input 
          type="text" 
          v-model="username" 
          placeholder="이름" 
          required
        />
        <input 
          type="password" 
          v-model="password" 
          placeholder="비밀번호" 
          required
        />
        <input 
          type="password" 
          v-model="mailCert" 
          placeholder="이메일 인증 확인" 
          :disabled="isCertDisabled"
          v-if="!isEditMode"
          required
        />

        <!-- 인증 버튼 -->
        <button 
          type="button" 
          @click="sendCert"
        >
          {{ isEditMode ? "수정" : (isSent ? "인증번호 확인" : "이메일 인증") }}
        </button>

        <!-- 타이머 -->
        <span v-if="isSent && !isEditMode" class="timer-text">
          {{ remainingTime }}초 ⏳ 인증번호 확인
        </span>
      </form>
      
      <div class="links">
        <a href="/">로그인으로 돌아가기</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const URL = '/api/hr';
const MAIL_SERVER = '/api/mail/send';
const MAIL_SERVER_CERT = '/api/mail/verify';

const router = useRouter()
const id = ref('')
const username = ref('')
const email = ref('')
const password = ref('')
const mailCert = ref('')
const certPwd = ref('')
const isCertDisabled = ref(true); // 처음에는 비활성화
const isEditMode = ref(false);


const isSent = ref(false)
const remainingTime = ref(60) // 1분 = 60초
let timer = null
const isLoading = ref(false); //로딩창

const route = useRoute();
onMounted(() => {
  const presentId = sessionStorage.getItem("accountId");
  if(presentId != null){
    isEditMode.value = true;
    findAll(presentId);
  }else{
    isEditMode.value = false;
  }

});




// 회원가입 요청 (이메일 전송 포함)
const handleSignup = async () => {
  try {
    isLoading.value = true; // 로딩 시작
    const to = encodeURIComponent(email.value);
    const response = await fetch(`${MAIL_SERVER}?to=${to}`, {
      method: "POST",
    });
    

    if (!response.ok) {
      throw new Error("서버 오류: " + response.status);
    }
    isCertDisabled.value = false;
    alert('인증번호가 전송되었습니다.');

  } catch (error) {
    alert("이메일 전송 실패:", error);
  } finally {
    isLoading.value = false; // 로딩 종료
  }
};

// 인증 버튼 클릭 시 → 버튼 비활성화 + 타이머 표시
const sendCert = async () => {
  if (isEditMode.value) {
    const sessionId = sessionStorage.getItem("accountId");
    updateAll(sessionId); // 수정용 함수 실행
    return;      // 여기서 종료
  }

  if(isSent.value){
    try {
      const certRes = await fetch(MAIL_SERVER_CERT, {
        method: "POST",
        headers: {
          "Content-Type": "application/json", // JSON 전송
        },
        body: JSON.stringify({
          username: email.value,
          password: mailCert.value
        })
      });

      if (!certRes.ok) {
        throw new Error("서버 오류: " + certRes.status);
      }

      const data = await certRes.json();

      if (data.status === 'fail') {
        throw new Error("인증번호 오류: " + data.message);
      } else {
        signup(); // ✅ 인증 성공 시 실행
      }

    } catch (err) {
      console.error(err.message);
      alert(err.message);
    }
  }else{
    if(id.value === null || id.value === ""){
      alert('아이디를 작성해주세요.');
    }else if(email.value === null || email.value === ""){
      alert('메일을 작성해주세요.');
    }else if(username.value === null || username.value === ""){
      alert('이름을 작성해주세요.');
    }else if(password.value === null || password.value === ""){
      alert('비밀번호를 작성해주세요.');
    }else{
      isSent.value = true
      remainingTime.value = 60

      // 타이머 시작
      timer = setInterval(() => {
        if (remainingTime.value > 0) {
          remainingTime.value--
        } else {
          clearInterval(timer)
          isSent.value = false // 시간이 끝나면 다시 인증 버튼 활성화
          isCertDisabled.value = true;
        }
      }, 1000)

      // 버튼 클릭하면 이메일도 전송
      handleSignup()
    }
  }
}

//회원가입 정보 전달.
const signup = async () => {
  try {
    const response = await fetch(URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json", // JSON 전송
      },
      // credentials: "include", // 쿠키 인증 필요 시
      body: JSON.stringify({
        id: id.value,
        email: email.value,
        name: username.value,
        pw: password.value
      })
    }).then(res => res.json())
      .then(data => {
        if (data.status === 'fail') {
          throw new Error("회원가입 오류: " + data.message);
        } else {
          alert("인증되었습니다.")
          router.push('/');
        }
      })
      .catch(err => {
        router.push({ path: '/', query: { run: 'true' } });
        alert(err.message);
      });
  } catch (error) {
    console.error("로그인 실패:", error);
  }
};

const updateAll = async (sessionId) => {
  const response = await fetch(URL+`/update/${sessionId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json", // JSON 전송
        },
        body: JSON.stringify({
          pw: password.value,
          id: id.value,
          name: username.value,
          email: email.value
        })
      }).then(res => res.json())
        .then(data => {
          if (data.status === 'fail') {
            throw new Error("회원정보 수정 오류: " + data.message);
          } else {
            alert('정보가 수정되었습니다.');
            router.push('/');
            username.value = '';
            password.value = '';
          }
        })
        .catch(err => {
          console.error(err.message);
          alert(err.message);
        });
}


//계정 정보 가져오기
const findAll = async (sessionId) => {
    const userAll = await fetch(URL+`/${sessionId}`);
    const data = await userAll.json();
    console.log(data.id)
    console.log(data.email)
    console.log(data.name)
    id.value = data.id;
    username.value = data.name;
    email.value = data.email;
  }

</script>
<style scoped>
/* 기존 로그인 디자인 그대로 재사용 가능 */
.login-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  display: flex;
  align-items: center;
  justify-content: center;
}

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
}

.login-box::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: linear-gradient(90deg, #667eea, #764ba2);
}

.logo {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
  background: linear-gradient(45deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.welcome-text {
  font-size: 16px;
  color: #666;
  margin-bottom: 35px;
}

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

.links {
  margin-top: 30px;
  display: flex;
  justify-content: center;
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

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(100px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.timer-box {
  display: flex;
  gap: 8px;
  align-items: center;
  font-weight: bold;
  color: #d9534f;
}
</style>
