# 👁️ EyesOn
<img src = "/uploads/c57ec9a73fccdc5dfde5d89b76d6ba0c/app_logo.png" width="160" height="160"> 
<img src = "/uploads/3837ce7829e4f82d353ca8b30ac3b954/logo_navi.png" width="280" height="150"> 
<img src = "/uploads/8e6aa1b8daaae9269860a38881c08f84/logo.png" width="280" height="150">

 [애플리케이션 아이콘]        [Eyes On 로고_1]               [Eyes On 로고_2]


#### **EyesOn** 프로젝트는 시각장애인 및 저시력자를 위한 보조 애플리케이션 입니다.

시각 장애의 92%는 후천적이며, 이들의 90%는 점자를 읽지 못합니다. 또한 인도의 높이가 낮은 장애물들은 흰지팡이로도 감지하기 어려워 큰 사고를 야기합니다. 
이러한 보행 중 위험과 생활 속 불편함을 조금이나마 해소할 수 있도록 저희는  


💡 **시각장애인의 눈[:Eyes] 을 뜨게[:On] 해주다.** 💡

라는 의미의 Eyes On(아이즈 온) 프로젝트를 진행하게 되었습니다.

## ERD

> 사용자, 엔젤, 민원 기능이 유기적으로 연결되어있고,
> 
> 
> 그를 효율적으로 활용하기 위해 ERD를 작성했습니다.
> 
<img src = "/uploads/b204fc11601468906e6af66dae7b6c3f/Untitled.png" width="900" height="400">

## 사용 기술

> 프로젝트에서 사용된 기술들입니다.
> 
<img src = "/uploads/13f90c545e2f8b90062c0eecc4c41d2e/tech_stack.png" width="900" height="400">

## 프로젝트 구조도

> 프로젝트 구조도 입니다.
> 
<img src = "/uploads/00a0bafc620ebc07d6b4d8c88aae7a6c/architecture.png" width="900" height="400">


## 문서

