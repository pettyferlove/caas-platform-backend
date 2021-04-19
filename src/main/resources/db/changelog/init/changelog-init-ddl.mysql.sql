--liquibase formatted sql

--changeset Petty:caas-init-ddl-1
CREATE TABLE biz_application_deployment (id VARCHAR(128) NOT NULL, name VARCHAR(255) NOT NULL COMMENT '部署名称', namespace_id VARCHAR(128) NULL COMMENT '应用所属命名空间ID', description_content VARCHAR(4000) NULL COMMENT '描述', auto_build_id VARCHAR(128) NULL COMMENT '自动构建项目ID，不使用自动构建则为空', images_depository_id VARCHAR(128) NULL COMMENT '镜像仓库ID', image_name VARCHAR(400) NULL COMMENT '镜像名称', image_tag VARCHAR(128) NULL COMMENT '镜像Tag', image_pull_strategy VARCHAR(64) NULL COMMENT '镜像拉取规则', instances_number INT NULL COMMENT '实例数量', strategy_type VARCHAR(64) DEFAULT 'RollingUpdate' NULL COMMENT '更新策略', max_surge INT DEFAULT 1 NULL COMMENT '升级过程中最多可以比原先设置多出的POD数量', max_unavaible INT DEFAULT 1 NULL COMMENT '升级过程中最多有多少个POD处于无法提供服务的状态', revision_history_limit INT DEFAULT 10 NULL COMMENT '历史版本最多保存数量', network VARCHAR(64) DEFAULT 'none' NULL COMMENT '网络设置', network_type VARCHAR(64) NULL COMMENT '网络类型', node VARCHAR(128) NULL COMMENT '指定部署节点', external_ip VARCHAR(400) NULL COMMENT '外部访问IP，以英文,分隔', del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_APPLICATION_DEPLOYMENT PRIMARY KEY (id), UNIQUE (id)) COMMENT='应用部署';
ALTER TABLE biz_application_deployment COMMENT = '应用部署';

--changeset Petty:caas-init-ddl-2
CREATE TABLE biz_application_deployment_network (id VARCHAR(128) NOT NULL, deployment_id VARCHAR(128) NULL, protocol VARCHAR(64) NULL COMMENT '通讯协议', port INT NULL COMMENT '端口号', target_port INT NULL COMMENT '目标端口号', del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_APPLICATION_DEPLOYMENT_NETWORK PRIMARY KEY (id), UNIQUE (id)) COMMENT='应用网络设置';
ALTER TABLE biz_application_deployment_network COMMENT = '应用网络设置';

--changeset Petty:caas-init-ddl-3
CREATE TABLE biz_config (id VARCHAR(128) NOT NULL, namespace_id VARCHAR(128) NULL COMMENT '命名空间ID', config_name VARCHAR(255) NULL COMMENT '配置名称', `description` VARCHAR(255) NULL COMMENT '描述', file_name VARCHAR(255) NULL COMMENT '配置文件名称', config_type VARCHAR(255) NULL COMMENT '配置类型', content TEXT NULL COMMENT '内容', env_type TINYINT(3) UNSIGNED DEFAULT 1 NULL COMMENT '环境信息', del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_CONFIG PRIMARY KEY (id)) COMMENT='配置文件管理';
ALTER TABLE biz_config COMMENT = '配置文件管理';

--changeset Petty:caas-init-ddl-4
CREATE TABLE biz_global_configuration (id VARCHAR(128) NOT NULL, docker_host VARCHAR(255) DEFAULT '0.0.0.0:2375' NOT NULL COMMENT 'docker主机地址', workspace VARCHAR(255) DEFAULT '/usr/local/build/workspace' NOT NULL COMMENT '源码构建存放目录', docker_registry_path VARCHAR(400) DEFAULT 'http://127.0.0.1' NOT NULL COMMENT 'Docker镜像仓库地址', docker_registry_username VARCHAR(255) NULL, docker_registry_password VARCHAR(255) NULL, maven_home VARCHAR(255) DEFAULT '/usr/local/maven' NULL, del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_GLOBAL_CONFIGURATION PRIMARY KEY (id));

