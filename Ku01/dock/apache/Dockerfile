FROM kjh010131/httpd:1.0

ENV REPO=static-web-temp
ENV TAG_VERSION=1.0

RUN curl -LJO https://github.com/jh010131/${REPO}/archive/refs/tags/v${TAG_VERSION}.tar.gz
RUN tar -zxvf static-web-temp-1.0.tar.gz
RUN mv ${REPO}-${TAG_VERSION}/* /usr/local/apache2/htdocs