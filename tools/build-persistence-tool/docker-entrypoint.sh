#!/bin/bash
set -e

function persistence() {
  cd "$BUILD_CODE_PATH"/"$PROJECT_NAME"/"$BUILD_TARGET_PATH"
  /bin/bash -c 'tar -zcvf /tmp/'"$JOB_NAME"'.tar.gz '"$BUILD_TARGET_NAME"
  # shellcheck disable=SC2181
  if [ "$?" != 0 ]; then
    echo "-------------压缩文件失败-------------"
    hook
    exit 1
  fi

  if [ ! -f /tmp/"$JOB_NAME".tar.gz ]; then
    echo "-------------构建产物不存在，请检查前置步骤是否出现错误-------------"
    hook
    exit 1
  else
    echo "-------------开始持久化构建产物-------------"
    curl --connect-timeout 60 -m 60 --request POST "$REMOTE_SERVER"'/api/v1/build/file/upload' -s --form 'userId='"$USER_ID" --form 'group="build"' --form 'storage="FileLocal"' --form 'file=@/tmp/'"$JOB_NAME"'.tar.gz' --form 'fileId='"$FILE_ID"
    # shellcheck disable=SC2181
    if [ "$?" != 0 ]; then
      echo "-------------持久化失败-------------"
      hook
      exit 1
    fi
    echo "-------------持久化构建产物完毕-------------"
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
  persistence
} || {
  hook
}

exec "$@"
