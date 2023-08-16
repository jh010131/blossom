pipeline {
    agent any
    environment {
        // 젠킨스에 저장한 자격 증명 정보를 GITHUB_CREDENTIAL 에 저장됨
        // GITHUB_CREDENTIAL_USR: 자격증명 생성할 때 입력한 username 이 저장된 환경변수
        // GITHUB_CREDENTIAL_PSW: 자격증명 생성할 때 입력한 password 가 저장된 환경변수
        GITHUB_CREDENTIAL = credentials('github-flask-cicd')
    }
    triggers {
        githubPush()
    }
    options {
        timestamps()
        timeout(time: 5, unit: 'MINUTES')
    }
    stages {
        stage('github create release') {
            steps {
                script {
                    def response = sh(script: """
                        curl -sSL \
                            -X POST \
                            -H "Accept: application/vnd.github+json" \
                            -H "Authorization: Bearer ${GITHUB_CREDENTIAL_PSW}" \
                            -H "X-GitHub-Api-Version: 2022-11-28" \
                            https://api.github.com/repos/jh010131/flask-cicd/releases \
                            -d '{
                                    "tag_name":"v1.2.0",
                                    "target_commitish":"main",
                                    "name":"v1.2.0",
                                    "body":"Description of release",
                                    "draft":false,
                                    "prerelease":false,
                                    "generate_release_notes":false
                                }'
                    """, returnStdout: true) // 스크립트 실행 후 출력 결과를 response 변수에 저장!

                    def json = readJSON text: "$response"
                    def id = json.id

                    sh 'tar -zcvf flask-web.tar.gz ./src'
                    
                    sh """
                        curl -sSL \
                          -X POST \
                          -H "Accept: application/vnd.github+json" \
                          -H "Authorization: Bearer ${GITHUB_CREDENTIAL_PSW}" \
                          -H "X-GitHub-Api-Version: 2022-11-28" \
                          -H "Content-Type: application/octet-stream" \
                          "https://uploads.github.com/repos/jh010131/flask-cicd/releases/${id}/assets?name=flask-web.tar.gz" \
                          --data-binary "@flask-web.tar.gz"
                    """
                }
            }
        }
        stage("Docker build - flask") {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub') {
                        docker.build('kjh010131/flask', './docker')
                    }
                }
            }
        }
        stage('Docker Image Push') {
            steps {
                script {
                    // Docker hub 에 로그인 하기 위해 사용
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub') {
                        def flask_img = docker.image('kjh010131/flask')
                        
                        flask_img.push('latest')
                        flask_img.push('2.5')
                    }
                }
            }
        }
    }
}