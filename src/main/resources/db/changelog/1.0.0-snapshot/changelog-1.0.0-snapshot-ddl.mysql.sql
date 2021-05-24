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

--changeset Petty:caas-1.0.0-snapshot-ddl-14
create table biz_persistent_storage
(
    id                 varchar(128)                  not null
        primary key,
    namespace_id       varchar(128)                  null comment '应用所属命名空间ID',
    name               varchar(255)                  null comment '持久化储存名称',
    init_size          varchar(255)                  null comment '初始容量大小',
    limit_size         varchar(255)                  null comment '最大容量大小',
    unit               varchar(255)                  null comment '容量单位',
    storage_class_name varchar(255)                  null comment '储存类名称',
    access_mode        varchar(255)                  null comment '访问模式',
    env_type           tinyint unsigned default '1'  null comment '环境类型',
    del_flag           bit              default b'0' null comment '删除标记 0 未删除 1 删除',
    creator            varchar(128)                  null comment '创建人',
    create_time        datetime                      null comment '创建时间',
    modifier           varchar(128)                  null comment '修改人',
    modify_time        datetime                      null comment '修改时间',
    group_id           varchar(128)                  null comment '项目组ID',
    tenant_id          varchar(128)                  null comment '租户ID'
)
    comment '持久化储存';

--rollback drop table biz_persistent_storage;

--changeset Petty:caas-1.0.0-snapshot-ddl-15
alter table biz_application_deployment_mount add persistent_storage_id varchar(128) null comment '持久化储存ID' after config_id;

--changeset Petty:caas-1.0.0-snapshot-ddl-16
create unique index application_deployment_unique_index on biz_application_deployment (namespace_id, name, del_flag);

drop index id on biz_application_deployment;

drop index name_del_flag_unique_index on biz_application_deployment;

drop index id on biz_service_discovery;

create unique index service_discovery_unique_index on biz_service_discovery (namespace_id, name, del_flag);

--changeset Petty:caas-1.0.0-snapshot-ddl-17
create table system_message
(
    id                    varchar(128)                 not null comment '数据唯一标识符'
        primary key,
    message               varchar(255)                 not null comment '消息',
    content               varchar(1000)                null comment '具体内容',
    time                  datetime                     null comment '消息产生时间',
    type                  varchar(128)                 null comment '消息类型 success、error',
    business_name         varchar(128)                 null comment '业务名称',
    business_id           varchar(400)                 null comment '业务ID',
    business_page_address varchar(128)                 null comment '该业务对应的页面地址（对应前端路径）',
    edit_page_address     varchar(128)                 null comment '编辑地址（对应前端路径）',
    view_page_address     varchar(128)                 null comment '查看地址（对应前端路径）',
    receiver              varchar(128)                 not null comment '接收人USER_ID',
    deliver               varchar(128)                 not null comment '发送人USER_ID，如果是系统消息则为system',
    state                 tinyint default 0            not null comment '状态 1已读 0未读 默认为0',
    del_flag              bit     default b'0'         null comment '删除标记 0 未删除 1 删除',
    creator               varchar(128) charset utf8mb4 null comment '创建人',
    create_time           datetime                     null comment '创建时间',
    modifier              varchar(128) charset utf8mb4 null comment '修改人',
    modify_time           datetime                     null comment '修改时间',
    group_id              varchar(128) charset utf8mb4 null comment '项目组ID',
    tenant_id             varchar(128) charset utf8mb4 null comment '租户ID'
)
    comment '系统消息' collate = utf8mb4_general_ci;

--rollback drop table system_message;

--changeset Petty:caas-1.0.0-snapshot-ddl-18
alter table biz_project_build add parent_id varchar(128) null comment '父项目ID' after id;

--rollback alter table biz_project_build drop column parent_id;

--changeset Petty:caas-1.0.0-snapshot-ddl-19
alter table biz_project_build change project_id remote_project_id varchar(128) null comment '远程仓库项目ID';

--rollback alter table biz_project_build change gitlab_project_id project_id varchar(128) null comment 'Gitlab项目ID';

--changeset Petty:caas-1.0.0-snapshot-ddl-20
alter table biz_project_build add depository_type tinyint unsigned default 1 null comment '仓库类型' after project_name;

--rollback alter table biz_project_build drop column depository_type;

--changeset Petty:caas-1.0.0-snapshot-ddl-21
alter table biz_project_build change clone_url remote_path varchar(500) not null comment 'Clone地址';

