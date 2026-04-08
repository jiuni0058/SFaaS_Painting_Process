# SFaaS: 도장 공정 모니터링 시스템 프론트엔드

SFaaS (Smart Factory as a Service) 프로젝트의 프론트엔드 레포지토리입니다. 이 프로젝트는 자동차 도장 공정의 데이터를 실시간으로 시각화하고 분석하여 공정 효율을 극대화하는 것을 목표로 하는 웹 기반 대시보드 애플리케이션입니다.

## ✨ 주요 기능
- 🔐 사용자 인증: 안전한 로그인을 위한 이메일 인증 기반 회원가입 및 비밀번호 찾기 기능을 제공합니다.
- 📊 실시간 대시보드:
  - 설비 상세 모니터링: 개별 설비의 온도, 압력 등 주요 데이터를 실시간 라인 차트로 시각화하고 가동률을 모니터링합니다.
  - 다운타임 분석: 설비의 비가동 시간, 원인, 발생 추이를 분석하여 생산성 저하 요인을 파악합니다.
  - 불량 데이터 분석: 공정에서 발생하는 불량 데이터를 유형, 설비별로 분석하여 품질 개선을 지원합니다.
  - 💬 실시간 정보 공유: Socket.IO 기반의 채팅 기능을 통해 현장 작업자 간의 원활한 소통을 지원합니다.
  - 🎨 동적 차트 시각화: Chart.js를 활용하여 바, 도넛, 라인 차트 등 다양한 형태로 데이터를 직관적으로 표현합니다.
## 🛠️ 기술 스택
- Framework: Vue.js 3 (Composition API)
- Build Tool: Vite
- Routing: Vue Router
- State Management: Pinia
- HTTP Client: Axios
- Real-time Communication: Socket.IO Client
- Charting: Chart.js & Vue Chart.js
- Deployment: Docker, Nginx
## 📂 프로젝트 구조
```
src
├── assets/         # CSS, 이미지, 폰트 등 정적 에셋
├── components/     # 재사용 가능한 UI 컴포넌트 (차트, 헤더, 사이드바 등)
├── router/         # Vue Router 설정 (라우팅 경로 관리)
├── stores/         # Pinia 스토어 (전역 상태 관리)
├── views/          # 라우팅되는 페이지 컴포넌트
│   ├── Login.vue
│   ├── Main.vue
│   ├── Detail.vue
│   ├── Downtime.vue
│   └── Defect.vue
├── App.vue         # 최상위 루트 컴포넌트
└── main.js         # 애플리케이션 진입점
```

## 🚀 시작하기
1. 레포지토리 클론
```
git clone https://github.com/Autoever-SF2-Team-One/SFaaS_Front.git
cd SFaaS_Front
```


2. 의존성 설치
Node.js 환경에서 아래 명령어를 실행하여 필요한 패키지를 설치합니다.
```
npm install
```

3. 개발 서버 실행
애플리케이션을 개발 모드로 실행합니다.
```
npm run dev
```

서버가 실행되면 http://localhost:5173 에서 애플리케이션을 확인할 수 있습니다.

  - 참고: 백엔드 API 서버와의 통신을 위해 vite.config.js 파일에 프록시 설정이 포함되어 있습니다. 백엔드 서버가 실행 중이어야 모든 기능이 정상적으로 동작합니다.

## 📦 빌드 및 배포
- 프로덕션 빌드
프로젝트를 배포용으로 빌드하려면 다음 명령어를 실행합니다. 빌드 결과물은 dist 디렉토리에 생성됩니다.
```
npm run build
```

- Docker를 이용한 배포
프로젝트 루트 디렉토리의 Dockerfile과 docker-compose.yml을 사용하여 애플리케이션을 컨테이너 환경에서 실행할 수 있습니다.
```
docker-compose up --build
```
