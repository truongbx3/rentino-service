create table if not exists `app_config` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `user_name` varchar(255),
    `config_code` varchar(255),
    `config_name` varchar(255),
    `description` varchar(255),
    `value` varchar(255),
    `created_by` varchar(255),
    `updated_by` varchar(255),
    `created_date` datetime,
    `updated_date` datetime,
    `is_active` int DEFAULT 1 COMMENT '1 is active 0 is deactive',
    `is_deleted` int DEFAULT 0 COMMENT '1 is deleted 0 is not deleted',
    `is_password` int DEFAULT 0 COMMENT '1 is set password 0 is not set password',
    `index_order` int
);

create table if not exists `attach_file` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `file_name` varchar(255),
    `path_name` varchar(255),
    `system` varchar(255),
    `created_date` datetime,
    `updated_date` datetime,
    `created_by` varchar(255),
    `updated_by` varchar(255),
    `is_deleted` int DEFAULT 0 COMMENT '1 is deleted 0 is not deleted'
);

create table if not exists `menu` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `menu_name` varchar(255),
    `menu_code` varchar(255),
    `url` varchar(255),
    `description` varchar(255),
    `form_name` varchar(255),
    `icon` varchar(255),
    `created_user` varchar(255),
    `updated_user` varchar(255),
    `created_date` datetime,
    `updated_date` datetime,
    `is_show` int DEFAULT 1 COMMENT '1 is show 0 is not show',
    `is_deleted` int DEFAULT 0 COMMENT '1 is deleted 0 is not deleted',
    `index_order` int,
    `parent_id` int
);

create table if not exists `setting` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
    `username` varchar(255),
    `table_name` varchar(255),
    `config` varchar(255),
    `created_by` varchar(255),
    `updated_by` varchar(255),
    `created_date` datetime,
    `updated_date` datetime,
    `is_deleted` int DEFAULT 0 COMMENT '1 is deleted 0 is not deleted',
    `status` int DEFAULT 1
);
