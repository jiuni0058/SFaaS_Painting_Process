# SFaaS_Embedded (Smart Factory as a Service - Embedded)

SFaaS 프로젝트의 임베디드(라즈베리파이) 시스템입니다.

## 📄 프로젝트 개요

본 프로젝트는 라즈베리파이 환경에서 스마트 팩토리의 센서 데이터 수집 및 통신을 시뮬레이션하는 임베디드 시스템입니다. Docker를 사용하여 **Modbus TCP 서버**와 **OPC UA 서버**를 각각 컨테이너화하여 독립적으로 실행하고 관리합니다.

-   **Modbus TCP 서버**: C언어로 작성되었으며, 가상의 온도 및 습도 데이터를 Modbus 프로토콜을 통해 제공합니다.
-   **OPC UA 서버**: Python으로 작성되었으며, OPC UA 프로토콜을 통해 온도 및 습도 데이터를 제공하고, 주기적으로 값을 업데이트하는 시뮬레이션을 포함합니다.

---

## 🏗️ 시스템 아키텍처

### 1. Modbus TCP 서버 (`modbus-cpp-docker`)

-   **언어**: C
-   **프로토콜**: Modbus TCP
-   **포트**: `502`
-   **기능**:
    -   가상의 온도 및 습도 센서 데이터를 생성합니다.
    -   Holding Register에 고정값(1234, 5678) 및 가상 센서 값을 저장합니다.
    -   Docker를 통해 ARM64 환경에서 빌드 및 실행됩니다.
    -   `libmodbus` 라이브러리를 사용하여 Modbus 통신을 구현합니다.

### 2. OPC UA 서버 (`opcua-docker`)

-   **언어**: Python
-   **프로토콜**: OPC UA
-   **포트**: `4001`
-   **기능**:
    -   `python-opcua` 라이브러리를 사용하여 OPC UA 서버를 구현합니다.
    -   `MyObject`라는 객체 노드 아래에 `Temperature`와 `Humidity` 변수를 생성합니다.
    -   `Temperature` 변수는 1초마다 1씩 증가하는 스레드를 통해 실시간 데이터 변화를 시뮬레이션합니다.
    -   경량화된 `python:3.9-slim` 이미지를 사용하여 Docker 컨테이너로 실행됩니다.

### 3. Node-RED 연동

-   루트 `package.json`에 `node-red-contrib-modbus` 의존성이 포함되어 있어, Node-RED를 이용해 Modbus 서버의 데이터를 시각화하거나 다른 시스템으로 전달하는 흐름을 구성할 수 있습니다.

---

## 🚀 시작하기

### 사전 요구사항

-   Docker
-   Docker Compose

### 설치 및 실행

#### 1. Modbus TCP 서버 실행

```bash
# 1. modbus-cpp-docker 디렉토리로 이동
cd modbus-cpp-docker

# 2. Docker Compose를 사용하여 컨테이너 빌드 및 실행
docker-compose up --build -d
```
- 서버가 정상적으로 실행되면 호스트의 502번 포트에서 Modbus TCP 통신이 가능합니다.

#### 2. OPC UA 서버 실행
```bash
# 1. opcua-docker 디렉토리로 이동
cd opcua-docker

# 2. Docker Compose를 사용하여 컨테이너 빌드 및 실행
docker-compose up --build -d
```
서버가 정상적으로 실행되면 opc.tcp://<라즈베리파이_IP>:4001 주소로 OPC UA 클라이언트 연결이 가능합니다.

## 🔧 주요 기술 스택
- Backend: C, Python, Node.js

- 프로토콜: Modbus TCP, OPC UA

- 컨테이너화: Docker, Docker Compose

### 주요 라이브러리:

- libmodbus-dev (C)

- opcua (Python)

- node-red-contrib-modbus (Node.js)
