create table if not exists `app_config_history` (
    `id` int PRIMARY KEY AUTO_INCREMENT,
     `rev` integer NOT NULL,
     `revtype` tinyint,
    `username` varchar(255),
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
