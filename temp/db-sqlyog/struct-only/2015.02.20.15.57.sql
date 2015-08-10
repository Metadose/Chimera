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

insert  into `assignments_project_manager`(`project_id`,`staff_id`,`project_position`) values (11,1,'asd');
insert  into `assignments_project_manager`(`project_id`,`staff_id`,`project_position`) values (11,12,'xxc');

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

insert  into `assignments_project_team`(`project_id`,`team_id`) values (11,3);
insert  into `assignments_project_team`(`project_id`,`team_id`) values (11,7);

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

/*Table structure for table `project_files` */

DROP TABLE IF EXISTS `project_files`;

CREATE TABLE `project_files` (
  `projectfile_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `size` bigint(20) NOT NULL,
  `location` varchar(255) NOT NULL,
  `staff_id` bigint(20) NOT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `date_uploaded` datetime NOT NULL,
  PRIMARY KEY (`projectfile_id`),
  KEY `FK_project_files` (`project_id`),
  KEY `FK_staff_files` (`staff_id`),
  CONSTRAINT `FK_project_files` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_staff_files` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

/*Data for the table `project_files` */

insert  into `project_files`(`projectfile_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`) values (2,'Scratchpad 2.txt','test',86,'C:/',1,NULL,'2015-02-19 15:25:54');
insert  into `project_files`(`projectfile_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`) values (3,'puttygen.exe','Putty generator',184320,'C:/',1,NULL,'2015-02-19 16:26:12');
insert  into `project_files`(`projectfile_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`) values (4,'VisualElementsManifest.xml','Chrome stuff',399,'C:/',1,11,'2015-02-19 17:01:15');
insert  into `project_files`(`projectfile_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`) values (5,'debug.log','Debug log of chrome',10890,'C:/',1,11,'2015-02-19 17:18:53');
insert  into `project_files`(`projectfile_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`) values (6,'en-US-3-0.bdic','qwdqwd   223',440949,'C:/',1,11,'2015-02-20 13:18:41');
insert  into `project_files`(`projectfile_id`,`name`,`description`,`size`,`location`,`staff_id`,`project_id`,`date_uploaded`) values (7,'en-US-3-0.bdic','232323',440949,'C:/',1,11,'2015-02-20 13:19:57');

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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

/*Data for the table `projects` */

insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`) values (11,NULL,'testing',0,1,'Rizal St., Central Brgy., Dipolog City, Zamboanga del Norte, Philippines','Another test project.');

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
  `email` varchar(32) DEFAULT NULL,
  `contact_number` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

/*Data for the table `staff` */

insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`contact_number`) values (1,NULL,'Engr.','Juan','Mercedez','Cebedo','Jr.','Engineer','mercedez@gmail.com','0922-611-0429');
insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`contact_number`) values (12,NULL,'Pref','First','Mid','Last','Suf','Pos','testpos@gmail.com','0927-961-9650');

/*Table structure for table `system_configuration` */

DROP TABLE IF EXISTS `system_configuration`;

CREATE TABLE `system_configuration` (
  `sysconf_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sysconf_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

/*Data for the table `system_configuration` */

insert  into `system_configuration`(`sysconf_id`,`name`,`value`) values (1,'SYS_HOME','C:/Program Files/VCC/PMSYS/');

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
  KEY `FK_tasks_staff` (`staff_id`),
  KEY `FK_tasks_team` (`team_id`),
  KEY `FK_tasks_project` (`project_id`),
  CONSTRAINT `FK_tasks_project` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_tasks_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `FK_tasks_team` FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1;

/*Data for the table `tasks` */

insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (9,'12','2015-12-12','2020-01-01',NULL,NULL,NULL,2);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (11,'2323','2012-12-12','2012-12-12',NULL,NULL,NULL,2);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (12,'1212','2012-12-11','2011-11-11',NULL,NULL,NULL,3);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (13,'313','2033-03-31','2033-03-31',11,1,3,3);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (17,'1','2011-11-11','2011-02-22',11,12,7,1);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (18,'333','2033-03-31','2033-03-30',11,1,8,3);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (19,'3','2033-03-31','2033-11-11',11,12,NULL,3);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (20,'test11','2011-11-11','2011-11-22',11,1,NULL,1);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (21,'1','2011-11-11','2011-11-11',11,12,3,1);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (22,'2','2022-02-22','2022-02-22',11,NULL,7,2);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (23,'22','2022-02-22','2033-03-11',11,NULL,8,2);
insert  into `tasks`(`task_id`,`content`,`date_start`,`date_end`,`project_id`,`staff_id`,`team_id`,`status`) values (24,'testing now','2011-11-11','2022-02-22',NULL,NULL,NULL,3);

/*Table structure for table `teams` */

DROP TABLE IF EXISTS `teams`;

CREATE TABLE `teams` (
  `team_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`team_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

/*Data for the table `teams` */

insert  into `teams`(`team_id`,`name`) values (3,'Underwater Welding');
insert  into `teams`(`team_id`,`name`) values (7,'All-terrain');
insert  into `teams`(`team_id`,`name`) values (8,'Building Team');

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
