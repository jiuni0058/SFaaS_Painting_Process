# SFaaS_Embedded (Smart Factory as a Service - Embedded)

SFaaS 프로젝트의 임베디드 시스템입니다.

## 📄 프로젝트 개요

본 프로젝트는 스마트 팩토리 환경의 센서 데이터 수집 및 통신을 시뮬레이션하는 임베디드 시스템입니다. Docker를 사용하여 **OPC UA 서버**를 컨테이너화해 독립적으로 실행하고 관리합니다.

- **OPC UA 서버**: Python으로 작성되었으며, OPC UA 프로토콜을 통해 온도 및 습도 데이터를 제공하고, 주기적으로 값을 업데이트하는 시뮬레이션을 포함합니다.

---

## 🏗️ 시스템 아키텍처

### 1. OPC UA 서버 (`opcua-docker`)

- **언어**: Python
- **프로토콜**: OPC UA
- **포트**: `4001`
- **기능**:
  - `python-opcua` 라이브러리를 사용하여 OPC UA 서버를 구현합니다.
  - `MyObject` 객체 노드 아래에 `Temperature`와 `Humidity` 변수를 생성합니다.
  - `Temperature` 변수는 1초마다 값이 갱신되도록 구성하여 실시간 데이터 변화를 시뮬레이션합니다.
  - Docker 컨테이너 기반으로 실행됩니다.

### 2. Node-RED 연동

- Node-RED를 통해 OPC UA 서버 데이터를 수집하고, 상위 시스템이나 모니터링 화면과 연동할 수 있도록 구성할 수 있습니다.

---

## 🚀 시작하기

### 사전 요구사항

- Docker
- Docker Compose

### 설치 및 실행

#### OPC UA 서버 실행

```bash
# 1. opcua-docker 디렉토리로 이동
cd opcua-docker

# 2. Docker Compose를 사용하여 컨테이너 빌드 및 실행
docker-compose up --build -d
