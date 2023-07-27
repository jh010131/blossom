FROM httpd:2.4.57

RUN apt update -y
RUN apt install -y curl