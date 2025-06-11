/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80405 (8.4.5)
 Source Host           : localhost:3306
 Source Schema         : javafx_sm

 Target Server Type    : MySQL
 Target Server Version : 80405 (8.4.5)
 File Encoding         : 65001

 Date: 08/06/2025 20:27:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `author` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `likes` int NULL DEFAULT 0,
  `note_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1931006723239735298 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES (1930297490688708610, '鸭子25元一只，每天现做现卖，高峰期一天能卖200多只。\"济南一烤鸭店的店员向记者介绍道。大观园的一家手撕鸭店，半只烤鸭+半斤油饼的组合，仅需23.8元。然而农贸市场上，农户养殖自售的整只活鸭却要20元左右一斤。记者调查发现，在这一价格差背后，隐藏着从养殖到售卖的完整经济链条。\n据批发市场上的业户介绍，一只鸭子早已衍生出了一条产业链，不仅鸭翅、鸭腿、鸭胗可以被分割之后单独售卖，连鸭毛、鸭血、鸭肠这些看似不起眼的部件都能得到很好的利用。因为\"鸭子浑身都是宝\"，所以用来做烤鸭的白条鸭鸭身的价格并不高，只占到一只肉鸭总体价值的25%左右', 'tom', '2025-06-05 00:16:00', 0, 1930297311315103745);
INSERT INTO `comments` VALUES (1930297569499680769, '1212鸭子25元一只，每天现做现卖，高峰期一天能卖200多只。\"济南一烤鸭店的店员向记者介绍道。大观园的一家手撕鸭店，半只烤鸭+半斤油饼的组合，仅需23.8元。然而农贸市场上，农户养殖自售的整只活鸭却要20元左右一斤。记者调查发现，在这一价格差背后，隐藏着从养殖到售卖的完整经济链条。\n据批发市场上的业户介绍，一只鸭子早已衍生出了一条产业链，不仅鸭翅、鸭腿、鸭胗可以被分割之后单独售卖，连鸭毛、鸭血、鸭肠这些看似不起眼的部件都能得到很好的利用。因为\"鸭子浑身都是宝\"，所以用来做烤鸭的白条鸭鸭身的价格并不高，只占到一只肉鸭总体价值的25%左右', 'tom', '2025-06-05 00:16:18', 0, 1930297311315103745);
INSERT INTO `comments` VALUES (1930298754961039361, '鸭子25元一只，每天现做现卖，高峰期一天能卖200多只。\"济南一烤鸭店的店员向记者介绍道。大观园的一家手撕鸭店，半只烤鸭+半斤油饼的组合，仅需23.8元。然而农贸市场上，农户养殖自售的整只活鸭却要20元左右一斤。记者调查发现，在这一价格差背后，隐藏着从养殖到售卖的完整经济链条。\n据批发市场上的业户介绍，一只鸭子早已衍生出了一条产业链，不仅鸭翅、鸭腿、鸭胗可以被分割之后单独售卖，连鸭毛、鸭血、鸭肠这些看似不起眼的部件都能得到很好的利用。因为\"', 'tom', '2025-06-05 00:21:01', 0, 1930297311315103745);
INSERT INTO `comments` VALUES (1930299868007739393, '鸭子25元一只，每天现做现卖，高峰期一天能卖200多只。\"济南一烤鸭店的店员向记者介绍道。大观园的一家手撕鸭店，半只烤鸭+半斤油饼的组合，仅需23.8元。然而农贸市场上，农户养殖自售的整只活鸭却要20元左右一斤。记者调查发现，在这一价格差背后，隐藏着从养殖到售卖的完整经济链条。\n据批发市场上的业户介绍，一只鸭子早已衍生出了一条产业链，不仅鸭翅、鸭腿、鸭胗可以被分割之后单独售卖，连鸭毛、鸭血、鸭肠这些看似不起眼的部件都能得到很好的利用。因为\"鸭子浑身都是宝\"，所以用来做烤鸭的白条鸭鸭身的价格并不高，只占到一只肉鸭总体价值的25%左右', 'admin', '2025-06-05 00:25:26', 0, 1930299809727885313);
INSERT INTO `comments` VALUES (1930646811095425025, '鸭子25元一只，每天现做现卖，高峰期一天能卖200多只。\"济南一烤鸭店的店员向记者介绍道。大观园的一家手撕鸭店，半只烤鸭+半斤油饼的组合，仅需23.8元。然而农贸市场上，农户养殖自售的整只活鸭却要20元左右一斤。记者调查发现，在这一价格差背后，隐藏着从养殖到售卖的完整经济链条。\n据批发市场上的业户介绍，一只鸭子早已衍生出了一条产业链，不仅鸭翅、鸭腿、鸭胗可以被分割之后单独售卖，连鸭毛、鸭血、鸭肠这些看似不起眼的部件都能得到很好的利用。因为\"鸭子浑身都是宝\"，所以用来做烤鸭的白条鸭鸭身的价格并不高，只占到一只肉鸭总体价值的25%左右', 'admin', '2025-06-05 23:24:04', 0, 1930613261482684418);
INSERT INTO `comments` VALUES (1930989277263024130, '随时随地，唤起你的桌面 AI 助手', 'admin', '2025-06-06 22:04:54', 0, 1930601243073609729);
INSERT INTO `comments` VALUES (1930989378001817602, '随时随地，唤起你的桌面 AI 助手', 'admin', '2025-06-06 22:05:18', 0, 1930603533935374337);
INSERT INTO `comments` VALUES (1930990861984296962, '其实吃鸭子来说应该是最安全的。因为鸭子很少生病，一般搞了疫苗后用药很少，抗生素用量比不上肉鸡的零头，便宜是因为肉鸭生长快，一般不到一个月就能出栏，料比很低，成本自然较低，加上肉鸭副产品附加值高，成本就更低了。', 'admin', '2025-06-06 22:11:12', 0, 1930297311315103745);
INSERT INTO `comments` VALUES (1930991470871396354, '鸭坯批发九块多 不要十块钱，单一条鸭舌就要一块 其他不太清楚，反正鸭身上最便宜的就是肉，毛都比它值钱', 'admin', '2025-06-06 22:13:37', 0, 1930297311315103745);
INSERT INTO `comments` VALUES (1931002124210610177, '高考前一天，哪些事该做，哪些事千万别做？10件事一定要做', 'tom', '2025-06-06 22:55:57', 0, 1930646937662742530);
INSERT INTO `comments` VALUES (1931002161850294274, '你稳住节奏、调整状态。愿每一位高考学子都能胸有成竹，提笔从容，落笔生花。', 'tom', '2025-06-06 22:56:06', 0, 1930646937662742530);
INSERT INTO `comments` VALUES (1931003620159459330, '你刷过的每一道题都不会白费，相信自己！', 'tom', '2025-06-06 23:01:54', 0, 1930966402422358017);
INSERT INTO `comments` VALUES (1931003653957160962, '希望所有高考生都能心态稳、不紧张，答题顺顺利利，考出好成绩', 'tom', '2025-06-06 23:02:02', 0, 1930966402422358017);
INSERT INTO `comments` VALUES (1931005081106903041, '风油精应该能带进考场吧？犯困的时候抹一抹超提神', 'tom', '2025-06-06 23:07:42', 0, 1930646937662742530);
INSERT INTO `comments` VALUES (1931005108361490434, '红牛喝多了手容易抖，想提神还是悠着点！', 'tom', '2025-06-06 23:07:49', 0, 1930646937662742530);
INSERT INTO `comments` VALUES (1931006479819165697, '早饭别吃太撑，七八分饱刚刚好，不然容易犯困。', 'admin', '2025-06-06 23:13:16', 0, 1931006181750870018);
INSERT INTO `comments` VALUES (1931006506696265729, '一只小的蚂蚱油腻早餐千万别吃！包子配豆浆，营养又顶饱！', 'admin', '2025-06-06 23:13:22', 0, 1931006181750870018);
INSERT INTO `comments` VALUES (1931006646978899969, '难题都是吓唬人的，把基础题做好才是关键！', 'tom', '2025-06-06 23:13:56', 0, 1931006181750870018);
INSERT INTO `comments` VALUES (1931006674464174082, '突然改变作息时间可不行，生物钟会乱套的！', 'tom', '2025-06-06 23:14:02', 0, 1931006181750870018);
INSERT INTO `comments` VALUES (1931006723239735297, '希望所有高考生都能心态稳、不紧张，答题顺顺利利，考出好成绩', 'tom', '2025-06-06 23:14:14', 0, 1931006181750870018);

-- ----------------------------
-- Table structure for minganci
-- ----------------------------
DROP TABLE IF EXISTS `minganci`;
CREATE TABLE `minganci`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `contentx` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '内容',
  `bjtime` datetime NULL DEFAULT NULL COMMENT '标记时间',
  `status` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '处理状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of minganci
-- ----------------------------
INSERT INTO `minganci` VALUES (3, 'sds', '2025-06-01 20:15:20', '待处理');

-- ----------------------------
-- Table structure for moods
-- ----------------------------
DROP TABLE IF EXISTS `moods`;
CREATE TABLE `moods`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `mood` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '心情文本',
  `fh` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片名称',
  `record_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_record_time`(`record_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户心情记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of moods