--changeset Petty:caas-init-ddl-5
CREATE TABLE biz_images_depository (id VARCHAR(128) NOT NULL, project_id VARCHAR(128) NOT NULL COMMENT '镜像仓库ProjectID', project_name VARCHAR(400) NOT NULL, del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_IMAGES_DEPOSITORY PRIMARY KEY (id));

--changeset Petty:caas-init-ddl-6
CREATE TABLE biz_namespace (id VARCHAR(128) NOT NULL, name VARCHAR(255) NOT NULL COMMENT '命名空间名称', istio BIT(1) DEFAULT 0 NULL COMMENT '是否开启istio注入', `description` VARCHAR(255) NULL COMMENT '描述信息', env_type BIT(1) DEFAULT 1 NULL COMMENT '环境类型', registry_secret_name VARCHAR(255) NULL COMMENT '仓库访问密钥', del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_NAMESPACE PRIMARY KEY (id));

--changeset Petty:caas-init-ddl-7
CREATE TABLE biz_project_build (id VARCHAR(128) NOT NULL, namespace_id VARCHAR(128) NULL COMMENT '命名空间ID', project_id VARCHAR(128) NULL COMMENT '项目ID', project_name VARCHAR(255) NOT NULL COMMENT '项目名称', clone_url VARCHAR(500) NOT NULL COMMENT 'Clone地址', open_auto_build TINYINT(3) UNSIGNED DEFAULT 1 NULL COMMENT '是否打开自动构建', need_build_project TINYINT(3) UNSIGNED DEFAULT 0 NOT NULL COMMENT '是否需要构建', build_tool VARCHAR(255) NULL COMMENT '构建工具', build_command VARCHAR(1000) NULL COMMENT '构建命令', build_params VARCHAR(255) NULL COMMENT '构建参数', need_build_image TINYINT(3) UNSIGNED DEFAULT 1 NULL COMMENT '是否构建镜像', build_target_path VARCHAR(255) NULL COMMENT '构建目标路径（相对路径）', build_target_name VARCHAR(255) NULL COMMENT '构建目标文件夹或文件名', dockerfile_already_exists TINYINT(3) UNSIGNED DEFAULT 0 NOT NULL COMMENT '源码是否存在Dockerfile', dockerfile_path VARCHAR(255) NULL COMMENT '源码Dockerfile相对路径', dockerfile_content VARCHAR(4000) NULL COMMENT 'Dockerfile内容（仅源码不存在Dockerfile时需要）', project_hook_id VARCHAR(128) NULL COMMENT 'Project触发器ID', trigger_method VARCHAR(64) DEFAULT 'branch' NOT NULL COMMENT '触发条件', branch VARCHAR(64) DEFAULT 'master' NOT NULL COMMENT '源码拉取分支', images_depository_alias VARCHAR(255) NOT NULL COMMENT '镜像仓库别名', source_project_name VARCHAR(255) NULL COMMENT '源码仓库的项目名称', source_project_web_url VARCHAR(600) NULL COMMENT '源码仓库Web访问地址', images_depository_id VARCHAR(128) NULL COMMENT '镜像仓库ID', persistent_build_file TINYINT(3) UNSIGNED DEFAULT 0 NULL COMMENT '是否需要持久化构建文件', env_type TINYINT(3) UNSIGNED DEFAULT 1 NULL COMMENT '环境信息', del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_PROJECT_BUILD PRIMARY KEY (id)) COMMENT='项目构建配置';
ALTER TABLE biz_project_build COMMENT = '项目构建配置';

--changeset Petty:caas-init-ddl-8
CREATE TABLE biz_project_build_history (id VARCHAR(128) NOT NULL, job_id VARCHAR(255) NOT NULL COMMENT '任务ID', build_id VARCHAR(128) NULL COMMENT '构建ID', image_full_name VARCHAR(400) NULL COMMENT '镜像全名', file_id VARCHAR(255) NULL COMMENT '附件ID', build_status VARCHAR(255) NULL COMMENT '构建状态', del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_PROJECT_BUILD_HISTORY PRIMARY KEY (id)) COMMENT='项目构建历史记录';
ALTER TABLE biz_project_build_history COMMENT = '项目构建历史记录';

