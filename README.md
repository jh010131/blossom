# blossom

##### Vagrantfile nginx에 맞추어 변경하는 내용
```bash
docker run -it -d -p 8080:80 --name nginx-server nginx
docker cp /vagrant/sample2 nginx-server:/usr/share/nginx/html/
```

### Docker Token 등록
env/docker_token 파일을 생성하여 [docker hub](https://hub.docker.com/settings/security) 사이트에 접속하여 생성한 Token을 등록합니다.