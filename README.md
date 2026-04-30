# 📱 TonePack: 상황 맞춤형 커뮤니케이션 템플릿 플랫폼

> **"어떻게 말해야 할지 고민되는 순간, 당신의 톤을 맞춤 설정하세요."**
> Z세대 대학생과 사회초년생을 위한 TPO(상황·상대) 기반 커뮤니케이션 가이드 및 템플릿 공유 플랫폼

---

### 📋 프로젝트 개요
- **팀명** : SWU GURU2 31조 - **TonePack**
- **개발 기간** : 2025.12 ~ 2026.01
- **핵심 타겟** : 메일 형식이 낯선 대학생, 상사와 소통이 어려운 취준생 및 사회초년생
- **핵심 가치** : '틀릴까 봐' 생기는 심리적 부담을 줄이고, 검증된 템플릿으로 소통 효율 극대화

---

### ✨ 주요 기능

#### 1. 지능형 복합 필터링 (Multi-Filtering)
- 상황과 상대 카테고리를 교차 선택하여 현재 **TPO**에 가장 적합한 **문구**를 즉시 탐색합니다.
- 단순 검색이 아닌 맥락 기반 추천으로 작성 시간을 획기적으로 단축합니다.
- **카테고리 구성**
  - **상황** : 출근/인사, 업무 보고, 요청/문의, 거절/사과, 경조사/휴가, 일상/기타
  - **타겟** : 상사/선배, 동료/동기, 후배/신입, 협력사/업체, 교수/교직원/공적관계

#### 2. 검증된 현실 기반 템플릿 & 커뮤니티
- 개발자 제공 데이터뿐만 아니라 사용자들이 실전에서 겪은 성공 사례를 공유하는 집단 지성 플랫폼
- 운영자가 선별한 '팀플 빌런 방지', '비즈니스 에티켓', '수강신청 팁' 등 실질적인 가이드 제공

#### 3. 신뢰도 기반 추천/비추천 시스템
- `LikeRecord` 테이블 설계를 통해 1인 1게시글 1투표 제한을 구현하여 데이터 무결성 확보
- 실제로 도움이 된 양질의 템플릿만 상단에 노출되어 정보의 퀄리티 보장

#### 4. 원클릭 클립보드 동기화
- 선택한 문구를 즉시 복사하여 카카오톡, 슬랙(Slack), 메일 등 외부 앱에 바로 붙여넣기 가능
- 오타 및 맞춤법 실수를 방지하여 커뮤니케이션 정확도 향상

#### 5. 마이페이지 개인화 아카이빙
- 본인이 작성한 템플릿 관리 및 본인만의 **'말투 팩(Tone Pack)'** 구축

---

### 🛠 기술 스택 및 아키텍처

#### Architecture & Framework
- **Pattern**: MVVM (ViewModel, Repository, LiveData)
- **Language**: Kotlin (Android)
- **UI**: XML 기반 화이트 & 블루 테마 디자인

#### Data Management
- **Local DB**: Room Persistence Library (오프라인 우선 아키텍처 채택)
- **Session**: DataStore & SharedPreferences (로그인 유지 및 세션 관리)

#### Database Schema
- **User Table**: 아이디(PK), 비밀번호 관리.
- **Template Table**: 인덱스(PK), 작성자 ID, 제목, 본문, 상황/상대 카테고리, 추천/비추천 수.
- **LikeRecord Table**: 사용자 ID + 게시글 Index 매핑을 통해 중복 투표 방지.

---

### 📂 파일 구조
```plaintext
com.tonepack.app
├── data
│   ├── local        # AppDatabase, DAO(User, Template), Entity(User, Template)
│   ├── repository   # AuthRepository, TemplateRepository
│   ├── seed         # 초기 데이터(SeedData) 주입 로직
│   └── session      # SessionManager (로그인 상태 관리)
├── ui
│   ├── auth         # Login, Register Activity & ViewModel
│   ├── main         # 필터링 및 템플릿 리스트 (MainViewModel)
│   ├── detail       # 상세 보기, 복사, 추천/비추천 기능
│   ├── editor       # 유저 템플릿 작성 화면
│   ├── community    # 운영자 팁 게시판 및 추천글 섹션
│   └── mypage       # 내 글 관리 및 삭제 (MyPageViewModel)
├── util             # ClipboardUtil, Extensions 등 공통 유틸리티
└── navigation       # Intent 상수 관리
```

---

### 👥 팀원 및 역할
| 이름 | 역할 | 담당 업무 |
| :--- | :--- | :--- |
| **전서연** | Backend / DB | Room DB 설계, 초기 데이터 구축, 필터링 및 로그인 로직 구현 |
| **박수민** | Main UI / List | 메인 화면 UI 디자인, RecyclerView Adapter 제작, 화면 전환 관리 |
| **민경** | Function / Detail | 클립보드 복사, 템플릿 등록/저장 기능, 추천 시스템 로직 구현 |
| **정현주** | MyPage / Contents | 마이페이지 UI 및 삭제 기능, 커뮤니티 팁 콘텐츠 기획 및 대본 작성 |

---

### 🔑 테스트 계정
ID: guru2
PW: guru2

---

### 💡 기대 효과
1. 사회초년생의 조직 적응력 향상: 비즈니스 프로토콜에 맞는 적절한 톤앤매너 가이드 제공
2. 대학 내 협업 효율 증대: 팀 프로젝트 소통 시 발생하는 심리적 부담과 오해 방지
3. 조직 문화 개선: 세대 및 직급 간 소통 격차를 줄이는 '안전한 소통' 플랫폼 역할


<hr style="background-color: #eee; height: 1px; border: 0;">

TonePack - Your Smart Communication Companion.
