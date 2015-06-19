/*
SQLyog Community Edition- MySQL GUI v6.07
Host - 5.6.24 : Database - test
*********************************************************************
Server version : 5.6.24
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

/*Table structure for table `assignments_project_manager` */

DROP TABLE IF EXISTS `assignments_project_manager`;

CREATE TABLE `assignments_project_manager` (
  `project_id` bigint(20) NOT NULL,
  `staff_id` bigint(20) NOT NULL,
  `project_position` varchar(32) NOT NULL,
  PRIMARY KEY (`project_id`,`staff_id`),
  KEY `FK_staff_id` (`staff_id`),
  CONSTRAINT `FK_staff_id` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `assignments_project_manager_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_project_manager` */

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

insert  into `assignments_project_staff`(`project_id`,`staff_id`) values (78,64),(78,65),(78,66),(78,67),(78,68);

/*Table structure for table `assignments_project_team` */

DROP TABLE IF EXISTS `assignments_project_team`;

CREATE TABLE `assignments_project_team` (
  `project_id` bigint(20) NOT NULL,
  `team_id` bigint(20) NOT NULL,
  PRIMARY KEY (`project_id`,`team_id`),
  KEY `FK_team_id` (`team_id`),
  CONSTRAINT `FK_team_id` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`),
  CONSTRAINT `assignments_project_team_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_project_team` */

/*Table structure for table `assignments_staff_delivery` */

DROP TABLE IF EXISTS `assignments_staff_delivery`;

CREATE TABLE `assignments_staff_delivery` (
  `staff_id` bigint(20) NOT NULL,
  `delivery_id` bigint(20) NOT NULL,
  PRIMARY KEY (`staff_id`,`delivery_id`),
  KEY `FK_assignments_staff_delivery_delivery` (`delivery_id`),
  CONSTRAINT `FK_assignments_staff_delivery_delivery` FOREIGN KEY (`delivery_id`) REFERENCES `deliveries` (`delivery_id`),
  CONSTRAINT `FK_assignments_staff_delivery_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_staff_delivery` */

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

insert  into `assignments_staff_team`(`staff_id`,`team_id`) values (62,29),(63,29),(64,30),(65,30),(67,32);

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

insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (12,32),(13,32),(14,32),(15,32),(16,32),(17,32),(18,32),(19,32),(20,32),(21,32),(22,32),(14,34),(15,34),(16,34),(17,34),(18,34),(19,34),(20,34),(12,35),(13,35),(14,35),(15,35),(16,35),(17,35),(18,35),(19,35),(20,35),(21,35),(22,35),(12,36),(13,36),(14,36),(15,36),(16,36),(17,36),(18,36),(19,36),(20,36),(21,36),(22,36),(12,37),(13,37),(14,37),(15,37),(16,37),(17,37),(18,37),(19,37),(20,37),(21,37),(22,37),(12,38),(13,38),(14,38),(15,38),(16,38),(17,38),(18,38),(19,38),(20,38),(21,38),(22,38),(12,39),(13,39),(14,39),(15,39),(16,39),(17,39),(18,39),(19,39),(20,39),(21,39),(22,39),(12,40),(13,40),(14,40),(15,40),(16,40),(17,40),(18,40),(19,40),(20,40),(21,40),(22,40),(12,41),(13,41),(14,41),(15,41),(16,41),(17,41),(18,41),(19,41),(20,41),(21,41),(22,41),(12,42),(13,42),(14,42),(15,42),(16,42),(17,42),(18,42),(19,42),(20,42),(21,42),(22,42),(12,43),(13,43),(14,43),(15,43),(16,43),(17,43),(18,43),(19,43),(20,43),(21,43),(22,43),(12,44),(13,44),(14,44),(15,44),(16,44),(17,44),(18,44),(19,44),(20,44),(21,44),(22,44),(12,45),(13,45),(14,45),(15,45),(16,45),(17,45),(18,45),(19,45),(20,45),(21,45),(22,45),(12,46),(13,46),(14,46),(15,46),(16,46),(17,46),(18,46),(19,46),(20,46),(21,46),(22,46),(12,47),(13,47),(14,47),(15,47),(16,47),(17,47),(18,47),(19,47),(20,47),(21,47),(22,47),(12,48),(13,48),(14,48),(15,48),(16,48),(17,48),(18,48),(19,48),(20,48),(21,48),(22,48),(12,49),(13,49),(14,49),(15,49),(16,49),(17,49),(18,49),(19,49),(20,49),(21,49),(22,49),(12,50),(13,50),(14,50),(15,50),(16,50),(17,50),(18,50),(19,50),(20,50),(21,50),(22,50),(12,51),(13,51),(14,51),(15,51),(16,51),(17,51),(18,51),(19,51),(20,51),(21,51),(22,51),(12,52),(13,52),(14,52),(15,52),(16,52),(17,52),(18,52),(19,52),(20,52),(21,52),(22,52),(12,53),(13,53),(14,53),(15,53),(16,53),(17,53),(18,53),(19,53),(20,53),(21,53),(22,53),(12,54),(13,54),(14,54),(15,54),(16,54),(17,54),(18,54),(19,54),(20,54),(21,54),(22,54),(12,55),(13,55),(14,55),(15,55),(16,55),(17,55),(18,55),(19,55),(20,55),(21,55),(22,55),(12,56),(13,56),(14,56),(15,56),(16,56),(17,56),(18,56),(19,56),(20,56),(21,56),(22,56),(12,57),(13,57),(14,57),(15,57),(16,57),(17,57),(18,57),(19,57),(20,57),(21,57),(22,57),(12,58),(13,58),(14,58),(15,58),(16,58),(17,58),(18,58),(19,58),(20,58),(21,58),(22,58),(12,59),(13,59),(14,59),(15,59),(16,59),(17,59),(18,59),(19,59),(20,59),(21,59),(22,59),(12,60),(13,60),(14,60),(15,60),(16,60),(17,60),(18,60),(19,60),(20,60),(21,60),(22,60),(12,61),(13,61),(14,61),(15,61),(16,61),(17,61),(18,61),(19,61),(20,61),(21,61),(22,61),(12,62),(13,62),(14,62),(15,62),(16,62),(17,62),(18,62),(19,62),(20,62),(21,62),(22,62),(12,63),(13,63),(14,63),(15,63),(16,63),(17,63),(18,63),(19,63),(20,63),(21,63),(22,63),(12,64),(13,64),(14,64),(15,64),(16,64),(17,64),(18,64),(19,64),(20,64),(21,64),(22,64),(12,65),(13,65),(14,65),(15,65),(16,65),(17,65),(18,65),(19,65),(20,65),(21,65),(22,65),(12,66),(13,66),(14,66),(15,66),(16,66),(17,66),(18,66),(19,66),(20,66),(21,66),(22,66),(12,67),(13,67),(14,67),(15,67),(16,67),(17,67),(18,67),(19,67),(20,67),(21,67),(22,67),(12,68),(13,68),(14,68),(15,68),(16,68),(17,68),(18,68),(19,68),(20,68),(21,68),(22,68),(12,69),(13,69),(14,69),(15,69),(16,69),(17,69),(18,69),(19,69),(20,69),(21,69),(22,69),(12,70),(13,70),(14,70),(15,70),(16,70),(17,70),(18,70),(19,70),(20,70),(21,70),(22,70),(12,71),(13,71),(14,71),(15,71),(16,71),(17,71),(18,71),(19,71),(20,71),(21,71),(22,71),(12,72),(13,72),(14,72),(15,72),(16,72),(17,72),(18,72),(19,72),(20,72),(21,72),(22,72),(12,73),(13,73),(14,73),(15,73),(16,73),(17,73),(18,73),(19,73),(20,73),(21,73),(22,73),(12,74),(13,74),(14,74),(15,74),(16,74),(17,74),(18,74),(19,74),(20,74),(21,74),(22,74),(12,75),(13,75),(14,75),(15,75),(16,75),(17,75),(18,75),(19,75),(20,75),(21,75),(22,75),(12,76),(13,76),(14,76),(15,76),(16,76),(17,76),(18,76),(19,76),(20,76),(21,76),(22,76),(12,77),(13,77),(14,77),(15,77),(16,77),(17,77),(18,77),(19,77),(20,77),(21,77),(22,77),(12,78),(13,78),(14,78),(15,78),(16,78),(17,78),(18,78),(19,78),(20,78),(21,78),(22,78),(12,79),(13,79),(14,79),(15,79),(16,79),(17,79),(18,79),(19,79),(20,79),(21,79),(22,79),(12,80),(13,80),(14,80),(15,80),(16,80),(17,80),(18,80),(19,80),(20,80),(21,80),(22,80);

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

insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (11,32),(12,32),(13,32),(14,32),(15,32),(16,32),(17,32),(18,32),(19,32),(20,32),(12,34),(13,34),(14,34),(15,34),(16,34),(17,34),(19,34),(11,35),(12,35),(13,35),(14,35),(15,35),(16,35),(17,35),(18,35),(19,35),(20,35),(11,36),(12,36),(13,36),(14,36),(15,36),(16,36),(17,36),(18,36),(19,36),(20,36),(11,37),(12,37),(13,37),(14,37),(15,37),(16,37),(17,37),(18,37),(19,37),(20,37),(11,38),(12,38),(13,38),(14,38),(15,38),(16,38),(17,38),(18,38),(19,38),(20,38),(11,39),(12,39),(13,39),(14,39),(15,39),(16,39),(17,39),(18,39),(19,39),(20,39),(11,40),(12,40),(13,40),(14,40),(15,40),(16,40),(17,40),(18,40),(19,40),(20,40),(11,41),(12,41),(13,41),(14,41),(15,41),(16,41),(17,41),(18,41),(19,41),(20,41),(11,42),(12,42),(13,42),(14,42),(15,42),(16,42),(17,42),(18,42),(19,42),(20,42),(11,43),(12,43),(13,43),(14,43),(15,43),(16,43),(17,43),(18,43),(19,43),(20,43),(11,44),(12,44),(13,44),(14,44),(15,44),(16,44),(17,44),(18,44),(19,44),(20,44),(11,45),(12,45),(13,45),(14,45),(15,45),(16,45),(17,45),(18,45),(19,45),(20,45),(11,46),(12,46),(13,46),(14,46),(15,46),(16,46),(17,46),(18,46),(19,46),(20,46),(11,47),(12,47),(13,47),(14,47),(15,47),(16,47),(17,47),(18,47),(19,47),(20,47),(11,48),(12,48),(13,48),(14,48),(15,48),(16,48),(17,48),(18,48),(19,48),(20,48),(11,49),(12,49),(13,49),(14,49),(15,49),(16,49),(17,49),(18,49),(19,49),(20,49),(11,50),(12,50),(13,50),(14,50),(15,50),(16,50),(17,50),(18,50),(19,50),(20,50),(11,51),(12,51),(13,51),(14,51),(15,51),(16,51),(17,51),(18,51),(19,51),(20,51),(11,52),(12,52),(13,52),(14,52),(15,52),(16,52),(17,52),(18,52),(19,52),(20,52),(11,53),(12,53),(13,53),(14,53),(15,53),(16,53),(17,53),(18,53),(19,53),(20,53),(11,54),(12,54),(13,54),(14,54),(15,54),(16,54),(17,54),(18,54),(19,54),(20,54),(11,55),(12,55),(13,55),(14,55),(15,55),(16,55),(17,55),(18,55),(19,55),(20,55),(11,56),(12,56),(13,56),(14,56),(15,56),(16,56),(17,56),(18,56),(19,56),(20,56),(11,57),(12,57),(13,57),(14,57),(15,57),(16,57),(17,57),(18,57),(19,57),(20,57),(11,58),(12,58),(13,58),(14,58),(15,58),(16,58),(17,58),(18,58),(19,58),(20,58),(11,59),(12,59),(13,59),(14,59),(15,59),(16,59),(17,59),(18,59),(19,59),(20,59),(11,60),(12,60),(13,60),(14,60),(15,60),(16,60),(17,60),(18,60),(19,60),(20,60),(11,61),(12,61),(13,61),(14,61),(15,61),(16,61),(17,61),(18,61),(19,61),(20,61),(11,62),(12,62),(13,62),(14,62),(15,62),(16,62),(17,62),(18,62),(19,62),(20,62),(11,63),(12,63),(13,63),(14,63),(15,63),(16,63),(17,63),(18,63),(19,63),(20,63),(11,64),(12,64),(13,64),(14,64),(15,64),(16,64),(17,64),(18,64),(19,64),(20,64),(11,65),(12,65),(13,65),(14,65),(15,65),(16,65),(17,65),(18,65),(19,65),(20,65),(11,66),(12,66),(13,66),(14,66),(15,66),(16,66),(17,66),(18,66),(19,66),(20,66),(11,67),(12,67),(13,67),(14,67),(15,67),(16,67),(17,67),(18,67),(19,67),(20,67),(11,68),(12,68),(13,68),(14,68),(15,68),(16,68),(17,68),(18,68),(19,68),(20,68),(11,69),(12,69),(13,69),(14,69),(15,69),(16,69),(17,69),(18,69),(19,69),(20,69),(11,70),(12,70),(13,70),(14,70),(15,70),(16,70),(17,70),(18,70),(19,70),(20,70),(11,71),(12,71),(13,71),(14,71),(15,71),(16,71),(17,71),(18,71),(19,71),(20,71),(11,72),(12,72),(13,72),(14,72),(15,72),(16,72),(17,72),(18,72),(19,72),(20,72),(11,73),(12,73),(13,73),(14,73),(15,73),(16,73),(17,73),(18,73),(19,73),(20,73),(11,74),(12,74),(13,74),(14,74),(15,74),(16,74),(17,74),(18,74),(19,74),(20,74),(11,75),(12,75),(13,75),(14,75),(15,75),(16,75),(17,75),(18,75),(19,75),(20,75),(11,76),(12,76),(13,76),(14,76),(15,76),(16,76),(17,76),(18,76),(19,76),(20,76),(11,77),(12,77),(13,77),(14,77),(15,77),(16,77),(17,77),(18,77),(19,77),(20,77),(11,78),(12,78),(13,78),(14,78),(15,78),(16,78),(17,78),(18,78),(19,78),(20,78),(11,79),(12,79),(13,79),(14,79),(15,79),(16,79),(17,79),(18,79),(19,79),(20,79),(11,80),(12,80),(13,80),(14,80),(15,80),(16,80),(17,80),(18,80),(19,80),(20,80);

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
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=latin1;

/*Data for the table `audit_logs` */

insert  into `audit_logs`(`auditlog_id`,`date_executed`,`ip_address`,`user_id`,`company_id`,`action`,`object_name`,`object_id`) values (1,'2015-06-03 15:47:55','127.0.0.1',34,4,2,'project',72),(2,'2015-06-03 15:50:35','127.0.0.1',34,4,2,'project',72),(3,'2015-06-03 15:56:07','127.0.0.1',34,4,2,'project',72),(4,'2015-06-04 15:22:52','127.0.0.1',34,4,1,'project',0),(5,'2015-06-04 15:27:46','127.0.0.1',34,4,8,'team',30),(6,'2015-06-04 15:35:07','127.0.0.1',34,4,1,'staff',0),(7,'2015-06-04 15:39:17','127.0.0.1',34,4,8,'team',30),(8,'2015-06-04 15:39:22','127.0.0.1',34,4,8,'team',30),(9,'2015-06-04 15:46:47','127.0.0.1',34,4,8,'staff',63),(10,'2015-06-04 15:46:51','127.0.0.1',34,4,8,'staff',64),(11,'2015-06-11 09:23:30','127.0.0.1',34,4,1,'project',0),(12,'2015-06-11 09:23:58','127.0.0.1',34,4,8,'staff',62),(13,'2015-06-11 09:24:12','127.0.0.1',34,4,9,'staff',62),(14,'2015-06-11 09:24:24','127.0.0.1',34,4,8,'staff',62),(15,'2015-06-11 09:25:03','127.0.0.1',34,4,8,'staff',63),(18,'2015-06-11 09:30:33','127.0.0.1',34,4,8,'staff',62),(19,'2015-06-11 10:02:24','127.0.0.1',34,4,8,'team',29),(20,'2015-06-11 10:17:07','127.0.0.1',34,4,1,'task',0),(21,'2015-06-11 10:19:03','127.0.0.1',34,4,1,'task',0),(22,'2015-06-11 10:20:02','127.0.0.1',34,4,1,'task',0),(23,'2015-06-11 10:42:01','127.0.0.1',34,4,8,'staff',63),(24,'2015-06-11 11:22:48','127.0.0.1',34,4,8,'staff',64),(25,'2015-06-11 11:22:56','127.0.0.1',34,4,9,'staff',64),(26,'2015-06-11 11:34:54','127.0.0.1',34,4,8,'team',30),(27,'2015-06-11 11:35:59','127.0.0.1',34,4,9,'team',30),(28,'2015-06-11 11:36:26','127.0.0.1',34,4,1,'staff',0),(29,'2015-06-11 11:36:41','127.0.0.1',34,4,8,'team',30),(30,'2015-06-11 11:39:46','127.0.0.1',34,4,1,'staff',0),(31,'2015-06-11 11:40:01','127.0.0.1',34,4,8,'staff',66),(32,'2015-06-11 15:31:55','127.0.0.1',34,4,1,'team',0),(33,'2015-06-11 15:32:45','127.0.0.1',34,4,1,'staff',0),(34,'2015-06-11 16:41:15','127.0.0.1',34,4,1,'team',0),(35,'2015-06-11 16:41:26','127.0.0.1',34,4,8,'team',32),(36,'2015-06-15 13:53:37','127.0.0.1',34,4,1,'staff',0),(37,'2015-06-15 17:25:40','127.0.0.1',34,4,11,'task',73),(38,'2015-06-16 11:47:01','127.0.0.1',34,4,2,'project',74),(39,'2015-06-16 11:53:02','127.0.0.1',34,4,8,'field',1),(40,'2015-06-16 11:54:40','127.0.0.1',34,4,15,'project',74),(41,'2015-06-16 11:57:38','127.0.0.1',34,4,14,'project',74),(42,'2015-06-16 11:57:48','127.0.0.1',34,4,15,'project',74),(43,'2015-06-16 11:57:51','127.0.0.1',34,4,14,'project',74),(44,'2015-06-16 11:58:21','127.0.0.1',34,4,8,'field',1),(45,'2015-06-16 11:59:23','127.0.0.1',34,4,8,'field',1),(46,'2015-06-16 12:01:09','127.0.0.1',34,4,8,'field',1),(47,'2015-06-16 12:03:49','127.0.0.1',34,4,8,'field',1),(48,'2015-06-16 13:37:50','127.0.0.1',34,4,2,'Field Assignment',1),(49,'2015-06-16 13:40:33','127.0.0.1',34,4,2,'Field Assignment',1),(50,'2015-06-16 13:42:21','127.0.0.1',34,4,2,'Field Assignment',1),(51,'2015-06-16 13:42:29','127.0.0.1',34,4,2,'Field Assignment',1),(52,'2015-06-16 13:42:38','127.0.0.1',34,4,2,'Field Assignment',1),(53,'2015-06-16 13:42:46','127.0.0.1',34,4,2,'Field Assignment',1),(54,'2015-06-16 13:42:56','127.0.0.1',34,4,8,'field',1),(55,'2015-06-16 13:43:05','127.0.0.1',34,4,2,'Field Assignment',1),(56,'2015-06-17 15:00:51','127.0.0.1',34,4,2,'project',74),(57,'2015-06-17 15:01:11','127.0.0.1',34,4,2,'staff',62),(58,'2015-06-18 13:39:27','127.0.0.1',34,4,1,'project',0),(59,'2015-06-18 13:44:14','127.0.0.1',34,4,3,'project',75),(60,'2015-06-18 13:44:29','127.0.0.1',34,4,1,'project',0),(61,'2015-06-18 14:35:01','127.0.0.1',34,4,8,'staff',62),(64,'2015-06-18 14:59:41','127.0.0.1',34,4,8,'staff',62),(65,'2015-06-18 17:28:57','127.0.0.1',34,4,8,'field',1),(66,'2015-06-19 08:28:37','127.0.0.1',34,4,8,'field',1),(67,'2015-06-19 08:28:51','127.0.0.1',34,4,8,'field',1),(68,'2015-06-19 15:34:16','127.0.0.1',34,4,3,'project',76),(69,'2015-06-19 15:34:22','127.0.0.1',34,4,3,'project',74),(70,'2015-06-19 15:34:24','127.0.0.1',34,4,3,'project',73),(71,'2015-06-19 15:34:26','127.0.0.1',34,4,3,'project',72),(72,'2015-06-19 15:34:44','127.0.0.1',34,4,1,'project',0),(73,'2015-06-19 16:06:10','127.0.0.1',34,4,3,'project',77),(74,'2015-06-19 16:06:19','127.0.0.1',34,4,1,'project',0);

/*Table structure for table `companies` */

DROP TABLE IF EXISTS `companies`;

CREATE TABLE `companies` (
  `company_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `date_started` datetime NOT NULL,
  `date_expiration` datetime NOT NULL,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `companies` */

insert  into `companies`(`company_id`,`name`,`description`,`date_started`,`date_expiration`) values (4,'VCC Construction','This is a demo company.','2014-01-01 00:00:00','2020-01-01 00:00:00');

/*Table structure for table `deliveries` */

DROP TABLE IF EXISTS `deliveries`;

CREATE TABLE `deliveries` (
  `delivery_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `datetime` datetime NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `storage_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`delivery_id`),
  KEY `FK_deliveries_project` (`project_id`),
  KEY `FK_deliveries_company` (`company_id`),
  KEY `FK_deliveries_storages` (`storage_id`),
  CONSTRAINT `FK_deliveries_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_deliveries_project` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_deliveries_storages` FOREIGN KEY (`storage_id`) REFERENCES `storages` (`storage_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `deliveries` */

/*Table structure for table `expenses` */

DROP TABLE IF EXISTS `expenses`;

CREATE TABLE `expenses` (
  `expense_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `value` double NOT NULL DEFAULT '0',
  `datetime` datetime NOT NULL,
  `task_id` bigint(20) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `staff_id` bigint(20) DEFAULT NULL,
  `team_id` bigint(20) DEFAULT NULL,
  `delivery_id` bigint(20) DEFAULT NULL,
  `material_id` bigint(20) DEFAULT NULL,
  `milestone_id` bigint(20) DEFAULT NULL,
  `reminder_id` bigint(20) DEFAULT NULL,
  `storage_id` bigint(20) DEFAULT NULL,
  `supplier_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`expense_id`),
  KEY `FK_expenses_companies` (`company_id`),
  KEY `FK_expenses_deliveries` (`delivery_id`),
  KEY `FK_expenses_materials` (`material_id`),
  KEY `FK_expenses_milestones` (`milestone_id`),
  KEY `FK_expenses_projects` (`project_id`),
  KEY `FK_expenses_reminders` (`reminder_id`),
  KEY `FK_expenses_staff` (`staff_id`),
  KEY `FK_expenses_storages` (`storage_id`),
  KEY `FK_expenses_suppliers` (`supplier_id`),
  KEY `FK_expenses_tasks` (`task_id`),
  CONSTRAINT `FK_expenses_companies` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_expenses_deliveries` FOREIGN KEY (`delivery_id`) REFERENCES `deliveries` (`delivery_id`),
  CONSTRAINT `FK_expenses_materials` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `FK_expenses_milestones` FOREIGN KEY (`milestone_id`) REFERENCES `milestones` (`milestone_id`),
  CONSTRAINT `FK_expenses_projects` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_expenses_reminders` FOREIGN KEY (`reminder_id`) REFERENCES `reminders` (`reminder_id`),
  CONSTRAINT `FK_expenses_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `FK_expenses_storages` FOREIGN KEY (`storage_id`) REFERENCES `storages` (`storage_id`),
  CONSTRAINT `FK_expenses_suppliers` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`),
  CONSTRAINT `FK_expenses_tasks` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `expenses` */

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

/*Table structure for table `materials` */

DROP TABLE IF EXISTS `materials`;

CREATE TABLE `materials` (
  `material_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` double NOT NULL DEFAULT '0',
  `purchase_datetime` datetime NOT NULL,
  `status` int(2) NOT NULL,
  `supplier_id` bigint(20) DEFAULT NULL,
  `delivery_id` bigint(20) DEFAULT NULL,
  `storage_id` bigint(20) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `task_id` bigint(20) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`material_id`),
  KEY `FK_materials_suppliers` (`supplier_id`),
  KEY `FK_materials_deliveries` (`delivery_id`),
  KEY `FK_materials_storages` (`storage_id`),
  KEY `FK_materials_projects` (`project_id`),
  KEY `FK_materials_tasks` (`task_id`),
  KEY `FK_materials_companies` (`company_id`),
  CONSTRAINT `FK_materials_companies` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_materials_deliveries` FOREIGN KEY (`delivery_id`) REFERENCES `deliveries` (`delivery_id`),
  CONSTRAINT `FK_materials_projects` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_materials_storages` FOREIGN KEY (`storage_id`) REFERENCES `storages` (`storage_id`),
  CONSTRAINT `FK_materials_suppliers` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`),
  CONSTRAINT `FK_materials_tasks` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `materials` */

/*Table structure for table `milestones` */

DROP TABLE IF EXISTS `milestones`;

CREATE TABLE `milestones` (
  `milestone_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) NOT NULL,
  `company_id` bigint(20) NOT NULL,
  PRIMARY KEY (`milestone_id`),
  KEY `FK_milestones_projects` (`project_id`),
  KEY `FK_milestones_companies` (`company_id`),
  CONSTRAINT `FK_milestones_companies` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_milestones_projects` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `milestones` */

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `photos` */

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `project_files` */

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
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1;

/*Data for the table `projects` */

insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (78,NULL,'asasdas',0,0,'dasdsadas','cdasdcsad',4);

/*Table structure for table `reminders` */

DROP TABLE IF EXISTS `reminders`;

CREATE TABLE `reminders` (
  `reminder_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(32) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `datetime` datetime NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`reminder_id`),
  KEY `FK_reminders_projects` (`project_id`),
  KEY `FK_reminders_companies` (`company_id`),
  CONSTRAINT `FK_reminders_companies` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_reminders_projects` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `reminders` */

/*Table structure for table `security_access` */

DROP TABLE IF EXISTS `security_access`;

CREATE TABLE `security_access` (
  `securityaccess_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `label` varchar(32) NOT NULL,
  PRIMARY KEY (`securityaccess_id`,`name`),
  UNIQUE KEY `securitygroup_id` (`securityaccess_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

/*Data for the table `security_access` */

insert  into `security_access`(`securityaccess_id`,`name`,`label`) values (12,'ACCESS_COMPANY','Company'),(13,'ACCESS_FIELD','Field'),(14,'ACCESS_PHOTO','Photo'),(15,'ACCESS_PROJECT','Project'),(16,'ACCESS_PROJECTFILE','Project File'),(17,'ACCESS_STAFF','Staff'),(18,'ACCESS_TASK','Task'),(19,'ACCESS_TEAM','Team'),(20,'ACCESS_SYSTEMUSER','System User'),(21,'ACCESS_CONFIG','Config'),(22,'ACCESS_LOG','Log');

/*Table structure for table `security_roles` */

DROP TABLE IF EXISTS `security_roles`;

CREATE TABLE `security_roles` (
  `securityrole_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `label` varchar(32) NOT NULL,
  PRIMARY KEY (`securityrole_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

/*Data for the table `security_roles` */

insert  into `security_roles`(`securityrole_id`,`name`,`label`) values (11,'ROLE_FIELD_EDITOR','Field Editor'),(12,'ROLE_TASK_EDITOR','Task Editor'),(13,'ROLE_PROJECT_EDITOR','Project Editor'),(14,'ROLE_STAFF_EDITOR','Staff Editor'),(15,'ROLE_PHOTO_EDITOR','Photo Editor'),(16,'ROLE_PROJECTFILE_EDITOR','Project File Editor'),(17,'ROLE_TEAM_EDITOR','Team Editor'),(18,'ROLE_CONFIG_EDITOR','Config Editor'),(19,'ROLE_SYSTEMUSER_EDITOR','System User Editor'),(20,'ROLE_LOG_EDITOR','Log Editor');

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
  `position` varchar(32) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `contact_number` varchar(32) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `wage` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`staff_id`),
  KEY `FK_staff_company` (`company_id`),
  CONSTRAINT `FK_staff_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=latin1;

/*Data for the table `staff` */

insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`contact_number`,`company_id`,`wage`) values (62,NULL,'Engr.','John','Dane','Doe','Jr.','Snr. Management Staff','test@gmail.com','0922 611 0429',4,350),(63,NULL,'','Anna','Adaza','de la Cruz','','QA','test@gmail.com','0922 611 0429',4,500),(64,NULL,'1','121','212','2','12','21','test@gmail.com','0922 611 0429',4,1000),(65,NULL,'QA','QA','QA','QA','QA','QA','test@gmail.com','0922 611 0429',4,400),(66,NULL,'man','man','man','man','man','manpower getter','test@gmail.com','0922 611 0429',4,600),(67,NULL,'w','w','w','w','w','w','test@gmail.com','0922 611 0429',4,600),(68,NULL,'kk','kk','kk','k','k','k','test@gmail.com','0922 611 0429',4,450);

/*Table structure for table `storages` */

DROP TABLE IF EXISTS `storages`;

CREATE TABLE `storages` (
  `storage_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `location` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`storage_id`),
  KEY `FK_storages_companies` (`company_id`),
  CONSTRAINT `FK_storages_companies` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `storages` */

/*Table structure for table `suppliers` */

DROP TABLE IF EXISTS `suppliers`;

CREATE TABLE `suppliers` (
  `supplier_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`supplier_id`),
  KEY `FK_suppliers_companies` (`company_id`),
  CONSTRAINT `FK_suppliers_companies` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `suppliers` */

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `system_configuration` */

insert  into `system_configuration`(`sysconf_id`,`name`,`value`,`company_id`) values (3,'SYS_HOME','C:/vcc/sys/',NULL),(4,'ROOT_INIT','1',NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=latin1;

/*Data for the table `system_users` */

insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (32,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(34,'com_admin1','2f3d253e456b75b2686dfb72a500aa0b',0,4,62,1,0),(35,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(36,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(37,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(38,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(39,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(40,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(41,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(42,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(43,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(44,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(45,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(46,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(47,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(48,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(49,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(50,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(51,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(52,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(53,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(54,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(55,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(56,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(57,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(58,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(59,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(60,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(61,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(62,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(63,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(64,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(65,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(66,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(67,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(68,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(69,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(70,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(71,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(72,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(73,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(74,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(75,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(76,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(77,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(78,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(79,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(80,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0);

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
  `milestone_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  KEY `FK_tasks_project` (`project_id`),
  KEY `FK_tasks_company` (`company_id`),
  CONSTRAINT `FK_tasks_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_tasks_project` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=latin1;

/*Data for the table `tasks` */

/*Table structure for table `teams` */

DROP TABLE IF EXISTS `teams`;

CREATE TABLE `teams` (
  `team_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `payroll_type` int(2) NOT NULL DEFAULT '0',
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`team_id`),
  KEY `FK_teams_company` (`company_id`),
  CONSTRAINT `FK_teams_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

/*Data for the table `teams` */

insert  into `teams`(`team_id`,`name`,`payroll_type`,`company_id`) values (29,'Quality Assurance',1,4),(30,'QA Team',2,4),(31,'walay labot',1,4),(32,'walay labot 2',1,4);

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
