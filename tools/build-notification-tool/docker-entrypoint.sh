#!/bin/bash
set -e

function notification() {
  curl --connect-timeout 60 -m 60 --request POST "$REMOTE_SERVER"'/api/v1/hooks/build/'"$NOTIFICATION_FLAG"'/'"$JOB_NAME"'?status=Success'
  # shellcheck disable=SC2181
  if [ "$?" != 0 ]; then
    echo "-------------构建结果通知失败-------------"
    exit 1
  fi
}

{
  notification
} || {
  curl --connect-timeout 60 -m 60 --request POST "$REMOTE_SERVER"'/api/v1/hooks/build/'"$NOTIFICATION_FLAG"'/'"$JOB_NAME"'?status=Fail'
  # shellcheck disable=SC2181
  if [ "$?" != 0 ]; then
    echo "-------------Hook通知失败，请联系管理员-------------"
    exit 1
  fi
}

exec "$@"
