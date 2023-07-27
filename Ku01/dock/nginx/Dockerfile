FROM nginx:1.25.1

ENV REPO=static-web-temp
ENV TAG_VERSION=1.2

RUN curl -LJO https://github.com/jh010131/${REPO}/archive/refs/tags/v${TAG_VERSION}.tar.gz
RUN tar -zxvf static-web-temp-1.2.tar.gz
RUN mv ${REPO}-${TAG_VERSION}/* /usr/share/nginx/html