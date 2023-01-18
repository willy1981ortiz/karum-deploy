-- MySQL Workbench Forward Engineering

-- -----------------------------------------------------
-- Schema karum_card_web_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema karum_card_web_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `karum_card_web_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci ;
USE `karum_card_web_db` ;

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user` (
    `user_id` INT NOT NULL AUTO_INCREMENT,
    `mobile_number` VARCHAR(45) NOT NULL,
    `user_auth` TEXT NULL,
    `status` FLOAT NOT NULL DEFAULT 0.0,
    `curp` TEXT NULL,
    `ine` TEXT NULL,
    `confirmation_code` VARCHAR(45) NULL,
    `is_handd_off` TINYINT NULL DEFAULT 0,
    `product_id` VARCHAR(65) NULL,
    `price` VARCHAR(65) NULL,
    `user_timestamp` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`),
    UNIQUE INDEX `user_mobile_number` (`mobile_number` ASC))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tc41`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tc41` (
    `name` VARCHAR(45) NOT NULL,
    `parent_surname` VARCHAR(45) NOT NULL,
    `mother_surname` VARCHAR(45) NOT NULL,
    `year` INT NULL,
    `month` INT NULL,
    `day` INT NULL,
    `curp_no` TEXT NOT NULL,
    `ine` TEXT NOT NULL,
    `street` TEXT NULL,
    `ext_no` VARCHAR(5) NULL,
    `int_no` VARCHAR(5) NULL,
    `zip_code` VARCHAR(45) NULL,
    `state` VARCHAR(45) NULL,
    `state_code` VARCHAR(45) NULL,
    `municipality` VARCHAR(45) NULL,
    `municipality_code` VARCHAR(45) NULL,
    `colony` VARCHAR(45) NULL,
    `colony_code` VARCHAR(45) NULL,
    `city` VARCHAR(45) NULL,
    `city_code` VARCHAR(45) NULL,
    `telephone` VARCHAR(15) NULL,
    `company_name` VARCHAR(45) NULL,
    `company_phone` VARCHAR(15) NULL,
    `monthly_income` VARCHAR(45) NULL,
    `state_of_birth` VARCHAR(45) NULL,
    `email` VARCHAR(95) NULL,
    `gender` VARCHAR(1) NULL,
    `applicationId` VARCHAR(45) NULL,
    `alert` ENUM('Y', 'N') NOT NULL DEFAULT 'N',
    `user_id_ref` INT NOT NULL,
    INDEX `user_to_tc41_user_id_ref_idx` (`user_id_ref` ASC),
    UNIQUE INDEX `user_id_ref_UNIQUE` (`user_id_ref` ASC),
    CONSTRAINT `user_to_tc41_user_id_ref`
        FOREIGN KEY (`user_id_ref`)
        REFERENCES `user` (`user_id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tc42`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tc42` (
    `tc42_id` INT NOT NULL AUTO_INCREMENT,
    `profession` TEXT NOT NULL,
    `home_phone` VARCHAR(15) NOT NULL,
    `sup_year` INT NOT NULL,
    `sup_month` INT NOT NULL,
    `sup_day` INT NULL,
    `street` TEXT NOT NULL,
    `ext_no` VARCHAR(5) NOT NULL,
    `int_no` VARCHAR(5) NOT NULL,
    `zip_code` VARCHAR(20) NOT NULL,
    `state` TEXT NOT NULL,
    `state_code` VARCHAR(45) NOT NULL,
    `municipality` TEXT NOT NULL,
    `municipality_code` VARCHAR(45) NOT NULL,
    `colony` TEXT NOT NULL,
    `colony_code` VARCHAR(45) NOT NULL,
    `city` TEXT NULL,
    `city_code` VARCHAR(45) NULL,
    `lab_year` INT NOT NULL,
    `lab_month` INT NOT NULL,
    `giro` VARCHAR(45) NOT NULL,
    `occupation` VARCHAR(45) NOT NULL,
    `family_reference_name1` VARCHAR(45) NOT NULL,
    `family_reference_name2` VARCHAR(45) NOT NULL,
    `family_reference_phone1` VARCHAR(45) NOT NULL,
    `family_reference_phone2` VARCHAR(45) NOT NULL,
    `family_relation1` VARCHAR(45) NOT NULL,
    `family_relation2` VARCHAR(45) NOT NULL,
    `personal_reference_name` VARCHAR(45) NOT NULL,
    `personal_reference_phone` VARCHAR(45) NOT NULL,
    `user_id_ref` INT NOT NULL,
    PRIMARY KEY (`tc42_id`),
    INDEX `user_to_tc42_user_id_ref_idx` (`user_id_ref` ASC),
    UNIQUE INDEX `user_id_ref_UNIQUE` (`user_id_ref` ASC),
    CONSTRAINT `user_to_tc42_user_id_ref`
        FOREIGN KEY (`user_id_ref`)
        REFERENCES `user` (`user_id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `document`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `document` (
    `document_id` INT NOT NULL AUTO_INCREMENT,
    `ine_front` LONGBLOB NULL,
    `ine_back` LONGBLOB NULL,
    `passport` LONGBLOB NULL,
    `proof_of_address` MEDIUMBLOB NULL,
    `proof_of_income` MEDIUMBLOB NULL,
    `ine_timestamp` DATE NULL,
    `passpot_timestamp` DATE NULL,
    `poa_timestamp` DATE NULL,
    `poi_timestamp` DATE NULL,
    `user_id` INT NOT NULL,
     PRIMARY KEY (`document_id`),
     INDEX `document_user_id_idx` (`user_id` ASC),
     UNIQUE INDEX `document_user` (`user_id` ASC),
     CONSTRAINT `document_user_id`
         FOREIGN KEY (`user_id`)
         REFERENCES `user` (`user_id`)
         ON DELETE NO ACTION
         ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shipping_address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shipping_address` (
    `shipping_address_id` INT NOT NULL AUTO_INCREMENT,
    `street` VARCHAR(45) NOT NULL,
    `ext_no` VARCHAR(45) NOT NULL,
    `int_no` VARCHAR(45) NOT NULL,
    `zip_code` VARCHAR(45) NOT NULL,
    `state` VARCHAR(45) NOT NULL,
    `state_code` VARCHAR(45) NOT NULL,
    `municipality` VARCHAR(45) NOT NULL,
    `municipality_code` VARCHAR(45) NOT NULL,
    `colony` VARCHAR(45) NOT NULL,
    `colony_code` VARCHAR(45) NOT NULL,
    `city` VARCHAR(45) NOT NULL,
    `city_code` VARCHAR(45) NOT NULL,
    `user_id` INT NOT NULL,
    PRIMARY KEY (`shipping_address_id`),
    INDEX `shippping_address_user_id_idx` (`user_id` ASC),
    UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC),
    CONSTRAINT `shippping_address_user_id`
        FOREIGN KEY (`user_id`)
        REFERENCES `user` (`user_id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION)
    ENGINE = InnoDB;

