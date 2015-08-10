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

insert  into `assignments_project_manager`(`project_id`,`staff_id`,`project_position`) values (72,62,'Project Manager');

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

insert  into `assignments_project_team`(`project_id`,`team_id`) values (72,29);

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

insert  into `assignments_staff_team`(`staff_id`,`team_id`) values (62,29),(63,29);

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

insert  into `assignments_task_staff`(`task_id`,`staff_id`) values (70,62),(72,62);

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

insert  into `assignments_task_team`(`task_id`,`team_id`) values (70,29),(71,30);

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

insert  into `assignments_user_access`(`securityaccess_id`,`user_id`) values (12,32),(13,32),(14,32),(15,32),(16,32),(17,32),(18,32),(19,32),(20,32),(21,32),(22,32),(14,34),(15,34),(16,34),(17,34),(18,34),(19,34),(20,34),(12,35),(13,35),(14,35),(15,35),(16,35),(17,35),(18,35),(19,35),(20,35),(21,35),(22,35);

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

insert  into `assignments_user_role`(`securityrole_id`,`user_id`) values (11,32),(12,32),(13,32),(14,32),(15,32),(16,32),(17,32),(18,32),(19,32),(20,32),(12,34),(13,34),(14,34),(15,34),(16,34),(17,34),(19,34),(11,35),(12,35),(13,35),(14,35),(15,35),(16,35),(17,35),(18,35),(19,35),(20,35);

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `audit_logs` */

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

insert  into `deliveries`(`delivery_id`,`name`,`description`,`datetime`,`project_id`,`company_id`,`storage_id`) values (1,'Test','Testing here','2015-05-25 00:00:00',72,4,NULL);

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

insert  into `expenses`(`expense_id`,`name`,`description`,`value`,`datetime`,`task_id`,`project_id`,`company_id`,`staff_id`,`team_id`,`delivery_id`,`material_id`,`milestone_id`,`reminder_id`,`storage_id`,`supplier_id`) values (1,'task test','teasdas',20030,'2015-05-12 12:00:00',70,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `fields` */

DROP TABLE IF EXISTS `fields`;

CREATE TABLE `fields` (
  `field_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`field_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `fields` */

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `milestones` */

insert  into `milestones`(`milestone_id`,`name`,`description`,`project_id`,`company_id`) values (1,'Test Milestone','Test description',72,4);

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
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

/*Data for the table `projects` */

insert  into `projects`(`project_id`,`thumbnail_url`,`name`,`type`,`status`,`location`,`notes`,`company_id`) values (72,NULL,'ABC Dormitory',0,0,'Rizal Avenue, Dipolog City','This is a test project.',4);

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

insert  into `reminders`(`reminder_id`,`title`,`content`,`datetime`,`project_id`,`company_id`) values (1,'Test title','Test content','2015-05-15 00:00:00',72,4);

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
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=latin1;

/*Data for the table `staff` */

insert  into `staff`(`staff_id`,`thumbnail_url`,`name_prefix`,`name_first`,`name_middle`,`name_last`,`name_suffix`,`position`,`email`,`contact_number`,`company_id`,`wage`) values (62,NULL,'Engr.','John','Dane','Doe','Jr.','Snr. Management Staff','test@gmail.com','0922 611 0429',4,300),(63,NULL,'','Anna','Adaza','de la Cruz','','QA','test@gmail.com','0922 611 0429',4,500);

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
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;

/*Data for the table `system_users` */

insert  into `system_users`(`user_id`,`username`,`password`,`super_admin`,`company_id`,`staff_id`,`company_admin`,`login_attempts`) values (32,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0),(34,'com_admin1','2f3d253e456b75b2686dfb72a500aa0b',0,4,62,1,0),(35,'root','691b0eec514f5033ebdb6b6144aa016a',1,NULL,NULL,0,0);

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
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;

/*Data for the table `tasks` */

insert  into `tasks`(`task_id`,`title`,`content`,`date_start`,`duration`,`project_id`,`status`,`company_id`,`milestone_id`) values (70,'Lemon','Excavate the site','2015-05-01',7,72,0,4,1),(71,'Excavation','Excavation of the building','2015-05-01',5,72,4,4,NULL),(72,'Test','Testing','2015-05-10',10,72,1,4,1);

/*Table structure for table `teams` */

DROP TABLE IF EXISTS `teams`;

CREATE TABLE `teams` (
  `team_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`team_id`),
  KEY `FK_teams_company` (`company_id`),
  CONSTRAINT `FK_teams_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;

/*Data for the table `teams` */

insert  into `teams`(`team_id`,`name`,`company_id`) values (29,'Quality Assurance',4),(30,'QA Team',4);

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
