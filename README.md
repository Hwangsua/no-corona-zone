# No Corona Zone (코로나안전지대)

: COVID-19(코로나 바이러스)가 없는 안전한 곳

### 1. 시연영상

###### 	클릭시 해당 링크로 이동합니다

<div>
	<a href="https://youtu.be/8StQhMiavKE" target="_blank"><image src = "https://img.youtube.com/vi/8StQhMiavKE/mqdefault.jpg"></a>	

</div>

### 2. 프로젝트 주제

- COVID-19(이하 코로나)로 인한 팬데믹 상황에서 안전하게 외출하고 싶은 사람들을 위한
  **코로나 확진**, **인구 밀집**, **백신 정보** 제공

- 코로나 관련 **기사와** **영상**, **선별진료소 지도 검색** 그리고 **실시간 채팅** 컨텐츠 제공

- 자체 **위치기반 소셜 네트워크** 구축, **모바일 웹** 화면 제공

  →  확진 발생 지역 뿐만 아니라 현재 인구 밀집 지역도 함께 파악할 수 있으므로 현 시국에서 높은 활용도 기대



### 3. 개발환경

#### 	Backend

- Java : JDK 11

- Python 3.8.5

- Framework : Spring Boot 2.5.3

- IDE : IntelliJ

- DBMS : H2 1.4.200

- WAS : Tomcat



#### 	Frontend

- Bootstrap 4.5.2

- Template Engine Thymeleaf

- HTML 5 CSS

- JavaScript



### 4. 사용 API

- SK Open API
- KOSIS API
- 공공데이터포털
- YouTube API
- Kakao Maps API
- Google API
- NAVER API



### 5. ERD

![](D:\메가IT\final_project_nocoronazone\마무리\피피티\피티파일\erd.png)



### 6. 팀원 역할

#### 	하지수 (팀장)

- ERD 공동 설계
- 전체 페이지 View 및 레이아웃 디자인
- 확진, 밀집, 거리두기 데이터 활용 메인 View
- 질병관리청 데이터 API, KOSIS API 활용 백신 접종 현황 및 지도 View



#### 	이종완

- SK API 활용 코로나 데이터 수집
- Kakao API 활용 선별진료소 검색



#### 	조경혜

- ERD 공동 설계
- DB CRUD 구현
- Spring WebSocket 채팅
- CO-SNS 로직 구현 (로그인, 글쓰기, 회원정보수정, 회원프로필)



#### 	황수아

- 기사 스크래핑
- 파이썬 파싱 및 리눅스 작업
- Kakao API 활용 글쓰기 위치 추가 및 글 검색
- CO-SNS 로직 구현 (회원가입, 팔로우, 댓글)



#### 	김현영

- CO-SNS 전체 페이지 View
- Oauth 2.0 활용 Google, Naver 간편 로그인
- YouTube API 활용 영상 목록 View



### 7. 힘들었던 점과 해결 방법

첨부된 NoCoronaZone.pdf 파일 참조



### 8. 향후 개선 사항

- 모바일 웹 최적화를 위해 모바일 기기의 카메라앱과 갤러리앱 연결 및 GPS
- OpenCV를 이용한 백신 접종 완료 증명서 검증
- CO-SNS의 실시간 트렌드 기능
- AWS를 이용한 호스팅
