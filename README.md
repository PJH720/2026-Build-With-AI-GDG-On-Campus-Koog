# Study Buddy Agent — CLI deployment

**CLI deployment** guide for the Kotlin / Koog–based Study Buddy learning-agent codelab. Workshop context (Korean) lives in [README.ko.md](./README.ko.md).

## Workshop context

- **Series**: Build with AI 2026 · GDG on Campus Korea codelab
- **Event page**: https://event-us.kr/gdgcampuskorea/event/122871
- **Codelab**: https://l2hyunwoo.github.io/koog-practice-univ/codelab/koog-study-buddy-agent
- **Upstream codelab repo**: https://github.com/l2hyunwoo/2026-Build-With-AI-GDG-On-Campus-Koog

### Branch map

| Branch       | Contents                                              |
| ------------ | ----------------------------------------------------- |
| `initial`    | Skeleton — build setup + data files                   |
| `page3`      | First agent + role assignment                         |
| `page4`      | Read lecture materials → review notes                 |
| `page5`      | Assignment analysis + note reuse                      |
| `page6`      | ChatMemory interactive homework help                  |
| `page7`      | Auto-generated exam-prep materials                    |
| `page8`      | Multi-agent learning-expert team                      |
| `page9`      | CLI design + packaging                                |
| `complete`   | Final polished version                                |

```bash
git checkout initial    # start the codelab
git checkout page5      # peek ahead if stuck
```

## Tech stack

- Kotlin 2.3.21
- Koog 0.8.0 (`koog-agents`)
- Google Gemini API (`GOOGLE_API_KEY`)
- Gradle · JDK 21

Optional Koog features in this project: chat memory (`agents-features-memory`), event/tool logging (`agents-features-event-handler`). Gradle applies JVM `--enable-native-access=ALL-UNNAMED` when running tasks.

## GitHub Releases

- **Releases:** [github.com/PJH720/2026-Build-With-AI-GDG-On-Campus-Koog/releases](https://github.com/PJH720/2026-Build-With-AI-GDG-On-Campus-Koog/releases)
- **v1.0.0** — See that release’s notes for changelog and usage. You can attach the ZIP produced locally by `./gradlew distZip` (for example `build/distributions/study-buddy-agent-codelab-1.0.0.zip`) to a GitHub Release.

## How to use

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

Default mode is an **interactive REPL** (banner → prompt **`학생 >`** waits for input). To run the **batch pipeline** where three agents read files and produce notes, use:

```bash
./gradlew run -- --team
# or
KOOG_MODE=team ./gradlew run
```

The `run` task uses `standardInput = System.in` for interactive stdin. Use a real **terminal (TTY)** so prompts behave the same when you run the packaged CLI below.

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
- To copy to another machine, archive the entire **`study-buddy-agent-codelab`** folder under `build/install/`.

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

### Quick reference

| Goal                         | Command                                                                 |
| ---------------------------- | ----------------------------------------------------------------------- |
| Dev run (interactive default)| `./gradlew run`                                                         |
| Batch team pipeline          | `./gradlew run -- --team`                                               |
| Local install folder         | `./gradlew installDist` → `build/install/study-buddy-agent-codelab/bin/study-buddy-agent-codelab` |
| ZIP artifact                 | `./gradlew distZip` → unzip → run `bin/` script                         |

Requirements: **JDK 21**, **`GOOGLE_API_KEY`**. For interactive input, use a **TTY terminal**.

## Default entry point

By default, `main()` runs **`runStudySession`** (interactive REPL with banner, `/help`, and agent chat). To run the **multi-agent batch pipeline** (`runStudyTeam`) with the same binary, pass **`--team`** or set **`KOOG_MODE=team`** (also accepts `batch` / `pipeline`).

## Interactive session (`runStudySession`) input rules

In interactive mode (default, `--repl`, `KOOG_MODE=repl`, etc.):

- Lines **starting with `/`** are treated as internal CLI commands (`/help`, `/exit`, `/clear`, …).
- **All other input** is forwarded unchanged to the learning agent (`AIAgent`).
- Unknown `/` commands print a hint and prompt again.
- **`/clear`** issues a **new session ID**, recreates the agent instance, and clears conversational memory.

This section is **deployment / operations** documentation; it is not duplicated in the hosted codelab steps.

## Troubleshooting

- **Missing `GOOGLE_API_KEY`**: the program exits with an error at startup. Export it in your shell and rerun.
- **Quota / API errors**: free-tier limits and similar — follow terminal messages and [Gemini rate limits](https://ai.google.dev/gemini-api/docs/rate-limits).

## License

MIT

---

© GDG on Campus Korea · Build with AI 2026 codelab
