#!/bin/bash
set -e

function pull() {
  if [ "$REMOTE_PATH" ] && [ "$PROJECT_NAME" ]; then
    echo "-------------开始拉取远程代码-------------"
    svn checkout "$REMOTE_PATH" "$BUILD_CODE_PATH"/"$PROJECT_NAME" --username "$USERNAME" --password "$PASSWORD"
    # shellcheck disable=SC2181
    if [ "$?" != 0 ]; then
      echo "-------------SVN拉取失败-------------"
      hook
      exit 1
    fi
    echo "-------------代码拉取完毕-------------"
  else
    echo "-------------SVN配置错误，请检查后再试-------------"
    hook
    exit 1
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
  pull
} || {
  hook
}

exec "$@"
