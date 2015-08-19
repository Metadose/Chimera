CREATE DATABASE  IF NOT EXISTS `test` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `test`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: test
-- ------------------------------------------------------
-- Server version	5.6.24

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
  `duration` double NOT NULL DEFAULT '0',
  `project_id` bigint(20) DEFAULT NULL,
  `status` int(2) NOT NULL DEFAULT '0',
  `company_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  KEY `FK_tasks_project` (`project_id`),
  KEY `FK_tasks_company` (`company_id`),
  CONSTRAINT `FK_tasks_company` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`),
  CONSTRAINT `FK_tasks_project` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (1,'Task 1','Test','2015-01-10',20,NULL,1,4),(2,'Task 2','Test','2015-01-30',5,NULL,0,4),(3,'Task 3','Test','2015-02-04',15,NULL,0,4),(4,'Task 4','Test','2015-02-19',15,NULL,0,4),(5,'Task 5','Test','2015-03-06',30,NULL,0,4),(7,'Task 1','Test','2015-01-10',20,NULL,0,4),(8,'Task 2','Test','2015-01-30',5,NULL,0,4),(9,'Task 3','Test','2015-02-04',15,NULL,0,4),(10,'Task 4','Test','2015-02-19',15,NULL,0,4),(11,'Task 5','Test','2015-03-06',30,NULL,0,4),(22,'Task 1','Test','2015-01-10',20,150,0,4),(23,'Task 2','Test','2015-01-30',5,150,0,4),(24,'Task 3','Test','2015-02-04',15,150,0,4),(25,'Task 4','Test','2015-02-19',15,150,0,4),(26,'Task 5','Test','2015-03-06',30,150,0,4),(27,'Task 1','Test','2015-01-10',20,148,0,4),(28,'Task 2','Test','2015-01-30',5,148,0,4),(29,'Task 3','Test','2015-02-04',15,148,0,4),(30,'Task 4','Test','2015-02-19',15,148,0,4),(31,'Task 5','Test','2015-03-06',30,148,0,4);
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

-- Dump completed on 2015-08-19 15:27:07
