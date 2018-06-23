pipeline {
  agent {
    docker {
      image 'maven:3.5.4-jdk-8'
      args '-v /home/odin/.m2:/root/.m2'
    }

  }
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