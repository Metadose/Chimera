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
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (62,'Engr.','John2','Dane','Doe','Jr.','Snr. Management Staff','test@gmail.com','0922 611 0429',4,350),(63,'','Anna','Adaza','de la Cruz','','QA','test@gmail.com','0922 611 0429',4,500),(64,'1','121','212','2','12','21','test@gmail.com','0922 611 0429',4,1000),(65,'QA','QA','QA','QA','QA','QA','test@gmail.com','0922 611 0429',4,400),(66,'man','man','man','man','man','manpower getter','test@gmail.com','0922 611 0429',4,600),(67,'w','w','w','w','w','w','test@gmail.com','0922 611 0429',4,600),(68,'kk','kk','kk','k','k','k','test@gmail.com','0922 611 0429',4,450),(79,'','Vic','Castillo','Cebedo','II','Test','cebedo.vii@gmail.com','9.226110429E9',4,500),(80,'','Smith','Johnson','Williams','','Test','cebedo.vii@gmail.com','9.226110429E9',4,400),(81,'','Williams','Smith','Johnson','','Test',NULL,NULL,4,300),(82,'','Johnson','Smith','Williams','','Test',NULL,NULL,4,350),(89,'Engr.','Bob','Marley','Lannister','','Test','cebedo.vii@gmail.com','9.226110429E9',4,350),(90,'Dr.','Erlang','Churva','Looo','III','Test','cebedo.vii@gmail.com','9.226110429E9',4,350),(91,'-1','','','','','','','',4,0),(92,'','','','','',NULL,NULL,NULL,4,0),(93,'qq','qqq','qqq','qq','','','','',4,0),(94,'','111rr','','','','','','',4,0),(95,'','','','','',NULL,NULL,NULL,4,0),(96,'','','','','',NULL,NULL,NULL,4,0),(97,'','','','','',NULL,NULL,NULL,4,0),(98,'','','','','',NULL,NULL,NULL,4,0),(99,'','','','','',NULL,NULL,NULL,4,0),(100,'','','','','',NULL,NULL,NULL,4,0),(101,'','','','','',NULL,NULL,NULL,4,0),(102,'','','','','',NULL,NULL,NULL,4,0),(103,'','','','','',NULL,NULL,NULL,4,0),(104,'','','','','',NULL,NULL,NULL,4,0),(105,'','','','','',NULL,NULL,NULL,4,0);
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

-- Dump completed on 2015-08-19 15:27:08
