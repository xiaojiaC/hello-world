CREATE DATABASE IF NOT EXISTS `demo`;
USE `demo`;

--
-- Table structure for table `dj_account`
--
CREATE TABLE `dj_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(15) NOT NULL COMMENT '用户名',
  `password` varchar(34) NOT NULL COMMENT '密码',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `dj_resume`
--
CREATE TABLE `dj_resume` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` int(11) NOT NULL COMMENT '账户ID',
  `gender` tinyint(1) NOT NULL COMMENT '0: 未知，1: 男，2: 女',
  `name` varchar(24) NOT NULL COMMENT '姓名',
  `home_address` varchar(255) DEFAULT NULL COMMENT '家庭住址',
  `email` varchar(36) DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;