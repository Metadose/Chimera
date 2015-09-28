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
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (39,'','Company','','Admin','','Demo 1 Company Admin','vcebedoii@icloud.com','09226110429',7,500),(40,'','Victorio','Castillo','Cebedo','II','Standard User 1','vcebedoii@icloud.com','09226110429',7,350),(41,'','Smith','Johnson','Williams','','Test','cebedo.vii@gmail.com','9226110429',7,400),(42,'','Williams','Smith','Johnson','','Test','cebedo.vii@gmail.com','9226110429',7,300),(43,'','Johnson','Smith','Williams','','Test','cebedo.vii@gmail.com','9226110429',7,350),(44,'Engr.','Bob','Marley','Lannister','','Test','cebedo.vii@gmail.com','9226110429',7,350),(45,'Dr.','Erlang','Churva','Looo','III','Test','cebedo.vii@gmail.com','9226110429',7,350),(46,'','','','','',NULL,NULL,NULL,8,0),(47,'','Vic','Castillo','Cebedo','II','Standard User 1','vcebedoii@icloud.com','09226110429',8,350),(48,'','Smith','Johnson','Williams','','Test','cebedo.vii@gmail.com','9226110429',8,400),(49,'','Williams','Smith','Johnson','','Test','cebedo.vii@gmail.com','9226110429',8,300),(50,'','Johnson','Smith','Williams','','Test','cebedo.vii@gmail.com','9226110429',8,350),(51,'Engr.','Bob','Marley','Lannister','','Test','cebedo.vii@gmail.com','9226110429',8,350),(52,'Dr.','Erlang','Churva','Looo','III','Test','cebedo.vii@gmail.com','9226110429',8,350),(53,'','Vic','Castillo','Cebedo','II','Test','cebedo.vii@gmail.com','9226110429',7,500),(54,'','Smith','Johnson','Williams','','Test','cebedo.vii@gmail.com','9226110429',7,400),(55,'','Williams','Smith','Johnson','','Test','cebedo.vii@gmail.com','9226110429',7,300),(56,'','Johnson','Smith','Williams','','Test','cebedo.vii@gmail.com','9226110429',7,350),(57,'Engr.','Bob','Marley','Lannister','','Test','cebedo.vii@gmail.com','9226110429',7,350),(58,'Dr.','Erlang','Churva','Looo','III','Test','cebedo.vii@gmail.com','9226110429',7,350),(59,'','Vic','Castillo','Cebedo','II','Test','cebedo.vii@gmail.com','9226110429',7,500),(60,'','Smith','Johnson','Williams','','Test','cebedo.vii@gmail.com','9226110429',7,400),(61,'','Williams','Smith','Johnson','','Test','cebedo.vii@gmail.com','9226110429',7,300),(62,'','Johnson','Smith','Williams','','Test','cebedo.vii@gmail.com','9226110429',7,350),(63,'Engr.','Bob','Marley','Lannister','','Test','cebedo.vii@gmail.com','9226110429',7,350),(64,'Dr.','Erlang','Churva','Looo','III','Test','cebedo.vii@gmail.com','9226110429',7,350),(65,'','Vic','Castillo','Cebedo','II','Test','cebedo.vii@gmail.com','9226110429',7,500),(66,'','Smith','Johnson','Williams','','Test','cebedo.vii@gmail.com','9226110429',7,400),(67,'','Williams','Smith','Johnson','','Test','cebedo.vii@gmail.com','9226110429',7,300),(68,'','Johnson1','Smith','Williams','','Test','cebedo.vii@gmail.com','9226110429',7,350),(69,'Engr.','Bob','Marley','Lannister','','Test','cebedo.vii@gmail.com','9226110429',7,350),(70,'Dr.','Erlang','Churva','Looo','III','Test','cebedo.vii@gmail.com','9226110429',7,350);
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-09-28  9:25:43