--changeset Petty:caas-init-ddl-9
CREATE TABLE biz_sql_build (id VARCHAR(128) NOT NULL, namespace_id VARCHAR(128) NOT NULL COMMENT '命名空间', project_name VARCHAR(255) NOT NULL COMMENT '项目名称', describe_info VARCHAR(255) NULL COMMENT '项目中文描述', depository_type TINYINT(3) UNSIGNED NOT NULL COMMENT '仓库类型', remote_path VARCHAR(255) NOT NULL COMMENT '远程仓库地址', remote_branch VARCHAR(255) NOT NULL COMMENT '远程分支', env VARCHAR(255) NULL COMMENT '环境变量', username VARCHAR(255) NULL COMMENT '账号名', password VARCHAR(255) NULL COMMENT '密码', config VARCHAR(1000) NULL COMMENT '配置', sql_from VARCHAR(255) NULL COMMENT '脚本生成起点', sql_to VARCHAR(255) NULL COMMENT '脚本生成终点', env_type TINYINT(3) UNSIGNED DEFAULT 1 NULL COMMENT '环境类型', auth_type TINYINT(3) UNSIGNED DEFAULT 1 NOT NULL COMMENT '认证类型', full BIT(1) DEFAULT 0 NOT NULL COMMENT '全量构建', del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_SQL_BUILD PRIMARY KEY (id)) COMMENT='SQL构建';
ALTER TABLE biz_sql_build COMMENT = 'SQL构建';

--changeset Petty:caas-init-ddl-10
CREATE TABLE biz_sql_build_history (id VARCHAR(128) NOT NULL, job_id VARCHAR(255) NOT NULL, build_id VARCHAR(128) NULL, file_id VARCHAR(255) NULL, build_status VARCHAR(255) NULL, start VARCHAR(255) NULL, end VARCHAR(255) NULL, del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_BIZ_SQL_BUILD_HISTORY PRIMARY KEY (id)) COMMENT='SQL构建历史记录';
ALTER TABLE biz_sql_build_history COMMENT = 'SQL构建历史记录';

