/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80022 (8.0.22)
 Source Host           : 127.0.0.1:3306
 Source Schema         : public

 Target Server Type    : MySQL
 Target Server Version : 80022 (8.0.22)
 File Encoding         : 65001

 Date: 01/02/2023 11:55:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cvn_friend
-- ----------------------------
DROP TABLE IF EXISTS `cvn_friend`;
CREATE TABLE `cvn_friend`  (
  `USER_ID` int NOT NULL,
  `FRIEND_ID` int NOT NULL,
  INDEX `FK_92OD4XCIXKN34NBSEY0EVMSCM_INDEX_8`(`FRIEND_ID` ASC) USING BTREE,
  INDEX `FK_RC6VLNFHY45H7IHCTN0M34SST_INDEX_8`(`USER_ID` ASC) USING BTREE,
  CONSTRAINT `FK_92OD4XCIXKN34NBSEY0EVMSCM` FOREIGN KEY (`FRIEND_ID`) REFERENCES `cvn_user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_RC6VLNFHY45H7IHCTN0M34SST` FOREIGN KEY (`USER_ID`) REFERENCES `cvn_user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cvn_friend
-- ----------------------------

-- ----------------------------
-- Table structure for cvn_friend_request
-- ----------------------------
DROP TABLE IF EXISTS `cvn_friend_request`;
CREATE TABLE `cvn_friend_request`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CREATE_AT` timestamp NOT NULL,
  `DESCRIPTION` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `TARGET_ID` int NOT NULL,
  `USER_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `PRIMARY_KEY_E`(`ID` ASC) USING BTREE,
  INDEX `FK_PS9W5F936O80QC6L7K83ORVMW_INDEX_E`(`USER_ID` ASC) USING BTREE,
  INDEX `FK_S8GANO8WFM617HAIO9XCARPHP_INDEX_E`(`TARGET_ID` ASC) USING BTREE,
  CONSTRAINT `FK_PS9W5F936O80QC6L7K83ORVMW` FOREIGN KEY (`USER_ID`) REFERENCES `cvn_user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_S8GANO8WFM617HAIO9XCARPHP` FOREIGN KEY (`TARGET_ID`) REFERENCES `cvn_user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cvn_friend_request
-- ----------------------------

-- ----------------------------
-- Table structure for cvn_group
-- ----------------------------
DROP TABLE IF EXISTS `cvn_group`;
CREATE TABLE `cvn_group`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CREATE_AT` timestamp NOT NULL,
  `DESCRIPTION` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ENABLED` tinyint(1) NOT NULL,
  `NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `PASSWORD` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `CREATOR_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UK_70EXW1DEEVPG4DIQQM0OV63CQ`(`NAME` ASC) USING BTREE,
  UNIQUE INDEX `UK_9K8JKECACK0MKPKS399WC1D1K`(`NAME` ASC) USING BTREE,
  UNIQUE INDEX `PRIMARY_KEY_7`(`ID` ASC) USING BTREE,
  UNIQUE INDEX `UK_9K8JKECACK0MKPKS399WC1D1K_INDEX_7`(`NAME` ASC) USING BTREE,
  INDEX `FK_P4LV3UR4IQDMBS0TVFHO8WT2N_INDEX_7`(`CREATOR_ID` ASC) USING BTREE,
  CONSTRAINT `FK_P4LV3UR4IQDMBS0TVFHO8WT2N` FOREIGN KEY (`CREATOR_ID`) REFERENCES `cvn_user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cvn_group
-- ----------------------------
INSERT INTO `cvn_group` VALUES (5, '2023-01-18 09:27:17', '1', 1, '1', '1', 99999);
INSERT INTO `cvn_group` VALUES (6, '2023-01-18 09:29:19', '4', 1, '4', '1', 99999);
INSERT INTO `cvn_group` VALUES (7, '2023-01-18 09:30:22', '5', 1, '5', '1', 99999);
INSERT INTO `cvn_group` VALUES (9, '2023-01-18 20:26:23', '123456', 1, '123456', '123456', 99999);
INSERT INTO `cvn_group` VALUES (10, '2023-01-20 08:58:50', 'string', 1, 'string', 'string', 99999);

-- ----------------------------
-- Table structure for cvn_group_admin
-- ----------------------------
DROP TABLE IF EXISTS `cvn_group_admin`;
CREATE TABLE `cvn_group_admin`  (
  `GROUP_ID` int NOT NULL,
  `USER_ID` int NOT NULL,
  PRIMARY KEY (`GROUP_ID`, `USER_ID`) USING BTREE,
  UNIQUE INDEX `PRIMARY_KEY_E3`(`GROUP_ID` ASC, `USER_ID` ASC) USING BTREE,
  INDEX `FK_5PIV848QUA1AB5S91N2UXHNOH_INDEX_E`(`USER_ID` ASC) USING BTREE,
  CONSTRAINT `FK_5PIV848QUA1AB5S91N2UXHNOH` FOREIGN KEY (`USER_ID`) REFERENCES `cvn_user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_OADAY8YXAW8GJFF4LYCB3RMT8` FOREIGN KEY (`GROUP_ID`) REFERENCES `cvn_group` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cvn_group_admin
-- ----------------------------
INSERT INTO `cvn_group_admin` VALUES (5, 99999);
INSERT INTO `cvn_group_admin` VALUES (6, 99999);
INSERT INTO `cvn_group_admin` VALUES (7, 99999);
INSERT INTO `cvn_group_admin` VALUES (9, 99999);

-- ----------------------------
-- Table structure for cvn_group_request
-- ----------------------------
DROP TABLE IF EXISTS `cvn_group_request`;
CREATE TABLE `cvn_group_request`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CREATE_AT` timestamp NOT NULL,
  `DESCRIPTION` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `TARGET_ID` int NOT NULL,
  `USER_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `PRIMARY_KEY_F`(`ID` ASC) USING BTREE,
  INDEX `FK_1TJ4OICEW729LB0M447CK5N2H_INDEX_F`(`TARGET_ID` ASC) USING BTREE,
  INDEX `FK_AC6LNHOFARMHK5DNVRJFPFTM8_INDEX_F`(`USER_ID` ASC) USING BTREE,
  CONSTRAINT `FK_1TJ4OICEW729LB0M447CK5N2H` FOREIGN KEY (`TARGET_ID`) REFERENCES `cvn_group` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_AC6LNHOFARMHK5DNVRJFPFTM8` FOREIGN KEY (`USER_ID`) REFERENCES `cvn_user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cvn_group_request
-- ----------------------------

-- ----------------------------
-- Table structure for cvn_group_user
-- ----------------------------
DROP TABLE IF EXISTS `cvn_group_user`;
CREATE TABLE `cvn_group_user`  (
  `GROUP_ID` int NOT NULL,
  `USER_ID` int NOT NULL,
  INDEX `FK_6WX8ICTH6QJCXFEL54U3P80P9_INDEX_4`(`USER_ID` ASC) USING BTREE,
  INDEX `FK_NFCAX69FJTJROG05JE7IHCHQC_INDEX_4`(`GROUP_ID` ASC) USING BTREE,
  CONSTRAINT `FK_6WX8ICTH6QJCXFEL54U3P80P9` FOREIGN KEY (`USER_ID`) REFERENCES `cvn_user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_NFCAX69FJTJROG05JE7IHCHQC` FOREIGN KEY (`GROUP_ID`) REFERENCES `cvn_group` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cvn_group_user
-- ----------------------------
INSERT INTO `cvn_group_user` VALUES (7, 99999);
INSERT INTO `cvn_group_user` VALUES (5, 99999);
INSERT INTO `cvn_group_user` VALUES (9, 99999);
INSERT INTO `cvn_group_user` VALUES (9, 99998);

-- ----------------------------
-- Table structure for cvn_reset_code
-- ----------------------------
DROP TABLE IF EXISTS `cvn_reset_code`;
CREATE TABLE `cvn_reset_code`  (
  `ID` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CREATE_AT` timestamp NOT NULL,
  `EXPIRE_AT` timestamp NOT NULL,
  `USER_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `PRIMARY_KEY_9`(`ID` ASC) USING BTREE,
  INDEX `FK_6D0RBAEX95DGPV43HSGELBRXX_INDEX_9`(`USER_ID` ASC) USING BTREE,
  CONSTRAINT `FK_6D0RBAEX95DGPV43HSGELBRXX` FOREIGN KEY (`USER_ID`) REFERENCES `cvn_user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cvn_reset_code
-- ----------------------------

-- ----------------------------
-- Table structure for cvn_user
-- ----------------------------
DROP TABLE IF EXISTS `cvn_user`;
CREATE TABLE `cvn_user`  (
  `ID` int NOT NULL,
  `ADMIN` tinyint(1) NOT NULL,
  `ALLOWPASS1` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ALLOWPASS2` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `CAN_ADDFRIEND` tinyint(1) NOT NULL,
  `CAN_CREATE_GROUP` tinyint(1) NOT NULL,
  `CREATE_AT` timestamp NOT NULL,
  `DESCRIPTION` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `DOSPASS` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `EMAIL` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ENABLED` tinyint(1) NOT NULL,
  `FRIENDPASS` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `NICK_NAME` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `PASSWORD` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `PASSWORD_HASH` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `RECIVE_LIMIT` bigint NOT NULL,
  `REGISTER_IP` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `SEND_LIMIT` bigint NOT NULL,
  `UPDATE_AT` timestamp NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `PRIMARY_KEY_7F`(`ID` ASC) USING BTREE,
  UNIQUE INDEX `UK_5IIE24XSE02BDHKB2WDPNTARK`(`NAME` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cvn_user
-- ----------------------------
INSERT INTO `cvn_user` VALUES (1, 0, NULL, NULL, 1, 1, '2023-01-20 08:43:25', '2', NULL, NULL, 1, NULL, '1101839859@qq.com', '1101839859', 'Lu_yJKcnxHOVmQGS8DhziWHtfKqC1i-IMmVh-hTFWqZbzMOUFMfvFBc1Zyf7KzhV9kXDSNJbTZJQSNIG0q2OXbbr3gg5gCMaKM74lxnx8uA42Ow2l-qMN8-qDJUR7O_qjkqBhWPaJEWqMsaEotfnjJ96c1TgioZ3y9iC-JyzSTM', 'fe0cefb2f83f7fe5f4ef633fa308ad5d', 0, '127.0.0.1', 0, '2023-01-20 08:43:25');
INSERT INTO `cvn_user` VALUES (2, 0, NULL, NULL, 1, 1, '2023-01-20 08:01:59', '2', NULL, NULL, 1, NULL, '2', '2', 'Lu_yJKcnxHOVmQGS8DhziWHtfKqC1i-IMmVh-hTFWqZbzMOUFMfvFBc1Zyf7KzhV9kXDSNJbTZJQSNIG0q2OXbbr3gg5gCMaKM74lxnx8uA42Ow2l-qMN8-qDJUR7O_qjkqBhWPaJEWqMsaEotfnjJ96c1TgioZ3y9iC-JyzSTM', 'fe0cefb2f83f7fe5f4ef633fa308ad5d', 0, '127.0.0.1', 0, '2023-01-20 08:02:07');
INSERT INTO `cvn_user` VALUES (99998, 0, '123456', '12345678', 1, 1, '2023-01-18 20:15:10', '0', '', NULL, 1, '12345678', '0', '0', 'pUMZeWDsWaiAJyHybsqjqEU73Su3PnCmuBraGfJGf64Xlgj4FDf8Uw_cPbwn8cJCSipkqz2yBZZhltzZ4VVDDAtzPY6aAxA-3z5w9UJMm532sOzRSZk5IzBp3K9zF6rRINEwlJYKbH1XKgfqZQSFGhX5hbE0HcP2AhCJUStFBa4', '063475ccec3a7319f873dcf747ab2c3d', 0, '192.168.175.138', 0, '2023-01-18 20:26:43');
INSERT INTO `cvn_user` VALUES (99999, 0, NULL, NULL, 1, 1, '2023-01-17 11:38:19', '1', NULL, NULL, 1, NULL, '1', '1', 'MOMRNuUux9vKZ8zEpxvBXVFBlCFug4PydoP91bG5MK3vf6dAwxQtxQ4F1nh48ZjgXSy4Z2JJh4XTzTEc3bLThGJUGE3n7-bxlup9EPxgXg_NJjbdOPuRsl3xgl-y7RC1PeI_77ODR4UdAkkD0sl9Mf9C3UYkSIDLn8bAm_t5ZNw', '0ba81ab62cdfd023c225cf9f77f3ee54', 0, '127.0.0.1', 0, '2023-01-18 20:26:23');

-- ----------------------------
-- Table structure for cvn_user_ex
-- ----------------------------
DROP TABLE IF EXISTS `cvn_user_ex`;
CREATE TABLE `cvn_user_ex`  (
  `ID` int NOT NULL AUTO_INCREMENT,
  `ATTR` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `LAST_LOGIN_AT` timestamp NULL DEFAULT NULL,
  `LAST_LOGIN_IP` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `RECIVE_FROM_SERVER` bigint NOT NULL,
  `SEND_TO_SERVER` bigint NOT NULL,
  `USER_IS_ONLINE` tinyint(1) NOT NULL,
  `USER_ID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `PRIMARY_KEY_E3F`(`ID` ASC) USING BTREE,
  INDEX `FK_P74Y8GRWKD53ERI8HIIPPRAQS_INDEX_E`(`USER_ID` ASC) USING BTREE,
  CONSTRAINT `FK_P74Y8GRWKD53ERI8HIIPPRAQS` FOREIGN KEY (`USER_ID`) REFERENCES `cvn_user` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cvn_user_ex
-- ----------------------------
INSERT INTO `cvn_user_ex` VALUES (1, '{}', '2023-01-18 20:21:37', '127.0.0.1', 7800, 7347, 0, 99999);
INSERT INTO `cvn_user_ex` VALUES (2, NULL, '2023-01-18 20:21:26', '192.168.175.138', 1617, 1106, 0, 99998);
INSERT INTO `cvn_user_ex` VALUES (3, NULL, NULL, NULL, 0, 0, 0, 2);
INSERT INTO `cvn_user_ex` VALUES (4, NULL, NULL, NULL, 0, 0, 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
