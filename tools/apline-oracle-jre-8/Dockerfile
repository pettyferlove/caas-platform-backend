FROM pettyfer/apline-base:1.1.0
LABEL maintainer="Alex Pettyfer <pettyferlove@live.cn>"
LABEL description="基础工具镜像-Oracle JRE 8"

# 需要在本地下载
ADD jre-8u281-linux-x64.tar.gz /usr/local
ENV JAVA_HOME /usr/local/jre1.8.0_281
ENV PATH $PATH:${JAVA_HOME}/bin
ENV LANG en_US.UTF8