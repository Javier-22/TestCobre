pipeline {
    agent any

    tools {
        // Asegúrate de que estos nombres coincidan con lo configurado en Jenkins:
        jdk 'JDK 11'
        maven 'MAVEN'
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
                bat "mvn clean install -DskipTests"
            }
        }

        stage('Run tests') {
            steps {
                bat "mvn test -Dtest=GeneralRunner -Dtest-suite=acceptance -DwithTags=EditCSV"
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

