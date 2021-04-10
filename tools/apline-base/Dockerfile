FROM alpine:latest
LABEL maintainer="Alex Pettyfer <pettyferlove@live.cn>"
LABEL description="基础工具镜像"

ARG GLIBC_VERSION=2.29-r0
ARG GLIBC_DOWNLOAD_URL=https://github.com/sgerrand/alpine-pkg-glibc/releases/download/${GLIBC_VERSION}
RUN set -eux && sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories
RUN apk update &&\
    apk upgrade &&\
    apk add --no-cache bash \
    bash-doc \
    bash-completion &&\
    /bin/bash &&\
    wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub &&\
    wget ${GLIBC_DOWNLOAD_URL}/glibc-${GLIBC_VERSION}.apk &&\
    wget ${GLIBC_DOWNLOAD_URL}/glibc-bin-${GLIBC_VERSION}.apk &&\
    wget ${GLIBC_DOWNLOAD_URL}/glibc-i18n-${GLIBC_VERSION}.apk &&\
    apk add glibc-${GLIBC_VERSION}.apk glibc-bin-${GLIBC_VERSION}.apk glibc-i18n-${GLIBC_VERSION}.apk &&\
    sleep 2 &&\
    rm -rf /tmp/* /var/cache/apk/* &&\
    /usr/glibc-compat/bin/localedef -i en_US -f UTF-8 en_US.UTF-8 &&\
    apk add -U tzdata &&\
    apk add --no-cache git openssh subversion curl &&\
    cp -r -f /usr/share/zoneinfo/Asia/Shanghai /etc/localtime &&\
    echo 'Asia/Shanghai' > /etc/timezone &&\
    apk del tzdata &&\
    rm glibc-${GLIBC_VERSION}.apk &&\
    rm glibc-bin-${GLIBC_VERSION}.apk &&\
    rm glibc-i18n-${GLIBC_VERSION}.apk
