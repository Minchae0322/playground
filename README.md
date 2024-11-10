# “HIT 운동장” 운동장 스포츠 관리 및 예약 시스템

https://successful-mandevilla-ead.notion.site/HIT-38a9d3a232784c88bc2002da1cbfb8a8?pvs=74

작업 영역: 1인프로젝트, 중국어, 최근프로젝트, 풀스택
작업기간: 2023년 11월 30일 → 2024년 3월 1일
Github: https://github.com/Minchae0322/playground
상태: 유지보수중

## 📽️  프로젝트 개요

**중국어를 사용**하는 프로젝트입니다.

학교 학우들이 연습경기 혹은 여가 시간에 즐길 **경기들을 만들고, 인원을 모으는데 어려움을 느끼는 것을 보고** 웹 프로젝트를 기획하게 되었습니다.

원하는 종목의 운동장을 선택 후, 시간을 지정하여 경기를 만들어 팀원을 모집할 수 있게 기획하였고, 경쟁 경기와 친선 경기로 나누고, 팀에서 참가와, 개인으로 참가 등 경기 유형을 세부적으로 분류하였습니다.

**웹 서버 구축부터, 프론트엔드 인터페이스 디자인, AWS 를 이용한 서버 배포**까지 진행하였습니다.

## ✍️ 프로젝트 기술 스택

BACKEND - Spring boot, Spring security, OAuth2, JPA, MySQL, JWT

![Untitled](Untitled.png)

- **Spring boot, JPA, MySQL 을 이용**하여 프로젝트의 전반적인 기능을 구현하였습니다.
- Spring Security 의 **OAuth2 서비스를 이용**해 회원가입 또는 로그인을 진행한 후, JWT 를 발급 AuthenticationFilter 이전에 **CustomFilter 를 만들어 JWT 의 유효성을 검증한 후**, 요청에 대한 작업을 수행하도록 **Stateless 환경**을 구현하였습니다.

SERVER - Nginx, Docker, EC2, LetsEncrypt

![Untitled](Untitled%201.png)

- 서버는 LetsEncrypt 로 부터 인증서를 발급받고, 80 포트로 오는 요청을 443 포트로 Redirect 하여 **HTTPS 암호화 프로토콜을 구현**하였습니다.
- EC2 Container 에서 Docker-Composer 를 활용해  FrontContainer(dist, nginx),  BackendContainer(Spring boot), MySQLContainer(MySql) **도커 환경을 구성**하였습니다.
- Nginx 가 URL “/” 로 부터 오는 요청을 정적으로 처리하고, “/api/” 요청은 Backend 서버로 요청합니다.

FRONTEND - Vue, Axios

- **반응형 유저 인터페이스**를 구성하였습니다.
- Axios 를 통해 REST API 를 호출하였습니다.

## 🛖 프로젝트 구조

![Untitled](Untitled%202.png)

### Athletics 폴더 Contoller-Service 파일 구조 (간략)

![Untitled](Untitled%203.png)

운동 경기 디렉토리의 Controller- Service 구조도

- 객체 지향 설계 원칙과 **SOLID 개발 원칙을 준수**하려고 노력하였고, **주기적인 리팩토링** 으로 코드의 품질을 높혔습니다.
- 다양한 유형의 운동 경기들이 추가 될 것을 예상하여 **Factory 패턴과 전략 패턴**을 적용하였습니다.
- 함수의 길이는 모두 15 줄 이하로 **유지 보수가 쉽고 가독성이 좋게** 작성하였습니다.

## 🏹 Trouble shooting

![Untitled](Untitled%204.png)

- 운동장에서 경기를 생성할 때, 운동장 타임 테이블 시간 중복 검사 후 중복되지 않으면 생성하도록 하였지만, **1초 동안 1000번 이상의 같은 요청이 들어올 때 같은 시간에 경기가 중복 생성되는 문제가 있었습니다.**

### 해결 방안 1:

