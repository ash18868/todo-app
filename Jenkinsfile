pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }

    environment {
        TEST_JWT_SECRET = 'jenkins-test-secret-key-that-is-at-least-32-bytes'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend tests') {
            steps {
                dir('todoapp') {
                    withEnv(["JWT_SECRET=${env.TEST_JWT_SECRET}"]) {
                        sh '''
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
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'todoapp/build/test-results/test/*.xml'
        }
        success {
            echo 'CI passed. Deployment remains manual.'
        }
    }
}
