# EyesOn
### 시각장애인의 눈[:Eyes] 을 뜨게[:On] 해주다.
<br/>
<img src="/uploads/8826ed638e19fc4b7721779382cd9063/app_logo.png" width="200" height="200"/>  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/uploads/87073df6594b49852dcdb0f2a34d0fbd/logo.png"  width="35%" height="35%"/>  
<br/><br/>
 
**EyesOn** 프로젝트는 시각장애인 및 저시력자를 위한 보조 애플리케이션 입니다.<br/>
시각 장애의 92%는 후천적이며, 이들의 90%는 점자를 읽지 못합니다.<br/>
또한 인도의 높이가 낮은 장애물들은 흰지팡이로도 감지하기 어려워 큰 사고를 야기합니다. <br/>
이러한 보행 중 위험과 생활 속 불편함을 조금이나마 해소할 수 있도록 저희는 **EyesOn** 프로젝트를 진행하게 되었습니다.
<br/><br/><br/>

## 📃 Description

#### 서비스 전체 기능 

>시각장애인 기능
<ul>
	<li>글자보기 기능 - 노래 보관함에 부를 노래를 보관 가능, 이를 통해 추후 추천에 가중치 반영</li>
	<li>물건찾기 기능 - 연습을 위해 특정 곡에 대해 녹음을 할 수 있는 기능. 부른 목록과 횟수로 추후 추천 가중치 반영</li>
	<li>장애물 인식 기능 - 자신과 목소리가 비슷한 사용자의 보관/녹음 곡 목록에 대해 추천 기능의 기반기능</li>
	<li>엔젤과 연결 기능 - 완전히 랜덤으로 리스트를 추천받는 기능</li>
	<li>민원 신청 기능 - 자신이 선호했던 노래와 비슷한 노래를 추천받는 기능</li>
</ul>
<br/>

>엔젤 기능  
<ul>
	<li>민원 처리 기능 - 일정 기간동안 자신이 녹음했던 노래의 종류/횟수를 기반으로 하여 추천받는 기능</li>
	<li>도움 주기 기능 - 자신과 목소리가 유사한 사용자의 보관함 목록을 기반으로 추천받는 기능</li>
	<li>알림 설정 기능 - 추천을 받은 곡이 부르기 적합하지 않은 곡이라면 앞으로의 추천 목록에 나오지 않도록 하는 기능</li>
</ul>
<br/><br/><br/>

## 📝 Design

#### 사용 기술

>프로젝트에서 사용된 기술들입니다.
<br/>  
<img src="/uploads/252e6871f3645ccab61e0015c4bee5ac/tech_stack.png"height="400"/>  
<br/>
<br/>

#### 프로젝트 구조도

>프로젝트 구조도입니다.
<br/>
<img src="/uploads/1a13669dd82686460f621842717a9dcb/SystemArchitecture.png"height="400"/>  
<br/>
<br/>

#### ERD

> ERD 입니다.
<br/>
<img src="/uploads/83b158f5064b33ec8d5d9f1b24bd631a/ERD.png"height="400"/>  
<br/><br/><br/>

## 📱 Android
#### 사용한 라이브러리
	
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
<br/>

#### 디렉터리 구조도
```
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
│ ├──📁objectdetecor'
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
<br/>

#### 아키텍처
>안드로이드는 클린 아키텍처를 도입했습니다.
<br/> 
<img src="/uploads/8fd5fba52ee0259d389cf059dc49351b/CleanArchitecture.png"height="300"/>
<br/>
<br/>