- MySQL의 격리 수준은 `Repeatable Read` 로 설정되어 있었으나, 데이터가 추가되기 전에  운동 경기(ArrayList) 로 오류 검사를 하면서 `Phantom Read`가 발생한 것으로 판단하였습니다.
- 경기 만드는 함수를 한 @Transaction 으로 설정하고, Playground 를 수정할 때  **비관적 Write Lock** 을 걸어 동일 시간 중복 생성을 방지하였습니다.
- **하지만** JPA 의 N+1 문제로 수 많은 쿼리가 생성되었고, 비관적 Lock 으로 자원 읽기 대기 속도가 기하급수적으로 늘어났습니다.

(0.1초동안 100개의 요청 평균응답 시간 : 20718ms)

![Untitled](Untitled%205.png)

### 해결 방안 2:

**JPA 에서는 가져온 엔티티를 영속성 콘텍스트에 캐싱하여 많은 데이터가 한번에 요청될 때 동시성 문제가 발생한 것으로 판단했습니다, 엔티티를 캐싱하지 않는 QueryDSL 로 SQL 을 날려,  N + 1 문제를 야기하는 엔티티들을 join** 하고 필요한 변수만 추출하니 동시성 문제도 해결되고 속도도 증가하였습니다. 하지만 문제 해결을 했다고 생각했으나, 관련 내용을 깊이 학습 후,  근본적인 문제점이 아닌것을 파악 후 fetch join 하여 근본적인 문제점을 해결하였습니다.

(0.1초동안 100개의 요청 **평균응답 시간 : 333ms) - 98.4% 감소**

![Untitled](Untitled%206.png)

## 💽 데이터 베이스 Entity 설계

![Untitled](Untitled%207.png)

- 정규화 **BCNF 형**을 준수하였습니다.

## 🏹 요구 사항 분석

![Untitled](Untitled%208.png)

- 과 축구부에 속해있었는데, 축구 부원들이 연습경기를 하고 싶어할 때 같이 할 인원을 구하는데 어려움을 느끼는 것을 보고 웹 개발을 시작하였습니다.

## 🎢 프로젝트 기능

![Untitled](Untitled%209.png)

- 부원들의 같은 단톡방에 참여해 있지 않으면 정보 전달 및 지원자를 구하기 어렵다는 요구사항을 듣고, 생각한 페이지들을 구성하였습니다.

![Untitled](Untitled%2010.png)

- **로그인 페이지** - OAuth2 를 활용한 로그인
- **유저 정보 페이지** - 프로필 사진 변경, 닉네임 변경, 현재 속해있는 팀, 로그아웃, 전적 표시

![Untitled](Untitled%2011.png)

![Untitled](Untitled%2012.png)

- **Home 페이지** - 최근 순서대로 진행 될 경기들을 표시
- **운동장 리스트 페이지** - 종목별로 운동장을 선택 후, 캠퍼스를 선택, 해당 운동장 정보와 곧 시작될 경기의 개수 표시

![Untitled](Untitled%2013.png)

![Untitled](Untitled%2014.png)

**운동장 세부 페이지** - 운동장에서 진행될 경기들을 표시, 경기를 클릭하면 각 경기 유형에 맞게 참가 요청을 보낼 수 있음

![Untitled](Untitled%2015.png)

![Untitled](Untitled%2016.png)

![Untitled](Untitled%2017.png)

**경기 만들기 페이지** - 경기 제목, 시간을 선택 후 만들기

**팀 목록 페이지** - 현재 있는 팀 리스트를 보여줍니다.

![Untitled](Untitled%2018.png)

![Untitled](Untitled%2019.png)

**참여 경기 관리 페이지** - 참여한 경기들을 보여줍니다.

**주최 경기 관리 페이지** - 주최한 경기들을 보여줍니다, 경기 결과를 입력 할 수 있습니다.

+경기 요청 관리 페이지, 랭킹 페이지, 팀 요청 페이지, 팀 정보 페이지 등이 있습니다.
