FROM pettyfer/alpine-base:1.0.0
LABEL maintainer="Alex Pettyfer <pettyferlove@live.cn>"
LABEL description="基础工具镜像-Oracle JDK 8"

# 需要在本地下载
ADD jdk-8u281-linux-x64.tar.gz /usr/local
ENV JAVA_HOME /usr/local/jdk1.8.0_281
ENV PATH $PATH:${JAVA_HOME}/bin
ENV LANG en_US.UTF8