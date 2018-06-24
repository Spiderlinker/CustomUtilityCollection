pipeline {
  agent any
  stages {
    stage('Preparation Stage') {
      steps {
        sh 'mvn clean'
      }
    }
    stage('Build Stage') {
      steps {
        sh 'mvn install'
      }
    }
    stage('Cleanup Stage') {
      steps {
        echo 'Should collect test results'
      }
    }
  }
}