-- ----------------------------
INSERT INTO `moods` VALUES (6, 2, '烦躁', 'upset.png', '2025-06-01 05:58:43');
INSERT INTO `moods` VALUES (8, 2, '心动', 'spectacular.png', '2025-06-01 06:11:24');
INSERT INTO `moods` VALUES (9, 2, '心动', 'spectacular.png', '2025-06-01 06:15:47');
INSERT INTO `moods` VALUES (10, 2, '开心', 'good.png', '2025-06-02 06:15:47');
INSERT INTO `moods` VALUES (12, 2, '开心', 'good.png', '2025-06-01 06:22:21');
INSERT INTO `moods` VALUES (13, 2, '兴奋', 'happy.png', '2025-06-01 10:37:24');
INSERT INTO `moods` VALUES (14, 2, '开心', 'good.png', '2025-06-01 17:22:38');
INSERT INTO `moods` VALUES (15, 2, '伤心', 'sad.png', '2025-06-01 17:35:47');
INSERT INTO `moods` VALUES (17, 2, '伤心', 'sad.png', '2025-06-01 19:58:45');
INSERT INTO `moods` VALUES (18, 2, '烦躁', 'upset.png', '2025-06-01 20:00:29');
INSERT INTO `moods` VALUES (19, 2, '生气', 'angry.png', '2025-06-01 20:02:34');
INSERT INTO `moods` VALUES (20, 2, '生气', 'angry.png', '2025-06-01 20:10:03');
INSERT INTO `moods` VALUES (21, 5, '生气', 'angry.png', '2025-06-01 20:20:17');
INSERT INTO `moods` VALUES (22, 2, '生气', 'angry.png', '2025-06-01 20:23:14');
INSERT INTO `moods` VALUES (23, 2, '生气', 'angry.png', '2025-06-01 20:41:59');
INSERT INTO `moods` VALUES (24, 2, '生气', 'angry.png', '2025-06-01 20:47:32');
INSERT INTO `moods` VALUES (25, 4, '烦躁', 'upset.png', '2025-06-01 21:07:16');
INSERT INTO `moods` VALUES (27, 1931005936241479682, '烦躁', 'upset.png', '2025-06-08 20:22:05');

