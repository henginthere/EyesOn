-- --------------------------------------------------------
-- 호스트:                          d201.kro.kr
-- 서버 버전:                        8.0.31 - MySQL Community Server - GPL
-- 서버 OS:                        Linux
-- HeidiSQL 버전:                  12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- eyeson 데이터베이스 구조 내보내기
DROP DATABASE IF EXISTS `eyeson`;
CREATE DATABASE IF NOT EXISTS `eyeson` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `eyeson`;

-- 테이블 eyeson.t_angel_info 구조 내보내기
DROP TABLE IF EXISTS `t_angel_info`;
CREATE TABLE IF NOT EXISTS `t_angel_info` (
  `angel_info_seq` bigint NOT NULL AUTO_INCREMENT,
  `angel_active` tinyint(1) DEFAULT '1',
  `angel_alarm_day` int DEFAULT '127',
  `angel_alarm_end` int DEFAULT '0',
  `angel_alarm_start` int DEFAULT '0',
  `angel_comp_cnt` int DEFAULT '0',
  `angel_gender` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `angel_help_cnt` int DEFAULT '0',
  `user_seq` bigint DEFAULT NULL,
  PRIMARY KEY (`angel_info_seq`),
  KEY `FKp3mkiqltcuu1s9t8xf9nqd9xh` (`user_seq`),
  CONSTRAINT `FKp3mkiqltcuu1s9t8xf9nqd9xh` FOREIGN KEY (`user_seq`) REFERENCES `t_user` (`user_seq`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 eyeson.t_angel_info:~5 rows (대략적) 내보내기
DELETE FROM `t_angel_info`;
INSERT INTO `t_angel_info` (`angel_info_seq`, `angel_active`, `angel_alarm_day`, `angel_alarm_end`, `angel_alarm_start`, `angel_comp_cnt`, `angel_gender`, `angel_help_cnt`, `user_seq`) VALUES
	(6, 1, 127, 23, 1, 0, 'm', 131, 15),
	(9, 1, 127, 18, 9, 0, 'f', 0, 20),
	(14, 1, 127, 19, 1, 1, 'm', 2, 24),
	(15, 0, 0, 0, 0, 0, 'f', 3, 25),
	(16, 1, 0, 0, 22, 0, 'f', 0, 27);

-- 테이블 eyeson.t_complaints 구조 내보내기
DROP TABLE IF EXISTS `t_complaints`;
CREATE TABLE IF NOT EXISTS `t_complaints` (
  `comp_seq` bigint NOT NULL AUTO_INCREMENT,
  `comp_address` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comp_content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comp_image` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comp_regtime` datetime DEFAULT NULL,
  `comp_result_content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comp_return` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comp_state` int DEFAULT NULL,
  `comp_title` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `angel_seq` bigint DEFAULT NULL,
  `blind_seq` bigint DEFAULT NULL,
  PRIMARY KEY (`comp_seq`),
  KEY `FKrjeo0ojo5a4ow397xiks45w59` (`blind_seq`),
  KEY `FKue363he3kprsp4dbvro52ib2` (`angel_seq`),
  CONSTRAINT `FKrjeo0ojo5a4ow397xiks45w59` FOREIGN KEY (`blind_seq`) REFERENCES `t_user` (`user_seq`) ON DELETE CASCADE,
  CONSTRAINT `FKue363he3kprsp4dbvro52ib2` FOREIGN KEY (`angel_seq`) REFERENCES `t_user` (`user_seq`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 eyeson.t_complaints:~24 rows (대략적) 내보내기
DELETE FROM `t_complaints`;
INSERT INTO `t_complaints` (`comp_seq`, `comp_address`, `comp_content`, `comp_image`, `comp_regtime`, `comp_result_content`, `comp_return`, `comp_state`, `comp_title`, `angel_seq`, `blind_seq`) VALUES
	(15, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '길가에 킥보드가 쓰러져있어요.', 'image.4.5e523296-d9b7-43de-8225-132914bf7716_1667801438480.jpg', '2022-11-07 15:10:39', NULL, NULL, 2, '아녕', 27, 11),
	(20, '대구광역시 수성구 수성동1가 신천동로320 신세계타운', '가로등이 쓰러져있습니다. 조치 부탁드려', '가로등.PNG', '2022-11-07 16:06:02', NULL, NULL, 0, NULL, NULL, 11),
	(21, '인천광역시 남동구 구월동 남동대로780 가로판매대', '도로가 얼어서 차가 움직이지 못해', '도로결빙.PNG', '2022-11-07 16:06:33', NULL, NULL, 0, NULL, NULL, 11),
	(22, '서울특별시 마포구 동교동 192-6', '도심에 멧돼지가 나타나 다 부수고 있어', '멧돼지.PNG', '2022-11-07 16:07:17', NULL, NULL, 0, NULL, NULL, 11),
	(23, '부산광역시 해운대구 우동 622-5', '해운대 바다 앞 볼라드가 부서졌네', '볼라드.PNG', '2022-11-07 16:07:46', NULL, NULL, 0, NULL, NULL, 11),
	(24, '전라북도 군산시 조촌동 887', '산사태로 인해 차가 마비됐어', '산사태.PNG', '2022-11-07 16:08:12', NULL, NULL, 0, NULL, NULL, 11),
	(25, '서울특별시 노원구 중계동 덕릉로676 가로판매대', '싱크홀에 빠져 죽을뻔 했습니다. 얼른 메꿔주세', '싱크홀.PNG', '2022-11-07 16:08:43', NULL, NULL, 0, NULL, NULL, 11),
	(26, '강원도 철원군 갈말읍 삼부연로25 철원우체국', '군부대에서 나온 쓰레기 때문에 농사를 못지어', '쓰레기.PNG', '2022-11-07 16:09:08', NULL, NULL, 0, NULL, NULL, 11),
	(27, '전라북도 전주시 완산구 서신동 감나무3길3 null', '육교에 있는 엘리베이터가 작동을 안합니다. 너무 힘들어', '엘리베이터.PNG', '2022-11-07 16:09:47', NULL, NULL, 0, NULL, NULL, 11),
	(28, '경기도 이천시 설성면 77-3', '자전거를 인도에 이렇게 세워놓으면 사람이 어떻게 지나다닙니', '자전거.PNG', '2022-11-07 16:10:28', NULL, NULL, 0, NULL, NULL, 11),
	(29, '전라남도 여수시 수정동 350-59', '점자블록이 깨져서 인식이 힘듭니다. ', '점자블록.PNG', '2022-11-07 16:11:01', NULL, NULL, 0, NULL, NULL, 11),
	(30, '대구광역시 달서구 용산동 선원로244 구두수선대 23', '침수때문에 시원하긴한데 통행에 방해되네요 물뺴주세', '침수.PNG', '2022-11-07 16:11:30', NULL, NULL, 0, NULL, NULL, 11),
	(36, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '서버 반대요', '20221108_101246359186994995279755.jpg', '2022-11-08 10:13:08', NULL, NULL, 0, NULL, NULL, 11),
	(37, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221108_1016496759904221057963806.jpg', '2022-11-08 10:17:06', NULL, '사진', 1, NULL, NULL, 11),
	(40, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '민아 신청합니다', '20221110_0917572204753837523895072.jpg', '2022-11-10 09:18:32', NULL, NULL, 0, NULL, NULL, 20),
	(41, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '사진 찍었어요', '20221110_16115780089749429686639.jpg', '2022-11-10 16:12:15', NULL, NULL, 0, NULL, NULL, 20),
	(43, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221114_1033438748344527297122406.jpg', '2022-11-14 10:34:03', NULL, 'no', 1, NULL, 15, 17),
	(44, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221114_1037146396748114084135530.jpg', '2022-11-14 10:37:37', NULL, NULL, 2, 'ff', 15, 17),
	(45, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221114_1055258280783465035233980.jpg', '2022-11-14 10:55:36', NULL, NULL, 2, 'ss', 15, 17),
	(46, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221114_1106331120792290227644439.jpg', '2022-11-14 11:06:41', NULL, NULL, 2, 'ㅡㅡ', 15, 17),
	(50, '경상북도 구미시 인의동 인동19길21 null', '안녕하세요', '20221114_2204012487313721500789791.jpg', '2022-11-14 22:04:12', NULL, NULL, 0, NULL, NULL, 17),
	(55, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '아주 미안하게 됐어요', '20221115_1426432714542296027716331.jpg', '2022-11-15 14:26:56', NULL, NULL, 0, NULL, NULL, 17),
	(56, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '찜닭 사십니다', '20221115_1433081611545705866560375.jpg', '2022-11-15 14:33:18', NULL, NULL, 0, NULL, NULL, 17),
	(57, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221116_1735338217806591793342340.jpg', '2022-11-16 17:35:45', NULL, NULL, 0, NULL, NULL, 17);

-- 테이블 eyeson.t_user 구조 내보내기
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE IF NOT EXISTS `t_user` (
  `user_seq` bigint NOT NULL AUTO_INCREMENT,
  `user_authority` int DEFAULT NULL,
  `user_date` datetime NOT NULL,
  `user_email` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_fcm` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_gender` char(1) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_pass` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 eyeson.t_user:~11 rows (대략적) 내보내기
DELETE FROM `t_user`;
INSERT INTO `t_user` (`user_seq`, `user_authority`, `user_date`, `user_email`, `user_fcm`, `user_gender`, `user_pass`) VALUES
	(2, 1, '2022-11-01 14:33:00', 'sa01070@naver.com', 'q1w2e3r4t5', 'm', 'q1w2e3r4t5'),
	(11, 2, '2022-11-02 10:10:10', 'sa01070@gmail.com', '1q2w3e4r5t', 'f', '1q2w3e4r5t'),
	(15, 2, '2022-11-02 14:34:59', 'taxfdi6371@gmail.com', 'd8zjTSUFQ8yILOCxtnKGPs:APA91bHaSNKTbx3tcpDfFQE3SkVz_ejyPpr6rDEer_BPH-IHqyVCU6eTRK1zTLivjB3ZjZjH7yM7Qc_I8EYzA4QzZSKGa8e5gpxtfBpr6kYLavd0ijeojAAFjJ-9npStiQ6_0VfjABNF', 'm', '$2a$10$DIvyiQNn9wJFNiIAkHAFj.NsjDiszayMQAVKRrLEviijtDaFaPCZW'),
	(17, 1, '2022-11-04 14:33:21', 'jetiyh7631@gmail.com', 'd8zjTSUFQ8yILOCxtnKGPs:APA91bHaSNKTbx3tcpDfFQE3SkVz_ejyPpr6rDEer_BPH-IHqyVCU6eTRK1zTLivjB3ZjZjH7yM7Qc_I8EYzA4QzZSKGa8e5gpxtfBpr6kYLavd0ijeojAAFjJ-9npStiQ6_0VfjABNF', 'm', '$2a$10$gpybwCCMat64rET2QsXn/uhJFRGeHSPv2BVrD7kD/6FYlmDkxmd7a'),
	(20, 1, '2022-11-07 17:54:52', 'enddl3054@gmail.com', 'eN9AcKUrSe2OPex6Hmi7mU:APA91bGlBkHWOKKWUqcqWTQ7LVHnUmuiVrunbmt3atownSFUnqu1W35Q5WoCLuL4HuK04Rb1ai8pZOivnkXvlw5fml6urHGe-ci3npfXvTvIfChV1HjpPA617gKJZFwn4OX5KJOZFM1s', 'f', '$2a$10$lV6o5salIXWP9d0mpC5WseHk5nrp8jOcyU4Now220.5Fo6.gEvQIa'),
	(24, 2, '2022-11-14 21:25:50', 'remoteyong1@gmail.com', 'dDM_VYuoS5Ok645NAOUz-a:APA91bFYtBL6ihMutRrlz2Y4dXf5YmM7OZnCDE55Uq3gCX0kvPxiH30M-I2_2BCMCaEeXtlanK7ADE5gWlZaxiNl22mW1U9TfGXNA16bN9xBIzzNLg8J4hIW9mjMHCe9Mh6rUVI0f-rA', 'm', '$2a$10$7ns3ro/CEOzV5DRUlWsA0u3/yoAzugf.mrFEO7Bi3vY2/w3gBPOYi'),
	(25, 2, '2022-11-15 14:33:55', 'enddl3054@naver.com', 'eN9AcKUrSe2OPex6Hmi7mU:APA91bGlBkHWOKKWUqcqWTQ7LVHnUmuiVrunbmt3atownSFUnqu1W35Q5WoCLuL4HuK04Rb1ai8pZOivnkXvlw5fml6urHGe-ci3npfXvTvIfChV1HjpPA617gKJZFwn4OX5KJOZFM1s', 'f', '$2a$10$u7uhu.m3HCHpDzJOTomBauZvzhC5UBXDSI3rcaSSHo8B6E6Pt3A2m'),
	(26, 1, '2022-11-15 14:46:39', 'kimmangji1111q@gmail.com', 'fxCK_DWIRqmV0HlzcbNQAu:APA91bGdBB_ow8PDg0tfMXtzRbQAZees3VMF5AycJHw7YFkR1L8WpZou0xLoXAkV4rKlo5kUHa13i8xTL79qRFZF02cYpB63b57AGNi3FSfc2HmOEbLS9tAsuSJ-lQc_4Fmzdu4sxCK0', 'f', '$2a$10$O7PRgiBUrlqMklnAVcP5hOMr/Qoi2.YpwGjhMZj82BFVm1AOySKk6'),
	(27, 2, '2022-11-15 14:48:02', 'kimmangangel@gmail.com', 'fxCK_DWIRqmV0HlzcbNQAu:APA91bGdBB_ow8PDg0tfMXtzRbQAZees3VMF5AycJHw7YFkR1L8WpZou0xLoXAkV4rKlo5kUHa13i8xTL79qRFZF02cYpB63b57AGNi3FSfc2HmOEbLS9tAsuSJ-lQc_4Fmzdu4sxCK0', 'f', '$2a$10$1aY6iFPHf77KnX.uM47cSO.oPwVDoMPshk7jhv2cCI8qcy6pUtqoq'),
	(38, 1, '2022-11-15 23:21:14', 'ebjdev@gmail.com', 'dDM_VYuoS5Ok645NAOUz-a:APA91bFYtBL6ihMutRrlz2Y4dXf5YmM7OZnCDE55Uq3gCX0kvPxiH30M-I2_2BCMCaEeXtlanK7ADE5gWlZaxiNl22mW1U9TfGXNA16bN9xBIzzNLg8J4hIW9mjMHCe9Mh6rUVI0f-rA', 'm', '$2a$10$x67rDJ4xOwEpAETkAa1MLO5g64DH/lhHAen3EEa.QuH8bWQn3INOG'),
	(40, 1, '2022-11-17 09:44:32', 'jongui7703@gmail.com', 'cNmxbvv4RJWt_gxhZPFKUk:APA91bG8VBZFdYk0mQ9wFTYS7CHJBCwf8f_dolM1QOT9NpMwDv4M67aU_gF2aAXFKR_97uBNyb7kvbXgM-QMpi4yH25HPGz-JVQFfFevgRoK1I0SfOxZjapB77XFhbbGmiv1E82m4jJP', 'm', '$2a$10$51fcp2ezOx2E/jDpW6t3HeMjoMcNfpEHaUo9P6GhvD.W27CRzrc0m');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
