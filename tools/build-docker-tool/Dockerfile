FROM pettyfer/alpine-base:1.0.0
LABEL maintainer="Alex Pettyfer <pettyferlove@live.cn>"
LABEL description="自动化工具-Docker构建镜像"

RUN apk update &&\
    apk upgrade &&\
    apk add --no-cache docker

RUN mkdir -p /tool
WORKDIR /tool

ADD ./docker-entrypoint.sh docker-entrypoint.sh
CMD ["/bin/sh", "-c", "#(nop) ", "ENTRYPOINT [\"/tool/docker-entrypoint.sh\"]"]
ENTRYPOINT ["/tool/docker-entrypoint.sh"]