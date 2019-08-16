--基于数据库序列方式生成id脚本
-- 序列表
DROP TABLE IF EXISTS `sequence`;
CREATE TABLE `sequence` (
  `name` VARCHAR(50) COLLATE utf8_bin NOT NULL COMMENT '序列的名字，唯一',
  `current_value` BIGINT(20) NOT NULL COMMENT '当前的值',
  `increment_value` INT(11) NOT NULL DEFAULT '1' COMMENT '步长，默认为1',
  PRIMARY KEY (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='公共的序列表，用于为非自增且要求唯一的字段记录和获取唯一ID。';

INSERT  INTO `sequence`(`name`,`current_value`,`increment_value`) VALUES ('user',0,5000);
-- 序列调用函数
DELIMITER $$
DROP FUNCTION IF EXISTS `seq_currval`$$
CREATE FUNCTION `seq_currval`(seq_name VARCHAR(50)) RETURNS INT(11)
    READS SQL DATA
	BEGIN
		DECLARE VALUE INTEGER;
		SET VALUE = 0;
		SELECT current_value INTO VALUE FROM sequence WHERE NAME = seq_name;
		RETURN VALUE;
	END$$
DELIMITER ;
-- 获取当前序列
DELIMITER $$
DROP FUNCTION IF EXISTS `seq_nextval`$$
CREATE FUNCTION `seq_nextval`(seq_name VARCHAR(50)) RETURNS INT(11)
    DETERMINISTIC
	BEGIN
		UPDATE sequence SET current_value = current_value + increment_value WHERE NAME = seq_name;
		RETURN seq_currval(seq_name);
	END$$
DELIMITER ;