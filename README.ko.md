# 과제 끝내기 에이전트 — Kotlin + Koog로 나만의 학습 도우미 만들기

Build with AI 2026 | GDG on Campus Korea 코드랩 프로젝트

## 행사 정보

- **행사 페이지**: https://event-us.kr/gdgcampuskorea/event/122871
- **세션 이름**: 5월부터 과제는 에이전트에게 맡기고 대학생활 즐겨보기(feat. Kotlin)

## 코드랩

- **코드랩 링크**: https://l2hyunwoo.github.io/koog-practice-univ/codelab/koog-study-buddy-agent
- **코드랩 레포(업스트림)**: https://github.com/l2hyunwoo/2026-Build-With-AI-GDG-On-Campus-Koog

## GitHub 릴리스

- **릴리스**: [github.com/PJH720/2026-Build-With-AI-GDG-On-Campus-Koog/releases](https://github.com/PJH720/2026-Build-With-AI-GDG-On-Campus-Koog/releases)
- **v1.0.0** — 릴리스 노트에서 변경 사항과 사용법을 확인하세요. 로컬에서 `./gradlew distZip`으로 만든 ZIP(예: `build/distributions/study-buddy-agent-codelab-1.0.0.zip`)을 GitHub Release에 첨부할 수 있습니다.

## 브랜치 구조

| 브랜치 | 내용 |
|--------|------|
| `initial` (기본) | 프로젝트 스켈레톤 — 빌드 설정 + 데이터 파일 |
| `page3` | 첫 에이전트 + 역할 부여 |
| `page4` | 강의자료 읽기 + 복습 노트 생성 |
| `page5` | 과제 분석 + 노트 활용 |
| `page6` | ChatMemory 대화형 과제 도움 |
| `page7` | 시험 대비 자료 자동 생성 |
| `page8` | Multi-Agent 학습 전문가 팀 |
| `page9` | CLI 디자인 + 배포 설정 |
| `complete` | 최종 완성본 |

```bash
# 코드랩 시작
git checkout initial

# 막혔을 때 정답 확인
git checkout page5
```

## 기술 스택

- Kotlin 2.3.21
- Koog 0.8.0
- Google Gemini API
- Gradle

## 라이선스

MIT
