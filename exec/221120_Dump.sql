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
  `angel_alarm_end` int DEFAULT '23',
  `angel_alarm_start` int DEFAULT '0',
  `angel_comp_cnt` int DEFAULT '0',
  `angel_gender` char(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `angel_help_cnt` int DEFAULT '0',
  `user_seq` bigint DEFAULT NULL,
  PRIMARY KEY (`angel_info_seq`),
  KEY `FKp3mkiqltcuu1s9t8xf9nqd9xh` (`user_seq`),
  CONSTRAINT `FKp3mkiqltcuu1s9t8xf9nqd9xh` FOREIGN KEY (`user_seq`) REFERENCES `t_user` (`user_seq`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 eyeson.t_angel_info:~5 rows (대략적) 내보내기
DELETE FROM `t_angel_info`;
INSERT INTO `t_angel_info` (`angel_info_seq`, `angel_active`, `angel_alarm_day`, `angel_alarm_end`, `angel_alarm_start`, `angel_comp_cnt`, `angel_gender`, `angel_help_cnt`, `user_seq`) VALUES
	(6, 1, 127, 23, 1, 0, 'm', 131, 15),
	(9, 1, 127, 18, 9, 0, 'f', 0, 20),
	(14, 1, 127, 24, 1, 3, 'm', 2, 24),
	(16, 1, 127, 24, 0, 0, 'f', 1, 27),
	(17, 1, 127, 24, 1, 0, 'm', 8, 41),
	(19, 1, 127, 23, 0, 0, 'm', 1, 46);

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
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 eyeson.t_complaints:~14 rows (대략적) 내보내기
DELETE FROM `t_complaints`;
INSERT INTO `t_complaints` (`comp_seq`, `comp_address`, `comp_content`, `comp_image`, `comp_regtime`, `comp_result_content`, `comp_return`, `comp_state`, `comp_title`, `angel_seq`, `blind_seq`) VALUES
	(40, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '민아 신청합니다', '20221110_0917572204753837523895072.jpg', '2022-11-10 09:18:32', NULL, NULL, 2, '결과제목', 27, 20),
	(41, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '사진 찍었어요', '20221110_16115780089749429686639.jpg', '2022-11-10 16:12:15', NULL, NULL, 0, NULL, NULL, 20),
	(43, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221114_1033438748344527297122406.jpg', '2022-11-14 10:34:03', NULL, 'no', 1, NULL, 15, 17),
	(44, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221114_1037146396748114084135530.jpg', '2022-11-14 10:37:37', NULL, NULL, 2, 'ff', 15, 17),
	(45, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221114_1055258280783465035233980.jpg', '2022-11-14 10:55:36', NULL, NULL, 2, 'ss', 15, 17),
	(46, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221114_1106331120792290227644439.jpg', '2022-11-14 11:06:41', NULL, NULL, 2, 'ㅡㅡ', 15, 17),
	(50, '경상북도 구미시 인의동 인동19길21 null', '안녕하세요', '20221114_2204012487313721500789791.jpg', '2022-11-14 22:04:12', NULL, NULL, 0, NULL, NULL, 17),
	(55, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '아주 미안하게 됐어요', '20221115_1426432714542296027716331.jpg', '2022-11-15 14:26:56', NULL, NULL, 0, NULL, NULL, 17),
	(56, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '찜닭 사십니다', '20221115_1433081611545705866560375.jpg', '2022-11-15 14:33:18', NULL, NULL, 0, NULL, NULL, 17),
	(57, '경상북도 구미시 임수동 3공단3로302 삼성전자 2공장', '안녕하세요', '20221116_1735338217806591793342340.jpg', '2022-11-16 17:35:45', NULL, NULL, 0, NULL, NULL, 17),
	(59, '경상북도 구미시 진평동 인동가산로24 ', '안녕하세요', '20221119_1909223313315808877334463.jpg', '2022-11-19 19:09:36', NULL, NULL, 0, NULL, NULL, 26),
	(60, '경상북도 구미시 진평동 인동가산로24 ', '안녕하세요 김영주입니다', '20221119_2019043717482324484117225.jpg', '2022-11-19 20:19:32', NULL, NULL, 0, NULL, NULL, 26),
	(86, '경상북도 구미시 옥계동 593-2', '민원', '20221120_2232073630657845669171550.jpg', '2022-11-20 22:32:32', NULL, NULL, 3, '신청했어요', 24, 38),
	(87, '경상북도 구미시 옥계동 593-2', '너무 졸려요', '20221120_2305385841522876344885834.jpg', '2022-11-20 23:05:53', NULL, NULL, 3, '자면 안돼요', 24, 38),
	(88, '경상북도 구미시 옥계동 593-2', '카드값이 많이 나와요', '20221120_231512826393345984559030.jpg', '2022-11-20 23:15:38', NULL, NULL, 2, '버티세요', 24, 38);

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
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 테이블 데이터 eyeson.t_user:~12 rows (대략적) 내보내기
DELETE FROM `t_user`;
INSERT INTO `t_user` (`user_seq`, `user_authority`, `user_date`, `user_email`, `user_fcm`, `user_gender`, `user_pass`) VALUES
	(2, 1, '2022-11-01 14:33:00', 'sa01070@naver.com', 'q1w2e3r4t5', 'm', 'q1w2e3r4t5'),
	(15, 2, '2022-11-02 14:34:59', 'taxfdi6371@gmail.com', 'cGlZT71SRZ-4l7HJdq1DtT:APA91bHfdKOWs1Kpx_eBHZg-FdWwpfemXmrQ_JxJL5zGQvnavNHdK61cunevFCTmWSKQNMqU9-cvljJdz46JzXcg5_s0vwUZKkQi6OJHA584_srT1Svv54FsuLugBOXRE5wXoV46AI_n', 'm', '$2a$10$DIvyiQNn9wJFNiIAkHAFj.NsjDiszayMQAVKRrLEviijtDaFaPCZW'),
	(17, 1, '2022-11-04 14:33:21', 'jetiyh7631@gmail.com', 'e4QCKUpgShaAXnnm0PIKJ7:APA91bHXlsFaIWoNi1VNG4VIzCBdGtOIZFc5A0MnwZTe-hwSoNuEpepRdxO-4aamf7scR3N7FfwSWB9TXqehZGOW1-jQ_RRk_IBle6DoMyZe6OYa-fs1y0wrnfu3tM18MrvwX-wdhbIV', 'm', '$2a$10$gpybwCCMat64rET2QsXn/uhJFRGeHSPv2BVrD7kD/6FYlmDkxmd7a'),
	(20, 1, '2022-11-07 17:54:52', 'enddl3054@gmail.com', 'c7KXIWs9RKucOuVApw4b98:APA91bElHMK-oQSXCn0GGI4d8MM142I87Lt4p8aGpOqiVmHcxHsGPQhAY39TaCtj6Vi6qlxAUMjzsbdr2b-ubodGpZXeWnOIvXl87Yb-R9s2P9dHdj_8bnyPmN0hOXDQ2lz5P-jw0Z1F', 'f', '$2a$10$lV6o5salIXWP9d0mpC5WseHk5nrp8jOcyU4Now220.5Fo6.gEvQIa'),
	(24, 2, '2022-11-14 21:25:50', 'remoteyong1@gmail.com', 'dDM_VYuoS5Ok645NAOUz-a:APA91bGNmVud8SFR5pA1HaRRzU-WI8YPvJJx4yOn1AZH5pEHspLj_0LNm6ufggov6GeufIitizW_sFMCf4fj3NkyqsHA--2pj5S3DjbcpgHu7W36ULuvQ_NLYG3sjB4aeQTEZpmxau0R', 'm', '$2a$10$7ns3ro/CEOzV5DRUlWsA0u3/yoAzugf.mrFEO7Bi3vY2/w3gBPOYi'),
	(26, 1, '2022-11-15 14:46:39', 'kimmangji1111q@gmail.com', 'e4QCKUpgShaAXnnm0PIKJ7:APA91bHXlsFaIWoNi1VNG4VIzCBdGtOIZFc5A0MnwZTe-hwSoNuEpepRdxO-4aamf7scR3N7FfwSWB9TXqehZGOW1-jQ_RRk_IBle6DoMyZe6OYa-fs1y0wrnfu3tM18MrvwX-wdhbIV', 'f', '$2a$10$O7PRgiBUrlqMklnAVcP5hOMr/Qoi2.YpwGjhMZj82BFVm1AOySKk6'),
	(27, 2, '2022-11-15 14:48:02', 'kimmangangel@gmail.com', 'e4QCKUpgShaAXnnm0PIKJ7:APA91bHXlsFaIWoNi1VNG4VIzCBdGtOIZFc5A0MnwZTe-hwSoNuEpepRdxO-4aamf7scR3N7FfwSWB9TXqehZGOW1-jQ_RRk_IBle6DoMyZe6OYa-fs1y0wrnfu3tM18MrvwX-wdhbIV', 'f', '$2a$10$1aY6iFPHf77KnX.uM47cSO.oPwVDoMPshk7jhv2cCI8qcy6pUtqoq'),
	(38, 1, '2022-11-15 23:21:14', 'ebjdev@gmail.com', 'dDM_VYuoS5Ok645NAOUz-a:APA91bGNmVud8SFR5pA1HaRRzU-WI8YPvJJx4yOn1AZH5pEHspLj_0LNm6ufggov6GeufIitizW_sFMCf4fj3NkyqsHA--2pj5S3DjbcpgHu7W36ULuvQ_NLYG3sjB4aeQTEZpmxau0R', 'm', '$2a$10$x67rDJ4xOwEpAETkAa1MLO5g64DH/lhHAen3EEa.QuH8bWQn3INOG'),
	(40, 1, '2022-11-17 09:44:32', 'jongui7703@gmail.com', 'cISgAQA7SlyyPCmafLlDmQ:APA91bF_fkpmuhTQQtvtgNTKK78e1EuUWtvVEUaX0W5oQrLfzKGVUvHVnSvzUZWO3H6fedQ8Z-f6UATJ6tkcc0X3lPQvExbjYGeY0blO4RymZcBPVeI0gernKmEL8UQ_HQXO_gAv0tIk', 'm', '$2a$10$51fcp2ezOx2E/jDpW6t3HeMjoMcNfpEHaUo9P6GhvD.W27CRzrc0m'),
	(41, 2, '2022-11-19 17:04:24', 'sa01070@gmail.com', 'cISgAQA7SlyyPCmafLlDmQ:APA91bF_fkpmuhTQQtvtgNTKK78e1EuUWtvVEUaX0W5oQrLfzKGVUvHVnSvzUZWO3H6fedQ8Z-f6UATJ6tkcc0X3lPQvExbjYGeY0blO4RymZcBPVeI0gernKmEL8UQ_HQXO_gAv0tIk', 'm', '$2a$10$tSZ/ocqlkzJiiyB0aPVwwOoitJdXAXIQahm9PFKewScoKPa15.lDa'),
	(42, 1, '2022-11-19 18:55:07', 'enddl3054@naver.com', 'c7KXIWs9RKucOuVApw4b98:APA91bElHMK-oQSXCn0GGI4d8MM142I87Lt4p8aGpOqiVmHcxHsGPQhAY39TaCtj6Vi6qlxAUMjzsbdr2b-ubodGpZXeWnOIvXl87Yb-R9s2P9dHdj_8bnyPmN0hOXDQ2lz5P-jw0Z1F', 'f', '$2a$10$JwEbGwV8EMNnbJGfDOcb..0jZhGOagHFv3QTFxqz2En.igAl2u0fe'),
	(43, 1, '2022-11-19 18:55:14', 'eyesontest@gmail.com', 'fNtQ3mqeSEKibZCj6vpdWE:APA91bFShPKY_hp83Fm94VI74r6ksO051CkueOPgHeYFDaiV_wWnwbsghkSavyRSqC0oBQSAgvzgcgTkRCDZYiKRBDPTMv0xryWjOkA5MMsZTCVZgxKnQIMNp2pmkTKcf9ayB9IWwxMu', 'm', '$2a$10$thhvslgwnRZJywqEGKPA9.MNf0aKQVpOeYxAcBS0QSjy57kae4Ubi'),
	(46, 2, '2022-11-20 20:52:56', 'kimjs1133557799@gmail.com', 'dZbSUWcYQ9Cxo2JJUw75mz:APA91bHiO1L2Z_bBBIITKCz6-JPjUffU_cbNnYPo6_-UBCVCo35ivbEAksZL3ukIo6hpp5rJ41baQJHBUnvrQXkYSVReYR4o8xQ3D0vJnjBKFtoBvK8xb9S3QYrw-cuECFf0IPAiWafC', 'm', '$2a$10$fv4gx.bKd6P/lZdIoPDIpuY0YS.KsKmSG57c1e.COYO6ydP9VpZ7q');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
