pipeline {         
        agent none
        stages { 
	   stage('Gradle build'){
		agent any
		steps{
			dir('BE') {sh 'chmod +x ./gradlew'}
			dir('BE') {sh './gradlew clean build'}
		}
	   }             
                stage('Docker build') {
                        agent any
                        steps {                                                     
                                sh 'docker build -t backimg ./BE'
                        }
                }
                stage('Docker run') {
                        agent any
                        steps {
                                sh 'docker ps -f name=back -q \
                                        | xargs --no-run-if-empty docker container stop'

                                sh 'docker container ls -a -f name=back -q \
                                        | xargs -r docker container rm'

                                sh 'docker run -v /home/files:/home/files -e TZ=Asia/Seoul -d --name back -p 8090:8090 backimg'
                        }
                }
		statge('Remove Images'){
			agent any
			steps {
				sh 'docker container prune -f'

				sh 'docker image prune -f'
			}
		}
        }

}
