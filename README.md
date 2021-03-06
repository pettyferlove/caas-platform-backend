## 目录

- [项目基本说明](#项目基本说明)
- [开发工具及所需环境](#开发工具及所需环境)
- [命名规范](#命名规范)

## 项目基本说明

- 名称

caas平台

- git 地址

## 开发工具及所需环境

- 开发工具

1. Idea （推荐）
2. Eclipse

- 所需环境

1. Oracle JDK 1.8或以上
2. Maven 3.6.0或以上
3. Nginx 1.15.1或以上
4. Redis 3.4或以上
5. RabbitMQ 3.8或以上
5. MySQL 8.0或以上
6. Docker For Windows 8.0或以上

- Docker环境配置（针对Windows环境）

1. Docker IP配置 10.0.100.0
2. Windows Route 加入桥接 route -p add 172.17.0.0 MASK 255.255.255.0 10.0.100.2;

## 端口说明

- 公认端口（Well Known Ports）：从0到1023，它们紧密绑定（binding）于一些服务。通常这些端口的通讯明确表明了某种服务的协议。例如：80端口实际上总是HTTP通讯。
- 注册端口（Registered Ports）：从1024到49151。它们松散地绑定于一些服务。也就是说有许多服务绑定于这些端口，这些端口同样用于许多其它目的。例如：许多系统处理动态端口从1024左右开始。
- 动态和/或私有端口（Dynamic and/or Private Ports）：从49152到65535。理论上，不应为服务分配这些端口。实际上，机器通常从1024起分配动态端口。但也有例外：SUN的RPC端口从32768开始。

### 中间件端口

6379 | redis | Redis端口

### 数据库端口

3306 | mysql | MYSQL连接端口

## 命名规范

### Service接口命名

3. 持久层Service 则必须是 IXxxService 样式

### DTO命名

> 数据传输对象，这个概念来源于J2EE的设计模式，原来的目的是为了EJB的分布式应用提供粗粒度的数据实体，以减少分布式调用的次数，从而提高分布式调用的性能和降低网络负载，但在这里，我泛指用于服务层之间的数据传输对象。 用于服务内部调用过程中的传输对象

1. 对象命名均为 XxxDTO样式

### VO命名

> 视图对象，用于展示层，它的作用是把某个指定页面（或组件）的所有数据封装起来

1. 对象命名均为 XxxVO样式

## 更新日志

序号  | 版本号 | 日期 | 修改人 | 概述
--- | --- | --- | --- | ---
1 | 1.0.0 | 2020-6-1 | Petty | 初版


