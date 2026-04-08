# SFaaS_Back

SFaaS (Software Factory as a Service) 프로젝트의 백엔드 레포지토리입니다. 이 프로젝트는 설비의 비가동 시간, 불량률 등 공장 운영에 필요한 주요 지표(KPI)를 모니터링하고 관리하는 API를 제공합니다.

---

##  주요 기능

* **사용자 관리**: 회원가입, 로그인, 정보 수정 및 비밀번호 재설정 기능을 제공합니다. 비밀번호는 SHA-256 알고리즘을 사용하여 안전하게 해싱 및 솔팅 처리됩니다.
* **이메일 인증**: 회원가입 또는 비밀번호 재설정 시 이메일로 인증 코드를 발송하고 검증하는 기능을 제공합니다.
* **비가동 시간(Downtime) 대시보드**:
    * KPI 데이터 제공 (오늘의 총 비가동 시간, 가장 빈번한 에러, 최장 비가동 설비)
    * 주간 비가동 발생 추이 및 상세 로그 조회
    * 설비별/유형별 에러 분포 시각화 데이터 제공
* **불량(Defect) 분석 대시보드**:
    * KPI 데이터 제공 (총 검수량, 불량 건수, 불량률, 최다 발생 불량 유형)
    * 일별 불량 발생 추이 및 상세 로그 조회
    * 유형별/설비별 불량 분포 시각화 데이터 제공
* **설비 상세 정보**:
    * 개별 설비의 상세 정보 및 PLC 데이터 조회
    * 설비의 시간당 가동률(Utilization Rate) 계산 및 제공

---

## 기술 스택

| 구분 | 기술 |
| --- | --- |
| **Backend** | Java 17, Spring Boot 3.3.0 |
| **Database** | MySQL 8.0, MyBatis 3.0.3 |
| **API** | RESTful API, Springdoc OpenAPI (Swagger UI) |
| **Build** | Maven |
| **Container** | Docker, Docker Compose |
| **Security** | SHA-256 Hashing |

---

## API 명세

프로젝트의 모든 API 문서는 Springdoc OpenAPI를 통해 자동으로 생성됩니다. 애플리케이션 실행 후 다음 URL에서 확인하실 수 있습니다.

* **Swagger UI**: `http://localhost:9090/swagger-ui.html`

---

## 데이터베이스

데이터베이스 스키마는 `src/main/resources/schema.sql` 파일에 정의되어 있으며, 초기 샘플 데이터는 `src/main/resources/data.sql` 파일에서 확인할 수 있습니다.

---

## 시작하기

### 사전 요구사항

* Java 17
* Maven
* Docker 및 Docker Compose

### 설치 및 실행

1.  **프로젝트 클론**
    ```bash
    git clone [https://github.com/your-username/SFaaS_Back.git](https://github.com/your-username/SFaaS_Back.git)
    cd SFaaS_Back/sfaas
    ```

2.  **데이터베이스 실행**
    프로젝트 루트의 `docker-compose.yml` 파일을 사용하여 MySQL 데이터베이스 컨테이너를 실행합니다.
    ```bash
    docker-compose up -d
    ```

3.  **애플리케이션 설정**
    `src/main/resources/application.properties` 파일이 로컬 개발 환경에 맞게 설정되어 있는지 확인합니다. 기본 설정은 다음과 같습니다.
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/sfaas_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    spring.datasource.username=root
    spring.datasource.password=root
    ```

4.  **애플리케이션 실행**
    Maven Wrapper를 사용하여 애플리케이션을 실행합니다.
    ```bash
    ./mvnw spring-boot:run
    ```
    애플리케이션은 기본적으로 9090 포트에서 실행됩니다.

---

## 기여 방법

이 프로젝트에 기여하고 싶으시다면, `CONTRIBUTING.md` 문서를 참고하여 이슈 생성 및 Pull Request 프로세스를 따라주세요.
