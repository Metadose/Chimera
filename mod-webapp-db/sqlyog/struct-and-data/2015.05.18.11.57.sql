/*
SQLyog Community Edition- MySQL GUI v6.07
Host - 5.5.32 : Database - test
*********************************************************************
Server version : 5.5.32
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

create database if not exists `test`;

USE `test`;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

/*Table structure for table `assignments_project_field` */

DROP TABLE IF EXISTS `assignments_project_field`;

CREATE TABLE `assignments_project_field` (
  `project_id` bigint(20) NOT NULL,
  `field_id` bigint(20) NOT NULL,
  `label` varchar(32) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`project_id`,`field_id`,`label`,`value`),
  KEY `FK_field_id` (`field_id`),
  CONSTRAINT `FK_fields` FOREIGN KEY (`field_id`) REFERENCES `fields` (`field_id`),
  CONSTRAINT `FK_project_field` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_project_field` */

insert  into `assignments_project_field`(`project_id`,`field_id`,`label`,`value`) values (11,1,'BIR','1234');
insert  into `assignments_project_field`(`project_id`,`field_id`,`label`,`value`) values (11,1,'Building Permit','#232-1213-24');
insert  into `assignments_project_field`(`project_id`,`field_id`,`label`,`value`) values (11,1,'SSS','ASDQ-1223-2015');
insert  into `assignments_project_field`(`project_id`,`field_id`,`label`,`value`) values (19,1,'char nessss','111hehehe :)');
insert  into `assignments_project_field`(`project_id`,`field_id`,`label`,`value`) values (19,1,'Test','test');
insert  into `assignments_project_field`(`project_id`,`field_id`,`label`,`value`) values (54,1,'wrwer','svsfdsdf');
insert  into `assignments_project_field`(`project_id`,`field_id`,`label`,`value`) values (67,1,'vvvvvvvv','vvvvvvvvvv');

/*Table structure for table `assignments_project_manager` */

DROP TABLE IF EXISTS `assignments_project_manager`;

CREATE TABLE `assignments_project_manager` (
  `project_id` bigint(20) NOT NULL,
  `staff_id` bigint(20) NOT NULL,
  `project_position` varchar(32) NOT NULL,
  PRIMARY KEY (`project_id`,`staff_id`),
  KEY `FK_staff_id` (`staff_id`),
  CONSTRAINT `assignments_project_manager_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_staff_id` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_project_manager` */

insert  into `assignments_project_manager`(`project_id`,`staff_id`,`project_position`) values (21,27,'xcxzc3');
insert  into `assignments_project_manager`(`project_id`,`staff_id`,`project_position`) values (21,28,'v');
insert  into `assignments_project_manager`(`project_id`,`staff_id`,`project_position`) values (23,28,'enginer');
insert  into `assignments_project_manager`(`project_id`,`staff_id`,`project_position`) values (54,27,'aasd');
insert  into `assignments_project_manager`(`project_id`,`staff_id`,`project_position`) values (54,45,'vv');

/*Table structure for table `assignments_project_team` */

DROP TABLE IF EXISTS `assignments_project_team`;

