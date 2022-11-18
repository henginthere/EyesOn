# README.md

## Eyeon

시각장애 및 저시력자를 위한 보조서비스

## **프로젝트 소개**

> 스마트폰을 활용하여 보행 중 위험과 생활 속 불편함을 조금이라도 해소할 수 있도록 도움을 주기 위한 애플리케이션 입니다.

글자 인식, 사물 인식, 장애물 인식, 도움 요청, 민원 처리의 기능이 있습니다.
시각장애인을 도와주는 엔젤 용 애플리케이션이 존재합니다.

## 프로젝트 기술 스택

**형상관리**
- Git

**커뮤니케이션**
- Mattermost, Webex, notion

**개발환경**
- OS: Window 10, 11
- IDE
    - IntelliJ
    - Android Studio
    - UI/UX: Figma
- Database 
    - Server: AWS RDS
    - DBMS: MySQL 8.0.31
- Server: AWS EC2 
    - OS: Ubuntu 20.04 LTS (GNU/Linux 5.4.0-1018-aws x86_64)
    - SSH: MobaXterm
- File Server: AWS S3
- GPU Server: JupyterHub(SSAFY)
    - OS: Ubuntu 20.04 LTS (Linux 5.4.0-124-generic x86_64)
    - CPU: Intel Xeon Gold 6248
    - GPU: 10 X Nvidia Tesla V100 32GB
- CI/CD: Jenkins, Docker, Nginx

**상세 기술**
 
- Frontend (Android)
    - Android Studio Dolphin | 2021.3.1
    - Kotlin 1.7.20
    - JDK 11.0.13
    - Gradle 7.5
    - SDK ( Min / Target / Compile - 24 / 33 / 33 )
    - WebRTC 1.0.32
    - Retrofit 2.9.0
    - Tensorflow-lite 2.9.0
    - AR Core 1.34
    - ML Kit 18.5
    - Dagger Hilt 2.44
    - WebRTC 1.0.32006
    - Firebase Cloud Messaging 23.1
    - Room 2.4.3
    - Paging 3.1.1

- Backend
    - JDK: 11
    - Spring Boot: 2.7.5
    - Gradle 7.5
    - Spring Security
    - Spring Data JPA
    - Springfox Swagger UI: 2.9.2
    - Jasypt
    - Lombok
    - Logger
    - Json Web Token
    - GSON
    - AWS
    - Naver Cloud Api

- Server 
    - AWS EC2
    - AWS S3
    - Ubuntu 20.04 LTS
    - Docker
    - Jenkins
    - CertBot
- IDE 	
    - HeidiSQL 12.1.0
    - WorkBench 8.0CE
    - Android Studio Dolphin | 2021.3.1
    - IntelliJ IDEA | 2022.1.4
    - Spring Tool Suite 3.9.14

- AI
    - Python 3.7.12
    - Tensorflow(GPU) 2.8.0
    - CudaToolkit: 11.3.1
    - CuDNN 8.4.1.50
    - Tensorflow-lite : 2.9.0
    - JupyterHub
    - Anaconda3