- **[기능명세서](https://www.notion.so/db1694e3f1c64543ab89e5627e562b82?v=c0cb789beddb4226ba396490b6da2afc)**
    - 프로젝트의 기능 명세서입니다.
- **[와이어프레임](https://www.figma.com/file/Fh80vZbnQpV1oeqbSWwYxf/%EC%9E%90%EC%9C%A8?node-id=27%3A28&t=DkyqGkomxJYHRJXZ-0)**
    - 피그마를 사용하여 제작된 와이어프레임
- **[API Documentation](https://www.notion.so/API-1227dcd49efd492383dbdfc127eaf667)**
    - RESTful 서버의 API 문서입니다.


# **📱Android**

### 디렉터리 구조도

```markdown
📁AOS
├──📁presentation
│ ├──📁base
│ ├──📁di
│ ├──📁module
│ ├──📁service
│ ├──📁util
│ ├──📁view
│ │ ├──📁binding
│ │ ├──📁angel
│ │ ├──📁blind
│ │ ├──📁intro
│ │ ├──📁loading
│ │ ├──📁login
│ │ │ ├──📁join
│ │ │ ├──📁login
├──📁domain
│ ├──📁base
│ ├──📁model
│ ├──📁repository
│ ├──📁usecase
│ │ │ ├──📁complaints
│ │ │ ├──📁help
│ │ │ ├──📁noti
│ │ │ ├──📁user
│ ├──📁utils
├──📁data
│ ├──📁api
│ ├──📁datasource
│ │ ├──📁paging
│ ├──📁mapper
│ ├──📁model
│ │ │ ├──📁entity
│ │ │ ├──📁request
│ │ │ ├──📁response
│ ├──📁repository(Impl)
│ ├──📁utils
├──📁mlkit
│ ├──📁objectdetecor
│ ├──📁textdetector
├──📁webrtc
│ ├──📁constants
│ ├──📁observers
│ ├──📁openvidu
│ ├──📁utils
│ ├──📁websocket
├──📁arcore
│ ├──📁common
│ ├──📁rendering
```

### 아키텍처

> 안드로이드는 **클린 아키텍처**를 도입했습니다.
> 

<img src = "/uploads/c5b84c93f95a04aaa4ea96e3c78de7f0/android_architecture.png" width="900" height="350">

### 사용한 라이브러리


	
| Name            | Description                                  |
| --------------- | -------------------------------------------- |
| Navigation      | 프래그먼트 전환 라이브러리                   |
| Glide           | 이미지 로딩 라이브러리                       |
| ViewModel       | 수명주기 고려 데이터를 저장, 관리 라이브러리 |
| Coroutine       | 비동기 처리 라이브러리                       |
| Coroutine Flow  | 비동기 데이터 스트림                         |
| TedPermission   | 안드로이드 권한 라이브러리                   |
| Retrofit        | HTTP 통신 라이브러리                         |
| Dagger Hilt     | 의존성 주입 라이브러리                       |
| okhttp3         | HTTP 통신 라이브러리                       |
| Google Auth     | 구글 소셜 로그인                             |
| Lottie          | 안드로이드 애니메이션 라이브러리             |
| MPAndroidChart  | 안드로이드 차트 라이브러리                  |
| ARCore          | 구글 증강 현실 플랫폼 API                  |
| ML Kit          | 구글 머신러닝 모바일 SDK                  |
| Paging 3        | 대규모 데이터 세트의 데이터 페이지 로드 라이브러리 |
| TensorFlow lite | 구글 머신러닝 오픈스스 모바일 라이브러리 |
| FCM             | 구글 알림 메시지 전송 라이브러리 |
| OpenVidu        | WebRTC 기반 화상 미팅 라이브러리 |



# **💽**BACK-END

### 디렉터리 구조

```markdown
📁BACK
├──📁main
│ ├──📁java
│ │ ├──📁config
│ │ ├──📁controller
│ │ ├──📁dto
│ │ ├──📁entity
│ │ ├──📁jwt
│ │ ├──📁mapper
│ │ ├──📁repository
│ │ ├──📁service
│ │ ├──📁util
│ ├──📁resources
│ │ ├──📁firebase
│ │ ├──📁templates
```
    

# ⭐주요기능

- **[Talkback 기능](./assets/md/talkback.md)**

## ❤ 회원가입

- **[시각 장애인 회원 가입](./assets/md/join_blind.md)**

- **[엔젤 회원 가입](./assets/md/join_angel.md)**

## 🤍 엔젤

- **[엔젤 알림 설정](./assets/md/angel_setting.md)**

- **[엔젤 도와주기](./assets/md/angel_help.md)**

- **[엔젤 민원 처리](./assets/md/angel_complaints.md)**


## 💜 시각장애인

- **[시각장애인 글자 인식](./assets/md/blind_scan_text.md)**

- **[시각장애인 물건 찾기](./assets/md/blind_find_object.md)**

- **[시각장애인 장애물 인식](./assets/md/blind_scan_obstacle.md)**

- **[시각장애인 민원 신청](./assets/md/blind_complaints.md)**

- **[시각장애인 엔젤과의 연결](./assets/md/blind_help.md)**


# 팀원 소개

### **🍀 TEAM EyesOn**

<table>
    <tr>
        <td height="140px" align="center"> <a href="https://github.com/us13579">
            <img src="https://avatars.githubusercontent.com/u/97679742?v=4" width="140px" /> <br><br> 김지수 <br>(Back-End) </a> <br></td>
        <td height="140px" align="center"> <a href="https://github.com/henginthere">
            <img src="https://avatars.githubusercontent.com/u/58303100?v=4" width="140px" /> <br><br> 배혜연 <br>(Back-End) </a> <br></td>
        <td height="140px" align="center"> <a href="https://github.com/jongui-94">
            <img src="https://avatars.githubusercontent.com/u/64008540?v=4" width="140px" /> <br><br> 박종욱 <br>(Back-End) </a> <br></td>
        <td height="140px" align="center"> <a href="https://github.com/JeongBJ">
            <img src="https://avatars.githubusercontent.com/u/85900947?v=4" width="140px" /> <br><br> 정봉진 <br>(AOS) </a> <br></td>
        <td height="140px" align="center"> <a href="https://github.com/mxxxxxji">
            <img src="https://avatars.githubusercontent.com/u/52437364?v=4" width="140px" /> <br><br> 김명지 <br>(AOS) </a> <br></td>
            <td height="140px" align="center"> <a href="https://github.com/taxfdi6371">
            <img src="https://avatars.githubusercontent.com/u/53108175?v=4" width="140px" /> <br><br> 권용준 <br>(AOS) </a> <br></td>
    </tr>
    <tr>
        <td align="center"> 👑팀장 <br> BackEnd <br> AI 전처리
        <td align="center"> 부팀장 <br> BackEnd <br> Infra <br> AI 전처리 
        <td align="center"> BackEnd <br> AI 전처리
        <td align="center">Android <br> Infra
        <td align="center">Android 
        <td align="center">Android
    </tr>
</table>

<div id="2">
<br><br>
</div>

## 프로젝트 구성 요소

### Android 구성 요소

| Package | Version | Comment |
| --- | --- | --- |
| Dagger Hilt | 2.44 | 의존성 주입 라이브러리  |
| Retrofit | 2.9.0 | HTTP 통신 라이브러리 |
| OKHttp | 4.10.0 | HTTP 통신 라이브러리 |
| Coroutines | 1.6.4 | 비동기 처리 라이브러리 |
| Navigation | 2.5.3 | 화면 이동 라이브러리 |
| PlayServicesAuth | 20.3.0 | Google OAuth |
| AR Core | 1.34.0 | AR 사용을 위한 라이브러리 |
| Tensorflow-lite-task-vision | 0.4.0 | Object Detection |
| Tensorflow-lite-gpu | 2.9.0 | Object Detection에 GPU 사용 |
| ML Kit | 18.5.0 | Text Recognition 라이브러리 |
| Firebase Cloud Messaigng  | 23.1.0 | PUSH 알림 사용을 위한 라이브러리 |
| Paging 3 | 3.1.1 | Pagination 라이브러리 |
| WebRTC | 1.0.32006 | 영상 통화를 위한 라이브러리 |
| Glide | 4.13.2 | 이미지 로딩 라이브러리 |
| AndroidViewAnamations | 2.4 | 뷰 애니메이션 라이브러리 |
| Lottie | 5.2.0 | 뷰 애니메이션 라이브러리 |
| Room | 2.4.3 | 로컬 Database |
| PlayServicesLocation | 21.0.1 | GPS 사용 라이브러리 |
| MPAndroidChart | 3.1.0 | 그래프, 차트 라이브러리 |

### Backend 구성 요소

| 기술 스택 | Version | Comment |
| --- | --- | --- |
| OpenJDK | 11 | Java vendor 사용 |
| SpringBoot | 2.7.3 | Rest API 웹 애플리케이션 개발 |
| Gradle | 7.5 | 프로젝트를 빌드하고 라이브러리를 관리 도구 |
| MySQL | 8.0.30 | DB |
| JPA | 2.7.3 | 서버와 DB의 연동을 위해 사용 |
| JWT | 0.11.2 | 로그인 유저 인증을 위해 토큰 사용 |
| Spring Security | 2.7.3 | 손쉬운 보안 관리를 위해 사용 |
| Swagger | 2.9.2 | API 문서화를 위해 사용 |
| Ubuntu | 20.04 LTS | 서비스 제공을 위해 리눅스 서버 구축 |
| Docker | latest | 컨테이너화 된 애플리케이션 관리 |
| Jenkins | latest | 자동화 배포 및 빌드 |
| Nginx | latest | Vue 클라이언트 웹 서버 사용 |
| GoogleApi | 1.30.10 | 구글 로그인 사용 |
| FirebaseAdmin | 6.8.1 | FirebaseCloudMessaging 사용 |
| MapStruct | 1.4.2 | Mapper 활용 |
| AWS | 2.3.1 | AWS S3 |
| Jasypt | 3.0.4 | 프로퍼티 암호화 |

### AI 구성 요소

| 기술 스택 | Version | Comment |
| --- | --- | --- |
| Tensorflow | 2.8.0 | AI 모델 학습 |
| Tensorflow-lite | 2.9.0 | 안드로이드 객체 탐지 |
| CudaToolkit | 11.3.1 | 학습 시 GPU 사용 |
| CuDNN | 8.4.1.50 | 학습 시 GPU 사용 |
| EfficientDet | 1 | 객체 탐지 모델 |
| ML Kit | 18.5 | 텍스트 인식 라이브러리 |

