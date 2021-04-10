FROM pettyfer/apline-tool-base:oracle-jdk-8_281
LABEL maintainer="Alex Pettyfer <pettyferlove@live.cn>"
LABEL description="自动化工具-Maven构建镜像"

ARG MAVEN_VERSION=3.8.1

RUN mkdir -p /tool
WORKDIR /tool

ADD ./apache-maven-${MAVEN_VERSION}-bin.tar.gz /usr/local
ADD ./settings.xml /usr/local/apache-maven-${MAVEN_VERSION}/conf
ENV MAVEN_HOME /usr/local/apache-maven-${MAVEN_VERSION}
ENV PATH $PATH:${MAVEN_HOME}/bin

ADD ./docker-entrypoint.sh docker-entrypoint.sh
CMD ["/bin/sh", "-c", "#(nop) ", "ENTRYPOINT [\"/tool/docker-entrypoint.sh\"]"]
ENTRYPOINT ["/tool/docker-entrypoint.sh"]