-- ----------------------------
-- Table structure for notes
-- ----------------------------
DROP TABLE IF EXISTS `notes`;
CREATE TABLE `notes`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `likes` int NULL DEFAULT 0,
  `comment_count` int NULL DEFAULT 0,
  `image_height` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1931006181750870019 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notes
-- ----------------------------
INSERT INTO `notes` VALUES (1930297311315103745, '2323', '2323', 'admin', 'file:/F:/02_packages/05_pc_media/pictures/蓝屏警告.jpg', 0, 5, 0, '2025-06-05 00:15:16');
INSERT INTO `notes` VALUES (1930299809727885313, '23232', '232', 'admin', '2025-06-05\\8f4baca9917b72c9e6164a.jpg', 0, 1, 0, '2025-06-05 00:25:12');
INSERT INTO `notes` VALUES (1930600692088909826, '2323', '232', 'admin', '2025-06-05\\8f4baca9917b72c9e6164a.jpg', 0, 0, 0, '2025-06-05 20:20:48');
INSERT INTO `notes` VALUES (1930600847546593282, '2323', '232', 'admin', '2025-06-05\\8f4baca9917b72c9e6164a.jpg', 0, 0, 0, '2025-06-05 20:21:25');
INSERT INTO `notes` VALUES (1930601243073609729, '232', '2323', 'admin', '2025-06-05\\8f4baca9917b72c9e6164a.jpg', 0, 1, 0, '2025-06-05 20:22:59');
INSERT INTO `notes` VALUES (1930603533935374337, '胜多负少发达地方撒地方', '胜多负少发达地方撒地方胜多负少发达地方撒地方胜多负少发达地方撒地方胜多负少发达地方撒地方胜多负少发达地方撒地方胜多负少发达地方撒地方', 'admin', '2025-06-05\\75450f8d38a29f0063dbc2.png', 1, 1, 0, '2025-06-05 20:32:06');
INSERT INTO `notes` VALUES (1930613261482684418, '23', '2323', 'admin', '2025-06-05\\0d4392a3da0aaeca96f243.png', 2, 1, 0, '2025-06-05 21:10:45');
INSERT INTO `notes` VALUES (1930646937662742530, '23', '23', 'admin', '2025-06-05\\4c4fecba0f7a4b81bb4a2b.jpg', 1, 4, 400, '2025-06-05 23:24:34');
INSERT INTO `notes` VALUES (1930966402422358017, '活鸭20元一斤，烤鸭为何20多元一只？', '鸭子25元一只，每天现做现卖，高峰期一天能卖200多只。\"济南一烤鸭店的店员向记者介绍道。大观园的一家手撕鸭店，半只烤鸭+半斤油饼的组合，仅需23.8元。然而农贸市场上，农户养殖自售的整只活鸭却要20元左右一斤。记者调查发现，在这一价格差背后，隐藏着从养殖到售卖的完整经济链条。\n据批发市场上的业户介绍，一只鸭子早已衍生出了一条产业链，不仅鸭翅、鸭腿、鸭胗可以被分割之后单独售卖，连鸭毛、鸭血、鸭肠这些看似不起眼的部件都能得到很好的利用。因为\"鸭子浑身都是宝\"，所以用来做烤鸭的白条鸭鸭身的价格并不高，只占到一只肉鸭总体价值的25%左右', 'tom', '2025-06-06\\744a3b9e424482f2cc57dc.png', 1, 2, 400, '2025-06-06 20:34:00');
INSERT INTO `notes` VALUES (1931006181750870018, '高考前一天，十要做和十不做！', '高考前一天，哪些事该做，哪些事千万别做？10件事一定要做、10个雷区千万别踩，帮你稳住节奏、调整状态。愿每一位高考学子都能胸有成竹，提笔从容，落笔生花。', 'rose', '2025-06-06\\054f31bc84eb59c76da35f.png', 2, 5, 400, '2025-06-06 23:12:04');

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `note_id` bigint NULL DEFAULT NULL COMMENT '所属帖子',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者',
  `type` int NULL DEFAULT NULL COMMENT '1:点赞，2：评论',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '评论内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1931006723487199235 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (1, 1930297311315103745, 'admin', 2, 'sdfsdfsdf', '2025-06-06 22:38:41');
