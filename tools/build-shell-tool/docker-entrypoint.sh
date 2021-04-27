#!/bin/bash
set -e

function run() {

  if [ "$SHELL_TYPE" = "PRE" ]; then
    echo "-------------开始执行前置脚本-------------"
    echo -e "$PRE_SHELL_SCRIPT" >pre_script.sh
    sh -n pre_script.sh
    # shellcheck disable=SC2181
    if [ "$?" != 0 ]; then
      echo "-------------脚本语法检查失败-------------"
      hook
      exit 1
    fi
    sh pre_script.sh
    # shellcheck disable=SC2181
    if [ "$?" != 0 ]; then
      echo "-------------脚本执行失败-------------"
      hook
      exit 1
    fi
    echo "-------------执行前置脚本结束-------------"
  fi

  if [ "$SHELL_TYPE" = "POST" ]; then
    echo "-------------开始执行后置脚本-------------"
    echo -e "$POST_SHELL_SCRIPT" >post_script.sh
    sh -n post_script.sh
    # shellcheck disable=SC2181
    if [ "$?" != 0 ]; then
      echo "-------------脚本语法检查失败-------------"
      hook
      exit 1
    fi
    sh post_script.sh
    # shellcheck disable=SC2181
    if [ "$?" != 0 ]; then
      echo "-------------脚本执行失败-------------"
      hook
      exit 1
    fi
    echo "-------------执行后置脚本结束-------------"
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
  run
} || {
  hook
}

exec "$@"
