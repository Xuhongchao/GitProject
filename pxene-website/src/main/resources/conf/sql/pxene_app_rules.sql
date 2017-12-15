/*
Navicat MySQL Data Transfer

Source Server         : 192.168.3.159
Source Server Version : 50630
Source Host           : 192.168.3.159:3306
Source Database       : pxene_app_rules

Target Server Type    : MYSQL
Target Server Version : 50630
File Encoding         : 65001

Date: 2016-12-14 15:42:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `app_category`
-- ----------------------------
DROP TABLE IF EXISTS `app_category`;
CREATE TABLE `app_category` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `app_category_num` varchar(50) NOT NULL,
  `app_category_name` varchar(100) NOT NULL,
  `app_category_baiduname` varchar(100) DEFAULT NULL,
  `app_category_appstore` varchar(100) DEFAULT NULL,
  `app_category_360name` varchar(100) DEFAULT NULL,
  `app_category_miname` varchar(100) DEFAULT NULL,
  `create_time` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_category
-- ----------------------------
INSERT INTO `app_category` VALUES ('1', '0001', '视频', '影音播放-视频', '娱乐', '影音视听', '影音视听', '2016-12-09');
INSERT INTO `app_category` VALUES ('2', '0002', '音乐', '影音播放-音乐', '音乐', '', '', '2016-12-09');
INSERT INTO `app_category` VALUES ('3', '0003', '购物', '理财购物-网购', '购物', '购物优惠', '时尚购物', '2016-12-09');
INSERT INTO `app_category` VALUES ('4', '0004', '阅读', '资讯阅读-漫画,资讯阅读-电子书', '报刊杂志,图书', '新闻阅读', '图书阅读','2016-12-09');
INSERT INTO `app_category` VALUES ('5', '0005', '导航', '旅游出行-地图导航', '导航', '', '', '2016-12-09');
INSERT INTO `app_category` VALUES ('6', '0006', '社交', '社交通讯', '社交', '通讯社交', '聊天社交', '2016-12-09');
INSERT INTO `app_category` VALUES ('7', '0007', '摄影', '拍摄美化', '摄影与摄像', '摄影摄像', '摄影摄像', '2016-12-09');
INSERT INTO `app_category` VALUES ('8', '0008', '新闻', '资讯阅读-新闻', '体育,新闻', '', '新闻资讯', '2016-12-09');
INSERT INTO `app_category` VALUES ('9', '0009', '工具', '系统工具-浏览器,系统工具-输入法', '工具,天气,商品指南', '系统安全', '实用工具', '2016-12-08');
INSERT INTO `app_category` VALUES ('10', '0010', '美化', '主题壁纸', '', '主题壁纸', '', '2016-12-10');
INSERT INTO `app_category` VALUES ('11', '0011', '教育', '办公学习-考试学习', '教育,参考', '教育学习', '学习教育', '2016-12-09');
INSERT INTO `app_category` VALUES ('12', '0012', '生活', '生活实用-日历,生活实用-天气,生活实用-美食,生活实用-生活服务', '生活,美食佳饮', '生活休闲', '居家生活', '2016-12-09');
INSERT INTO `app_category` VALUES ('13', '0013', '安全', '系统工具-优化,系统工具-安全', '', '', '', '2016-12-09');
INSERT INTO `app_category` VALUES ('14', '0014', '旅游', '旅游出行-旅行攻略,旅游出行-购票,旅游出行-酒店', '旅游', '地图旅游', '旅行交通', '2016-12-09');
INSERT INTO `app_category` VALUES ('15', '0015', '儿童', '', '儿童', '', '', '2016-12-09');
INSERT INTO `app_category` VALUES ('16', '0016', '理财', '理财购物-记账,理财购物-彩票,理财购物-股票基金,理财购物-银行', '财务', '金融理财', '金融理财', '2016-12-09');
INSERT INTO `app_category` VALUES ('17', '0017', '系统', '系统工具-优化,系统工具-安全','', '', '', '2016-12-09');
INSERT INTO `app_category` VALUES ('18', '0018', '健康', '生活实用-医疗健康,生活实用-运动健身,生活实用-母婴', '健身健美,医疗', '健康医疗', '医疗健康', '2016-12-09');
INSERT INTO `app_category` VALUES ('19', '0019', '娱乐', '生活实用-娱乐', '', '', '娱乐消遣', '2016-12-09');
INSERT INTO `app_category` VALUES ('20', '0020', '办公', '办公学习-办公,办公学习-笔记', '效率,商务', '办公商务', '效率办公', '2016-12-09');
INSERT INTO `app_category` VALUES ('21', '0021', '通讯', '社交通讯-通讯', '', '', '', '2016-12-09');
INSERT INTO `app_category` VALUES ('22', '0022', '游戏', '', '', '游戏', '游戏', '2016-12-09');

-- ----------------------------
-- Table structure for `app_crawl_info`
-- ----------------------------
DROP TABLE IF EXISTS `app_crawl_info`;
CREATE TABLE `app_crawl_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `app_id` int(10) NOT NULL,
  `app_crawl_num` varchar(50) NOT NULL,
  `app_industry_id` int(10) NOT NULL,
  `app_crawl_behavior` varchar(100) NOT NULL,
  `app_crawl_domain` varchar(100) NOT NULL,
  `app_crawl_paramreg` varchar(100) NOT NULL,
  `app_crawl_urlreg` varchar(100) NOT NULL,
  `app_crawl_urlexample` varchar(3000) NOT NULL,
  `app_crawl_comments` varchar(100) NOT NULL,
  `app_crawl_createtime` date NOT NULL,
  `app_status` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2355 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_crawl_info
-- ----------------------------

-- ----------------------------
-- Table structure for `app_crawl_list`
-- ----------------------------
DROP TABLE IF EXISTS `app_crawl_list`;
CREATE TABLE `app_crawl_list` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(50) NOT NULL,
  `app_apkName` varchar(50) NOT NULL,
  `app_category_id` int(10) NOT NULL,
  `app_version` varchar(50) NOT NULL,
  `app_update_time` date DEFAULT NULL,
  `app_source` varchar(50) NOT NULL,
  `app_logo_url` varchar(100) NOT NULL,
  `app_download_url` varchar(500) NOT NULL,
  `app_create_time` date NOT NULL,
  `app_status` int(10) NOT NULL DEFAULT '0',
  `app_type` int(10) NOT NULL DEFAULT '0',
  `app_ranking1` int(10) NOT NULL DEFAULT '0',
  `app_ranking2` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10078 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_crawl_list
-- ----------------------------

-- ----------------------------
-- Table structure for `app_industry`
-- ----------------------------
DROP TABLE IF EXISTS `app_industry`;
CREATE TABLE `app_industry` (
  `id` int(50) NOT NULL AUTO_INCREMENT,
  `app_industry_num` varchar(50) COLLATE utf8_bin NOT NULL,
  `app_industry_name` varchar(100) COLLATE utf8_bin NOT NULL,
  `app_fetch_content` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '该行业所出内容',
  `app_paramreg_num` int(10) DEFAULT NULL,
  `create_time` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of app_industry
-- ----------------------------
INSERT INTO `app_industry` VALUES ('1', '0001', '电话号码', '电话号码', '2016-08-01');
INSERT INTO `app_industry` VALUES ('2', '0002', '地理位置', '纬度，经度（参数正则：纬度在前）', '2016-08-02');
INSERT INTO `app_industry` VALUES ('3', '0003', '购车', '汽车码，城市码（参数正则：汽车码在前，城市码若没有补位NULL）', '2016-08-03');
INSERT INTO `app_industry` VALUES ('4', '0004', '电商', '商品编号', '2016-08-04');
INSERT INTO `app_industry` VALUES ('5', '0005', '天气', '城市', '2016-08-05');
INSERT INTO `app_industry` VALUES ('6', '0006', '墙漆', null, '2016-08-06');
INSERT INTO `app_industry` VALUES ('7', '0007', '二手车', '汽车编码', '2016-08-07');
INSERT INTO `app_industry` VALUES ('8', '0008', '飞机票', '出发城市，到达城市，出发时间，参数正则按此顺序，没有为NULL', '2016-08-08');
INSERT INTO `app_industry` VALUES ('9', '0009', '火车票', '出发城市，到达城市，出发时间，参数按此顺序，没有为NULL', '2016-08-09');
INSERT INTO `app_industry` VALUES ('10', '0010', '旅游', '出发地，目的地，出发时间，参数正则按此顺序', '2016-08-10');
INSERT INTO `app_industry` VALUES ('11', '0011', '酒店', '', '2016-08-11');
INSERT INTO `app_industry` VALUES ('12', '0012', '母婴', null, '2016-08-12');
INSERT INTO `app_industry` VALUES ('13', '0013', '综合新闻', '频道id', '2016-08-13');
INSERT INTO `app_industry` VALUES ('14', '0014', '汽车摇号', null, '2016-08-14');
INSERT INTO `app_industry` VALUES ('15', '0015', '股票', '股票编号', '2016-08-15');
INSERT INTO `app_industry` VALUES ('16', '0016', '保险', '保险编号', '2016-08-16');
INSERT INTO `app_industry` VALUES ('17', '0017', '记账', null, '2016-08-17');
INSERT INTO `app_industry` VALUES ('18', '0018', '办公', null, '2016-08-18');
INSERT INTO `app_industry` VALUES ('19', '0019', '女性', null, '2016-08-19');
INSERT INTO `app_industry` VALUES ('20', '0020', '外卖', null, '2016-08-20');
INSERT INTO `app_industry` VALUES ('21', '0021', '图像美化', null, '2016-08-21');
INSERT INTO `app_industry` VALUES ('22', '0022', '相机', null, '2016-08-22');
INSERT INTO `app_industry` VALUES ('23', '0023', '体育', null, '2016-08-23');
INSERT INTO `app_industry` VALUES ('24', '0024', '婚恋', null, '2016-08-24');
INSERT INTO `app_industry` VALUES ('25', '0025', '教育', null, '2016-08-25');
INSERT INTO `app_industry` VALUES ('26', '0026', '家装', null, '2016-08-26');
INSERT INTO `app_industry` VALUES ('27', '0027', '家居', null, '2016-08-27');
INSERT INTO `app_industry` VALUES ('28', '0028', '餐饮菜谱', null, '2016-08-28');
INSERT INTO `app_industry` VALUES ('29', '0029', '阅读', null, '2016-08-29');
INSERT INTO `app_industry` VALUES ('30', '0030', '居家', null, '2016-08-30');
INSERT INTO `app_industry` VALUES ('31', '0031', '招聘', null, '2016-08-31');
INSERT INTO `app_industry` VALUES ('32', '0032', '租房买房', null, '2016-09-01');
INSERT INTO `app_industry` VALUES ('33', '0033', '在线视频', '频道号', '2016-09-02');
INSERT INTO `app_industry` VALUES ('34', '0034', '短视频', null, '2016-09-03');
INSERT INTO `app_industry` VALUES ('35', '0035', '音乐', null, '2016-09-04');
INSERT INTO `app_industry` VALUES ('36', '0036', 'K歌', null, '2016-09-05');
INSERT INTO `app_industry` VALUES ('37', '0037', '图片', null, '2016-09-06');
INSERT INTO `app_industry` VALUES ('38', '0038', '社交', null, '2016-09-07');
INSERT INTO `app_industry` VALUES ('39', '0039', '财经', null, '2016-09-08');
INSERT INTO `app_industry` VALUES ('40', '0040', '城市出行', '出发地，目的地，参数正则按此顺序', '2016-09-09');
INSERT INTO `app_industry` VALUES ('41', '0041', '零售', null, '2016-09-10');
INSERT INTO `app_industry` VALUES ('43', '0043', '生鲜', null, '2016-09-11');
INSERT INTO `app_industry` VALUES ('44', '0044', '理财(支付)', null, '2016-09-12');
INSERT INTO `app_industry` VALUES ('45', '0045', '运动休闲', null, '2016-09-13');
INSERT INTO `app_industry` VALUES ('46', '0046', '投资', null, '2016-09-14');
INSERT INTO `app_industry` VALUES ('47', '0047', '银行', null, '2016-09-15');
INSERT INTO `app_industry` VALUES ('48', '0048', '医疗', null, '2016-09-16');
INSERT INTO `app_industry` VALUES ('49', '0049', '租车用车', null, '2016-09-17');
INSERT INTO `app_industry` VALUES ('50', '0050', '导购平台', null, '2016-09-18');
INSERT INTO `app_industry` VALUES ('51', '0051', '汽车服务', null, '2016-09-19');
INSERT INTO `app_industry` VALUES ('52', '0052', '票务', null, '2016-09-20');
INSERT INTO `app_industry` VALUES ('53', '0053', '物流信息平台', null, '2016-09-21');
INSERT INTO `app_industry` VALUES ('54', '0054', '用户ID', '用户id', '2016-09-22');
INSERT INTO `app_industry` VALUES ('55', '0055', '广告ID', '广告ID（每条只出一个广告id，url多个id时分开多条）', '2016-09-23');
INSERT INTO `app_industry` VALUES ('56', '0056', '汽车票', '出发地，目的地，出发时间，参数正则按此顺序，没有字段为NULL', '2016-10-09');
INSERT INTO `app_industry` VALUES ('57', '0057', '搜索', '搜索关键词', '2016-10-09');
INSERT INTO `app_industry` VALUES ('114', '0058', '购物车', '商品码，数量，参数正则按此顺序', '2016-10-27');
INSERT INTO `app_industry` VALUES ('115', '0059', '身份证号', '身份证号', '2016-10-27');
INSERT INTO `app_industry` VALUES ('116', '0060', '路线', '路线id', '2016-11-22');

-- ----------------------------
-- Table structure for `app_info`
-- ----------------------------
DROP TABLE IF EXISTS `app_info`;
CREATE TABLE `app_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `app_num` varchar(50) NOT NULL,
  `app_name` varchar(50) NOT NULL,
  `app_category_id` int(10) NOT NULL,
  `app_source` varchar(50) DEFAULT NULL,
  `app_domain` varchar(100) NOT NULL,
  `app_attach_param` varchar(100) NOT NULL,
  `app_os` int(10) NOT NULL,
  `app_create_time` date NOT NULL,
  `app_isexport` int(10) DEFAULT NULL,
  `app_logo_url` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_info
-- ----------------------------

-- ----------------------------
-- Table structure for `app_url_count`
-- ----------------------------
DROP TABLE IF EXISTS `app_url_count`;
CREATE TABLE `app_url_count` (
  `url_code` varchar(12) NOT NULL,
  `url_count` int(20) DEFAULT NULL,
  `statistics_time` date DEFAULT NULL,
  PRIMARY KEY (`url_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_url_count
-- ----------------------------
