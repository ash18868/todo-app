pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    options {
        disableConcurrentBuilds()
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }

    environment {
        TEST_JWT_SECRET = 'jenkins-test-secret-key-that-is-at-least-32-bytes'
    }

    stages {
        stage('Backend tests') {
            steps {
                dir('todoapp') {
                    withEnv(["JWT_SECRET=${env.TEST_JWT_SECRET}"]) {
                        sh '''
                            chmod +x gradlew
                            ./gradlew test --no-daemon \
                              --tests 'com.teamtetra.todoapp.TodoappApplicationTests' \
                              --tests 'com.teamtetra.todoapp.rest.RestAssuredTests'
                        '''
                    }
                }
            }
        }

        stage('Frontend tests and build') {
            steps {
                sh '''
                    docker run --rm \
                      --user "$(id -u):$(id -g)" \
                      -e HOME=/tmp \
                      -v "$WORKSPACE/todoapp/frontend:/app" \
                      -w /app \
                      node:lts-slim \
                      sh -c 'npm ci && npm test -- --watch=false && npm run build'
                '''
            }
        }

        stage('Build backend image') {
            steps {
                withEnv(["JWT_SECRET=${env.TEST_JWT_SECRET}"]) {
                    sh 'docker compose build backend'
                }
            }
        }

        stage('Build frontend image') {
            steps {
                withEnv(["JWT_SECRET=${env.TEST_JWT_SECRET}"]) {
                    sh 'docker compose build frontend'
                }
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'todo-app-prod-jwt-secret',
                        variable: 'PROD_JWT_SECRET'
                    )
                ]) {
                    sh '''
                        export JWT_SECRET="$PROD_JWT_SECRET"
                        export CORS_ALLOWED_ORIGINS="https://todo.webweaver.dev,http://localhost:4200,http://127.0.0.1:4200"

                        docker compose \
                        --project-name todo-app-prod \
                        up -d \
                        --no-build \
                        --force-recreate \
                        --remove-orphans
                    '''
                }
            }
        }

        stage('Verify deployment') {
            steps {
                sh '''
                    attempts=0

                    until curl --fail --silent http://localhost:8080/actuator/health
                    do
                        attempts=$((attempts + 1))

                        if [ "$attempts" -ge 12 ]; then
                            echo "Backend failed its health check"
                            docker compose --project-name todo-app-prod ps
                            docker compose --project-name todo-app-prod logs --tail=100
                            exit 1
                        fi

                        sleep 5
                    done

                    curl --fail --silent http://localhost:4200/ > /dev/null
                    echo "Deployment verified successfully."
                '''
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'todoapp/build/test-results/test/*.xml'
        }
        success {
            echo 'CI passed and deployment completed successfully.'
        }
    }
}
