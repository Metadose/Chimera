/*
SQLyog Community Edition- MySQL GUI v6.07
Host - 5.6.26-log : Database - test
*********************************************************************
Server version : 5.6.26-log
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

insert  into `assignments_project_field`(`project_id`,`field_id`,`label`,`value`) values (32,1,'qweqweqwe2222vvvqq','qweqweqwewqeqwe');

/*Table structure for table `assignments_project_staff` */

DROP TABLE IF EXISTS `assignments_project_staff`;

CREATE TABLE `assignments_project_staff` (
  `project_id` bigint(20) NOT NULL,
  `staff_id` bigint(20) NOT NULL,
  PRIMARY KEY (`project_id`,`staff_id`),
  KEY `FK_assignments_project_staff_stf` (`staff_id`),
  CONSTRAINT `FK_assignments_project_staff` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_assignments_project_staff_stf` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_project_staff` */

insert  into `assignments_project_staff`(`project_id`,`staff_id`) values (32,74),(32,75),(32,77),(32,78),(32,79);

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

/*Table structure for table `audit_logs` */

DROP TABLE IF EXISTS `audit_logs`;

CREATE TABLE `audit_logs` (
  `auditlog_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_executed` datetime NOT NULL,
  `ip_address` varchar(15) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `action` int(4) NOT NULL,
  `object_name` varchar(64) DEFAULT NULL,
  `object_id` bigint(20) DEFAULT NULL,
  `object_key` varchar(255) DEFAULT NULL,
  `assoc_object_name` varchar(64) DEFAULT NULL,
  `assoc_object_id` bigint(20) DEFAULT NULL,
  `assoc_object_key` varchar(255) DEFAULT NULL,
  `entry_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`auditlog_id`),
  KEY `FK_audit_logs_users` (`user_id`),
  KEY `FK_audit_logs_project_id` (`project_id`),
  KEY `FK_audit_logs_company_id` (`company_id`),
  CONSTRAINT `FK_audit_logs_company_id` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_audit_logs_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_audit_logs_users` FOREIGN KEY (`user_id`) REFERENCES `system_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Data for the table `audit_logs` */

insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`project_id`,`action`,`object_name`,`object_id`,`object_key`,`assoc_object_name`,`assoc_object_id`,`assoc_object_key`,`entry_name`) values (1,'2015-09-30 11:49:04','127.0.0.1',228,10,32,120,'project',32,NULL,'staff',NULL,'All','All'),(2,'2015-09-30 11:49:17','127.0.0.1',228,10,32,116,'project',32,NULL,'staff',75,NULL,' Smith Johnson Williams '),(3,'2015-09-30 11:49:17','127.0.0.1',228,10,32,116,'project',32,NULL,'staff',76,NULL,' Williams Smith Johnson '),(4,'2015-09-30 11:49:17','127.0.0.1',228,10,32,116,'project',32,NULL,'staff',77,NULL,' Johnson Smith Williams '),(5,'2015-09-30 11:49:31','127.0.0.1',228,10,32,101,'project',32,NULL,'staff',NULL,'Mass','Mass'),(6,'2015-09-30 11:49:31','127.0.0.1',228,10,32,116,'project',32,NULL,'staff',76,NULL,' Williams Smith Johnson '),(7,'2015-09-30 11:49:31','127.0.0.1',228,10,32,116,'project',32,NULL,'staff',77,NULL,' Johnson Smith Williams '),(8,'2015-09-30 11:49:31','127.0.0.1',228,10,32,116,'project',32,NULL,'staff',78,NULL,'Engr. Bob Marley Lannister '),(9,'2015-09-30 11:49:32','127.0.0.1',228,10,32,116,'project',32,NULL,'staff',79,NULL,'Dr. Erlang Churva Looo III'),(10,'2015-09-30 11:49:32','127.0.0.1',228,10,32,116,'project',32,NULL,'staff',74,NULL,' Vic Castillo Cebedo II'),(11,'2015-09-30 11:49:32','127.0.0.1',228,10,32,116,'project',32,NULL,'staff',75,NULL,' Smith Johnson Williams '),(12,'2015-09-30 11:49:52','127.0.0.1',228,10,32,119,'project',32,NULL,'staff',76,NULL,' Williams Smith Johnson ');

/*Table structure for table `companies` */

DROP TABLE IF EXISTS `companies`;

CREATE TABLE `companies` (
  `company_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `date_started` datetime NOT NULL,
  `date_expiration` datetime NOT NULL,
  `beta_tester` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

/*Data for the table `companies` */

insert  into `companies`(`company_id`,`name`,`description`,`date_started`,`date_expiration`,`beta_tester`) values (10,'Demo 1','','2015-09-01 00:00:00','2016-01-07 00:00:00',1);

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

/*Table structure for table `projects` */

DROP TABLE IF EXISTS `projects`;

CREATE TABLE `projects` (
  `project_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `status` int(2) NOT NULL,
  `location` varchar(108) DEFAULT NULL,
  `notes` text,
  `company_id` bigint(20) DEFAULT NULL,
  `physical_target` double DEFAULT '0',
  `date_start` date DEFAULT NULL,
  `date_completion_target` date DEFAULT NULL,
  `date_completion_actual` date DEFAULT NULL,
  PRIMARY KEY (`project_id`),
  KEY `FK_projects_company` (`company_id`),
  CONSTRAINT `FK_projects_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

/*Data for the table `projects` */

insert  into `projects`(`project_id`,`name`,`status`,`location`,`notes`,`company_id`,`physical_target`,`date_start`,`date_completion_target`,`date_completion_actual`) values (32,'Demo1',1,'asdasdsad','asdadsasdxx',10,123123,'2015-09-01','2015-10-01',NULL);

/*Table structure for table `staff` */

DROP TABLE IF EXISTS `staff`;

CREATE TABLE `staff` (
  `staff_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name_prefix` varchar(8) NOT NULL,
  `name_first` varchar(32) NOT NULL,
  `name_middle` varchar(16) NOT NULL,
  `name_last` varchar(16) NOT NULL,
  `name_suffix` varchar(8) NOT NULL,
  `position` varchar(32) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `contact_number` varchar(32) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `wage` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`staff_id`),
  KEY `FK_staff_company` (`company_id`),
  CONSTRAINT `FK_staff_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=latin1;

/*Data for the table `staff` */

insert  into `staff`(`staff_id`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`contact_number`,`company_id`,`wage`) values (72,'','','','','',NULL,NULL,NULL,10,0),(73,'','','','','',NULL,NULL,NULL,10,0),(74,'','Vic','Castillo','Cebedo','II','Test','cebedo.vii@gmail.com','9226110429',10,500),(75,'','Smith','Johnson','Williams','','Test','cebedo.vii@gmail.com','9226110429',10,400),(76,'','Williams','Smith','Johnson','','Test','cebedo.vii@gmail.com','9226110429',10,300),(77,'','Johnson','Smith','Williams','','Test','cebedo.vii@gmail.com','9226110429',10,350),(78,'Engr.','Bob','Marley','Lannister','','Test','cebedo.vii@gmail.com','9226110429',10,350),(79,'Dr.','Erlang','Churva','Looo','III','Test','cebedo.vii@gmail.com','9226110429',10,350);

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

/*Data for the table `system_configuration` */

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
) ENGINE=InnoDB AUTO_INCREMENT=229 DEFAULT CHARSET=latin1;

/*Data for the table `system_users` */

insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (219,'root_098','4ca3c78a6db368685a6a1a67045de747',1,NULL,NULL,0,0),(227,'demo_admin1','54044c4ed6c192034e51acfaaf48d948',0,10,72,1,0),(228,'standard_user1','1f27cd46ff32ac1069d0410ddfeca481',0,10,73,0,0);

/*Table structure for table `tasks` */

DROP TABLE IF EXISTS `tasks`;

CREATE TABLE `tasks` (
  `task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `content` varchar(255) NOT NULL,
  `date_start` date NOT NULL,
  `actual_date_start` date DEFAULT NULL,
  `duration` double NOT NULL DEFAULT '0',
  `actual_duration` double NOT NULL DEFAULT '0',
  `project_id` bigint(20) DEFAULT NULL,
  `status` int(2) NOT NULL DEFAULT '0',
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  KEY `FK_tasks_project` (`project_id`),
  KEY `FK_tasks_company` (`company_id`),
  CONSTRAINT `FK_tasks_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_tasks_project` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=latin1;

/*Data for the table `tasks` */

insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`actual_date_start`,`duration`,`actual_duration`,`project_id`,`status`,`company_id`) values (84,'Task 1','Test 4','2015-01-10','2015-01-10',3,5,32,2,10),(85,'Task 2','','2015-01-13','2015-01-15',2,3,32,2,10),(86,'Task 3','Test 6','2015-01-15','2015-01-18',6,5,32,2,10),(87,'Task 4','Test 7','2015-01-21',NULL,10,0,32,1,10),(88,'Task 5','Test 8','2015-01-31','2015-01-31',3,4,32,2,10),(89,'Task 5','Test 8','2015-02-03','2015-02-04',3,5,32,2,10);

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
