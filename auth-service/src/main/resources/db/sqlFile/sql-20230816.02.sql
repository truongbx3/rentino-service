create table if not exists `revinfo` (
    `rev` int PRIMARY KEY AUTO_INCREMENT,
    `revtstmp` BIGINT not null  COMMENT ' 0 entity is created; 1 entity is updated; 2 entity is removed;'
    );
