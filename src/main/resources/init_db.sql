CREATE SCHEMA IF NOT EXISTS `zlagoda` DEFAULT CHARACTER SET utf8;
USE `zlagoda`;
SET NAMES utf8mb4;

DROP TABLE IF EXISTS `Sale`;
DROP TABLE IF EXISTS `Check`;
DROP TABLE IF EXISTS `Store_Product`;
DROP TABLE IF EXISTS `Customer_Card`;
DROP TABLE IF EXISTS `Product`;
DROP TABLE IF EXISTS `Category`;
DROP TABLE IF EXISTS `Employee`;

-- ----------------------------
-- Table structure for employees
-- ----------------------------
CREATE TABLE `Employee`  (
                             `id_employee` varchar(10) NOT NULL,
                             `empl_surname` varchar(50) NOT NULL,
                             `empl_name` varchar(50) NOT NULL,
                             `empl_patronymic` varchar(50) ,
                             `empl_role` varchar(10) NOT NULL,
                             `salary` decimal(13, 4) UNSIGNED NOT NULL,
                             `date_of_birth` date NOT NULL,
                             `date_of_start` date NOT NULL,
                             `phone_number` varchar(13) NOT NULL,
                             `city` varchar(50) NOT NULL,
                             `street` varchar(50) NOT NULL,
                             `zip_code` varchar(9) NOT NULL,
                             PRIMARY KEY (`id_employee`)
);

-- ----------------------------
-- Table structure for categories
-- ----------------------------
CREATE TABLE `Category` (
                            `category_number` int NOT NULL AUTO_INCREMENT,
                            `category_name` varchar(50) NOT NULL,
                            PRIMARY KEY (`category_number`)
);

-- ----------------------------
-- Table structure for products
-- ----------------------------
CREATE TABLE `Product`  (
                            `id_product` int NOT NULL AUTO_INCREMENT,
                            `category_number` int NOT NULL,
                            `product_name` varchar(50) NOT NULL,
                            `characteristics` varchar(100) NOT NULL,
                            CONSTRAINT fk_category_number
                                FOREIGN KEY (`category_number`)
                                    REFERENCES Category (`category_number`)
                                    ON UPDATE CASCADE
                                    ON DELETE NO ACTION,
                            PRIMARY KEY (`id_product`)
);

-- ----------------------------
-- Table structure for customer cards
-- ----------------------------
CREATE TABLE `Customer_Card`  (
                                  `card_number` varchar(13) NOT NULL,
                                  `cust_surname` varchar(50) NOT NULL,
                                  `cust_name` varchar(50) NOT NULL,
                                  `cust_patronymic` varchar(50),
                                  `phone_number` varchar(13) NOT NULL,
                                  `city` varchar(50),
                                  `street` varchar(50),
                                  `zip_code` varchar(9),
                                  `percent` int NOT NULL,
                                  PRIMARY KEY (`card_number`)
);

-- ----------------------------
-- Table structure for checks
-- ----------------------------
CREATE TABLE `Check`  (
                          `check_number` varchar(10) NOT NULL,
                          `id_employee` varchar(10) NOT NULL,
                          `card_number` varchar(13),
                          `print_date` datetime NOT NULL,
                          `sum_total` decimal(13, 4) NOT NULL,
                          `vat` decimal(13, 4) NOT NULL,
                          CONSTRAINT fk_id_employee
                              FOREIGN KEY (`id_employee`)
                                  REFERENCES Employee (`id_employee`)
                                  ON UPDATE CASCADE
                                  ON DELETE NO ACTION,
                          CONSTRAINT fk_card_number
                              FOREIGN KEY (`card_number`)
                                  REFERENCES Customer_Card (`card_number`)
                                  ON UPDATE CASCADE
                                  ON DELETE NO ACTION,
                          PRIMARY KEY (`check_number`)
);

-- ----------------------------
-- Table structure for store products
-- ----------------------------
CREATE TABLE `Store_Product`  (
                                  `UPC` varchar(12) NOT NULL,
                                  `UPC_prom` varchar(12),
                                  `id_product` int NOT NULL,
                                  `selling_price` decimal(13, 4) NOT NULL,
                                  `products_number` int NOT NULL,
                                  `promotional_product` boolean NOT NULL,
                                  CONSTRAINT `fk_UPC_prom`
                                      FOREIGN KEY (`UPC_prom`)
                                          REFERENCES `Store_Product` (`UPC`)
                                          ON UPDATE CASCADE
                                          ON DELETE SET NULL,
                                  CONSTRAINT fk_id_product
                                      FOREIGN KEY (`id_product`)
                                          REFERENCES Product (`id_product`)
                                          ON UPDATE CASCADE
                                          ON DELETE NO ACTION,
                                  PRIMARY KEY (`UPC`)
);

-- ----------------------------
-- Table structure for sales
-- ----------------------------
CREATE TABLE `Sale`  (
                         `UPC` varchar(12) NOT NULL,
                         `check_number` varchar(10) NOT NULL,
                         `product_number` int NOT NULL,
                         `selling_price` DECIMAL(13, 4) NOT NULL,
                         PRIMARY KEY (`UPC`, `check_number`),
                         CONSTRAINT fk_UPC
                             FOREIGN KEY (`UPC`)
                                 REFERENCES Store_Product (`UPC`)
                                 ON UPDATE CASCADE
                                 ON DELETE NO ACTION,
                         CONSTRAINT fk_check_number
                             FOREIGN KEY (`check_number`)
                                 REFERENCES `Check` (`check_number`)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE
);
