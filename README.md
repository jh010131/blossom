# blossom

sudo vi /etc/hosts
192.168.223.10 repo.image.co.kr

sudo vi /etc/docker/daemon.json
{
    "insecure-registries": ["http://repo.image.co.kr"]
}
sudo systemctl restart docker
docker pull repo.image.co.kr/registry