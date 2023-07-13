FROM ubuntu:focal
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update \
    && apt-get install -y nginx git
RUN git clone https://github.com/jh010131/static-web-temp.git
COPY sites-available/default /etc/nginx/sites-available/default
RUN cp -r static-web-temp/* /usr/share/nginx/html
CMD ["nginx", "-g", "daemon off;"]
EXPOSE 80