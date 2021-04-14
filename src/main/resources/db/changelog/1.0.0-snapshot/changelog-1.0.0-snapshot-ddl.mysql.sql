--liquibase formatted sql

--changeset Petty:caas-1.0.0-snapshot-ddl-1

alter table biz_global_configuration drop column docker_host;

alter table biz_global_configuration drop column workspace;

alter table biz_global_configuration drop column maven_home;

alter table biz_global_configuration add cluster_server varchar(1000) null comment 'CAAS集群地址;号分隔' after docker_registry_password;

--changeset Petty:caas-1.0.0-snapshot-ddl-2
alter table biz_application_deployment add env_type tinyint unsigned default 1 null comment '环境类型' after external_ip;

alter table biz_application_deployment_network add env_type tinyint unsigned default 1 null comment '环境类型' after target_port;

--changeset Petty:caas-1.0.0-snapshot-ddl-3
alter table biz_application_deployment add environment_variable varchar(1000) null comment '应用环境变量' after image_tag;

--changeset Petty:caas-1.0.0-snapshot-ddl-4
create table biz_application_deployment_volume
(
    id            varchar(128)     not null
        primary key,
    deployment_id varchar(128)     null,
    volume_name   varchar(128)     null comment '卷名',
    volume_type   varchar(255)     null comment '卷类型（配置文件、目录、空目录、主机目录）',
    volume_path   varchar(255)     null comment '卷地址（空目录和配置文件不需指定）',
    config_id     varchar(128)     null comment '配置文件ID',
    mount_name    varchar(128)     null comment '挂载名称',
    mount_path    varchar(255)     null comment '挂载地址',
    del_flag      bit default b'0' null comment '删除标记 0 未删除 1 删除',
    creator       varchar(128)     null comment '创建人',
    create_time   datetime         null comment '创建时间',
    modifier      varchar(128)     null comment '修改人',
    modify_time   datetime         null comment '修改时间',
    group_id      varchar(128)     null comment '项目组ID',
    tenant_id     varchar(128)     null comment '租户ID'
);