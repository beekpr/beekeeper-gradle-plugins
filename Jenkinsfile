pipeline {
    agent {
        node {
            // We use monolith node for caching
            label 'monolith'
        }
    }

    options { skipStagesAfterUnstable() }

    tools {
        jdk "JDK 11"
    }

    environment {
        // This is the version that will be used for tagging and publishing
        // Please bump this version before merging to master
        VERSION = "0.14.1"
    }
    stages {
        stage("Clean and Check") {
            steps {
                gradle {
                    tasks('clean')
                    tasks('check')
                }
            }
            post {
                always {
                    junit '**/test-results/**/*.xml'
                }
            }
        }
        stage("Publish") {
            when {
                branch 'master'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'beekeeper-gradle-portal-credentials', usernameVariable: 'GRADLE_PUBLISH_KEY', passwordVariable: 'GRADLE_PUBLISH_SECRET')]) {
                    gradle {
                        tasks('publishPlugins')
                        switches('--Pversion=$VERSION -Pgradle.publish.key=$GRADLE_PUBLISH_KEY -Pgradle.publish.secret=$GRADLE_PUBLISH_SECRET')
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
