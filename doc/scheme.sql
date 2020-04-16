SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo_order
-- ----------------------------
DROP TABLE IF EXISTS `demo_order`;
CREATE TABLE `demo_order` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `commodity_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                              `pay_type` int(5) DEFAULT NULL,
                              `order_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                              `newpay_no` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                              `status` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
                              `create_time` datetime DEFAULT NULL,
                              `end_time` datetime DEFAULT NULL,
                              `price` bigint(20) DEFAULT NULL,
                              PRIMARY KEY (`id`) USING BTREE,
                              KEY `status_idx` (`status`,`end_time`) USING BTREE,
                              KEY `uid_idx` (`order_no`) USING BTREE,
                              KEY `upper_idx` (`newpay_no`) USING BTREE,
                              KEY `self_bank_idx` (`price`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
