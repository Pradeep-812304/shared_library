def call (string gitUrl, string gitToken) {
     pipeline {

    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: gitUrl,
                        credentialsId: gitToken
                    ]]
                ])
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

          stage('Deploy to Artifactory') {
    steps {
        configFileProvider([configFile(fileId: "aedd10e5-3c10-44df-808f-4f6ae7217819", variable: 'MAVEN_SETTINGS')]) {
            sh 'mvn deploy -s $MAVEN_SETTINGS'
                }
                
            }
        }

    }
}
    
}

