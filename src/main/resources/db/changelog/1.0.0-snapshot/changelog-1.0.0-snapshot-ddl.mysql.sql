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