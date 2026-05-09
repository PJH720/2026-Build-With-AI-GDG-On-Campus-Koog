# Study Buddy Agent — CLI deployment

Kotlin / Koog 기반 학습 에이전트 코드랩의 **CLI 배포** 안내입니다. 행사·코드랩 문맥은 [README.ko.md](./README.ko.md)를 참고하세요.

## GitHub Releases

- **릴리스 페이지:** [github.com/PJH720/2026-Build-With-AI-GDG-On-Campus-Koog/releases](https://github.com/PJH720/2026-Build-With-AI-GDG-On-Campus-Koog/releases)
- **v1.0.0** — 변경 요약·사용 안내는 해당 릴리스 노트를 보세요. 로컬에서 `./gradlew distZip`로 만든 `build/distributions/study-buddy-agent-codelab-1.0.0.zip`을 릴리스에 첨부해 배포할 수 있습니다.

**스택(요약):** JDK 21 · [Koog](https://github.com/JetBrains/koog) `koog-agents` **0.8.0** · 채팅 메모리(`agents-features-memory`) · 이벤트/도구 로깅(`agents-features-event-handler`) · Google Gemini (`GOOGLE_API_KEY`). Gradle 실행 시 JVM에 `--enable-native-access=ALL-UNNAMED`가 적용됩니다.

## How to Use

### 1. Prerequisites

- **JDK 21** — must match the JVM toolchain configured in Gradle.
- **`GOOGLE_API_KEY`** — set your Google Gemini API key in the environment.

```bash
export GOOGLE_API_KEY='YOUR_API_KEY'
```

### 2. Run during development

```bash
./gradlew run
```

기본 모드는 **대화형 REPL**(배너 → `학생 >` 입력 대기). 세 에이전트가 파일을 읽고 노트를 만드는 **일괄 파이프라인**은 `./gradlew run -- --team` 또는 `KOOG_MODE=team ./gradlew run` 입니다.

The `run` task uses `standardInput = System.in` for interactive stdin. Use a real **terminal (TTY)** so prompts work the same way when you run the packaged CLI below.

### 3. Build an unpacked distribution (`installDist`)

Creates `build/install/study-buddy-agent-codelab/` with `bin/` and `lib/`.

```bash
./gradlew installDist
```

Run:

```bash
cd build/install/study-buddy-agent-codelab
./bin/study-buddy-agent-codelab
```

- **Windows**: `build\install\study-buddy-agent-codelab\bin\study-buddy-agent-codelab.bat`
- To copy to another machine, archive the whole **`study-buddy-agent-codelab`** folder under `build/install/`.

### 4. Build a ZIP archive (`distZip`)

```bash
./gradlew distZip
```

Artifact (version comes from `build.gradle.kts`):

- **`build/distributions/study-buddy-agent-codelab-1.0.0.zip`**

After unzipping you get a root folder such as `study-buddy-agent-codelab-1.0.0/` containing `bin/` and `lib/`. With `GOOGLE_API_KEY` set:

```bash
cd study-buddy-agent-codelab-1.0.0
./bin/study-buddy-agent-codelab
```

Gradle may also emit a `.tar` next to the ZIP under `build/distributions/`; either format has the same layout.

---

### 한국어 요약

| 목적 | 명령 |
|------|------|
| 개발 실행 (대화형 기본) | `./gradlew run` |
| 일괄 팀 파이프라인 | `./gradlew run -- --team` |
| 로컬 배포 폴더 생성 | `./gradlew installDist` → `build/install/study-buddy-agent-codelab/bin/study-buddy-agent-codelab` |
| ZIP 배포물 | `./gradlew distZip` → `build/distributions/study-buddy-agent-codelab-1.0.0.zip` 압축 해제 후 `bin/` 스크립트 실행 |

사전 조건: **JDK 21**, 환경 변수 **`GOOGLE_API_KEY`** 설정. 대화형 입력은 **TTY 터미널**에서 실행하세요.

## 기본 진입점 동작

`main()` 기본값은 **`runStudySession`** (대화형 REPL, 배너·`/help`·에이전트 대화). 같은 바이너리로 **Multi-Agent 팀 일괄 파이프라인**(`runStudyTeam`)을 쓰려면 실행 인자 **`--team`** 또는 환경 변수 **`KOOG_MODE=team`** (또는 `batch`, `pipeline`)을 사용합니다.

## 대화형 과제 세션 (`runStudySession`) 입력 규칙

대화형 모드(`기본`, `--repl`, `KOOG_MODE=repl` 등)에서는 다음 규칙이 적용됩니다.

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