--rollback alter table biz_project_build change remote_path clone_url varchar(500) not null comment 'Clone地址';

--changeset Petty:caas-1.0.0-snapshot-ddl-22
alter table biz_project_build change branch remote_branch varchar(64) default 'master' not null comment '源码拉取分支' after remote_path;

--rollback alter table biz_project_build change remote_branch branch varchar(64) default 'master' not null comment '源码拉取分支' after remote_path;

--changeset Petty:caas-1.0.0-snapshot-ddl-23
alter table biz_project_build add remote_owner varchar(128) null comment '远程仓库地址空间' after remote_branch;

--rollback alter table biz_project_build drop column remote_owner;

--changeset Petty:caas-1.0.0-snapshot-ddl-24
alter table biz_project_build add remote_repo varchar(255) null comment '仓库路径（相对路径）' after remote_owner;

--rollback alter table biz_project_build drop column remote_repo;

--changeset Petty:caas-1.0.0-snapshot-ddl-25
alter table biz_project_build add pre_shell_script text null comment '前置脚本' after build_params;

--rollback alter table biz_project_build drop column pre_shell_script;

--changeset Petty:caas-1.0.0-snapshot-ddl-26
alter table biz_project_build add post_shell_script text null comment '后置脚本' after pre_shell_script;

--rollback alter table biz_project_build drop column post_shell_script;

--changeset Petty:caas-1.0.0-snapshot-ddl-27
alter table biz_project_build add run_pre_shell_script bit default false null  comment '是否执行前置脚本' after post_shell_script;

--rollback alter table biz_project_build drop column run_pre_shell_script;

--changeset Petty:caas-1.0.0-snapshot-ddl-28
alter table biz_project_build add run_post_shell_script bit default false null  comment '是否执行后置脚本' after run_pre_shell_script;

--rollback alter table biz_project_build drop column run_post_shell_script;

--changeset Petty:caas-1.0.0-snapshot-ddl-29
alter table biz_project_build modify depository_type varchar(128) default 'gitlab_v4' null comment '仓库类型';

--rollback alter table biz_project_build modify depository_type tinyint unsigned default 1 null comment '仓库类型';

--changeset Petty:caas-1.0.0-snapshot-ddl-30
alter table biz_project_build modify remote_project_id varchar(128) null comment '远程仓库项目ID' after depository_type;

--changeset Petty:caas-1.0.0-snapshot-ddl-31
alter table biz_user_configuration add subversion_username varchar(400) null comment 'SVN用户名' after private_key;

--changeset Petty:caas-1.0.0-snapshot-ddl-32
alter table biz_user_configuration add subversion_password varchar(400) null comment 'SVN密码' after subversion_username;

--changeset Petty:caas-1.0.0-snapshot-ddl-33
alter table biz_project_build add link_project bit default false null comment '是否需要关联其他项目' after parent_id;

--changeset Petty:caas-1.0.0-snapshot-ddl-34
alter table biz_application_deployment add run_status varchar(255) null comment '运行状态' after env_type;

--changeset Petty:caas-1.0.0-snapshot-ddl-35
alter table biz_application_deployment add open_liveness_probe bit default false null comment '是否开启存活探针' after node;

--changeset Petty:caas-1.0.0-snapshot-ddl-36
alter table biz_application_deployment add open_readiness_probe bit default false null comment '是否开启就绪探针' after open_liveness_probe;

--changeset Petty:caas-1.0.0-snapshot-ddl-37
alter table biz_application_deployment add liveness_probe json null comment '存活探针配置' after open_readiness_probe;

--changeset Petty:caas-1.0.0-snapshot-ddl-38
alter table biz_application_deployment add readiness_probe json null comment '就绪探针配置' after liveness_probe;

--changeset Petty:caas-1.0.0-snapshot-ddl-39
alter table biz_application_deployment modify environment_variable json null comment '应用环境变量';

--changeset Petty:caas-1.0.0-snapshot-ddl-40
alter table biz_service_discovery modify match_label json null comment '匹配标签';

--changeset Petty:caas-1.0.0-snapshot-ddl-41
alter table biz_service_discovery modify ports json null comment '端口映射表';

--changeset Petty:caas-1.0.0-snapshot-ddl-42
alter table biz_application_deployment_mount drop column del_flag;

--changeset Petty:caas-1.0.0-snapshot-ddl-43
alter table biz_project_build add project_describe varchar(400) null comment '描述信息' after project_name;