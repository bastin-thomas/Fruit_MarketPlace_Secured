-- --------------------------------------------------------
-- Hôte:                         192.168.1.21
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

-- Listage des données de la table PourStudent.accounts : ~2 rows (environ)
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` (`login`, `password`) VALUES
	('laure', '123'),
	('Lucas', '123'),
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
	(1, 'carottes', 2.28, 125, 'carottes.jpg'),
	(2, 'cerises', 9.76, 100, 'cerises.jpg'),
	(3, 'artichaut', 1.62, 150, 'artichaut.jpg'),
	(4, 'bananes', 2.6, 1155, 'bananes.jpg'),
	(5, 'champignons', 10.25, 48, 'champignons.jpg'),
	(6, 'concombre', 1.17, 156, 'concombre.jpg'),
	(7, 'courgette', 1.18, 55, 'courgette.jpg'),
	(8, 'haricots', 0.12, 50, 'haricots.jpg'),
	(9, 'laitue', 1.62, 75, 'laitue.jpg'),
	(10, 'oranges', 3.78, 104, 'oranges.jpg'),
	(11, 'oignons', 2.12, 105, 'oignons.jpg'),
	(12, 'nectarines', 10.38, 91, 'nectarines.jpg'),
	(13, 'peches', 8.48, 94, 'peches.jpg'),
	(14, 'poivron', 1.29, 161, 'poivron.jpg'),
	(15, 'pommes de terre', 2.17, 100, 'pommesDeTerre.jpg'),
	(16, 'pommes', 4, 100, 'pommes.jpg'),
	(17, 'citrons', 4.44, 971, 'citrons.jpg'),
	(18, 'ail', 1.08, 95, 'ail.jpg'),
	(19, 'aubergine', 1.62, 95, 'aubergine.jpg'),
	(20, 'echalotes', 6.48, 95, 'echalotes.jpg'),
	(21, 'tomates', 7.63, 240, 'tomates.jpg');
/*!40000 ALTER TABLE `articles` ENABLE KEYS */;

-- Listage de la structure de la table PourStudent. employees
CREATE TABLE IF NOT EXISTS `employees` (
  `login` varchar(100) NOT NULL,
  `password` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Listage des données de la table PourStudent.employees : ~0 rows (environ)
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` (`login`, `password`) VALUES
	('Lucas', '123'),
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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- Listage des données de la table PourStudent.factures : ~17 rows (environ)
/*!40000 ALTER TABLE `factures` DISABLE KEYS */;
INSERT INTO `factures` (`id`, `idClient`, `date`, `montant`, `payé`) VALUES
	(8, 'Thomas', '2023-11-06 22:08:19', 95, 1),
	(9, 'Thomas', '2023-11-06 22:11:56', 195, 1),
	(10, 'Thomas', '2023-11-06 22:14:39', 11, 1),
	(11, 'Thomas', '2023-11-06 22:28:51', 2, 1),
	(12, 'Thomas', '2023-11-06 22:30:37', 2.16, 1),
	(13, 'Thomas', '2023-11-06 22:32:47', 6291.48, 1),
	(15, 'Thomas', '2023-11-09 13:02:30', 20.5, 0),
	(16, 'Lucas', '2023-11-15 03:10:42', 209.07, 1),
	(17, 'Lucas', '2023-11-16 11:37:16', 109, 1),
	(18, 'Lucas', '2023-11-16 11:49:44', 17, 1),
	(19, 'Lucas', '2023-11-16 11:50:41', 12, 0),
	(20, 'laure', '2023-12-28 21:53:23', 51, 1),
	(21, 'laure', '2023-12-28 21:55:04', 21, 0),
	(22, 'Thomas', '2023-12-29 02:31:54', 4401, 0),
	(23, 'Thomas', '2023-12-29 02:34:53', 71, 0),
	(24, 'Thomas', '2023-12-29 02:36:55', 400, 0),
	(25, 'Thomas', '2023-12-29 02:37:24', 76, 0),
	(26, 'Thomas', '2023-12-29 02:55:15', 344, 0);
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

-- Listage des données de la table PourStudent.ventes : ~56 rows (environ)
/*!40000 ALTER TABLE `ventes` DISABLE KEYS */;
INSERT INTO `ventes` (`idFacture`, `idArticle`, `quantité`) VALUES
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
	(16, 7, 9),
	(17, 1, 14),
	(17, 2, 7),
	(17, 3, 7),
	(18, 1, 8),
	(19, 1, 6),
	(20, 5, 5),
	(21, 8, 1),
	(21, 10, 3),
	(22, 1, 1205),
	(22, 2, 129),
	(22, 3, 156),
	(22, 4, 10),
	(22, 5, 10),
	(22, 6, 10),
	(22, 7, 10),
	(22, 8, 10),
	(22, 9, 10),
	(23, 4, 3),
	(23, 5, 5),
	(23, 6, 5),
	(23, 7, 5),
	(23, 8, 7),
	(23, 9, 2),
	(24, 4, 7),
	(24, 5, 21),
	(24, 8, 21),
	(24, 10, 21),
	(24, 11, 7),
	(24, 12, 7),
	(25, 4, 18),
	(25, 5, 3),
	(25, 8, 6),
	(26, 1, 25),
	(26, 4, 50),
	(26, 6, 50),
	(26, 7, 50),
	(26, 9, 25);
/*!40000 ALTER TABLE `ventes` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
