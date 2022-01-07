# No Corona Zone (코로나안전지대)

: COVID-19(코로나 바이러스)가 없는 안전한 곳

### 1. 시연영상

###### 	클릭시 해당 링크로 이동합니다

<div style="display: flex;">
<div>
	<a href="https://youtu.be/8StQhMiavKE" target="_blank"><image src = "https://img.youtube.com/vi/8StQhMiavKE/mqdefault.jpg"></a>	
</div>
<div>
	<a href="https://youtu.be/LH1cPDe83Sk" target="_blank"><image src = "https://img.youtube.com/vi/LH1cPDe83Sk/mqdefault.jpg"></a>	
</div>
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

![erd](https://user-images.githubusercontent.com/81146582/137341200-397975be-4a60-40b1-84f1-d1e50eb08880.png)



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

#### 하지수 (팀장)

- 메인 페이지 실시간 현황 및 컬러 지도

  지도 각 지역을 알맞은 위치의 버튼으로 넣어야 하는 문제

  -> 팀원들과 함께 PhotoShop으로 직접 이미지 편집

  DB의 데이터를 상대값으로 대신해 각 지역 지도 이미지 색을 변경해야 하는 문제

  -> png 파일을 svg로 변경하여 색 변경이 용이하도록 함

  -> fill 속성 내 rgba()의 alpha 값(0~1) 에 계산한 상대값을 대입해 해결

- 백신 접종 현황 및 컬러 지도

  Ajax로 요청한 API 데이터는 json이므로 메인에서 사용한 방식대로 지도의 색을 변경할 수 없는 문제

  -> jquery 내에서 html style 을 바꾸는 $('<style>').text(css 내용).appendTo(html태그)로 해결



#### 이종완

- SK API 코로나 데이터 수집

  필수 파라미터 전달 시 지역명을 통일시켜 보내야 하는 문제

  -> 필수 파라미터의 지역명을 배열을 만들어 더 효율적으로 해결

  json 방식의 파일을 DB에 저장하는 문제

  -> restTemplate.exchange() 함수를 활용하여 Map 형식으로 값을 받아서 해결

  동일한 지역명 DB 저장 시 덮어씌워지는 문제

  -> hashMap을 이용하여 해결



#### 조경혜

- Spring WebSocket을 이용한 채팅

  node.js의 socket으로 구현을 시도했지만 Spring에서의 node.js 활용은 쉽지 않아, Spring WebSocket으로 다시 시도

  -> 따로 프로젝트를 생성하여 구현 후 NCZ 프로젝트에 적용

- 프로필 이미지 DB 저장 및 출력

  ajax로 이미지 업로드 시 서버에서 인지 못하는 문제

  -> Javascript에서 form-data 설정하고 csrf 토큰 값을 함께 전송,  서버에서는 MultipartFile 사용으로 파일을 인지하고 전송과 저장이 가능하도록 해결

  업로드된 이미지를 업로드한 사용자와 이어주는 문제

  -> @OneToOne과 mappedBy property를 사용하여 해결

  이미지 업로드 시 이미 동일한 파일명이 존재할 때 생기는 에러 발생 문제

  -> 파일명을 사용자 고유 번호로 변경함으로 해결

  이미지 업로드가 계속 될수록 프로젝트 용량이 커지는 문제

  -> 같은 이름으로 저장 시도 시 기존 파일에 덮어씌움으로 해결



#### 황수아

- Python 활용 기사, 거리두기 단계 스크래핑, Linux 사용

  Linux 터미널에서 war 파일을 실행하여 테스트하는 중 csv 파일을 생성하는 부분에서 csv 파일명 뒤 공백이 생겨 ArticleService에서 인식 못하는 문제

  -> OS 별 다른 개행문자로 인한 오류로 추정하여, 인자 받을 시에 re.sub으로 필요없는 부분을 치환하여 해결

- 팔로잉, 팔로워 구현

  나열된 리스트에 추가된 각 상황에 따른 버튼과 이벤트의 click 이벤트가 발생하지 않는 문제

  -> 리스트 나열 시 Thymeleaf의 each에 num 추가, 그 num을 버튼의 id값에 index값으로 덧붙여 이벤트 함수를 다르게 지정하여 해결

- CO-SNS

  회원가입 직후 다른 사용자 프로필 요청 시 에러 발생

  -> 영속성 문제로 매개변수의 member를 find()하여 다시 member에 넣음으로써 detach된 상태를 attach된 상태로 만들어 해결



#### 김현영

- Oauth2.0 활용 타 플랫폼 간편 로그인

  API 제공 Javascript로 구현 시 인증이 안되는 문제

  -> Oauth 프로토콜 사용으로 해결

  Facebook API, provider 사용 시 DB update가 안되는 문제

  -> Oauth를 좀 더 공부 후 문제 해결 예정

- YouTube API

  Ajax json 형식의 응답 data를 View로 전달하는 문제

  -> 요청받은 data를 jquery로 해당 View에 append 함으로써 해결

- CO-SNS View

  header의 이미지가 특정 페이지에서만 안보이는 문제

  -> 이미지에 Thymeleaf 적용하여 해결


### 8. 향후 개선 사항

- 모바일 웹 최적화를 위해 모바일 기기의 카메라앱과 갤러리앱 연결 및 GPS
- OpenCV를 이용한 백신 접종 완료 증명서 검증
- CO-SNS의 실시간 트렌드 기능
- AWS를 이용한 호스팅
