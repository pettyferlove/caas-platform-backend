--liquibase formatted sql

--changeset Petty:caas-1.0.0-snapshot-dml-1
update biz_project_build set depository_type='gitlab_v4' where depository_type = '1';