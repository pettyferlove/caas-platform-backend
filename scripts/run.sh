#!/bin/bash
# shellcheck disable=SC2086
# shellcheck disable=SC2009
set -e
# 设置环境变量
export JAVA_HOME=$JAVA_HOME
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar


APP_NAME="kubernetes-caas"
APP_VERSION="0.4.0-SNAPSHOT"
JASYPT_PASSWORD="bali-auth-server"
SERVER_PORT=9090
PROFILE_NAME=prd
JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Xmn128m -Xms512m -Xmx512m -Dfile.encoding=UTF-8 -Dloader.path=."

usage() {
	echo "usage: run start|stop|restart"
}
start() {
	echo start $APP_NAME
	startup
	echo $APP_NAME started
}

startup() {
	nohup java -jar $JAVA_OPTS  "${APP_NAME}-${APP_VERSION}.jar" --server.port=$SERVER_PORT --spring.profiles.active=$PROFILE_NAME --jasypt.encryptor.password=$JASYPT_PASSWORD  >/dev/null 2>&1 &
}
stop() {
	PID=$(ps -ef | grep ${APP_NAME}-${APP_VERSION}.jar | grep -v grep | awk '{ print $2 }')
	if [ -z "$PID" ]
	then
	    echo $APP_NAME is already stopped
	else
	    echo kill $PID
	    kill -9  $PID
	fi
}

restart() {
	echo stopping $APP_NAME
	stop
	echo $APP_NAME stopped
	echo start $APP_NAME
	startup
	echo $APP_NAME started
}


if [ $# -ne 1 ]
then
	usage
	exit 0
fi


case $1 in
	'start')
		start
	;;
	'stop')
		stop
	;;
	'restart')
		restart
	;;
	*)
		usage
	;;
esac



# tail -f ./nohup.out
