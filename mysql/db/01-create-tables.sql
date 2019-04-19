create database if not exists `commenter`;
use `commenter`;

# drop
drop table if exists `auths`;
drop table if exists `tokens`;
drop table if exists `users`;

# create
create table if not exists `auths`
(
  `username`  char(255) unique not null,
  `password`  char(60) not null,
  primary key (`username`)
);

create table if not exists `tokens`
(
  `token`           char(255) unique not null,
  `auth_username`   char(255) not null,
  `created_at`      timestamp not null,
  primary key (`token`)
);


create table IF not exists `users`
(
 `id`               INT(20) AUTO_INCREMENT,
 `name`             VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`)
);
