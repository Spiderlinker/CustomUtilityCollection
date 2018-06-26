pipeline {
  agent any
  stages {
    stage('Wiping workspace') {
      steps {
        sh 'mvn clean'
      }
    }
    stage('Compile Stage') {
      steps {
        sh 'mvn compile'
      }
    }
    stage('Install') {
      steps {
        sh 'mvn install -fae'
      }
    }
    stage('Archiving Stage') {
      parallel {
        stage('Store JUnit-Test Results') {
          steps {
            junit 'target/surefire-reports/*.xml'
          }
        }
        stage('Archive artifacts') {
          steps {
            archiveArtifacts 'target/*.jar'
          }
        }
      }
    }
    stage('Analyse code') {
      steps {
        sh '''mvn sonar:sonar \\
  -Dsonar.host.url=http://192.168.1.144:9000 \\
  -Dsonar.login=2a8ca52549d2c4be786c6defb1c94debeca1fdb5'''
      }
    }
  }
}