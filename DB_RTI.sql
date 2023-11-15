-- --------------------------------------------------------
-- Hôte:                         192.168.1.19
-- Version du serveur:           8.0.21 - Source distribution
-- SE du serveur:                Linux
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Listage de la structure de la base pour PourStudent
CREATE DATABASE IF NOT EXISTS `PourStudent` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `PourStudent`;

-- Listage de la structure de la table PourStudent. accounts
CREATE TABLE IF NOT EXISTS `accounts` (
  `login` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Listage des données de la table PourStudent.accounts : ~4 rows (environ)
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` (`login`, `password`) VALUES
	('Lucas', '123'),
	('lulu', '123'),
	('th', 'th'),
	('Thomas', '123');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;

-- Listage de la structure de la table PourStudent. articles
CREATE TABLE IF NOT EXISTS `articles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `intitule` varchar(20) DEFAULT NULL,
  `prix` float DEFAULT NULL,
  `stock` int DEFAULT NULL,
  `image` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- Listage des données de la table PourStudent.articles : ~21 rows (environ)
/*!40000 ALTER TABLE `articles` DISABLE KEYS */;
INSERT INTO `articles` (`id`, `intitule`, `prix`, `stock`, `image`) VALUES
	(1, 'carottes', 2.16, 29, 'carottes.jpg'),
	(2, 'cerises', 9.75, 12, 'cerises.jpg'),
	(3, 'artichaut', 1.62, 10, 'artichaut.jpg'),
	(4, 'bananes', 2.6, 0, 'bananes.jpg'),
	(5, 'champignons', 10.25, 17, 'champignons.jpg'),
	(6, 'concombre', 1.17, 2, 'concombre.jpg'),
	(7, 'courgette', 1.17, 10, 'courgette.jpg'),
	(8, 'haricots', 10.82, 50, 'haricots.jpg'),
	(9, 'laitue', 1.62, 50, 'laitue.jpg'),
	(10, 'oranges', 3.78, 200, 'oranges.jpg'),
	(11, 'oignons', 2.12, 25, 'oignons.jpg'),
	(12, 'nectarines', 10.38, 25, 'nectarines.jpg'),
	(13, 'peches', 8.48, 25, 'peches.jpg'),
	(14, 'poivron', 1.29, 165, 'poivron.jpg'),
	(15, 'pommes de terre', 2.17, 25, 'pommesDeTerre.jpg'),
	(16, 'pommes', 4, 25, 'pommes.jpg'),
	(17, 'citrons', 4.44, 981, 'citrons.jpg'),
	(18, 'ail', 1.08, 25, 'ail.jpg'),
	(19, 'aubergine', 1.62, 25, 'aubergine.jpg'),
	(20, 'echalotes', 6.48, 25, 'echalotes.jpg'),
	(21, 'tomates', 5.49, 22, 'tomates.jpg');
/*!40000 ALTER TABLE `articles` ENABLE KEYS */;

-- Listage de la structure de la table PourStudent. employees
CREATE TABLE IF NOT EXISTS `employees` (
  `login` varchar(100) NOT NULL,
  `password` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Listage des données de la table PourStudent.employees : ~1 rows (environ)
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` (`login`, `password`) VALUES
	('Thomas', '123');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;

-- Listage de la structure de la table PourStudent. factures
CREATE TABLE IF NOT EXISTS `factures` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idClient` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `montant` float NOT NULL DEFAULT '0',
  `payé` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_factures_accounts` (`idClient`),
  CONSTRAINT `FK_factures_accounts` FOREIGN KEY (`idClient`) REFERENCES `accounts` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- Listage des données de la table PourStudent.factures : ~16 rows (environ)
/*!40000 ALTER TABLE `factures` DISABLE KEYS */;
INSERT INTO `factures` (`id`, `idClient`, `date`, `montant`, `payé`) VALUES
	(1, 'th', '2023-10-12 02:40:57', 48, 0),
	(2, 'th', '2023-10-12 02:44:28', 58, 0),
	(3, 'th', '2023-10-12 02:48:03', 14, 0),
	(4, 'th', '2023-10-12 02:51:32', 160, 0),
	(5, 'th', '2023-10-12 02:53:28', 107, 0),
	(6, 'lulu', '2023-10-12 02:57:29', 154, 0),
	(7, 'lulu', '2023-10-12 11:36:35', 76, 0),
	(8, 'Thomas', '2023-11-06 22:08:19', 95, 1),
	(9, 'Thomas', '2023-11-06 22:11:56', 195, 1),
	(10, 'Thomas', '2023-11-06 22:14:39', 11, 1),
	(11, 'Thomas', '2023-11-06 22:28:51', 2, 1),
	(12, 'Thomas', '2023-11-06 22:30:37', 2.16, 0),
	(13, 'Thomas', '2023-11-06 22:32:47', 6291.48, 1),
	(14, 'Thomas', '2023-11-09 12:20:41', 0, 0),
	(15, 'Thomas', '2023-11-09 13:02:30', 20.5, 0),
	(16, 'Lucas', '2023-11-15 03:10:42', 209.07, 0);
/*!40000 ALTER TABLE `factures` ENABLE KEYS */;

-- Listage de la structure de la table PourStudent. ventes
CREATE TABLE IF NOT EXISTS `ventes` (
  `idFacture` int NOT NULL,
  `idArticle` int NOT NULL,
  `quantité` int DEFAULT NULL,
  PRIMARY KEY (`idFacture`,`idArticle`),
  KEY `FK_ventes_articles` (`idArticle`),
  CONSTRAINT `FK_ventes_articles` FOREIGN KEY (`idArticle`) REFERENCES `articles` (`id`),
  CONSTRAINT `FK_ventes_factures` FOREIGN KEY (`idFacture`) REFERENCES `factures` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Listage des données de la table PourStudent.ventes : ~43 rows (environ)
/*!40000 ALTER TABLE `ventes` DISABLE KEYS */;
INSERT INTO `ventes` (`idFacture`, `idArticle`, `quantité`) VALUES
	(1, 3, 30),
	(2, 2, 6),
	(3, 3, 9),
	(4, 2, 15),
	(4, 3, 1),
	(4, 4, 5),
	(5, 2, 1),
	(5, 13, 4),
	(5, 15, 21),
	(5, 17, 2),
	(5, 20, 2),
	(6, 12, 2),
	(6, 14, 3),
	(6, 19, 7),
	(6, 21, 22),
	(7, 1, 2),
	(7, 8, 5),
	(7, 14, 2),
	(7, 21, 3),
	(8, 3, 5),
	(8, 5, 5),
	(8, 6, 5),
	(8, 7, 5),
	(8, 9, 5),
	(8, 10, 5),
	(9, 4, 7),
	(9, 5, 1),
	(9, 6, 1),
	(9, 7, 1),
	(9, 8, 13),
	(9, 9, 16),
	(10, 1, 1),
	(10, 2, 1),
	(11, 1, 1),
	(12, 1, 1),
	(13, 17, 1417),
	(15, 5, 2),
	(16, 1, 18),
	(16, 2, 9),
	(16, 3, 9),
	(16, 4, 18),
	(16, 6, 9),
	(16, 7, 9);
/*!40000 ALTER TABLE `ventes` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
