-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.6.26-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assignments_project_field`
--

DROP TABLE IF EXISTS `assignments_project_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignments_project_field`
--

LOCK TABLES `assignments_project_field` WRITE;
/*!40000 ALTER TABLE `assignments_project_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `assignments_project_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignments_project_staff`
--

DROP TABLE IF EXISTS `assignments_project_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assignments_project_staff` (
  `project_id` bigint(20) NOT NULL,
  `staff_id` bigint(20) NOT NULL,
  PRIMARY KEY (`project_id`,`staff_id`),
  KEY `FK_assignments_project_staff_stf` (`staff_id`),
  CONSTRAINT `FK_assignments_project_staff` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_assignments_project_staff_stf` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignments_project_staff`
--

LOCK TABLES `assignments_project_staff` WRITE;
/*!40000 ALTER TABLE `assignments_project_staff` DISABLE KEYS */;
INSERT INTO `assignments_project_staff` VALUES (32,74),(32,75),(32,76),(32,77),(32,78),(32,79);
/*!40000 ALTER TABLE `assignments_project_staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignments_task_staff`
--

DROP TABLE IF EXISTS `assignments_task_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assignments_task_staff` (
  `task_id` bigint(20) NOT NULL,
  `staff_id` bigint(20) NOT NULL,
  PRIMARY KEY (`task_id`,`staff_id`),
  KEY `FK_assignments_task_staff` (`staff_id`),
  CONSTRAINT `FK_assignments_task_staff` FOREIGN KEY (`staff_id`) REFERENCES `staff` (`staff_id`),
  CONSTRAINT `FK_tasks_task` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignments_task_staff`
--

LOCK TABLES `assignments_task_staff` WRITE;
/*!40000 ALTER TABLE `assignments_task_staff` DISABLE KEYS */;
/*!40000 ALTER TABLE `assignments_task_staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `entry_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`auditlog_id`),
  KEY `FK_audit_logs_users` (`user_id`),
  KEY `FK_audit_logs_project_id` (`project_id`),
  KEY `FK_audit_logs_company_id` (`company_id`),
  CONSTRAINT `FK_audit_logs_company_id` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_audit_logs_project_id` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`),
  CONSTRAINT `FK_audit_logs_users` FOREIGN KEY (`user_id`) REFERENCES `system_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=982 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_logs`
--

LOCK TABLES `audit_logs` WRITE;
/*!40000 ALTER TABLE `audit_logs` DISABLE KEYS */;
INSERT INTO `audit_logs` VALUES (324,'2015-09-28 14:34:32','127.0.0.1',NULL,NULL,NULL,800,'',-1,NULL),(335,'2015-09-28 14:37:51','127.0.0.1',219,NULL,NULL,804,'',-1,NULL),(336,'2015-09-28 14:37:51','127.0.0.1',219,NULL,NULL,114,'project',-1,NULL),(338,'2015-09-28 14:37:53','127.0.0.1',219,NULL,NULL,114,'project',-1,NULL),(339,'2015-09-28 14:38:07','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(648,'2015-09-28 14:48:59','127.0.0.1',219,NULL,NULL,804,'',-1,NULL),(649,'2015-09-28 14:48:59','127.0.0.1',219,NULL,NULL,114,'project',-1,NULL),(650,'2015-09-28 14:49:09','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(651,'2015-09-28 14:49:15','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(652,'2015-09-28 14:49:19','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(653,'2015-09-28 14:49:24','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(654,'2015-09-28 14:49:43','127.0.0.1',219,NULL,NULL,114,'systemuser',-1,NULL),(655,'2015-09-28 14:49:48','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(656,'2015-09-28 14:49:59','127.0.0.1',219,NULL,NULL,100,'systemuser',226,'demo_admin1'),(657,'2015-09-28 14:49:59','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(658,'2015-09-28 14:49:59','127.0.0.1',219,NULL,NULL,110,'systemuser',226,'demo_admin1'),(766,'2015-09-28 14:58:19','127.0.0.1',219,NULL,NULL,804,'',-1,NULL),(767,'2015-09-28 14:58:19','127.0.0.1',219,NULL,NULL,114,'project',-1,NULL),(768,'2015-09-28 14:58:20','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(769,'2015-09-28 14:58:22','127.0.0.1',219,NULL,NULL,107,'company',9,NULL),(770,'2015-09-28 14:58:23','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(771,'2015-09-28 14:58:33','127.0.0.1',219,NULL,NULL,100,'company',10,NULL),(772,'2015-09-28 14:58:33','127.0.0.1',219,NULL,NULL,110,'company',10,NULL),(773,'2015-09-28 14:58:35','127.0.0.1',219,NULL,NULL,114,'systemuser',-1,NULL),(774,'2015-09-28 14:58:36','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(775,'2015-09-28 14:58:43','127.0.0.1',219,NULL,NULL,100,'systemuser',227,NULL),(776,'2015-09-28 14:58:43','127.0.0.1',219,NULL,NULL,114,'company',-1,NULL),(777,'2015-09-28 14:58:43','127.0.0.1',219,NULL,NULL,110,'systemuser',227,NULL),(778,'2015-09-28 14:58:49','127.0.0.1',227,10,NULL,804,'',-1,NULL),(779,'2015-09-28 14:58:49','127.0.0.1',227,10,NULL,114,'project',-1,NULL),(780,'2015-09-28 14:59:03','127.0.0.1',227,10,NULL,100,'project',30,NULL),(781,'2015-09-28 14:59:03','127.0.0.1',227,10,NULL,110,'project',30,NULL),(782,'2015-09-28 14:59:03','127.0.0.1',227,10,NULL,110,'projectAux',-1,NULL),(783,'2015-09-28 14:59:03','127.0.0.1',227,10,NULL,114,'project',30,NULL),(784,'2015-09-28 14:59:03','127.0.0.1',227,10,NULL,114,'cost',-1,NULL),(785,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'project',30,NULL),(786,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'estimationoutput',-1,NULL),(787,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'project',30,NULL),(788,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'staff',-1,NULL),(789,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'project',30,NULL),(790,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'payroll',-1,NULL),(791,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'project',30,NULL),(792,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'delivery',-1,NULL),(793,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'project',30,NULL),(794,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'material',-1,NULL),(795,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'project',30,NULL),(796,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'pullout',-1,NULL),(797,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'project',30,NULL),(798,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,114,'expense',-1,NULL),(799,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,112,'project',30,NULL),(800,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,112,'com.cebedo.pmsys.pojo.JSONTimelineGantt',-1,NULL),(801,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,112,'project',30,NULL),(802,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,112,'com.cebedo.pmsys.pojo.JSONCalendarEvent',-1,NULL),(803,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,113,'project',30,NULL),(804,'2015-09-28 14:59:04','127.0.0.1',227,10,NULL,113,'com.cebedo.pmsys.enums.TaskStatus',-1,NULL),(805,'2015-09-28 14:59:09','127.0.0.1',227,10,NULL,107,'project',30,NULL),(806,'2015-09-28 14:59:09','127.0.0.1',227,10,NULL,114,'project',-1,NULL),(807,'2015-09-28 15:00:59','127.0.0.1',227,10,NULL,100,'project',31,NULL),(808,'2015-09-28 15:00:59','127.0.0.1',227,10,NULL,110,'project',31,NULL),(809,'2015-09-28 15:00:59','127.0.0.1',227,10,NULL,110,'projectAux',-1,NULL),(810,'2015-09-28 15:00:59','127.0.0.1',227,10,NULL,114,'project',31,NULL),(811,'2015-09-28 15:00:59','127.0.0.1',227,10,NULL,114,'cost',-1,NULL),(812,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'project',31,NULL),(813,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'estimationoutput',-1,NULL),(814,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'project',31,NULL),(815,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'staff',-1,NULL),(816,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'project',31,NULL),(817,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'payroll',-1,NULL),(818,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'project',31,NULL),(819,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'delivery',-1,NULL),(820,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'project',31,NULL),(821,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'material',-1,NULL),(822,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'project',31,NULL),(823,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'pullout',-1,NULL),(824,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'project',31,NULL),(825,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,114,'expense',-1,NULL),(826,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,112,'project',31,NULL),(827,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,112,'com.cebedo.pmsys.pojo.JSONTimelineGantt',-1,NULL),(828,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,112,'project',31,NULL),(829,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,112,'com.cebedo.pmsys.pojo.JSONCalendarEvent',-1,NULL),(830,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,113,'project',31,NULL),(831,'2015-09-28 15:01:00','127.0.0.1',227,10,NULL,113,'com.cebedo.pmsys.enums.TaskStatus',-1,NULL),(832,'2015-09-28 15:01:04','127.0.0.1',227,10,NULL,107,'project',31,NULL),(833,'2015-09-28 15:01:04','127.0.0.1',227,10,NULL,114,'project',-1,NULL),(834,'2015-09-28 15:01:17','127.0.0.1',227,10,NULL,100,'project',32,NULL),(835,'2015-09-28 15:01:17','127.0.0.1',227,10,NULL,110,'project',32,NULL),(836,'2015-09-28 15:01:17','127.0.0.1',227,10,NULL,110,'projectAux',-1,NULL),(837,'2015-09-28 15:01:17','127.0.0.1',227,10,NULL,114,'project',32,NULL),(838,'2015-09-28 15:01:17','127.0.0.1',227,10,NULL,114,'cost',-1,NULL),(839,'2015-09-28 15:01:17','127.0.0.1',227,10,NULL,114,'project',32,NULL),(840,'2015-09-28 15:01:17','127.0.0.1',227,10,NULL,114,'estimationoutput',-1,NULL),(841,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'project',32,NULL),(842,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'staff',-1,NULL),(843,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'project',32,NULL),(844,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'payroll',-1,NULL),(845,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'project',32,NULL),(846,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'delivery',-1,NULL),(847,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'project',32,NULL),(848,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'material',-1,NULL),(849,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'project',32,NULL),(850,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'pullout',-1,NULL),(851,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'project',32,NULL),(852,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,114,'expense',-1,NULL),(853,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,112,'project',32,NULL),(854,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,112,'com.cebedo.pmsys.pojo.JSONTimelineGantt',-1,NULL),(855,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,112,'project',32,NULL),(856,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,112,'com.cebedo.pmsys.pojo.JSONCalendarEvent',-1,NULL),(857,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,113,'project',32,NULL),(858,'2015-09-28 15:01:18','127.0.0.1',227,10,NULL,113,'com.cebedo.pmsys.enums.TaskStatus',-1,NULL),(859,'2015-09-28 15:01:22','127.0.0.1',227,10,NULL,804,'',-1,NULL),(860,'2015-09-28 15:01:22','127.0.0.1',227,10,NULL,114,'project',-1,NULL),(861,'2015-09-28 15:01:24','127.0.0.1',227,10,NULL,114,'systemuser',-1,NULL),(862,'2015-09-28 15:01:33','127.0.0.1',227,10,NULL,100,'systemuser',228,NULL),(863,'2015-09-28 15:01:33','127.0.0.1',227,10,NULL,110,'systemuser',228,NULL),(864,'2015-09-28 15:01:38','127.0.0.1',228,10,NULL,804,'',-1,NULL),(865,'2015-09-28 15:01:38','127.0.0.1',228,10,NULL,114,'project',-1,NULL),(866,'2015-09-28 15:01:39','127.0.0.1',228,10,NULL,110,'project',32,NULL),(867,'2015-09-28 15:01:39','127.0.0.1',228,10,NULL,110,'projectAux',-1,NULL),(868,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'project',32,NULL),(869,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'cost',-1,NULL),(870,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'project',32,NULL),(871,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'estimationoutput',-1,NULL),(872,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'project',32,NULL),(873,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'staff',-1,NULL),(874,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'project',32,NULL),(875,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'payroll',-1,NULL),(876,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'project',32,NULL),(877,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'delivery',-1,NULL),(878,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'project',32,NULL),(879,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'material',-1,NULL),(880,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'project',32,NULL),(881,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'pullout',-1,NULL),(882,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'project',32,NULL),(883,'2015-09-28 15:01:40','127.0.0.1',228,10,NULL,114,'expense',-1,NULL),(884,'2015-09-28 15:01:41','127.0.0.1',228,10,NULL,112,'project',32,NULL),(885,'2015-09-28 15:01:41','127.0.0.1',228,10,NULL,112,'com.cebedo.pmsys.pojo.JSONTimelineGantt',-1,NULL),(886,'2015-09-28 15:01:41','127.0.0.1',228,10,NULL,112,'project',32,NULL),(887,'2015-09-28 15:01:41','127.0.0.1',228,10,NULL,112,'com.cebedo.pmsys.pojo.JSONCalendarEvent',-1,NULL),(888,'2015-09-28 15:01:41','127.0.0.1',228,10,NULL,113,'project',32,NULL),(889,'2015-09-28 15:01:41','127.0.0.1',228,10,NULL,113,'com.cebedo.pmsys.enums.TaskStatus',-1,NULL),(890,'2015-09-28 15:01:51','127.0.0.1',228,10,NULL,123,'project',32,NULL),(891,'2015-09-28 15:01:51','127.0.0.1',228,10,NULL,123,'org.springframework.web.multipart.MultipartFile',-1,NULL),(892,'2015-09-28 15:01:52','127.0.0.1',228,10,NULL,101,'task',-1,NULL),(893,'2015-09-28 15:01:52','127.0.0.1',228,10,NULL,101,'project',32,NULL),(894,'2015-09-28 15:01:52','127.0.0.1',228,10,NULL,101,'task',-1,NULL),(895,'2015-09-28 15:01:52','127.0.0.1',228,10,NULL,110,'project',32,NULL),(896,'2015-09-28 15:01:52','127.0.0.1',228,10,NULL,110,'projectAux',-1,NULL),(897,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'project',32,NULL),(898,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'cost',-1,NULL),(899,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'project',32,NULL),(900,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'estimationoutput',-1,NULL),(901,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'project',32,NULL),(902,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'staff',-1,NULL),(903,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'project',32,NULL),(904,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'payroll',-1,NULL),(905,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'project',32,NULL),(906,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'delivery',-1,NULL),(907,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'project',32,NULL),(908,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'material',-1,NULL),(909,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'project',32,NULL),(910,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'pullout',-1,NULL),(911,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'project',32,NULL),(912,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,114,'expense',-1,NULL),(913,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,112,'project',32,NULL),(914,'2015-09-28 15:01:53','127.0.0.1',228,10,NULL,112,'com.cebedo.pmsys.pojo.JSONTimelineGantt',-1,NULL),(915,'2015-09-28 15:01:54','127.0.0.1',228,10,NULL,112,'project',32,NULL),(916,'2015-09-28 15:01:54','127.0.0.1',228,10,NULL,112,'com.cebedo.pmsys.pojo.JSONCalendarEvent',-1,NULL),(917,'2015-09-28 15:01:54','127.0.0.1',228,10,NULL,113,'project',32,NULL),(918,'2015-09-28 15:01:54','127.0.0.1',228,10,NULL,113,'com.cebedo.pmsys.enums.TaskStatus',-1,NULL),(919,'2015-09-28 15:01:59','127.0.0.1',228,10,NULL,110,'task',85,NULL),(920,'2015-09-28 15:02:02','127.0.0.1',228,10,NULL,105,'task',85,NULL),(921,'2015-09-28 15:02:02','127.0.0.1',228,10,NULL,110,'task',85,NULL),(922,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,110,'project',32,NULL),(923,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,110,'projectAux',-1,NULL),(924,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'project',32,NULL),(925,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'cost',-1,NULL),(926,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'project',32,NULL),(927,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'estimationoutput',-1,NULL),(928,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'project',32,NULL),(929,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'staff',-1,NULL),(930,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'project',32,NULL),(931,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'payroll',-1,NULL),(932,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'project',32,NULL),(933,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'delivery',-1,NULL),(934,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'project',32,NULL),(935,'2015-09-28 15:02:04','127.0.0.1',228,10,NULL,114,'material',-1,NULL),(936,'2015-09-28 15:02:05','127.0.0.1',228,10,NULL,114,'project',32,NULL),(937,'2015-09-28 15:02:05','127.0.0.1',228,10,NULL,114,'pullout',-1,NULL),(938,'2015-09-28 15:02:05','127.0.0.1',228,10,NULL,114,'project',32,NULL),(939,'2015-09-28 15:02:05','127.0.0.1',228,10,NULL,114,'expense',-1,NULL),(940,'2015-09-28 15:02:05','127.0.0.1',228,10,NULL,112,'project',32,NULL),(941,'2015-09-28 15:02:05','127.0.0.1',228,10,NULL,112,'com.cebedo.pmsys.pojo.JSONTimelineGantt',-1,NULL),(942,'2015-09-28 15:02:05','127.0.0.1',228,10,NULL,112,'project',32,NULL),(943,'2015-09-28 15:02:05','127.0.0.1',228,10,NULL,112,'com.cebedo.pmsys.pojo.JSONCalendarEvent',-1,NULL),(944,'2015-09-28 15:02:05','127.0.0.1',228,10,NULL,113,'project',32,NULL),(945,'2015-09-28 15:02:05','127.0.0.1',228,10,NULL,113,'com.cebedo.pmsys.enums.TaskStatus',-1,NULL),(946,'2015-09-28 15:02:16','127.0.0.1',228,10,NULL,123,'company',10,NULL),(947,'2015-09-28 15:02:16','127.0.0.1',228,10,NULL,123,'org.springframework.web.multipart.MultipartFile',-1,NULL),(948,'2015-09-28 15:02:16','127.0.0.1',228,10,NULL,100,'staff',74,NULL),(949,'2015-09-28 15:02:16','127.0.0.1',228,10,NULL,100,'staff',75,NULL),(950,'2015-09-28 15:02:17','127.0.0.1',228,10,NULL,100,'staff',76,NULL),(951,'2015-09-28 15:02:17','127.0.0.1',228,10,NULL,100,'staff',77,NULL),(952,'2015-09-28 15:02:17','127.0.0.1',228,10,NULL,100,'staff',78,NULL),(953,'2015-09-28 15:02:17','127.0.0.1',228,10,NULL,100,'staff',79,NULL),(954,'2015-09-28 15:02:17','127.0.0.1',228,10,NULL,114,'company',10,NULL),(955,'2015-09-28 15:02:17','127.0.0.1',228,10,NULL,114,'staff',-1,NULL),(956,'2015-09-28 15:02:17','127.0.0.1',228,10,NULL,101,'project',32,NULL),(957,'2015-09-28 15:02:17','127.0.0.1',228,10,NULL,101,'staff',-1,NULL),(958,'2015-09-28 15:02:17','127.0.0.1',228,10,NULL,110,'project',32,NULL),(959,'2015-09-28 15:02:17','127.0.0.1',228,10,NULL,110,'projectAux',-1,NULL),(960,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'project',32,NULL),(961,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'cost',-1,NULL),(962,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'project',32,NULL),(963,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'estimationoutput',-1,NULL),(964,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'project',32,NULL),(965,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'staff',-1,NULL),(966,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'project',32,NULL),(967,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'payroll',-1,NULL),(968,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'project',32,NULL),(969,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'delivery',-1,NULL),(970,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'project',32,NULL),(971,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'material',-1,NULL),(972,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'project',32,NULL),(973,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'pullout',-1,NULL),(974,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'project',32,NULL),(975,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,114,'expense',-1,NULL),(976,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,112,'project',32,NULL),(977,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,112,'com.cebedo.pmsys.pojo.JSONTimelineGantt',-1,NULL),(978,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,112,'project',32,NULL),(979,'2015-09-28 15:02:18','127.0.0.1',228,10,NULL,112,'com.cebedo.pmsys.pojo.JSONCalendarEvent',-1,NULL),(980,'2015-09-28 15:02:19','127.0.0.1',228,10,NULL,113,'project',32,NULL),(981,'2015-09-28 15:02:19','127.0.0.1',228,10,NULL,113,'com.cebedo.pmsys.enums.TaskStatus',-1,NULL);
/*!40000 ALTER TABLE `audit_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `companies` (
  `company_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `date_started` datetime NOT NULL,
  `date_expiration` datetime NOT NULL,
  `beta_tester` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies`
--

LOCK TABLES `companies` WRITE;
/*!40000 ALTER TABLE `companies` DISABLE KEYS */;
INSERT INTO `companies` VALUES (10,'Demo 1','','2015-09-01 00:00:00','2016-01-07 00:00:00',1);
/*!40000 ALTER TABLE `companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fields`
--

DROP TABLE IF EXISTS `fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fields` (
  `field_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`field_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fields`
--

LOCK TABLES `fields` WRITE;
/*!40000 ALTER TABLE `fields` DISABLE KEYS */;
INSERT INTO `fields` VALUES (1,'Textfield');
/*!40000 ALTER TABLE `fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projects`
--

DROP TABLE IF EXISTS `projects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projects`
--

LOCK TABLES `projects` WRITE;
/*!40000 ALTER TABLE `projects` DISABLE KEYS */;
INSERT INTO `projects` VALUES (32,'Demo1',0,'','',10,123123,'2015-09-01','2015-10-01',NULL);
/*!40000 ALTER TABLE `projects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (72,'','','','','',NULL,NULL,NULL,10,0),(73,'','','','','',NULL,NULL,NULL,10,0),(74,'','Vic','Castillo','Cebedo','II','Test','cebedo.vii@gmail.com','9226110429',10,500),(75,'','Smith','Johnson','Williams','','Test','cebedo.vii@gmail.com','9226110429',10,400),(76,'','Williams','Smith','Johnson','','Test','cebedo.vii@gmail.com','9226110429',10,300),(77,'','Johnson','Smith','Williams','','Test','cebedo.vii@gmail.com','9226110429',10,350),(78,'Engr.','Bob','Marley','Lannister','','Test','cebedo.vii@gmail.com','9226110429',10,350),(79,'Dr.','Erlang','Churva','Looo','III','Test','cebedo.vii@gmail.com','9226110429',10,350);
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_configuration`
--

DROP TABLE IF EXISTS `system_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_configuration`
--

LOCK TABLES `system_configuration` WRITE;
/*!40000 ALTER TABLE `system_configuration` DISABLE KEYS */;
INSERT INTO `system_configuration` VALUES (2,'ROOT_INIT','1',NULL);
/*!40000 ALTER TABLE `system_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_users`
--

DROP TABLE IF EXISTS `system_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_users`
--

LOCK TABLES `system_users` WRITE;
/*!40000 ALTER TABLE `system_users` DISABLE KEYS */;
INSERT INTO `system_users` VALUES (219,'root_098','4ca3c78a6db368685a6a1a67045de747',1,NULL,NULL,0,0),(227,'demo_admin1','54044c4ed6c192034e51acfaaf48d948',0,10,72,1,0),(228,'standard_user1','1f27cd46ff32ac1069d0410ddfeca481',0,10,73,0,0);
/*!40000 ALTER TABLE `system_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (84,'Task 1','Test 4','2015-01-10','2015-01-10',3,5,32,2,10),(85,'Task 2','','2015-01-13','2015-01-15',2,3,32,2,10),(86,'Task 3','Test 6','2015-01-15','2015-01-18',6,5,32,2,10),(87,'Task 4','Test 7','2015-01-21','2015-01-23',10,8,32,2,10),(88,'Task 5','Test 8','2015-01-31','2015-01-31',3,4,32,2,10),(89,'Task 5','Test 8','2015-02-03','2015-02-04',3,5,32,2,10);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-09-28 16:23:18
