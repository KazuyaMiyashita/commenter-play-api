create database if not exists `commenter`;
use `commenter`;

# drop
drop table if exists `auths`;
drop table if exists `tokens`;
drop table if exists `users`;
drop table if exists `comments`;
drop table if exists `follows`;


# create
create table if not exists `auths`
(
  `id`              char(64) unique not null,
  `email`           char(255) unique not null,
  `password`        char(60) not null,
  primary key (`id`),
  index (`email`)
);

create table if not exists `tokens`
(
  `token`           char(255) unique not null,
  `auth_id`         char(255) not null,
  `created_at`      timestamp not null,
  primary key (`token`),
  foreign key (`auth_id`) references `auths`(`id`)
);


create table if not exists `users`
(
  `id`               char(64) unique not null,
  `auth_id`          char(64) unique not null,
  `name`             VARCHAR(32) NOT NULL,
  primary key (`id`),
  foreign key (`auth_id`) references `auths`(`id`)
);

create table if not exists `comments`
(
  `id`              char(64) unique not null,
  `user_id`         char(64) not null,
  `comment`         char(255) not null,
  `created_at`      timestamp not null,
  primary key (`id`),
  foreign key (`user_id`) references `users`(`id`)
);

create table if not exists `follows`
(
  `follower`    char(64) not null,
  `followee`    char(64) not null,
  check (`follower` <> `followee`),
  index (`follower`),
  index (`followee`),
  primary key (`follower`, `followee`),
  foreign key (`follower`) references `users`(`id`),
  foreign key (`followee`) references `users`(`id`)
);
