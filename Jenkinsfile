pipeline {

    agent any

    tools {
        jdk 'JDK-11.0.23'
        maven 'apache-maven-3.9.9'
    }

    triggers {
        githubPush()
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/Javier-22/TestCobre.git'
            }
        }

        stage('Maven build') {
            steps {
                sh "mvn clean install -DskipTests"
            }
        }

        stage('Test') {
            steps {
                sh "mvn clean test -Dtest=GeneralRunner -Dtest-suite=acceptance -DwithTags=EditCSV"
            }
        }

        stage('Publicar Reporte Karate') {
            steps {
                publishHTML([
                    reportDir: 'target/karate-reports',
                    reportFiles: 'karate-summary.html',
                    reportName: 'Reporte de pruebas Karate',
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: true
                ])
            }
        }
    }
}
