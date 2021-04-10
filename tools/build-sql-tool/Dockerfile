FROM pettyfer/apline-tool-base:oracle-jre-8_281
LABEL maintainer="Alex Pettyfer <pettyferlove@live.cn>"
LABEL description="自动化工具-SQL构建镜像"

RUN mkdir -p /tool
WORKDIR /tool
COPY ./commands/build-sql-1.0.3.jar build-sql.jar
ADD ./docker-entrypoint.sh docker-entrypoint.sh
CMD ["/bin/sh", "-c", "#(nop) ", "ENTRYPOINT [\"/tool/docker-entrypoint.sh\"]"]
ENTRYPOINT ["/tool/docker-entrypoint.sh"]