pipeline {
    agent any
    
    environment {
        IMAGE = 'shlomilory/petclinic_proj'
        DOCKER_CREDENTIALS_ID = 'valhala'
        DOCKERFILE_PATH = "spring-petclinic/Dockerfile"
        CONTEXT = "/home/jenkins/workspace/Final/welcome/app/bookinfo/src/productpage"
        
    }
    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/shlomilory/jb_project.git'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'pwd'
                    sh 'docker build --no-cache -f ${DOCKERFILE_PATH} -t ${IMAGE}:${BUILD_NUMBER} ${CONTEXT}'
                }
            }
        }
        stage('Tag Docker Image') {
            steps {
                script {
                    sh 'docker tag ${IMAGE}:${BUILD_NUMBER} ${IMAGE}:latest'
                }
            }
        }
        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh 'echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin'
                    }
                    
                    sh 'docker push ${IMAGE}:${BUILD_NUMBER}'
                    sh 'docker push ${IMAGE}:latest'
                }
            }
        }
    }
    post {
        success {
            echo 'Docker image build and push successful!'
        }
        failure {
            echo 'Docker image build or push failed.'
        }
    }
}