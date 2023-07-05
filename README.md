# blossom

##### Vagrantfile nginx에 맞추어 변경하는 내용
```bash
docker run -it -d -p 8080:80 --name nginx-server nginx
docker cp /vagrant/sample2 nginx-server:/usr/share/nginx/html/
```