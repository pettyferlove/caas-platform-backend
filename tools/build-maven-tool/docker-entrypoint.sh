#!/bin/bash
set -e

# shellcheck disable=SC2112
POM_PATH=""
if [ "$POM_PATH" ]; then
  POM_PATH="$POM_PATH"
else
  POM_PATH="pom.xml"
fi

function hook() {
  curl --connect-timeout 60 -m 60 --request POST "$REMOTE_SERVER"'/api/v1/hooks/build/'"$NOTIFICATION_FLAG"'/'"$JOB_NAME"'?status=Fail' -s
  # shellcheck disable=SC2181
  if [ "$?" != 0 ]; then
    echo "-------------Hook通知失败，请联系管理员-------------"
    exit 1
  fi
}

{
  if [ -z "$BUILD_CODE_PATH" ]; then
    echo "-------------构建目标目录配置获取失败-------------"
    hook
    exit 1
  fi
  echo "-------------开始项目构建-------------"
  if [ "$BUILD_COMMAND" ]; then
    /bin/bash -c "$BUILD_COMMAND"' -f '"$BUILD_CODE_PATH"'/'"$PROJECT_NAME"'/'"$POM_PATH"' '"$BUILD_PARAMS"
    # shellcheck disable=SC2181
    if [ "$?" != 0 ]; then
      echo "-------------项目构建失败-------------"
      hook
      exit 1
    fi
  else
    echo "-------------构建命令错误-------------"
    hook
    exit 1
  fi
  echo "-------------项目构建成功-------------"
} || {
  hook
}

exec "$@"