INSERT INTO `record` VALUES (1931002124508405762, 1930646937662742530, 'tom', 2, '高考前一天，哪些事该做，哪些事千万别做？10件事一定要做', '2025-06-06 22:55:57');
INSERT INTO `record` VALUES (1931002162030649345, 1930646937662742530, 'tom', 2, '你稳住节奏、调整状态。愿每一位高考学子都能胸有成竹，提笔从容，落笔生花。', '2025-06-06 22:56:06');
INSERT INTO `record` VALUES (1931002321976242178, 1930646937662742530, 'admin', 1, NULL, '2025-06-06 22:56:45');
INSERT INTO `record` VALUES (1931003620474032129, 1930966402422358017, 'tom', 2, '你刷过的每一道题都不会白费，相信自己！', '2025-06-06 23:01:54');
INSERT INTO `record` VALUES (1931003654133321729, 1930966402422358017, 'tom', 2, '希望所有高考生都能心态稳、不紧张，答题顺顺利利，考出好成绩', '2025-06-06 23:02:02');
INSERT INTO `record` VALUES (1931005081375338497, 1930646937662742530, 'tom', 2, '风油精应该能带进考场吧？犯困的时候抹一抹超提神', '2025-06-06 23:07:42');
INSERT INTO `record` VALUES (1931005108600565761, 1930646937662742530, 'tom', 2, '红牛喝多了手容易抖，想提神还是悠着点！', '2025-06-06 23:07:49');
INSERT INTO `record` VALUES (1931006402610417665, 1931006181750870018, 'admin', 1, NULL, '2025-06-06 23:12:57');
INSERT INTO `record` VALUES (1931006479999520769, 1931006181750870018, 'admin', 2, '早饭别吃太撑，七八分饱刚刚好，不然容易犯困。', '2025-06-06 23:13:16');
INSERT INTO `record` VALUES (1931006506922758146, 1931006181750870018, 'admin', 2, '一只小的蚂蚱油腻早餐千万别吃！包子配豆浆，营养又顶饱！', '2025-06-06 23:13:22');
INSERT INTO `record` VALUES (1931006613311221762, 1931006181750870018, 'tom', 1, NULL, '2025-06-06 23:13:48');
INSERT INTO `record` VALUES (1931006647184420865, 1931006181750870018, 'tom', 2, '难题都是吓唬人的，把基础题做好才是关键！', '2025-06-06 23:13:56');
INSERT INTO `record` VALUES (1931006674816495618, 1931006181750870018, 'tom', 2, '突然改变作息时间可不行，生物钟会乱套的！', '2025-06-06 23:14:02');
INSERT INTO `record` VALUES (1931006723487199234, 1931006181750870018, 'tom', 2, '希望所有高考生都能心态稳、不紧张，答题顺顺利利，考出好成绩', '2025-06-06 23:14:14');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `registration_time` datetime NOT NULL,
  `last_login_time` datetime NULL DEFAULT NULL,
  `avatar_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1931005936241479683 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', 'u2ser1@example.com', '2025-06-01 04:54:05', '2025-06-08 20:23:08', '2025-06-06\\7442ac9b5ba87fe10a2d0d.png');
INSERT INTO `user` VALUES (2, 'adminuser', '123456', 'admin@example.com', '2025-06-01 04:54:05', '2025-06-01 20:46:09', NULL);
INSERT INTO `user` VALUES (1930953382304468993, 'tom', '123456', 'tom@163.com', '2025-06-06 19:42:16', '2025-06-06 23:13:45', NULL);
INSERT INTO `user` VALUES (1931005936241479682, 'rose', '123456', 'rose@163.com', '2025-06-06 23:11:06', '2025-06-08 20:21:53', '2025-06-06\\21415aa839359c93461109.jpeg');

-- ----------------------------
-- Table structure for user_likes
-- ----------------------------
DROP TABLE IF EXISTS `user_likes`;
CREATE TABLE `user_likes`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `note_id` int NULL DEFAULT NULL,
  `comment_id` int NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_note_like`(`user_name` ASC, `note_id` ASC) USING BTREE,
  UNIQUE INDEX `unique_comment_like`(`user_name` ASC, `comment_id` ASC) USING BTREE,
  INDEX `note_id`(`note_id` ASC) USING BTREE,
  INDEX `comment_id`(`comment_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_likes
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
