# SFaaS 프로젝트 기여 가이드 (CONTRIBUTING.md)

이 문서는 프로젝트에 원활하게 기여하기 위한 규칙과 가이드라인을 담고 있습니다. 잠시 시간을 내어 읽어보시면 우리 모두의 시간을 절약하고 보다 효율적으로 협업할 수 있습니다.

---

## 목차

1.  [Git 브랜치 전략](#git-브랜치-전략)
2.  [커밋 메시지 컨벤션](#커밋-메시지-컨벤션)
3.  [Pull Request (PR) 프로세스](#pull-request-pr-프로세스)
4.  [이슈(Issue) 관리 방안](#이슈issue-관리-방안)
5.  [코딩 컨벤션](#코딩-컨벤션)
---

## Git 브랜치 전략

우리 프로젝트는 **Git Flow**를 기반으로 한 브랜치 전략을 사용합니다.

-   **`main`**: 릴리스된 안정적인 버전의 코드가 존재하는 브랜치입니다. 오직 `develop`과 `hotfix` 브랜치에서만 병합(Merge)될 수 있습니다.
-   **`develop`**: 다음에 릴리스될 버전을 개발하는 메인 브랜치입니다. 모든 기능 개발은 이 브랜치에서 시작됩니다.
-   **`feature/{기능이름}`**: 새로운 기능을 개발할 때 사용하는 브랜치입니다.
    -   생성: `develop` 브랜치에서 생성합니다. (예: `feature/login-api`, `feature/user-profile-ui`)
    -   규칙: 기능 개발이 완료되면 `develop` 브랜치로 Pull Request를 보냅니다.
-   **`hotfix/{수정내용}`**: `main` 브랜치에 배포된 버전에서 발생한 긴급한 버그를 수정할 때 사용합니다.
    -   생성: `main` 브랜치에서 생성합니다.
    -   규칙: 수정이 완료되면 **`main`과 `develop` 브랜치 모두에** 병합(Merge)되어야 합니다.

### 작업 흐름 예시
1. `develop` 브랜치에서 최신 코드를 받습니다. (`git pull origin develop`)
2. 새로운 기능 브랜치를 생성합니다. (`git checkout -b feature/my-new-feature`)
3. 코드를 작성하고 커밋합니다.
4. 작업이 완료되면 `develop` 브랜치로 Pull Request를 생성합니다.

---

## 커밋 메시지 컨벤션

우리 프로젝트는 **Conventional Commits** 규칙을 따릅니다. 이는 커밋 히스토리를 명확하게 파악하고, 버전 관리를 자동화하는 데 도움이 됩니다.

반드시 영어로 작성해 주십시오!

### 형식
```
<타입>(<스코프>): <제목>

(<본문>)

(<꼬리말>)

```

### 주요 타입
-   **`feat`**: 새로운 기능 추가
-   **`fix`**: 버그 수정
-   **`docs`**: 문서 수정 (README.md, CONTRIBUTING.md 등)
-   **`style`**: 코드 포맷팅, 세미콜론 누락 등 기능 변경이 없는 스타일 수정
-   **`refactor`**: 코드 리팩토링 (기능 변경 없이 코드 구조를 개선)
-   **`test`**: 테스트 코드 추가 또는 수정
-   **`chore`**: 빌드 관련 파일 수정, 패키지 매니저 설정 변경 등

### 예시


```
feat: Add user login API
fix: Correct password validation logic
docs: Update CONTRIBUTING.md with PR template info
```


---

## Pull Request (PR) 프로세스

1.  **PR 생성**: 기능 개발 완료 후, `feature/...` 브랜치에서 `develop` 브랜치로 PR을 생성합니다.
2.  **템플릿 작성**: PR 생성 시 자동으로 나타나는 `.github/PULL_REQUEST_TEMPLATE.md`의 내용을 충실하게 작성합니다. 특히 관련된 이슈 번호를 꼭 포함시켜주세요. (예: `Closes #123`)
3.  **코드 리뷰**:
    -   최소 1명 이상의 다른 팀원에게 코드 리뷰를 요청하고 승인(Approve)을 받아야 합니다.
    -   리뷰어는 개선점에 대해 자유롭게 의견을 제시하고, PR 작성자는 이를 적극적으로 반영합니다.
    -   모든 코멘트와 토론이 해결(Resolve)되어야 합니다.
4.  **Merge 조건**:
    -   CI 빌드 및 테스트가 모두 통과해야 합니다.
    -   리뷰어의 승인을 받아야 합니다.
    -   `develop` 브랜치와 충돌(Conflict)이 없어야 합니다.
    -   모든 조건이 충족되면 **Squash and Merge** 방식으로 병합하여 커밋 히스토리를 깔끔하게 유지합니다.

---

## 이슈(Issue) 관리 방안

1.  **이슈 생성**: 버그 발견, 새로운 기능 제안 등 모든 논의는 이슈를 통해 시작합니다. `.github/ISSUE_TEMPLATE/` 에 있는 템플릿 중 목적에 맞는 것을 선택하여 작성해주세요.
2.  **라벨링 규칙**: 이슈의 성격과 상태를 명확히 하기 위해 아래와 같은 라벨을 사용합니다.
    -   **타입 라벨**:
        -   `Bug`: 버그 리포트
        -   `Feature`: 기능 요청
        -   `Docs`: 문서 관련
    -   **중요도 라벨**:
        -   `High`: 즉시 처리 필요
        -   `Medium`: 일반적인 우선순위
        -   `Low`: 급하지 않은 작업
    -   **상태 라벨**:
        -   `In Progress`: 작업 진행 중
        -   `Needs Review`: 검토 필요
## 코딩 컨벤션
**1. 패키지 구조**
    - `controller, service, model, mapper` 단위로 패키지를 구분한다.
    - `Controller` 클래스는 반드시 `controller` 패키지에 위치한다.

**2. 클래스 선언부**
    - 모든 컨트롤러는 @RestController를 사용한다.
    - 공통 prefix는 @RequestMapping("/api")로 지정한다.
    - 클래스명은 *Controller로 끝나도록 한다.

**3. 의존성 주입**
    - @Autowired는 생성자 주입 방식을 원칙으로 한다.
    - final 키워드를 활용하여 불변성을 보장한다.
    - 필드 주입은 금지한다.    

**4. API 메서드 작성 규칙**

- 엔드포인트 규칙
    - HTTP 메서드에 맞는 @GetMapping, @PostMapping, @PutMapping, @DeleteMapping을 사용한다.
    - URL 네이밍은 소문자, 단수형을 원칙으로 한다.
    - 필요 시 /{id} 경로 변수를 사용한다.

- 파라미터 규칙
    - @RequestParam은 명시적으로 작성한다.
    - 변수명은 DB 컬럼 그대로 쓰지 말고 Java 네이밍 컨벤션(camelCase)을 따른다
    - 불필요한 혼동(machine_id vs machine_name)이 없도록 리팩토링한다.

- 응답 규칙
    - 응답은 ResponseEntity<T> 사용을 권장한다.
    - 단일 객체 반환 시 Optional 활용 고려.
    - 리스트/맵 반환 시 제네릭 명시 (List<DetailModel>, Map<String, List<DetailModel>>).
