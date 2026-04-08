# SFaaS - 자동차 도장공정 모니터링 시스템

현대오토에버 모빌리티 SW 스쿨 2기 1조의 스마트 팩토리 프로젝트입니다. 자동차 도장공정의 주요 설비를 실시간으로 모니터링하여, 고장을 사전에 예측하고 선제적으로 대응하는 시스템을 구축합니다.

## 📝 프로젝트 개요

자동차 생산 공정의 자동화가 가속화되면서, 과거 작업자의 경험에 의존했던 설비 고장 예측이 어려워졌습니다. 특히 고온의 작업 환경과 유해 물질을 다루는 도장 공정은 작업자의 접근이 제한되어, 돌발적인 설비 문제가 큰 생산 차질로 이어질 수 있습니다.

본 프로젝트는 도장공정 핵심 설비에 대한 **실시간 데이터 모니터링 시스템**을 구축하여, 공정 효율을 극대화하고 안정적인 생산 라인을 확보하는 것을 목표로 합니다.

## ✨ 주요 기능

- **사용자 관리**: 회원가입 및 로그인 기능
- **메인 모니터링**: 생산 라인의 현재 가동 상태를 실시간으로 확인
- **장비별 세부 모니터링**: 각 장비(전착, 건조 오븐, 스프레이 등)의 가동률, 최근 구동 데이터, 관련 정보 조회
- **공정별 불량 모니터링**: 공정별/전체 불량률, 불량 유형 및 상세 내역 시각화
- **장비별 오류 모니터링**: 장비별/전체 고장률, 고장 원인 및 상세 내역 시각화

## 🛠️ 기술 스택

| 구분 | 기술 |
| --- | --- |
| **Frontend** | Vue.js, Vite, Chart.js |
| **Backend** | Java, Spring Boot, Spring Security |
| **Database** | MySQL |
| **Embedded/Data**| Raspberry Pi, Node-RED, OPC-UA, Modbus, Docker |

## 🏗️ 시스템 아키텍처
<img width="657" height="327" alt="system" src="https://github.com/user-attachments/assets/297dc038-ffc8-4431-9509-eed4b3084572" />

1.  **데이터 수집**: 현장의 PLC 및 센서(온습도 등) 데이터를 OPC-UA를 통해 수집합니다.
2.  **데이터 처리**: Node-RED가 수집된 데이터를 가공하여 비가동 시간을 계산하고, MySQL 데이터베이스에 저장합니다.
3.  **백엔드 서버**: Spring Boot 기반의 API 서버가 프론트엔드에 필요한 데이터를 제공합니다.
4.  **프론트엔드**: Vue.js 기반의 웹 애플리케이션이 대시보드, 차트 등을 통해 데이터를 시각화하여 사용자에게 보여줍니다.

## 🚀 시작하기

### Backend

```bash
cd backend/sfaas
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## 👨‍👩‍👧‍👦 팀원

- **이현호**: 팀장, Downtime, Detail, Defect 페이지 구축
- **강선규**: SRS, IDD, SDD 작성, Detail 페이지 구축
- **이지운**: SMWP 페이지 구축, Downtime, Defect, Detail 백엔드 구축,  Node-RED 서버 구축 
- **이창열**: Login 페이지 구축, 채팅 서비스 구현

## 발표자료

[발표자료 PDF](./SFaaS 1조 자동차 도장공정 모니터링 시스템.pdf)

## 🎥 시연 영상
[![Video Label](http://img.youtube.com/vi/4vvZFJvkxrw/0.jpg)](https://youtu.be/4vvZFJvkxrw)
