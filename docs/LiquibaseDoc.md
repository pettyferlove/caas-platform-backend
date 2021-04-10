# 介绍
介绍
Liquibase是一个用于数据库重构和迁移的开源工具，通过日志文件的形式记录数据库的变更，然后执行日志文件中的修改，将数据库更新或回滚到一致的状态。它的目标是提供一种数据库类型无关的解决方案，通过执行schema类型的文件来达到迁移。其有点主要有以下：

* 支持几乎所有主流的数据库，如MySQL, PostgreSQL, Oracle, Sql Server, DB2等；
* 支持多开发者的协作维护；
* 日志文件支持多种格式，如XML, YAML, JSON, SQL等；
* 支持多种运行方式，如命令行、Spring集成、Maven插件、Gradle插件等。

# 配置文件
db/liquibase.properties 数据库连接参数
db/db.changelog-master.yml Liquibase配置文件入口
``` 
// 可增加新的include
  - include:
      file: db/changelog/1.0.0/db.changelog-1.0.0.yml
```

/db/changelog/日期/***.mysql.sql 需要执行的sql
/db/changelog/日期/changelogdb.changelog-日期.yml 该目录下所有sql的总集，会被db.changelog-master.yml引用



# SQL文件编写示例
``` 
--liquibase formatted sql

--changeset Petty:1.0.0-ddl-1
CREATE TABLE biz_news_info (id VARCHAR(128) NOT NULL, title VARCHAR(128) NOT NULL COMMENT '资讯标题', cover_image VARCHAR(256) NULL COMMENT '封面图片路径', category VARCHAR(64) NULL COMMENT '资讯类别 @dict information_classification', type VARCHAR(64) NULL COMMENT '资讯类型 @dict information_type', citation_source VARCHAR(128) NULL COMMENT '引用来源', citation_source_url TEXT NULL COMMENT '引用URL', classify VARCHAR(64) NOT NULL COMMENT '资讯分类', editor VARCHAR(64) NOT NULL COMMENT '责任编辑', summary VARCHAR(256) NULL COMMENT '资讯摘要', content TEXT NOT NULL COMMENT '资讯内容', keyword VARCHAR(256) NULL COMMENT '关键字', stick TINYINT(3) UNSIGNED DEFAULT 0 NOT NULL COMMENT '置顶状态 0 否 1是', sort TINYINT(3) DEFAULT 0 NOT NULL COMMENT '排序设置', comment TINYINT(3) UNSIGNED DEFAULT 0 NOT NULL COMMENT '开放评论  0 否 1是', publish TINYINT(3) UNSIGNED DEFAULT 0 NOT NULL COMMENT '发布状态  0 否 1是', publisher VARCHAR(64) NULL COMMENT '发布人', publish_date datetime NULL COMMENT '发布时间', del_flag TINYINT(3) UNSIGNED NOT NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', tenant_id VARCHAR(255) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_NEWS_INFO PRIMARY KEY (id)) COMMENT='资讯信息表';
ALTER TABLE biz_news_info COMMENT = '资讯信息表';

```
* --liquibase formatted sql 标记为liquibase检查标记，有该标记的liquibase会进行版本对比，如果数据库还未更新，这执行对应sql

* --changeset Petty:1590579738857-1 ChangeSet编写人，以及ChangeSet Id，ChangeSet Id会作为liquibase，liquibase会根据该值判断是否执行


# 约定（强制）
1. 项目采用Liquibase进行数据库操作管理，但不限制本地测试数据。仅要求必要的操作记录到日志文件。
2. ddl语句必须在dml语句之前执行，可通过文件引入顺序来决定执行顺序

# Maven插件命令
```
// 逆向初始化SQL语句（生成DDL语句）
$ mvn liquibase:generateChangeLog
```

```
// 逆向初始化SQL语句（生成DML语句）
$ mvn liquibase:generateChangeLog -Dliquibase.diffTypes=data
```