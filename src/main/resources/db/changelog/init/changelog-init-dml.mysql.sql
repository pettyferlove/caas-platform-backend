--liquibase formatted sql

--changeset Petty:caas-init-dml-1
INSERT INTO system_role (id, `role`, role_name, `description`, sort, status, del_flag, creator, create_time, modifier, modify_time, tenant_id) VALUES ('0000000000000000001', 'DEVELOPER', '开发者', '开发者角色，用于进行开发用以及查看开发相关页面', 1, 1, 0, '0000000000000000001', '2019-06-08 09:54:11', '0000000000000000001', '2020-04-16 19:54:26', NULL);
INSERT INTO system_role (id, `role`, role_name, `description`, sort, status, del_flag, creator, create_time, modifier, modify_time, tenant_id) VALUES ('0000000000000000002', 'USER', '用户', '用户', 1, 1, 0, '0000000000000000001', '2019-01-08 10:40:57', '0000000000000000001', '2020-04-16 19:54:27', NULL);
INSERT INTO system_role (id, `role`, role_name, `description`, sort, status, del_flag, creator, create_time, modifier, modify_time, tenant_id) VALUES ('0000000000000000003', 'ADMIN', '系统管理员', '系统管理员', 1, 1, 0, '0000000000000000001', '2019-01-08 10:40:57', '0000000000000000001', '2020-04-16 19:54:28', NULL);

--changeset Petty:caas-init-dml-2
INSERT INTO system_user (id, login_name, user_name, password, user_sex, user_born, user_avatar, email, user_address, mobile_tel, user_tel, user_iden_type, user_iden, status, del_flag, creator, create_time, modifier, modify_time, tenant_id) VALUES ('0000000000000000001', 'admin', '超级管理员', '{bcrypt}$2a$10$CloqYzLozi08W.LzTLmlZuhxcC9pRWxx1bRSmT8W1YHYLKc1/oROK', NULL, NULL, 'https://bali-attachment.oss-cn-shanghai.aliyuncs.com/bali/avatar/430ae478c2e74ee1b563caca5fbf0349.png', NULL, NULL, NULL, '13094186549', NULL, NULL, 1, 0, '0000000000000000001', '2019-07-14 17:14:18', '0000000000000000001', '2020-05-16 12:53:45', NULL);

--changeset Petty:caas-init-dml-3
INSERT INTO system_user_role (id, user_id, role_id, del_flag, creator, create_time, modifier, modify_time, group_id, tenant_id) VALUES ('0000000000000000001', '0000000000000000001', '0000000000000000001', 0, NULL, NULL, NULL, NULL, NULL, NULL);

