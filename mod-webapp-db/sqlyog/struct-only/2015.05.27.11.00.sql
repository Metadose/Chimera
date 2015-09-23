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

/*Table structure for table `fields` */

DROP TABLE IF EXISTS `fields`;

CREATE TABLE `fields` (
  `field_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`field_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

/*Table structure for table `security_access` */

DROP TABLE IF EXISTS `security_access`;

CREATE TABLE `security_access` (
  `securityaccess_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `label` varchar(32) NOT NULL,
  PRIMARY KEY (`securityaccess_id`,`name`),
  UNIQUE KEY `securitygroup_id` (`securityaccess_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

/*Table structure for table `security_roles` */

DROP TABLE IF EXISTS `security_roles`;

CREATE TABLE `security_roles` (
  `securityrole_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `label` varchar(32) NOT NULL,
  PRIMARY KEY (`securityrole_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

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
