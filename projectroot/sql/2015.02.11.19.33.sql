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
  CONSTRAINT `assignments_project_field_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_field_id` FOREIGN KEY (`field_id`) REFERENCES `fields` (`field_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_project_field` */

insert  into `assignments_project_field`(`project_id`,`field_id`,`label`,`value`) values (11,1,'BIR','12345');

/*Table structure for table `assignments_project_manager` */

DROP TABLE IF EXISTS `assignments_project_manager`;

CREATE TABLE `assignments_project_manager` (
  `project_id` bigint(20) NOT NULL,
  `staff_id` bigint(20) NOT NULL,
  `project_position` varchar(32) NOT NULL,
  PRIMARY KEY (`project_id`,`staff_id`),
  KEY `FK_staff_id` (`staff_id`),
  CONSTRAINT `assignments_project_manager_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_staff_id` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_project_manager` */

insert  into `assignments_project_manager`(`project_id`,`staff_id`,`project_position`) values (11,1,'Project Manager');

/*Table structure for table `assignments_project_team` */

DROP TABLE IF EXISTS `assignments_project_team`;

CREATE TABLE `assignments_project_team` (
  `project_id` bigint(20) NOT NULL,
  `team_id` bigint(20) NOT NULL,
  PRIMARY KEY (`project_id`,`team_id`),
  KEY `FK_team_id` (`team_id`),
  CONSTRAINT `assignments_project_team_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_team_id` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `assignments_project_team` */

insert  into `assignments_project_team`(`project_id`,`team_id`) values (11,3),(11,7);

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
  `thumbnail_url` varchar(255) DEFAULT NULL,
  `name` varchar(32) NOT NULL,
  `type` int(2) NOT NULL,
  `status` int(2) NOT NULL,
  `location` varchar(108) DEFAULT NULL,
  `notes` text,
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

/*Data for the table `projects` */

insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`) values (11,NULL,'testing',0,1,'Rizal St., Central Brgy., Dipolog City, Zamboanga del Norte, Philippines','Another test project.'),(14,NULL,'test',0,0,'','');

/*Table structure for table `staff` */

DROP TABLE IF EXISTS `staff`;

CREATE TABLE `staff` (
  `staff_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `thumbnail_url` varchar(255) DEFAULT NULL,
  `name_prefix` varchar(8) DEFAULT NULL,
  `name_first` varchar(32) NOT NULL,
  `name_middle` varchar(16) DEFAULT NULL,
  `name_last` varchar(16) NOT NULL,
  `name_suffix` varchar(8) DEFAULT NULL,
  `position` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Data for the table `staff` */

insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`) values (1,'src/img/img1.jpg','Engr.','Juan','Mercedez','Cebedo','Jr.','Engineer'),(12,NULL,'Pref','First','Mid','Last','Suf','Pos');

/*Table structure for table `tasks` */

DROP TABLE IF EXISTS `tasks`;

CREATE TABLE `tasks` (
  `task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `date_start` date NOT NULL,
  `date_end` date NOT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `staff_id` bigint(20) DEFAULT NULL,
  `team_id` bigint(20) DEFAULT NULL,
  `status` int(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`task_id`),
  KEY `FK_tasks_projects` (`project_id`),
  KEY `FK_tasks_staff` (`staff_id`),
  KEY `FK_tasks_team` (`team_id`),
  CONSTRAINT `FK_tasks_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `FK_tasks_projects` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_tasks_team` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

/*Data for the table `tasks` */

insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (9,'12','2012-12-12','2012-12-12',14,12,3,1);

/*Table structure for table `teams` */

DROP TABLE IF EXISTS `teams`;

CREATE TABLE `teams` (
  `team_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`team_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

/*Data for the table `teams` */

insert  into `teams`(`team_id`,`name`) values (3,'Underwater Welding'),(7,'All-terrain'),(8,'Building Team');

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