CREATE TABLE `assignments_project_team` (
  `project_id` bigint(20) NOT NULL,
  `team_id` bigint(20) NOT NULL,
  PRIMARY KEY (`project_id`,`team_id`),
  KEY `FK_team_id` (`team_id`),
  CONSTRAINT `assignments_project_team_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_team_id` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_project_team` */

insert  into `assignments_project_team`(`project_id`,`team_id`) values (11,7);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (12,7);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (54,7);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (19,14);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (21,14);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (23,14);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (19,15);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (19,16);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (19,17);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (19,18);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (25,18);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (19,19);

/*Table structure for table `assignments_staff_field` */

DROP TABLE IF EXISTS `assignments_staff_field`;

CREATE TABLE `assignments_staff_field` (
  `staff_id` bigint(20) NOT NULL,
  `field_id` bigint(20) NOT NULL,
  `label` varchar(32) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`staff_id`,`field_id`,`label`,`value`),
  KEY `field_id` (`field_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_staff_field` */

/*Table structure for table `assignments_staff_team` */

DROP TABLE IF EXISTS `assignments_staff_team`;

CREATE TABLE `assignments_staff_team` (
  `staff_id` bigint(20) NOT NULL,
  `team_id` bigint(20) NOT NULL,
  PRIMARY KEY (`staff_id`,`team_id`),
  KEY `FK_team` (`team_id`),
  CONSTRAINT `FK_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `FK_team` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_staff_team` */

insert  into `assignments_staff_team`(`staff_id`,`team_id`) values (28,14);
insert  into `assignments_staff_team`(`staff_id`,`team_id`) values (60,14);
insert  into `assignments_staff_team`(`staff_id`,`team_id`) values (61,14);

/*Table structure for table `assignments_task_field` */

DROP TABLE IF EXISTS `assignments_task_field`;

CREATE TABLE `assignments_task_field` (
  `task_id` bigint(20) NOT NULL,
  `field_id` bigint(20) NOT NULL,
  `label` varchar(32) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`task_id`,`field_id`,`label`,`value`),
  KEY `field_id` (`field_id`),
  CONSTRAINT `FK_assignments_task_field` FOREIGN KEY (`field_id`) REFERENCES `fields` (`field_id`),
  CONSTRAINT `FK_tasks` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_task_field` */

/*Table structure for table `assignments_task_staff` */

DROP TABLE IF EXISTS `assignments_task_staff`;

CREATE TABLE `assignments_task_staff` (
  `task_id` bigint(20) NOT NULL,
  `staff_id` bigint(20) NOT NULL,
  PRIMARY KEY (`task_id`,`staff_id`),
  KEY `FK_assignments_task_staff` (`staff_id`),
  CONSTRAINT `FK_assignments_task_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `FK_tasks_task` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_task_staff` */

insert  into `assignments_task_staff`(`task_id`,`staff_id`) values (26,27);
insert  into `assignments_task_staff`(`task_id`,`staff_id`) values (41,27);
insert  into `assignments_task_staff`(`task_id`,`staff_id`) values (34,28);
insert  into `assignments_task_staff`(`task_id`,`staff_id`) values (35,28);
insert  into `assignments_task_staff`(`task_id`,`staff_id`) values (41,28);
insert  into `assignments_task_staff`(`task_id`,`staff_id`) values (42,28);
insert  into `assignments_task_staff`(`task_id`,`staff_id`) values (73,28);
insert  into `assignments_task_staff`(`task_id`,`staff_id`) values (70,60);
insert  into `assignments_task_staff`(`task_id`,`staff_id`) values (70,62);

/*Table structure for table `assignments_task_team` */

DROP TABLE IF EXISTS `assignments_task_team`;

CREATE TABLE `assignments_task_team` (
  `task_id` bigint(20) NOT NULL,
  `team_id` bigint(20) NOT NULL,
  PRIMARY KEY (`task_id`,`team_id`),
  KEY `FK_task_team` (`team_id`),
  CONSTRAINT `FK_task` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`),
  CONSTRAINT `FK_task_team` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_task_team` */

insert  into `assignments_task_team`(`task_id`,`team_id`) values (26,7);
insert  into `assignments_task_team`(`task_id`,`team_id`) values (41,14);
insert  into `assignments_task_team`(`task_id`,`team_id`) values (34,15);
insert  into `assignments_task_team`(`task_id`,`team_id`) values (35,15);
insert  into `assignments_task_team`(`task_id`,`team_id`) values (40,15);

/*Table structure for table `assignments_user_access` */

DROP TABLE IF EXISTS `assignments_user_access`;

CREATE TABLE `assignments_user_access` (
  `securityaccess_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`securityaccess_id`,`user_id`),
  KEY `FK_assignments_access_user` (`user_id`),
  CONSTRAINT `FK_assignments_access` FOREIGN KEY (`securityaccess_id`) REFERENCES `security_access` (`securityaccess_id`),
  CONSTRAINT `FK_assignments_access_user` FOREIGN KEY (`user_id`) REFERENCES `system_users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_user_access` */

insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (1,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (2,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (3,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (4,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (5,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (6,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (7,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (8,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (9,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (10,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (11,1);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (2,8);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (3,8);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (4,8);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (5,8);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (6,8);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (7,8);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (8,8);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (9,8);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (10,8);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (2,9);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (3,9);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (4,9);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (5,9);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (6,9);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (7,9);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (8,9);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (9,9);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (10,9);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (2,11);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (3,11);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (4,11);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (5,11);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (6,11);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (7,11);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (8,11);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (9,11);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (2,12);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (3,12);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (4,12);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (5,12);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (6,12);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (7,12);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (8,12);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (9,12);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (1,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (2,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (3,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (4,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (5,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (6,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (7,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (8,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (9,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (10,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (11,21);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (3,24);
insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (8,24);

/*Table structure for table `assignments_user_role` */

DROP TABLE IF EXISTS `assignments_user_role`;

CREATE TABLE `assignments_user_role` (
  `securityrole_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`securityrole_id`,`user_id`),
  KEY `FK_assignments_user` (`user_id`),
  CONSTRAINT `FK_assignments_user` FOREIGN KEY (`user_id`) REFERENCES `system_users` (`user_id`),
  CONSTRAINT `FK_assignments_user_role` FOREIGN KEY (`securityrole_id`) REFERENCES `security_roles` (`securityrole_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_user_role` */

insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (1,1);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (2,1);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (3,1);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (4,1);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (5,1);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (6,1);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (7,1);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (8,1);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (9,1);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (10,1);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (2,8);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (3,8);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (4,8);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (5,8);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (6,8);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (7,8);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (8,8);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (9,8);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (2,9);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (3,9);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (4,9);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (5,9);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (6,9);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (7,9);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (8,9);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (9,9);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (2,11);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (3,11);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (4,11);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (5,11);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (6,11);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (7,11);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (9,11);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (1,21);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (2,21);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (3,21);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (4,21);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (5,21);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (6,21);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (7,21);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (8,21);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (9,21);
insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (10,21);

/*Table structure for table `audit_logs` */

DROP TABLE IF EXISTS `audit_logs`;

CREATE TABLE `audit_logs` (
  `auditlog_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_executed` datetime NOT NULL,
  `ip_address` varchar(15) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `action` int(4) NOT NULL,
  `object_name` varchar(64) NOT NULL,
  `object_id` bigint(20) NOT NULL,
  PRIMARY KEY (`auditlog_id`),
  KEY `FK_audit_logs_users` (`user_id`),
  CONSTRAINT `FK_audit_logs_users` FOREIGN KEY (`user_id`) REFERENCES `system_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=latin1;

/*Data for the table `audit_logs` */

insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (1,'2015-04-06 11:58:18','127.0.0.1',8,1,1,'project',23);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (2,'2015-04-06 13:34:49','127.0.0.1',8,1,2,'project',23);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (3,'2015-04-06 14:44:11','127.0.0.1',1,1,2,'project',23);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (4,'2015-04-06 14:44:46','127.0.0.1',1,1,2,'project',23);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (5,'2015-04-09 11:36:51','127.0.0.1',9,2,1,'project',24);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (6,'2015-04-09 11:45:16','127.0.0.1',9,2,1,'project',25);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (12,'2015-04-10 15:07:41','127.0.0.1',1,NULL,1,'project',32);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (13,'2015-04-10 15:20:14','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (14,'2015-04-10 15:25:10','127.0.0.1',1,NULL,1,'project',34);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (15,'2015-04-10 16:06:49','127.0.0.1',1,NULL,1,'project',35);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (16,'2015-04-10 16:10:10','127.0.0.1',1,NULL,1,'project',36);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (17,'2015-04-13 14:16:48','127.0.0.1',1,NULL,1,'project',51);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (18,'2015-04-13 14:21:32','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (19,'2015-04-13 14:24:06','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (20,'2015-04-13 14:31:55','127.0.0.1',1,NULL,1,'project',54);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (21,'2015-04-13 14:56:07','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (22,'2015-04-13 14:56:35','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (23,'2015-04-13 15:00:19','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (24,'2015-04-13 15:40:02','127.0.0.1',1,NULL,1,'project',32);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (25,'2015-04-13 15:58:24','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (26,'2015-04-13 16:01:14','127.0.0.1',1,NULL,1,'project',32);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (27,'2015-04-13 16:17:00','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (28,'2015-04-13 16:23:58','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (29,'2015-04-13 16:48:56','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (30,'2015-04-13 16:49:25','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (31,'2015-04-13 16:50:27','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (32,'2015-04-13 16:51:47','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (33,'2015-04-13 16:55:45','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (34,'2015-04-13 16:59:53','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (35,'2015-04-14 08:14:30','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (36,'2015-04-14 08:35:42','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (37,'2015-04-14 08:36:45','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (38,'2015-04-14 08:40:05','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (39,'2015-04-14 09:58:54','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (40,'2015-04-14 10:05:49','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (41,'2015-04-14 10:15:09','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (42,'2015-04-14 10:36:04','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (43,'2015-04-14 10:40:38','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (44,'2015-04-14 14:18:07','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (45,'2015-04-15 09:25:32','127.0.0.1',1,NULL,1,'project',11);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (46,'2015-04-15 09:25:51','127.0.0.1',1,NULL,1,'project',33);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (47,'2015-04-15 09:31:10','127.0.0.1',1,NULL,1,'project',34);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (48,'2015-04-15 09:39:53','127.0.0.1',1,NULL,1,'project',35);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (49,'2015-04-15 09:40:20','127.0.0.1',1,NULL,1,'project',35);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (50,'2015-04-15 09:42:03','127.0.0.1',1,NULL,1,'project',35);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (51,'2015-04-15 09:45:23','127.0.0.1',1,NULL,1,'project',35);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (52,'2015-04-15 09:46:19','127.0.0.1',1,NULL,1,'project',36);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (53,'2015-04-15 09:50:07','127.0.0.1',1,NULL,1,'project',38);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (54,'2015-04-15 10:03:05','127.0.0.1',1,NULL,1,'project',39);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (55,'2015-04-15 10:10:26','127.0.0.1',1,NULL,1,'project',40);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (56,'2015-04-15 10:51:01','127.0.0.1',1,NULL,1,'project',41);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (57,'2015-04-15 10:52:50','127.0.0.1',1,NULL,1,'project',42);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (58,'2015-04-15 10:53:04','127.0.0.1',1,NULL,1,'project',43);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (59,'2015-04-15 10:53:11','127.0.0.1',1,NULL,1,'project',44);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (60,'2015-04-15 11:00:39','127.0.0.1',1,NULL,1,'project',45);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (61,'2015-04-15 11:00:42','127.0.0.1',1,NULL,1,'project',46);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (62,'2015-04-15 11:20:44','127.0.0.1',1,NULL,1,'project',47);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (63,'2015-04-15 11:35:00','127.0.0.1',1,NULL,1,'project',50);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (64,'2015-04-15 11:35:08','127.0.0.1',1,NULL,1,'project',32);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (65,'2015-04-15 11:38:13','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (66,'2015-04-15 11:38:32','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (67,'2015-04-15 11:39:31','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (68,'2015-04-15 11:41:45','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (69,'2015-04-15 11:51:13','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (70,'2015-04-15 11:51:45','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (71,'2015-04-15 11:53:42','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (72,'2015-04-15 11:54:41','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (73,'2015-04-15 13:12:40','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (74,'2015-04-15 13:13:34','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (75,'2015-04-15 13:13:43','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (76,'2015-04-15 13:14:25','127.0.0.1',1,NULL,1,'project',52);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (77,'2015-04-15 13:25:52','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (78,'2015-04-15 13:42:14','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (79,'2015-04-15 14:33:23','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (80,'2015-04-15 14:37:31','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (81,'2015-04-15 14:57:18','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (82,'2015-04-15 14:58:32','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (83,'2015-04-15 14:59:43','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (84,'2015-04-15 15:03:01','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (85,'2015-04-15 15:15:23','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (86,'2015-04-15 15:18:53','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (87,'2015-04-15 15:20:23','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (88,'2015-04-15 15:32:27','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (89,'2015-04-15 15:33:54','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (90,'2015-04-15 15:35:57','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (91,'2015-04-15 15:36:39','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (92,'2015-04-15 15:37:28','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (93,'2015-04-15 15:38:11','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (94,'2015-04-15 15:40:26','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (95,'2015-04-15 15:40:35','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (96,'2015-04-15 16:26:13','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (97,'2015-04-15 16:26:23','127.0.0.1',1,NULL,1,'project',53);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (98,'2015-04-15 16:50:33','127.0.0.1',1,NULL,1,'project',54);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (99,'2015-04-16 10:51:38','127.0.0.1',1,NULL,1,'project',54);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (100,'2015-04-16 10:52:00','127.0.0.1',1,NULL,1,'project',54);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (101,'2015-04-16 10:52:15','127.0.0.1',1,NULL,1,'project',54);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (102,'2015-04-16 10:52:42','127.0.0.1',1,NULL,1,'project',54);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (103,'2015-04-16 10:53:07','127.0.0.1',1,NULL,1,'project',54);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (104,'2015-04-16 10:53:31','127.0.0.1',1,NULL,1,'project',54);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (105,'2015-04-16 16:40:47','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (106,'2015-04-20 17:49:03','127.0.0.1',1,NULL,1,'project',51);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (107,'2015-04-20 17:50:00','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (108,'2015-04-20 17:54:27','127.0.0.1',1,NULL,1,'project',67);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (109,'2015-04-20 17:54:48','127.0.0.1',1,NULL,1,'project',67);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (110,'2015-04-20 17:55:20','127.0.0.1',1,NULL,1,'project',67);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (111,'2015-04-20 17:55:32','127.0.0.1',1,NULL,1,'project',67);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (112,'2015-04-20 17:55:59','127.0.0.1',1,NULL,1,'project',67);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (113,'2015-04-21 13:51:13','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (114,'2015-04-21 13:58:54','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (115,'2015-04-21 14:03:56','127.0.0.1',1,NULL,1,'project',55);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (116,'2015-04-21 14:04:14','127.0.0.1',1,NULL,1,'project',55);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (117,'2015-04-21 14:04:47','127.0.0.1',1,NULL,1,'project',55);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (118,'2015-04-21 14:13:22','127.0.0.1',1,NULL,1,'project',55);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (119,'2015-04-21 14:13:35','127.0.0.1',1,NULL,1,'project',55);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (120,'2015-04-21 14:13:48','127.0.0.1',1,NULL,1,'project',56);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (121,'2015-04-21 15:58:58','127.0.0.1',1,NULL,1,'project',57);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (122,'2015-04-24 15:55:22','127.0.0.1',1,NULL,1,'project',57);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (123,'2015-04-28 11:14:14','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (124,'2015-04-28 11:19:35','127.0.0.1',1,NULL,1,'project',58);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (125,'2015-04-28 11:20:38','127.0.0.1',1,NULL,1,'project',0);
insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (126,'2015-04-28 11:20:58','127.0.0.1',1,NULL,1,'project',58);

/*Table structure for table `companies` */

DROP TABLE IF EXISTS `companies`;

CREATE TABLE `companies` (
  `company_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `date_started` datetime NOT NULL,
  `date_expiration` datetime NOT NULL,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `companies` */

insert  into `companies`(`company_id`,`name`,`description`,`date_started`,`date_expiration`) values (1,'Com1','Com1','2014-12-12 12:12:12','2015-11-11 11:11:11');
insert  into `companies`(`company_id`,`name`,`description`,`date_started`,`date_expiration`) values (2,'Com2','Com2','2014-12-12 12:12:12','2016-12-12 12:12:12');
insert  into `companies`(`company_id`,`name`,`description`,`date_started`,`date_expiration`) values (3,'qqssas','aaqe','2014-12-12 00:00:00','2015-12-12 00:00:00');

/*Table structure for table `fields` */

DROP TABLE IF EXISTS `fields`;

CREATE TABLE `fields` (
  `field_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`field_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `fields` */

insert  into `fields`(`field_id`,`name`) values (1,'Textfield');

/*Table structure for table `photos` */

DROP TABLE IF EXISTS `photos`;

CREATE TABLE `photos` (
  `photo_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `size` bigint(20) NOT NULL,
  `location` varchar(255) NOT NULL,
  `staff_id` bigint(20) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `date_uploaded` datetime NOT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`photo_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=latin1;

/*Data for the table `photos` */

insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (2,'TEST7B.bmp','Pixels free vectors',1228856,'C:/Program Files/VCC/PMSYS//project/11/photos/TEST7B.bmp',1,11,'2015-02-20 17:21:09',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (3,'phaser.png','Phaser',180337,'C:/Program Files/VCC/PMSYS//project/11/photos/phaser.png',1,11,'2015-02-20 17:21:51',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (10,'CLIFSHD.bmp','123123123123213',1228856,'C:/Program Files/VCC/PMSYS//company/0/project/13/photo/CLIFSHD.bmp',NULL,13,'2015-03-10 14:28:58',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (11,'VILINT.bmp','123123123',1228856,'C:/Program Files/VCC/PMSYS//company/0/project/13/photo/VILINT.bmp',NULL,13,'2015-03-10 14:29:58',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (12,'VILSHD.bmp','213123123213',1228856,'C:/Program Files/VCC/PMSYS//company/0/project/13/photo/VILSHD.bmp',NULL,13,'2015-03-10 14:30:05',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (13,'VILFLR.bmp','1231231 2321 213',1228856,'C:/Program Files/VCC/PMSYS//company/1/project/14/photo/VILFLR.bmp',1,14,'2015-03-10 14:31:04',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (14,'VILFLR.bmp','123123dascasc',1228856,'C:/Program Files/VCC/PMSYS//company/1/project/14/photo/VILFLR.bmp',1,14,'2015-03-10 14:35:37',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (15,'CLIFVEG2.bmp','1231ewd ac',1228856,'C:/Program Files/VCC/PMSYS//company/1/project/14/photo/CLIFVEG2.bmp',1,14,'2015-03-10 14:35:44',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (16,'BENCH.bmp','1231 dac1231',1228856,'C:/Program Files/VCC/PMSYS//company/1/project/14/photo/BENCH.bmp',1,14,'2015-03-10 14:36:16',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (17,'VILFLR.bmp','213 ',1228856,'C:/Program Files/VCC/PMSYS//company/1/project/15/photo/VILFLR.bmp',1,15,'2015-03-10 14:36:58',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (18,'_BACKUP_.bmp','backup!',1228856,'C:/Program Files/VCC/PMSYS//company/2/project/16/photo/_BACKUP_.bmp',12,16,'2015-03-10 14:37:26',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (19,'BENCH.GIF','bench',17016,'C:/Program Files/VCC/PMSYS//company/2/project/16/photo/BENCH.GIF',12,16,'2015-03-10 14:37:32',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (20,'VILDAM.bmp','vildam',1228856,'C:/Program Files/VCC/PMSYS//company/2/project/17/photo/VILDAM.bmp',12,17,'2015-03-10 14:37:52',NULL);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (29,'blur-background04.jpg','',402286,'C:/Program Files/VCC/PMSYS//company/1/project/21/photo/blur-background04.jpg',32,21,'2015-03-24 17:40:03',1);
insert  into `photos`(`photo_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (37,'phaser.png','aaaa',180337,'C:/Program Files/VCC/PMSYS//company/2/project/25/photo/phaser.png',NULL,25,'2015-04-24 15:58:49',2);

/*Table structure for table `project_files` */

DROP TABLE IF EXISTS `project_files`;

CREATE TABLE `project_files` (
  `projectfile_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `size` bigint(20) NOT NULL,
  `location` varchar(255) NOT NULL,
  `staff_id` bigint(20) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `date_uploaded` datetime NOT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`projectfile_id`),
  KEY `FK_project_files` (`project_id`),
  KEY `FK_project_files_company` (`company_id`),
  KEY `FK_staff_files` (`staff_id`),
  CONSTRAINT `FK_project_files` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_project_files_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_staff_files` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

/*Data for the table `project_files` */

insert  into `project_files`(`projectfile_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (7,'.tm_properties','qweqweqwe',165,'C:/Program Files/VCC/PMSYS//company/0/systemuser/1/files/.tm_properties',NULL,NULL,'2015-03-10 11:14:28',NULL);
insert  into `project_files`(`projectfile_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (8,'CHANGELOG.md','123sad',2241,'C:/Program Files/VCC/PMSYS//company/0/systemuser/1/files/CHANGELOG.md',NULL,NULL,'2015-03-10 11:14:53',NULL);
insert  into `project_files`(`projectfile_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (16,'blur-background04.jpg','232323',402286,'C:/Program Files/VCC/PMSYS//company/1/project/21/projectfile/blur-background04.jpg',NULL,21,'2015-03-24 17:39:20',1);
insert  into `project_files`(`projectfile_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`,`company_id`) values (25,'CALP 60.csv','1123123 aca a as as as as e12dacv qef as',966,'C:/Program Files/VCC/PMSYS//company/2/project/25/projectfile/CALP 60.csv',NULL,25,'2015-04-24 15:22:10',NULL);

/*Table structure for table `projects` */

DROP TABLE IF EXISTS `projects`;

CREATE TABLE `projects` (
  `project_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `thumbnail_url` varchar(255) DEFAULT NULL,
  `name` varchar(32) NOT NULL,
  `type` int(2) NOT NULL,
  `status` int(2) NOT NULL,
  `location` varchar(108) DEFAULT NULL,
  `notes` text,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`project_id`),
  KEY `FK_projects_company` (`company_id`),
  CONSTRAINT `FK_projects_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=latin1;

/*Data for the table `projects` */

insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (11,'C:/Program Files/VCC/PMSYS//project/11/profile/photo/mastercard.png','testing',0,3,'Rizal St., Central Brgy., Dipolog City, Zamboanga del Norte, Philippines','Another test project.',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (12,NULL,'Test',0,0,'123','123',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (13,'C:/Program Files/VCC/PMSYS//company/0/project/13/profile/photo/CLIFFVEG.bmp','Root\'s Project',0,0,'Root','Root',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (19,NULL,'Com1\'s Project din pag may time',0,4,'Rizal St., Central Brgy., Dipolog City, Zamboanga del Norte, USA','uuuuu',1);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (21,NULL,'Test5 55555',0,3,'wow','Ccc',1);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (22,NULL,'test',0,0,'test','Another test project. 123',1);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (23,NULL,'testqweqwe',0,4,'Rizal St., Central Brgy., Dipolog City, Zamboanga del Norte, Philippines','\'\'\'\'\"\"\"\"\'\'\'\"\"',1);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (25,NULL,'www',0,0,'','',2);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (54,NULL,'\"/>',0,3,'123123ccc','123123',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (58,NULL,'xxxxx2',0,3,'Rrrxxx','xxx',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (59,NULL,'asdasda',0,0,'','',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (60,NULL,'asdasdasd',0,0,'asdasda','dddddddddd',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (61,NULL,'vvv',0,0,'','',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (62,NULL,'aasd',0,0,'','dddddddddd',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (63,NULL,'Aaaaaaaaaaaaaaaaaa',0,0,'','',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (64,NULL,'qqqqqqq',0,0,'','',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (65,NULL,'asdasd',0,0,'sss','',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (66,NULL,'qeqwea',0,0,'asdasdasdqq','12asd',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (67,NULL,'karon 2',0,2,'2323','232',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (68,NULL,'224234',0,2,'ssdfsdf','33434',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (69,NULL,'234234q',0,4,'234234','234234',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (70,NULL,'113212312',0,3,'13123123','22323',NULL);
insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (71,NULL,'xxxxxxxxx',0,0,'xxxxxxx','xxxxxxxxx',NULL);

/*Table structure for table `security_access` */

DROP TABLE IF EXISTS `security_access`;

CREATE TABLE `security_access` (
  `securityaccess_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `label` varchar(32) NOT NULL,
  PRIMARY KEY (`securityaccess_id`,`name`),
  UNIQUE KEY `securitygroup_id` (`securityaccess_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

/*Data for the table `security_access` */

insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (1,'ACCESS_COMPANY','Company');
insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (2,'ACCESS_FIELD','Field');
insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (3,'ACCESS_PHOTO','Photo');
insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (4,'ACCESS_PROJECT','Project');
insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (5,'ACCESS_PROJECTFILE','File');
insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (6,'ACCESS_STAFF','Staff');
insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (7,'ACCESS_TASK','Task');
insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (8,'ACCESS_TEAM','Team');
insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (9,'ACCESS_SYSTEMUSER','User');
insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (10,'ACCESS_CONFIG','Configuration');
insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (11,'ACCESS_LOG','Log');

/*Table structure for table `security_roles` */

DROP TABLE IF EXISTS `security_roles`;

CREATE TABLE `security_roles` (
  `securityrole_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `label` varchar(32) NOT NULL,
  PRIMARY KEY (`securityrole_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

/*Data for the table `security_roles` */

insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (1,'ROLE_FIELD_EDITOR','Field Editor');
insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (2,'ROLE_TASK_EDITOR','Task Editor');
insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (3,'ROLE_PROJECT_EDITOR','Project Editor');
insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (4,'ROLE_STAFF_EDITOR','Staff Editor');
insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (5,'ROLE_PHOTO_EDITOR','Photo Editor');
insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (6,'ROLE_PROJECTFILE_EDITOR','File Editor');
insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (7,'ROLE_TEAM_EDITOR','Team Editor');
insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (8,'ROLE_CONFIG_EDITOR','Configuration Editor');
insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (9,'ROLE_SYSTEMUSER_EDITOR','User Editor');
insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (10,'ROLE_LOG_EDITOR','Log Editor');

/*Table structure for table `staff` */

DROP TABLE IF EXISTS `staff`;

CREATE TABLE `staff` (
  `staff_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `thumbnail_url` varchar(255) DEFAULT NULL,
  `name_prefix` varchar(8) DEFAULT NULL,
  `name_first` varchar(32) DEFAULT NULL,
  `name_middle` varchar(16) DEFAULT NULL,
  `name_last` varchar(16) DEFAULT NULL,
  `name_suffix` varchar(8) DEFAULT NULL,
  `position` varchar(16) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `wage` double NOT NULL DEFAULT '0',
  `contact_number` varchar(32) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`staff_id`),
  KEY `FK_staff_company` (`company_id`),
  CONSTRAINT `FK_staff_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=latin1;

/*Data for the table `staff` */

insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (27,NULL,'Engr','What ever','What ever','What ever','Sss','xx','test 2',300,'2',NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (28,'C:/Program Files/VCC/PMSYS//company/1/staff/28/profile/photo/1f8c010.jpg','Engr','Com1 User','Com1 User','Com1 User','Test','Com1 User','Com1 User',300,'1234',1);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (29,NULL,'Engr','Com2','Com2','Com2','Com2','Com2','Com2',300,'1234',2);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (44,NULL,'1123','121','1212','2121','121','112','2',300,'22',NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (45,NULL,'5','5','5','5','5','55','5',300,'5',NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (46,NULL,'e','e','e','e','e','e','e',300,'e',NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (47,NULL,'rt','rt','rt','rt','rt','rt','rt',300,'rt',NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (48,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,300,NULL,NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (49,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,300,NULL,NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (50,'C:/Program Files/VCC/PMSYS//company/0/staff/50/profile/photo/phaser.png','asd22','asdas','asd','asd','asd','asdas','asda',300,'asda',NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (51,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,300,NULL,NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (52,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,300,NULL,NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (53,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,300,NULL,NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (54,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,300,NULL,NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (55,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,300,NULL,2);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (56,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,300,NULL,2);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (57,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,300,NULL,2);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (59,NULL,'1','1','2','22','2','22','2122',300,'2zz',NULL);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (60,NULL,'1','standard','staff','staff','asd','asd','asd',300,'asdss',1);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (61,NULL,'na','norm','asd','a2','d','ad','asda',300,'asd',1);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (62,NULL,'','2','2','2','2','2','2',300,'22',1);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (63,NULL,'5','5','5','5','5','5','5',300,'5',1);
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`wage`,`contact_number`,`company_id`) values (64,NULL,'77','77','77','77','77','77','77',300,'77',1);

/*Table structure for table `system_configuration` */

DROP TABLE IF EXISTS `system_configuration`;

CREATE TABLE `system_configuration` (
  `sysconf_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sysconf_id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK_system_configuration_company` (`company_id`),
  CONSTRAINT `FK_system_configuration_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `system_configuration` */

insert  into `system_configuration`(`sysconf_id`,`name`,`value`,`company_id`) values (1,'SYS_HOME','C:/Program Files/VCC/PMSYS/',NULL);
insert  into `system_configuration`(`sysconf_id`,`name`,`value`,`company_id`) values (2,'ROOT_INIT','1',NULL);

/*Table structure for table `system_users` */

DROP TABLE IF EXISTS `system_users`;

CREATE TABLE `system_users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(255) NOT NULL,
  `super_admin` tinyint(1) NOT NULL DEFAULT '0',
  `company_id` bigint(20) DEFAULT NULL,
  `staff_id` bigint(20) DEFAULT NULL,
  `company_admin` tinyint(1) NOT NULL DEFAULT '0',
  `login_attempts` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  KEY `FK_system_users_company` (`company_id`),
  KEY `FK_system_users_staff` (`staff_id`),
  CONSTRAINT `FK_system_users_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_system_users_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;

/*Data for the table `system_users` */

insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (1,'root','a07a745b60b1de0788f1b3b982bc8ed5',1,NULL,NULL,0,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (8,'company_admin','30f870b2ed1ef7d1929fd31eb3adddbe',0,1,28,1,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (9,'company_admin2','fbcaaa4f06eb5bced3ae4bcbed8df5b5',0,2,59,1,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (10,'normalUser2','b008ee2b12850310e5cdb02e8f392c05',0,1,61,0,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (11,'gmanager','fe952de159007ef4d68e78a81bed8067',0,1,27,0,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (12,'standardstaff','b8d7e7241c61952429955e5f62246b34',0,1,60,0,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (21,'root2','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (22,'1111','0716dd41bccb40adcefbf87ef9beaacb',1,NULL,48,0,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (23,'222','d381b503ee81a09ed23b7166c4aa839f',1,NULL,49,1,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (24,'4rfv5tgb6yhn','9b111dd7a7b3e6ff8837c06d2ba84a51',0,1,50,1,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (25,'2323','e76b35c40334874afaadad17da81b890',1,NULL,51,1,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (26,'555','c6b40b10e670237817b54789d7a18b4d',1,NULL,52,1,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (27,'qwe','b8cb28d257b36011bf93a22751385231',1,NULL,53,1,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (28,'23','1848b5ac7bbdb409550a4105ac7a5ae5',1,NULL,54,1,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (29,'tt','d284e6f25f469cbbe58f67c19b5033e3',1,2,55,1,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (30,'1234','c8f5041ae0d08f9910eadbeca5f76880',1,2,56,1,0);
insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (31,'551','15700ac5ec39aa4cd488c91dd9dd01a1',1,2,57,1,0);

/*Table structure for table `tasks` */

DROP TABLE IF EXISTS `tasks`;

CREATE TABLE `tasks` (
  `task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `content` varchar(255) NOT NULL,
  `date_start` date NOT NULL,
  `duration` int(8) NOT NULL DEFAULT '0',
  `project_id` bigint(20) DEFAULT NULL,
  `status` int(2) NOT NULL DEFAULT '0',
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  KEY `FK_tasks_project` (`project_id`),
  KEY `FK_tasks_company` (`company_id`),
  CONSTRAINT `FK_tasks_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_tasks_project` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=latin1;

/*Data for the table `tasks` */

insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (26,'root2','Root Task2','2015-01-30',23,NULL,2,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (27,'root2','Root Task 2','2015-01-12',3,NULL,1,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (34,'title me','123123','2015-01-13',4,NULL,1,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (35,'titlqeqw','222222222222','2015-01-14',5,NULL,3,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (39,'34','34','2015-01-15',6,NULL,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (40,'tudss','123123123','2015-01-16',7,NULL,2,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (41,'qwwe','123123','2015-01-17',8,NULL,2,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (42,'title','23123','2015-01-12',12,NULL,3,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (49,'111','11','2011-11-11',11,NULL,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (50,'111','111','2011-11-11',11,NULL,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (51,'111','222','2012-02-22',15,NULL,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (52,'111','11','2011-11-11',11,NULL,2,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (54,'33','33','2011-11-11',222,NULL,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (55,'44','4444','2011-11-11',122,21,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (56,'11','111','2011-11-11',22,NULL,2,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (57,'12112','1111111','2011-11-11',22,19,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (58,'13123','123123','2011-11-22',22,19,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (59,'3434','5555','2011-11-20',2,19,2,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (60,'2323','333','2011-11-19',30,19,1,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (61,'Excavation','Excavation of the building','2015-04-20',20,NULL,1,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (62,'2323','2323','2014-04-12',12,NULL,1,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (63,'223','111','2014-04-13',22,NULL,1,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (64,'3434','3434','2022-02-22',22,NULL,2,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (65,'2323','23232','2022-02-22',22,NULL,2,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (66,'2233','333','2022-02-22',22,NULL,0,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (67,'1123','12312','2022-02-22',22,NULL,0,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (68,'223','232','2022-02-22',22,NULL,0,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (69,'11','22','2011-11-11',24,NULL,1,NULL);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (70,'Excavation','Excavation of the building','2015-05-08',10,23,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (71,'Flooring','Flooring hehehehe','2015-05-07',30,23,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (72,'1213','123','2015-05-01',1,NULL,0,1);
insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`) values (73,'for me','for me','2015-05-05',30,23,3,1);

/*Table structure for table `teams` */

DROP TABLE IF EXISTS `teams`;

CREATE TABLE `teams` (
  `team_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`team_id`),
  KEY `FK_teams_company` (`company_id`),
  CONSTRAINT `FK_teams_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

/*Data for the table `teams` */

insert  into `teams`(`team_id`,`name`,`company_id`) values (7,'Love',NULL);
insert  into `teams`(`team_id`,`name`,`company_id`) values (8,'Dope Team',NULL);
insert  into `teams`(`team_id`,`name`,`company_id`) values (9,'Root Team',NULL);
insert  into `teams`(`team_id`,`name`,`company_id`) values (14,'Ass',1);
insert  into `teams`(`team_id`,`name`,`company_id`) values (15,'Com1 Test Team 2',1);
insert  into `teams`(`team_id`,`name`,`company_id`) values (16,'vvv',1);
insert  into `teams`(`team_id`,`name`,`company_id`) values (17,'666',1);
insert  into `teams`(`team_id`,`name`,`company_id`) values (18,'xxxxx',1);
insert  into `teams`(`team_id`,`name`,`company_id`) values (19,'Bbb',1);
insert  into `teams`(`team_id`,`name`,`company_id`) values (27,'11111',NULL);
insert  into `teams`(`team_id`,`name`,`company_id`) values (28,'232323',NULL);
insert  into `teams`(`team_id`,`name`,`company_id`) values (29,'neyo',1);

/* Procedure structure for procedure `test_multi_sets` */

/*!50003 DROP PROCEDURE IF EXISTS  `test_multi_sets` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `test_multi_sets`()
    DETERMINISTIC
begin
 select user() as first_col;
 select user() as first_col, now() as second_col;
 select user() as first_col, now() as second_col, now() as third_col;
 end */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
