#!/bin/bash
set -e

function hook() {
  curl --connect-timeout 60 -m 60 --request POST "$REMOTE_SERVER"'/api/v1/hooks/build/'"$NOTIFICATION_FLAG"'/'"$JOB_NAME"'?status=Fail' -s
  # shellcheck disable=SC2181
  if [ "$?" != 0 ]; then
    echo "-------------Hook通知失败，请联系管理员-------------"
    exit 1
  fi
}

function init() {
  mkdir /root/.docker/
  mkdir /etc/docker/
  echo -e '{"auths": {"'"$DOCKER_REGISTRY_PATH"'": {"auth": "'"$(echo -n "$DOCKER_REGISTRY_USERNAME":"$DOCKER_REGISTRY_PASSWORD" | base64)"'"}}}' >/root/.docker/config.json
  echo -e '{"registry-mirrors": ["https://fkw5u2of.mirror.aliyuncs.com"],"insecure-registries":["'"$DOCKER_REGISTRY_PATH"'"]}' >/etc/docker/daemon.json
}

function build() {
  cd "$BUILD_CODE_PATH"/"$PROJECT_NAME"

  if [ "$DOCKER_IMAGE_NAME" ] && [ "$DOCKER_IMAGE_TAG" ]; then
    if [ "$DOCKERFILE_EXIST" = "yes" ]; then
      if [ ! -f "$DOCKERFILE_PATH" ]; then
        echo "-------------Dockerfile不存在，请检查配置-------------"
        hook
        exit 1
      fi
      echo "-------------开始构建镜像-------------"
      docker build -f "$DOCKERFILE_PATH" -t "$DOCKER_IMAGE_NAME":"$DOCKER_IMAGE_TAG" .
      # shellcheck disable=SC2181
      if [ "$?" != 0 ]; then
        echo "-------------构建镜像失败，请检查配置-------------"
        exit 1
      fi
      echo "-------------镜像构建完毕-------------"
    else
      if [ "$DOCKERFILE_CONTENT" ]; then
        echo -e "$DOCKERFILE_CONTENT" >Dockerfile
        echo "-------------开始构建镜像-------------"
        docker build -t "$DOCKER_IMAGE_NAME":"$DOCKER_IMAGE_TAG" .
        # shellcheck disable=SC2181
        if [ "$?" != 0 ]; then
          echo "-------------构建镜像失败，请检查配置-------------"
          exit 1
        fi
        echo "-------------镜像构建完毕-------------"
      else
        echo "-------------未设置Dockerfile参数，无法进行构建-------------"
        hook
        exit 1
      fi
    fi

    echo "-------------开始推送镜像-------------"
    docker push "$DOCKER_IMAGE_NAME":"$DOCKER_IMAGE_TAG"
    # shellcheck disable=SC2181
    if [ "$?" != 0 ]; then
      echo "-------------镜像推送失败，请检查配置-------------"
      exit 1
    fi
    echo "-------------推送镜像完毕-------------"
    echo "-------------开始移除本地镜像缓存-------------"
    docker rmi -f "$DOCKER_IMAGE_NAME":"$DOCKER_IMAGE_TAG"
    # shellcheck disable=SC2181
    if [ "$?" != 0 ]; then
      echo "-------------删除镜像缓存失败，请检查配置-------------"
      exit 1
    fi
    echo "-------------镜像缓存清理成功-------------"
  else
    echo "-------------无法进行构建，请设置镜像名称与镜像标签-------------"
    hook
    exit 1
  fi

}

{
  init
  build
} || {
  hook
}

exec "$@"
