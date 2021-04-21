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

--changeset Petty:caas-1.0.0-snapshot-ddl-5
alter table biz_namespace modify env_type tinyint unsigned default 1 null comment '环境类型';

--changeset Petty:caas-1.0.0-snapshot-ddl-6
alter table biz_application_deployment_volume modify mount_name varchar(128) null comment '挂载名称' after deployment_id;

alter table biz_application_deployment_volume modify mount_path varchar(255) null comment '挂载地址' after mount_name;

rename table biz_application_deployment_volume to biz_application_deployment_mount;

--changeset Petty:caas-1.0.0-snapshot-ddl-7
create or replace view user_list_view as
select u.id, u.login_name, u.user_name, u.user_avatar, group_concat(r.role_name) roles, u.create_time
from system_user u
         left join system_user_role ur on u.id = ur.user_id
         left join system_role r on ur.role_id = r.id group by u.id;

--changeset Petty:caas-1.0.0-snapshot-ddl-8
create unique index project_unique_index on biz_project_build (project_name, namespace_id, del_flag);

create unique index project_unique_index on biz_sql_build (project_name, namespace_id, del_flag);

--changeset Petty:caas-1.0.0-snapshot-ddl-9
alter table biz_application_deployment_network add network_name varchar(128) null comment '网络名称' after deployment_id;

alter table biz_application_deployment_network drop column protocol;

alter table biz_application_deployment_network add network varchar(64) null comment '网络设置' after network_name;

alter table biz_application_deployment_network drop column port;

alter table biz_application_deployment_network add network_type varchar(64) null comment '网络类型' after network;

alter table biz_application_deployment_network drop column target_port;

alter table biz_application_deployment_network add external_ip int null comment '外部访问IP，以英文,分隔' after network_type;

alter table biz_application_deployment_network add port_map varchar(1000) null comment '端口映射表' after external_ip;

alter table biz_application_deployment drop column network;

alter table biz_application_deployment drop column network_type;

alter table biz_application_deployment drop column external_ip;

rename table biz_application_deployment_network to biz_network;

--changeset Petty:caas-1.0.0-snapshot-ddl-10
alter table biz_network change port_map ports varchar(1000) null comment '端口映射表';

--changeset Petty:caas-1.0.0-snapshot-ddl-11
alter table biz_network modify external_ip varchar(400) null comment '外部访问IP，以英文,分隔';

--changeset Petty:caas-1.0.0-snapshot-ddl-12
alter table biz_network add match_label varchar(1000) null comment '匹配标签' after network;

alter table biz_network change network_name name varchar(128) null comment '服务名称';

rename table biz_network to biz_service_discovery;

alter table biz_service_discovery comment '服务发现';

--changeset Petty:caas-1.0.0-snapshot-ddl-13
alter table biz_service_discovery add namespace_id varchar(128) null after id;
