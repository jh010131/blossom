## 개인 리포지토리와 도커 서버 연결

### Registry 다운로드
#### docker-repo:~$
```bash
docker pull registry
```

### 인증서 생성 및 등록
#### docker-repo:~$
```bash
sudo openssl req -newkey rsa:4096 -nodes -sha256 \
> -keyout dock.repo.co.kr.key \
> -subj "/C=KR/ST=Busan/L=Centum/O=mzcloud/OU=mzcloud/CN=dock.repo.co.kr/emailAddress=loojinhwan@gmail.com" \
> -addext "subjectAltName = DNS:dock.repo.co.kr" \
> -x509 -days 365 -out dock.repo.co.kr.crt
```

### 개인 리포지토리에 인증서 정보 저장
#### docker-repo:~$
```bash
mkdir certs
sudo cp dock.repo.co.kr.* certs/
```
### 도커 서버에 인증키를 저장할 디렉토리 생성
#### docker-server1:~$
```bash
sudo mkdir -p /etc/docker/certs.d/dock.repo.co.kr/
```
### SCP를 이용해 개인 리포지토리에서 도커 서버로 공개 인증키 전달
#### docker-repo:~$
```bash
cp /vagrant/.vagrant/machines/dock1/virtualbox/private_key ~/dock1.key 
# 버츄얼 박스 프라이빗 키 권한 변경을 위한 복사
sudo chmod 600 dock1.key 
# 권한 변경
scp -i dock1.key certs/dock.repo.co.kr.crt vagrant@192.168.33.100:/home/vagrant
# scp를 이용하여 도커 서버 vagrant로 프라이빗 키 전달
```

#### docker-server1:~$
```bash
sudo cp dock.repo.co.kr.crt /etc/docker/certs.d/dock.repo.co.kr/
# 인증키 복사
```
### Registry 컨테이너 Run
#### docker-repo:~$
```bash
docker run -d --restart=always --name image_repo \
> -v /home/vagrant/certs:/certs \
> -e REGISTRY_HTTP_ADDR=0.0.0.0:443 \
> -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/dock.repo.co.kr.crt \
> -e REGISTRY_HTTP_TLS_KEY=/certs/dock.repo.co.kr.key \
> -p 443:443 registry
```

### 도커 서버에서 image 생성 및 업로드
#### docker-server1:~$
```bash
docker pull ubuntu
docker tag ubuntu:latest dock.repo.co.kr/ubuntu:latest
docker images
```
REPOSITORY               TAG       IMAGE ID       CREATED       SIZE
dock.repo.co.kr/ubuntu   latest    5a81c4b8502e   13 days ago   77.8MB
ubuntu                   latest    5a81c4b8502e   13 days ago   77.8MB

```bash
sudo vi /etc/hosts
sudo su - -c 'echo "192.168.33.200    dock.repo.co.kr" >> /etc/hosts'
```

```bash
docker push dock.repo.co.kr/ubuntu:latest
```