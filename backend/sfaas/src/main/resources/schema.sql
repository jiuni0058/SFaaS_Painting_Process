-- ----------------------------
-- 스크립트 실행 전 설정
-- ----------------------------
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0; -- 외부 키 제약 조건 비활성화

-- ----------------------------
-- 1. 테이블 삭제 (DROP TABLE)
-- 외부 키(FK) 관계의 역순으로 자식 테이블부터 삭제합니다.
-- ----------------------------
DROP TABLE IF EXISTS `check_result`;
DROP TABLE IF EXISTS `downtime`;
DROP TABLE IF EXISTS `plc`;
DROP TABLE IF EXISTS `machine_data_map`;
DROP TABLE IF EXISTS `check_order`;
DROP TABLE IF EXISTS `HR`;
DROP TABLE IF EXISTS `item`;
DROP TABLE IF EXISTS `machine`;
DROP TABLE IF EXISTS `data`;
DROP TABLE IF EXISTS `error`;

-- ----------------------------
-- 2. 테이블 생성 (CREATE TABLE)
-- PRIMARY KEY를 구문 내에 바로 정의합니다.
-- ----------------------------
CREATE TABLE `HR` (
  `id` VARCHAR(64) NOT NULL,
  `pw` VARCHAR(255) NOT NULL,
  `email` VARCHAR(254) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hr_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `item` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `vin` VARCHAR(64) NOT NULL,
  `color` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_item_vin` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `machine_data_map` (
   `map_id` INT NOT NULL AUTO_INCREMENT,
   `machine_id` VARCHAR(255) NOT NULL,
   `data_id` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`map_id`),
  UNIQUE KEY `UK_machine_data_pair` (`machine_id`,`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `machine` (
	`machine_id`	VARCHAR(255)	NOT NULL,
	`machine_name`	VARCHAR(255)	NULL,
	`isRun`	BOOLEAN	NULL,
	PRIMARY KEY (`machine_id`)
);

CREATE TABLE `data` (
	`data_id`	VARCHAR(255)	NOT NULL,
	`data_name`	VARCHAR(255)	NULL,
	`threshold`	DOUBLE	NULL,
	PRIMARY KEY (`data_id`)
);

CREATE TABLE `error` (
	`error_id`	VARCHAR(255)	NOT NULL,
	`error_name`	VARCHAR(255)	NULL,
	PRIMARY KEY (`error_id`)
);

CREATE TABLE `check_order` (
	`id`	INT	NOT NULL AUTO_INCREMENT,
	`create_dt`	DATETIME	NULL,
	`item_id`	INT	NOT NULL,
	`hr_id`	VARCHAR(255)	NOT NULL,
	`machine_id`	VARCHAR(255)	NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `check_result` (
	`result_id`	INT	NOT NULL AUTO_INCREMENT,
	`create_dt`	DATETIME	NULL,
	`checkorder_id`	INT	NOT NULL,
	`error_id`	VARCHAR(255)	NULL,
	PRIMARY KEY (`result_id`)
);

CREATE TABLE `downtime` (
	`id`	INT	NOT NULL AUTO_INCREMENT,
	`start_dt`	DATETIME	NULL,
	`end_dt`	DATETIME	NULL,
	`machine_id`	VARCHAR(255)	NOT NULL,
	`error_id`	VARCHAR(255)	NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `plc` (
	`plc_id`	INT	NOT NULL AUTO_INCREMENT,
	`value`	DOUBLE	NULL,
	`create_dt`	DATETIME	NULL,
	`machine_id`	VARCHAR(255)	NOT NULL,
	PRIMARY KEY (`plc_id`)
);

-- ----------------------------
-- 3. 외부 키 설정 (Foreign Key Constraints)
-- 모든 테이블이 생성된 후 관계를 설정합니다.
-- ----------------------------
ALTER TABLE `check_order` ADD CONSTRAINT `FK_item_TO_check_order` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`);
ALTER TABLE `check_order` ADD CONSTRAINT `FK_HR_TO_check_order` FOREIGN KEY (`hr_id`) REFERENCES `HR` (`id`);
ALTER TABLE `check_order` ADD CONSTRAINT `FK_machine_TO_check_order` FOREIGN KEY (`machine_id`) REFERENCES `machine` (`machine_id`);

ALTER TABLE `check_result` ADD CONSTRAINT `FK_check_order_TO_check_result` FOREIGN KEY (`checkorder_id`) REFERENCES `check_order` (`id`);
ALTER TABLE `check_result` ADD CONSTRAINT `FK_error_TO_check_result` FOREIGN KEY (`error_id`) REFERENCES `error` (`error_id`);

ALTER TABLE `downtime` ADD CONSTRAINT `FK_machine_TO_downtime` FOREIGN KEY (`machine_id`) REFERENCES `machine` (`machine_id`);
ALTER TABLE `downtime` ADD CONSTRAINT `FK_error_TO_downtime` FOREIGN KEY (`error_id`) REFERENCES `error` (`error_id`);

ALTER TABLE `plc` ADD CONSTRAINT `FK_machine_TO_plc` FOREIGN KEY (`machine_id`) REFERENCES `machine` (`machine_id`);

ALTER TABLE `machine_data_map` ADD CONSTRAINT `FK_machine_TO_machine_data_map` FOREIGN KEY (`machine_id`) REFERENCES `machine` (`machine_id`);
ALTER TABLE `machine_data_map` ADD CONSTRAINT `FK_data_TO_machine_data_map` FOREIGN KEY (`data_id`) REFERENCES `data` (`data_id`);

-- ----------------------------
-- 스크립트 실행 후 설정 복원
-- ----------------------------
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS; -- 외부 키 제약 조건 다시 활성화