# Study Buddy Agent — CLI deployment

Kotlin / Koog 기반 학습 에이전트 코드랩의 **CLI 배포** 안내입니다. 행사·코드랩 문맥은 [README.ko.md](./README.ko.md)를 참고하세요.

## 준비 사항

- **JDK 21** — Gradle이 설정한 JVM toolchain과 일치해야 합니다.
- **`GOOGLE_API_KEY`** — Google Gemini API 키를 환경 변수로 설정합니다.

```bash
export GOOGLE_API_KEY='YOUR_API_KEY'
```

## 배포 산출물 만들기

Gradle **Application** 플러그인으로 실행 가능한 배포 디렉터리 또는 ZIP을 만듭니다.

### 디렉터리 형태 (`installDist`)

```bash
./gradlew installDist
```

생성 위치:

- **Unix/macOS 실행 스크립트**: `build/install/study-buddy-agent-codelab/bin/study-buddy-agent-codelab`
- **Windows**: `build/install/study-buddy-agent-codelab/bin/study-buddy-agent-codelab.bat`
- **라이브러리 JAR**: `build/install/study-buddy-agent-codelab/lib/`

압축해서 다른 머신으로 옮길 때는 위 **`study-buddy-agent-codelab`** 디렉터리 전체를 포함하면 됩니다.

### ZIP 아카이브 (`distZip`)

```bash
./gradlew distZip
```

생성물: `build/distributions/study-buddy-agent-codelab.zip` (루트 프로젝트 이름 기준)

받는 쪽에서 압축을 풀고 `bin/` 아래 스크립트를 실행합니다.

## 실행 방법

환경 변수를 설정한 터미널에서 스크립트를 실행합니다.

```bash
cd build/install/study-buddy-agent-codelab
./bin/study-buddy-agent-codelab
```

또는 ZIP을 풀었다면 동일하게 `bin/` 경로를 지정합니다.

### 개발 중 Gradle로 실행

```bash
./gradlew run
```

대화형 표준 입력을 쓰는 경우를 위해 `run` 태스크에 `standardInput = System.in`이 설정되어 있습니다. 배포 바이너리도 **터미널(TTY)** 에서 실행해야 프롬프트 입력이 정상 동작합니다.

## 기본 진입점 동작

현재 `main()`은 **`runStudyTeam`** 을 호출합니다. 즉, 배포 CLI를 실행하면 **Multi-Agent 학습 전문가 팀 파이프라인**이 한 번 실행되는 흐름입니다.

## 대화형 과제 세션 (`runStudySession`) 입력 규칙

소스에 포함된 **`runStudySession`** 을 진입점으로 바꿔 사용할 때는 다음 규칙이 적용됩니다.

- **`/` 로 시작하는 줄**만 내부 CLI 명령(`/help`, `/exit`, `/clear` 등)으로 처리합니다.
- **그 외 모든 입력**은 학습 에이전트(`AIAgent`)로 그대로 전달됩니다.
- 알 수 없는 `/` 명령은 안내 메시지 후 다시 입력을 받습니다.
- `/clear` 시 **세션 ID를 새로 발급**하고 에이전트 인스턴스를 재생성해 대화 메모리를 비웁니다.

이 내용은 코드랩 본문이 아니라 **배포·운영 문서**용으로 여기에만 정리했습니다.

## 문제 해결

- **`GOOGLE_API_KEY` 미설정**: 프로그램 시작 시 오류로 종료합니다. 셸에서 export 후 다시 실행하세요.
- **할당량 / API 오류**: Gemini 무료 등급 한도 등 — 터미널에 출력되는 안내와 [Gemini rate limits](https://ai.google.dev/gemini-api/docs/rate-limits) 문서를 참고하세요.

---

© GDG on Campus Korea · Build with AI 2026 코드랩
