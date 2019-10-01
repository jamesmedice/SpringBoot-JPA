CREATE DATABASE IF NOT EXISTS marketgateway;

USE marketgateway;

CREATE TABLE IF NOT EXISTS `marketgateway`.`databasechangelog` (
  `ID` VARCHAR(255) NOT NULL,
  `AUTHOR` VARCHAR(255) NOT NULL,
  `FILENAME` VARCHAR(255) NOT NULL,
  `DATEEXECUTED` DATETIME NOT NULL,
  `ORDEREXECUTED` INT(11) NOT NULL,
  `EXECTYPE` VARCHAR(10) NOT NULL,
  `MD5SUM` VARCHAR(35) NULL DEFAULT NULL,
  `DESCRIPTION` VARCHAR(255) NULL DEFAULT NULL,
  `COMMENTS` VARCHAR(255) NULL DEFAULT NULL,
  `TAG` VARCHAR(255) NULL DEFAULT NULL,
  `LIQUIBASE` VARCHAR(20) NULL DEFAULT NULL,
  `CONTEXTS` VARCHAR(255) NULL DEFAULT NULL,
  `LABELS` VARCHAR(255) NULL DEFAULT NULL,
  `DEPLOYMENT_ID` VARCHAR(10) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


CREATE TABLE IF NOT EXISTS `marketgateway`.`databasechangeloglock` (
  `ID` INT(11) NOT NULL,
  `LOCKED` BIT(1) NOT NULL,
  `LOCKGRANTED` DATETIME NULL DEFAULT NULL,
  `LOCKEDBY` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


CREATE TABLE IF NOT EXISTS `marketgateway`.`jhi_authority` (
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`name`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


CREATE TABLE IF NOT EXISTS `marketgateway`.`jhi_persistent_audit_event` (
  `event_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `principal` VARCHAR(50) NOT NULL,
  `event_date` TIMESTAMP NULL DEFAULT NULL,
  `event_type` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  INDEX `idx_persistent_audit_event` (`principal` ASC, `event_date` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


CREATE TABLE IF NOT EXISTS `marketgateway`.`jhi_persistent_audit_evt_data` (
  `event_id` BIGINT(20) NOT NULL,
  `name` VARCHAR(150) NOT NULL,
  `value` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`event_id`, `name`),
  INDEX `idx_persistent_audit_evt_data` (`event_id` ASC),
  CONSTRAINT `fk_evt_pers_audit_evt_data`
    FOREIGN KEY (`event_id`)
    REFERENCES `marketgateway`.`jhi_persistent_audit_event` (`event_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


CREATE TABLE IF NOT EXISTS `marketgateway`.`jhi_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(50) NOT NULL,
  `password_hash` VARCHAR(60) NOT NULL,
  `first_name` VARCHAR(50) NULL DEFAULT NULL,
  `last_name` VARCHAR(50) NULL DEFAULT NULL,
  `email` VARCHAR(254) NULL DEFAULT NULL,
  `image_url` VARCHAR(256) NULL DEFAULT NULL,
  `activated` BIT(1) NOT NULL,
  `lang_key` VARCHAR(6) NULL DEFAULT NULL,
  `activation_key` VARCHAR(20) NULL DEFAULT NULL,
  `reset_key` VARCHAR(20) NULL DEFAULT NULL,
  `created_by` VARCHAR(50) NOT NULL,
  `created_date` TIMESTAMP NOT NULL,
  `reset_date` TIMESTAMP NULL DEFAULT NULL,
  `last_modified_by` VARCHAR(50) NULL DEFAULT NULL,
  `last_modified_date` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ux_user_login` (`login` ASC),
  UNIQUE INDEX `ux_user_email` (`email` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = latin1;


CREATE TABLE IF NOT EXISTS `marketgateway`.`jhi_user_authority` (
  `user_id` BIGINT(20) NOT NULL,
  `authority_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`user_id`, `authority_name`),
  INDEX `fk_authority_name` (`authority_name` ASC),
  CONSTRAINT `fk_authority_name`
    FOREIGN KEY (`authority_name`)
    REFERENCES `marketgateway`.`jhi_authority` (`name`),
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `marketgateway`.`jhi_user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


CREATE TABLE IF NOT EXISTS `marketgateway`.`jobs` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `job_id` BIGINT(20) NULL DEFAULT NULL,
  `job_name` VARCHAR(255) NULL DEFAULT NULL,
  `regular_expression` VARCHAR(255) NULL DEFAULT NULL,
  `jhi_enable` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


DROP TABLE IF EXISTS QRTZ_LOCKS;
 