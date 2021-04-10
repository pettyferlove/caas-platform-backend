FROM pettyfer/apline-base:1.1.0
LABEL maintainer="Alex Pettyfer <pettyferlove@live.cn>"
LABEL description="自动化工具-NodeJS构建镜像"

RUN apk update &&\
    apk upgrade &&\
    apk add --no-cache nodejs npm yarn

RUN npm config set registry https://registry.npm.taobao.org/

RUN mkdir -p /tool
WORKDIR /tool

ADD ./docker-entrypoint.sh docker-entrypoint.sh
CMD ["/bin/sh", "-c", "#(nop) ", "ENTRYPOINT [\"/tool/docker-entrypoint.sh\"]"]
ENTRYPOINT ["/tool/docker-entrypoint.sh"]