--changeset Petty:caas-init-ddl-11
CREATE TABLE biz_user_configuration (id VARCHAR(128) NOT NULL, gitlab_home_path VARCHAR(400) NOT NULL COMMENT 'gitlab地址', gitlab_api_token VARCHAR(128) NOT NULL COMMENT 'Gitlab令牌', private_key VARCHAR(1000) NOT NULL COMMENT 'RSA私钥', del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', user_key_id INT NOT NULL COMMENT 'SSHkey ID', CONSTRAINT PK_BIZ_USER_CONFIGURATION PRIMARY KEY (id)) COMMENT='用户个人配置';
ALTER TABLE biz_user_configuration COMMENT = '用户个人配置';

--changeset Petty:caas-init-ddl-12
CREATE TABLE system_attachment_info (id VARCHAR(128) NOT NULL, file_name VARCHAR(255) NOT NULL COMMENT '文件名', file_type VARCHAR(255) NOT NULL COMMENT '文件类型', size BIGINT NOT NULL COMMENT '文件大小', storage_type INT NOT NULL COMMENT '储存类型', md5 VARCHAR(255) NOT NULL COMMENT 'MD5值', `path` VARCHAR(255) NOT NULL COMMENT '地址', del_flag TINYINT(3) UNSIGNED DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', tenant_id VARCHAR(255) NULL, CONSTRAINT PK_SYSTEM_ATTACHMENT_INFO PRIMARY KEY (id));

--changeset Petty:caas-init-ddl-13
CREATE TABLE system_oauth_client_details (id VARCHAR(128) NOT NULL, client_id VARCHAR(128) NOT NULL, resource_ids VARCHAR(256) NULL, client_secret VARCHAR(256) NULL, scope VARCHAR(256) NULL, authorized_grant_types VARCHAR(256) NULL, web_server_redirect_uri VARCHAR(256) NULL, authorities VARCHAR(256) NULL, access_token_validity INT NULL, refresh_token_validity INT NULL, additional_information VARCHAR(1024) NULL, auto_approve VARCHAR(256) NULL, del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_SYSTEM_OAUTH_CLIENT_DETAILS PRIMARY KEY (id)) COMMENT='终端信息表';
ALTER TABLE system_oauth_client_details COMMENT = '终端信息表';

--changeset Petty:caas-init-ddl-14
CREATE TABLE system_role (id VARCHAR(128) NOT NULL, `role` VARCHAR(128) NOT NULL COMMENT '角色', role_name VARCHAR(128) NOT NULL COMMENT '角色名称', `description` VARCHAR(400) NULL COMMENT '描述', sort SMALLINT NULL COMMENT '排序号', status TINYINT(3) DEFAULT 1 NOT NULL COMMENT '状态 1有效 0无效 默认为1', del_flag TINYINT(3) UNSIGNED DEFAULT 0 NOT NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', tenant_id VARCHAR(255) NULL COMMENT '租户ID', CONSTRAINT PK_SYSTEM_ROLE PRIMARY KEY (id)) COMMENT='用户角色';
ALTER TABLE system_role COMMENT = '用户角色';

--changeset Petty:caas-init-ddl-15
CREATE TABLE system_user (id VARCHAR(128) DEFAULT '' NOT NULL, login_name VARCHAR(256) NOT NULL COMMENT '用户登录名', user_name VARCHAR(128) NOT NULL COMMENT '用户全名', password VARCHAR(128) NOT NULL COMMENT '账号密码', user_sex TINYINT(3) NULL COMMENT '性别', user_born datetime NULL COMMENT '生日', user_avatar VARCHAR(128) NULL COMMENT '用户头像', email VARCHAR(128) NULL COMMENT '电子邮件', user_address VARCHAR(400) NULL COMMENT '居住地址', mobile_tel VARCHAR(128) NULL COMMENT '移动电话', user_tel VARCHAR(96) NULL COMMENT '用户联系电话', user_iden_type VARCHAR(64) NULL COMMENT '用户证件类型', user_iden VARCHAR(128) NULL COMMENT '证件ID', status TINYINT(3) DEFAULT 0 NOT NULL COMMENT '是否有效 0 无效 1 有效', del_flag TINYINT(3) UNSIGNED DEFAULT 0 NOT NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', tenant_id VARCHAR(255) NULL COMMENT '租户ID', CONSTRAINT PK_SYSTEM_USER PRIMARY KEY (id)) COMMENT='用户信息';
ALTER TABLE system_user COMMENT = '用户信息';

--changeset Petty:caas-init-ddl-16
CREATE TABLE system_user_role (id VARCHAR(128) NOT NULL, user_id VARCHAR(128) NOT NULL, role_id VARCHAR(128) NOT NULL, del_flag BIT(1) DEFAULT 0 NULL COMMENT '删除标记 0 未删除 1 删除', creator VARCHAR(128) NULL COMMENT '创建人', create_time datetime NULL COMMENT '创建时间', modifier VARCHAR(128) NULL COMMENT '修改人', modify_time datetime NULL COMMENT '修改时间', group_id VARCHAR(128) NULL COMMENT '项目组ID', tenant_id VARCHAR(128) NULL COMMENT '租户ID', CONSTRAINT PK_SYSTEM_USER_ROLE PRIMARY KEY (id)) COMMENT='用户角色关联信息';
ALTER TABLE system_user_role COMMENT = '用户角色关联信息';

--changeset Petty:caas-init-ddl-17
ALTER TABLE system_user ADD CONSTRAINT login_name UNIQUE (login_name, del_flag, tenant_id, id);

--changeset Petty:caas-init-ddl-18
ALTER TABLE system_user ADD CONSTRAINT mobile_tel UNIQUE (mobile_tel, del_flag, tenant_id, id);

--changeset Petty:caas-init-ddl-19
ALTER TABLE biz_application_deployment ADD CONSTRAINT name_del_flag_unique_index UNIQUE (name, del_flag);

--changeset Petty:caas-init-ddl-20
ALTER TABLE system_role ADD CONSTRAINT `role` UNIQUE (`role`, del_flag, tenant_id);

--changeset Petty:caas-init-ddl-21
CREATE INDEX role_code ON system_user_role(role_id);

--changeset Petty:caas-init-ddl-22
CREATE INDEX system_auto_build_log_auto_build_id_index ON biz_project_build_history(build_id);

--changeset Petty:caas-init-ddl-23
CREATE INDEX user_code ON system_user_role(user_id);