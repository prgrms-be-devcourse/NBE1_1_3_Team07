
<img width="132" alt="로고" src="https://github.com/user-attachments/assets/f381eb6e-5c46-48ad-8df2-e4718b7784f1">

## NBE1_2_Team07

Programmers DevCourse BE 1기 2차 7팀 프로젝트

***

# 1. 개요
<img width="50%" alt="대표이미지" src="https://github.com/user-attachments/assets/b6d8693c-d524-47b6-b2ee-32edd1ab99cd">

## 프로젝트 명

**개발자 신입 및 취업 준비생을 위한 IT 뉴스 레터 : develetter**

## 기획 의도

 develetter는 신입 개발자와 취업 준비생을 위한 맞춤형 뉴스레터 서비스입니다. 기술 환경이 빠르게 변화하는 요즘, 우리는 종종 필터 버블에 갇혀 중요한 정보를 놓치거나, 반대로 과도한 정보로 인해 콘텐츠 피로를 겪곤 합니다.

 이런 문제를 해결하고자 develetter를 기획했습니다. 우리는 사용자의 관심사에 맞춘 채용 공고, 컨퍼런스 일정, 개발 블로그 콘텐츠를 정기적으로 이메일로 제공합니다. 개인화된 필터링을 통해 꼭 필요한 정보만을 정확하게 전달함으로써, 신입 개발자와 취업 준비생들의 성장을 돕는 것이 develetter의 목표입니다.

## 팀원 소개

| 이름 | Github | 역할 |
| --- | --- | --- |
| [팀장] 조재중 | https://github.com/m-a-king | 채용 정보, 프론트 |
| 배건우 | https://github.com/CodeItpython | 회원가입 및 로그인처리 |
| 송경훈 | https://github.com/rudgns328 | 채용 정보 |
| 양아영 | https://github.com/ayoung-dev | 메일 발송 |
| 최철진 | https://github.com/Cheol-Jin | 기술 블로그 |

## 개발 진행 상황
[GitHub Project를 통해 진행 상황 관리](https://github.com/orgs/prgrms-be-devcourse/projects/44/views/4)

## 개발 기간

1차 개발 | 2024/09/23 ~ 2024/10/10

2차 개발 | 2024/10/11 ~ 2024/11/06

---

# 2. 주요 기능

### 회원 관리

- **회원가입(일반/SNS 연동)**
- **로그인/로그아웃**
- **구독 신청/취소**
- **마이페이지 - 관심 키워드 설정**

### 메일

- **맞춤형 정보(채용공고, 기술 블로그, 컨퍼런스) 정기 발송**

### 채용공고

- **사람인 API로 채용 정보 파싱**
- **회원의 관심 키워드에 맞는 채용 공고 제공**

### 기술 블로그

- **구글 검색 API로 기술 블로그 내용 파싱**
- **회원의 관심 키워드에 맞는 기술 블로그 제공**

### 컨퍼런스

- **IT 관련 컨퍼런스 정보 제공**
  
---

## 상세 기능

**회원관리**

- 이메일 인증을 통한 회원가입
- Thymeleaf를 통해 동적HTML사용하여 인증메일 템플릿 생성
- jwt토큰 발급을통한 로그인처리로 안정화된 로그인서비스 제공
- DB에 저장되는 password를 PasswordEncoder를 사용하여 Encrypt

**메일**

- Scheduler를 통해 정기적으로 메일 발송
- 메일 발송 후 deleted 컬럼 update로 Mail Table 컬럼 soft delete
- Thymeleaf를 통해 동적 HTML 생성하여 메일 템플릿 생성

**채용공고**

- WebClient 라이브러리를 사용하여 사람인 채용 공고 API 호출 및 응답 데이터 저장
- Spring Batch를 통해 사용자마다 맞춤화 된 채용 공고 필터링(QuerydslNoOffsetPagingItemReader를 활용한 성능 개선)
- Scheduler를 통해 주기적으로 사람인 채용 공고 업데이트 및 맞춤화 된 채용 공고 필터링

**기술 블로그**

- Google Programmable Search Engine API 사용하여 기술 블로그 데이터 탐색 및 DB 저장
- Scheduler를 통해 주기적으로 기술 블로그 데이터 업데이트 및 사용자 맞춤 관심사에 기반한 필터링

**컨퍼런스**

- 관리자에 의한 컨퍼런스 일정 CRUD
</aside>

---

# 3. ERD
<img width="521" alt="erd" src="https://github.com/user-attachments/assets/f784166a-1891-410c-bd96-92f767e89196">

---

# 4. 화면 설계

[develetter Figma](https://embed.figma.com/design/kqnI5bYEDtzKxzMLFlYNlp/develetter?node-id=0-1&node-type=canvas&t=JVjCxHuVzjakaIBT-0&embed-host=notion&footer=false&theme=system)

[맛보기 시연 화면 Vercel](https://develetter-fe.vercel.app/)

---

# 5. 요구사항 명세

[요구사항 명세 - Google Sheets](https://docs.google.com/spreadsheets/d/1ZPpwRtQhfJMNHB68tt5XvnMUpD6XriXt35Ug6cR5_mA/edit?gid=0#gid=0)

<img width="1739" alt="요구사항명세" src="https://github.com/user-attachments/assets/182d31b2-b3c2-40de-bc47-371700036431">

---

# 6. API 명세

[Postman API 명세](https://programmers-7-teeam.postman.co/workspace/d6e3fd25-4044-49a6-8410-82ad9dbbde13/documentation/37784458-a6b40313-d6f5-4b6f-aa45-2c53c2e0adf4)

- User를 제외한 나머지 기능의 호출은 Scheduler를 통해서 관리됩니다.

---

# 7. 개발 환경

- OS : Mac
- IDE : JetBrain IntelliJ

| Software | 세부 Spec 사양 (Version) |
| --- | --- |
| Java | Java 17 |
| Spring Boot | 3.3.4 |
| Spring Boot Libraries | Batch, Data JPA, JDBC, Mail, Oauth2, Web, Validation, Security, Thymeleaf: 3.3.4 |
| Lombok | 1.18.20 |
| QueryDSL | JPA: 5.0.0 |
| MySQL Connector | 8.3.0 |
| Jwt | jwt:0.11.2 |

### **협업도구**

- Discord
- Slack
- Notion
- Github
- JetBrain Code With Me

### 코딩 컨벤션

[Git  Convention](https://www.notion.so/Git-Convention-bac0523312b046d6908f50d2d81fd3e8?pvs=21)

[Code Convention](https://www.notion.so/Code-Convention-1b666e9e4ef04237b97004d0b79bae3b?pvs=21)

---

# 9. 트러블 슈팅

[트러블 슈팅](https://www.notion.so/65f3422e2062484eaf061aa8d867f28d?pvs=21)

# 10. 추후 개발 예정

  건우 - 이메일 인증을 링크를통해 인증하는 기능 개발
  
  경훈 - Spring Batch를 통해 모든 유저에게 맞춤화 된 채용 공고를 필터링할 때 비동기적으로 리팩토링
  
  아영 - 메일 전송 spring batch 사용
  
  재중 - 회원 탈퇴, 구독 중인 키워드 표기, ~~패키지를 기반으로 MSA 구조~~ 
  
  철진 - 기술 블로그 데이터 관련 Spring Batch 사용
