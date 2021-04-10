#!/bin/sh
set -e

# shellcheck disable=SC2112
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
  cd "$BUILD_CODE_PATH"/"$PROJECT_NAME"

  if [ "$BUILD_TOOL" = "npm" ]; then
    npm install
  fi

  if [ "$BUILD_TOOL" = "yarn" ]; then
    yarn install
  fi

  /bin/bash -c "$BUILD_COMMAND"

} ||
  {
    hook
  }

exec "$@"
