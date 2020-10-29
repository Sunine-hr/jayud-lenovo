## 佳裕达小工具-SQL脚本
## jayud-tools
## SQL脚本

-- ----------------------------
-- Table structure for sensitive_commodity
-- ----------------------------
DROP TABLE IF EXISTS `sensitive_commodity`;
CREATE TABLE `sensitive_commodity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '品名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 178 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '敏感品名表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for cargo_name
-- ----------------------------
DROP TABLE IF EXISTS `cargo_name`;
CREATE TABLE `cargo_name`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `xh` bigint(20) NULL DEFAULT NULL COMMENT '序号',
  `dh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '袋号',
  `dz` decimal(10, 4) NULL DEFAULT NULL COMMENT '袋重',
  `ytdh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '圆通单号',
  `tdh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提单号',
  `sl` int(11) NULL DEFAULT NULL COMMENT '数量(PCS)',
  `zl` decimal(10, 4) NULL DEFAULT NULL COMMENT '重量(PCS)',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `hpmc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '货品名称',
  `js` int(11) NULL DEFAULT NULL COMMENT '件数',
  `pce` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'PCE',
  `jz` decimal(10, 4) NULL DEFAULT NULL COMMENT '价值',
  `xm1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名1',
  `xm2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名2',
  `address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `hm1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '号码1',
  `xm3` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名3',
  `hm2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '号码2',
  `bjdh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标记单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 256 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '货物名称表' ROW_FORMAT = DYNAMIC;
