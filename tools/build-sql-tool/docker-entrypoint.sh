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

{
  if [ -z "$BUILD_CODE_PATH" ]; then
    echo "-------------构建目标目录配置获取失败-------------"
    hook
    exit 1
  fi
  if [ "$FULL" = "yes" ]; then
    echo "-------------开始全量构建-------------"
    if [ "$CONFIG" ]; then
      echo -e "$CONFIG" >config.properties
      java -jar build-sql.jar -d "$BUILD_CODE_PATH"/"$PROJECT_NAME" --verbose -o -g "$BUILD_CODE_PATH"/"$PROJECT_NAME" -c config.properties
    else
      java -jar build-sql.jar -d "$BUILD_CODE_PATH"/"$PROJECT_NAME" --verbose -o -g "$BUILD_CODE_PATH"/"$PROJECT_NAME"
    fi
    # shellcheck disable=SC2181
    if [ "$?" != 0 ]; then
      echo "-------------全量构建失败-------------"
      hook
      exit 1
    fi
    echo "-------------全量构建完毕-------------"
  else
    if [ "$FORM" ] && [ "$TO" ]; then
      echo "-------------开始增量构建-------------"
      if [ "$CONFIG" ]; then
        echo -e "$CONFIG" >config.properties
        java -jar build-sql.jar -d "$BUILD_CODE_PATH"/"$PROJECT_NAME" --verbose -f "$FORM" -t "$TO" -g "$BUILD_CODE_PATH"/"$PROJECT_NAME" -c config.properties
      else
        java -jar build-sql.jar -d "$BUILD_CODE_PATH"/"$PROJECT_NAME" --verbose -f "$FORM" -t "$TO" -g "$BUILD_CODE_PATH"/"$PROJECT_NAME"
      fi
      # shellcheck disable=SC2181
      if [ "$?" != 0 ]; then
        echo "-------------增量构建失败-------------"
        hook
        exit 1
      fi
      echo "-------------增量构建完毕-------------"
    fi
  fi
} || {
  hook
}

exec "$@"
