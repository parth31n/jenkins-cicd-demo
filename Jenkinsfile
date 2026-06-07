pipeline {
    agent any

    environment {
        APP_NAME    = 'jenkins-cicd-demo'
        IMAGE_NAME  = "parth/${APP_NAME}"
        IMAGE_TAG   = "${BUILD_NUMBER}"
        DOCKER_CRED = credentials('docker-hub-credentials')
    }

    tools {
        maven 'Maven-3.9'
        jdk   'JDK-17'
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Checking out source code..."
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "Building the application..."
                sh 'mvn clean compile -q'
            }
        }

        stage('Test') {
            steps {
                echo "Running unit & integration tests..."
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
                archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
            }
        }

        stage('Package') {
            steps {
                echo "Packaging the JAR..."
                sh 'mvn package -DskipTests -q'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker image: ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest"
            }
        }

        stage('Docker Push') {
            when {
                branch 'main'
            }
            steps {
                echo "Pushing image to Docker Hub..."
                sh """
                    echo ${DOCKER_CRED_PSW} | docker login -u ${DOCKER_CRED_USR} --password-stdin
                    docker push ${IMAGE_NAME}:${IMAGE_TAG}
                    docker push ${IMAGE_NAME}:latest
                """
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo "Deploying application..."
                sh """
                    docker compose pull app
                    docker compose up -d --force-recreate app
                """
            }
        }
    }

    post {
        success {
            echo "Pipeline succeeded! Build #${BUILD_NUMBER} deployed."
        }
        failure {
            echo "Pipeline failed at stage. Check logs above."
        }
        always {
            sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true"
            cleanWs()
        }
    }
}
