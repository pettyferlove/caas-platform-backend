$prefix = "pettyfer"
$version = "1.0.0"
$jdk_version = "oracle-jdk-8_281"
$jre_version = "oracle-jre-8_281"
# 基础镜像
Write-Output "Begin building the base tool image,version:$version"
Set-Location "alpine-base"
docker build -t "$prefix/alpine-base:$version" .
docker push "$prefix/alpine-base:$version"
Set-Location "../"
Write-Output "The base tool image has been built"

# 基础JDK镜像
Write-Output "Begin building the oracle jdk image,version:$jdk_version"
Set-Location "alpine-oracle-jdk-8"
docker build -t "$prefix/alpine-tool-base:$jdk_version" .
docker push "$prefix/alpine-tool-base:$jdk_version"
Set-Location "../"
Write-Output "The oracle jdk image has been built"

# 基础JRE镜像
Write-Output "Begin building the oracle jre image,version:$jre_version"
Set-Location "alpine-oracle-jre-8"
docker build -t "$prefix/alpine-tool-base:$jre_version" .
docker push "$prefix/alpine-tool-base:$jre_version"
Set-Location "../"
Write-Output "The oracle jre image has been built"

# Docker构建镜像
Write-Output "Begin building the docker build image,version:$version"
Set-Location "build-docker-tool"
docker build -t "$prefix/build-docker-tool:$version" .
docker push "$prefix/build-docker-tool:$version"
Set-Location "../"
Write-Output "The docker build image has been built"

# Git工具镜像
Write-Output "Begin building the git tool image,version:$version"
Set-Location "build-git-tool"
docker build -t "$prefix/build-git-tool:$version" .
docker push "$prefix/build-git-tool:$version"
Set-Location "../"
Write-Output "The git tool image has been built"


# Maven工具镜像
Write-Output "Begin building the maven tool image,version:$version"
Set-Location "build-maven-tool"
docker build -t "$prefix/build-maven-tool:$version" .
docker push "$prefix/build-maven-tool:$version"
Set-Location "../"
Write-Output "The maven tool image has been built"

# NodeJS工具镜像
Write-Output "Begin building the nodejs tool image,version:$version"
Set-Location "build-nodejs-tool"
docker build -t "$prefix/build-nodejs-tool:$version" .
docker push "$prefix/build-nodejs-tool:$version"
Set-Location "../"
Write-Output "The nodejs tool image has been built"

# Notification工具镜像
Write-Output "Begin building the notification tool image,version:$version"
Set-Location "build-notification-tool"
docker build -t "$prefix/build-notification-tool:$version" .
docker push "$prefix/build-notification-tool:$version"
Set-Location "../"
Write-Output "The notification tool image has been built"

# Persistence工具镜像
Write-Output "Begin building the persistence tool image,version:$version"
Set-Location "build-persistence-tool"
docker build -t "$prefix/build-persistence-tool:$version" .
docker push "$prefix/build-persistence-tool:$version"
Set-Location "../"
Write-Output "The persistence tool image has been built"

# Shell工具镜像
Write-Output "Begin building the shell tool image,version:$version"
Set-Location "build-shell-tool"
docker build -t "$prefix/build-shell-tool:$version" .
docker push "$prefix/build-shell-tool:$version"
Set-Location "../"
Write-Output "The shell tool image has been built"

# SQL工具镜像
Write-Output "Begin building the sql tool image,version:$version"
Set-Location "build-sql-tool"
docker build -t "$prefix/build-sql-tool:$version" .
docker push "$prefix/build-sql-tool:$version"
Set-Location "../"
Write-Output "The sql tool image has been built"

# Subversion
Write-Output "Begin building the subversion tool image,version:$version"
Set-Location "build-subversion-tool"
docker build -t "$prefix/build-subversion-tool:$version" .
docker push "$prefix/build-subversion-tool:$version"
Set-Location "../"
Write-Output "The subversion tool image has been built"