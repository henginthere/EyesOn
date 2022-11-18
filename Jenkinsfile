pipeline {         
        agent none
        stages { 
	   stage('Gradle build'){
		agent any
		steps{
			dir('BE') {sh 'chmod +x ./gradlew'} // 권한 추가
			dir('BE') {sh './gradlew clean build'} // 빌드
		}
	   }             
                stage('Docker build') {
                        agent any
                        steps {                                                     
                                sh 'docker build -t backimg ./BE' //도커 이미지 빌드
                        }
                }
                stage('Docker run') {
                        agent any
                        steps {
                                sh 'docker ps -f name=back -q \
                                        | xargs --no-run-if-empty docker container stop' // 실행 중인 컨테이너 중지
                                sh 'docker container ls -a -f name=back -q \
                                        | xargs -r docker container rm'                 // 이전 컨테이너 삭제
                                sh 'docker run -v /home/files:/home/files -e JAVA_OPTS=-Djasypt.encryptor.password=gumid201 -e TZ=Asia/Seoul -d --name back -p 8090:8090 backimg' // 도커 이미지 실행, Jasypt 암호 입력
                        }
                }
		stage('Remove Images'){
			agent any
			steps {
				sh 'docker container prune -f'
				sh 'docker image prune -f'      // 이전 이미지, 컨테이너 모두 삭제
			}
		}
        }

}
