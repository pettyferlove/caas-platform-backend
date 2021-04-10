#!/bin/bash
set -e

function createKey() {
  if [ "$PRIVATE_KEY" ]; then
    mkdir /root/.ssh/
    echo -e "$PRIVATE_KEY" >/root/.ssh/id_rsa
    chmod 600 /root/.ssh/id_rsa
  fi
  if [ -z "$BUILD_CODE_PATH" ]; then
    echo "-------------构建目标目录配置获取失败-------------"
    hook
    exit 1
  fi
}

function pull() {
  if [ "$GIT_ROOT" ] && [ "$PRIVATE_KEY" ]; then
    touch /root/.ssh/known_hosts
    ssh-keyscan "$GIT_ROOT" >>/root/.ssh/known_hosts

    if [ "$REMOTE_PATH" ] && [ "$PROJECT_NAME" ]; then
      echo "-------------开始拉取远程代码-------------"
      git clone --progress --verbose -b "$REMOTE_BRANCH" "$REMOTE_PATH" "$BUILD_CODE_PATH"/"$PROJECT_NAME"
      # shellcheck disable=SC2181
      if [ "$?" != 0 ]; then
        echo "-------------Git拉取失败-------------"
        hook
        exit 1
      fi
      echo "-------------代码拉取完毕-------------"
    else
      echo "-------------Git配置错误，请检查后再试-------------"
      hook
      exit 1
    fi
  fi
}

function hook() {
  curl --connect-timeout 60 -m 60 --request POST "$REMOTE_SERVER"'/api/v1/hooks/build/'"$NOTIFICATION_FLAG"'/'"$JOB_NAME"'?status=Fail' -s
  # shellcheck disable=SC2181
  if [ "$?" != 0 ]; then
    echo "-------------Hook通知失败，请联系管理员-------------"
    exit 1
  fi
}

{
  createKey
  pull
} || {
  hook
}

exec "$@"
