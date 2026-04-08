-- 데이터 삽입은 외래 키(FK) 제약조건에 위배되지 않도록,
-- 참조되는 테이블(부모 테이블)부터 순서대로 진행해야 합니다.

-- ----------------------------
-- 1. 독립적인 기본 데이터 추가
-- ----------------------------

-- 사용자(HR) 샘플 데이터
-- 실제 운영 시에는 pw(비밀번호)를 반드시 암호화하여 저장해야 합니다.
INSERT INTO `HR` (`id`, `pw`, `email`, `name`) VALUES
('user01', 'hashed_password_123', 'user01@company.com', '김철수'),
('user02', 'hashed_password_456', 'user02@company.com', '이영희');

-- 품목(item) 샘플 데이터 (id는 AUTO_INCREMENT로 자동 생성됨)
INSERT INTO `item` (`vin`, `color`) VALUES
('KNA12345VIN001', 'Snow White Pearl'),
('KNA12345VIN002', 'Aurora Black'),
('KNA12345VIN003', 'Steel Gray');

-- 설비(machine) 샘플 데이터
INSERT INTO `machine` (`machine_id`, `machine_name`, `isRun`) VALUES
('PAINT-ROBOT-01', '1번 도장 로봇', 1),
('DRY-OVEN-01', '1번 건조 오븐', 1),
('PAINT-ROBOT-02', '2번 도장 로봇', 0);

-- 에러(error) 샘플 데이터
INSERT INTO `error` (`error_id`, `error_name`) VALUES
('E01', '도장 흘러내림 (Sagging)'),
('E02', '오렌지 필 (Orange Peel)'),
('E03', '핀홀 (Pinholing)');

-- 데이터 임계치(data) 샘플 데이터
INSERT INTO `data` (`data_id`, `data_name`, `threshold`) VALUES
('TEMP-H', '오븐 상한 온도', 85.5),
('HUMID-H', '작업장 상한 습도', 60.0),
('PRESSURE-L', '분사 압력 하한', 3.5);


-- ----------------------------
-- 2. 관계를 가지는 데이터 추가
-- ----------------------------

-- 설비-데이터 맵(machine_data_map) 샘플 데이터
INSERT INTO `machine_data_map` (`machine_id`, `data_id`) VALUES
('PAINT-ROBOT-01', 'HUMID-H'),
('PAINT-ROBOT-01', 'PRESSURE-L'),
('DRY-OVEN-01', 'TEMP-H');

-- 검사 지시(check_order) 샘플 데이터
-- item_id 1번(Snow White Pearl) 차량을 user01(김철수)이 PAINT-ROBOT-01에서 검사하도록 지시
INSERT INTO `check_order` (`item_id`, `hr_id`, `machine_id`, `create_dt`) VALUES
(1, 'user01', 'PAINT-ROBOT-01', NOW()),
(2, 'user01', 'PAINT-ROBOT-01', NOW()),
(3, 'user02', 'PAINT-ROBOT-02', NOW());

-- PLC 데이터(plc) 샘플 데이터
INSERT INTO `plc` (`machine_id`, `value`, `create_dt`) VALUES
('PAINT-ROBOT-01', 55.3, NOW()), -- 습도 데이터
('DRY-OVEN-01', 85.1, NOW()), -- 온도 데이터
('DRY-OVEN-01', 86.2, NOW()); -- 온도 임계치 초과 데이터

-- 비가동(downtime) 샘플 데이터
-- 1번 로봇은 에러 없이 계획된 점검으로 비가동
INSERT INTO `downtime` (`machine_id`, `start_dt`, `end_dt`, `error_id`) VALUES
('PAINT-ROBOT-01', '2025-09-18 09:00:00', '2025-09-18 10:00:00', NULL),
('PAINT-ROBOT-02', '2025-09-18 11:00:00', NULL, 'E01'); -- 2번 로봇은 E01 에러로 현재 비가동 중


-- ----------------------------
-- 3. 가장 마지막 순서의 데이터 추가
-- ----------------------------

-- 검사 결과(check_result) 샘플 데이터
-- 1번 검사지시는 정상 통과 (error_id가 NULL)
INSERT INTO `check_result` (`checkorder_id`, `error_id`, `create_dt`) VALUES
(1, NULL, NOW()),
-- 2번 검사지시는 E02(오렌지 필) 불량 발생
(2, 'E02', NOW()),
-- 3번 검사지시는 E01(흘러내림) 불량 발생
(3, 'E01', NOW());