-- 创建数据库
CREATE DATABASE IF NOT EXISTS book_chat DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE book_chat;

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(加密存储)',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    open_id VARCHAR(64) COMMENT '微信OpenID',
    enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用: 0-禁用, 1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_open_id (open_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 书籍表
CREATE TABLE IF NOT EXISTS t_book (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    title VARCHAR(100) NOT NULL COMMENT '书名',
    author VARCHAR(100) COMMENT '作者',
    publisher VARCHAR(100) COMMENT '出版社',
    isbn VARCHAR(20) NOT NULL COMMENT 'ISBN编码',
    cover VARCHAR(255) COMMENT '封面图片URL',
    description TEXT COMMENT '描述',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_isbn (isbn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表';

-- 聊天记录表
CREATE TABLE IF NOT EXISTS t_chat_message (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    book_id BIGINT NOT NULL COMMENT '书籍ID',
    content TEXT NOT NULL COMMENT '消息内容',
    type VARCHAR(10) NOT NULL COMMENT '消息类型: user-用户消息, ai-AI回复',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_book_id (book_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天记录表';

-- 初始化测试数据
-- 添加测试用户(密码：123456)
INSERT INTO t_user (username, password, nickname, avatar) VALUES 
('test', '$2a$10$aF5FVE8W7HzBQ.wX3nEJruriweGdE4RYqU7oB1XzODtRJ/2dQeZ02', '测试用户', 'https://example.com/avatar.png');

-- 添加测试书籍数据
INSERT INTO t_book (title, author, publisher, isbn, cover, description) VALUES 
('三体', '刘慈欣', '重庆出版社', '9787536692930', 'https://img3.doubanio.com/view/subject/l/public/s2768378.jpg', '文化大革命如火如荼进行的同时，军方探寻外星文明的绝秘计划"红岸工程"取得了突破性进展。但在按下发射键的那一刻，历经劫难的叶文洁没有意识到，她彻底改变了人类的命运。地球文明向宇宙发出的第一声啼鸣，以太阳为中心，以光速向宇宙深处飞驰……'),
('活着', '余华', '作家出版社', '9787506365437', 'https://img2.doubanio.com/view/subject/l/public/s29053580.jpg', '《活着》是余华的代表作，讲述了一个人一生的故事，这是一个历尽世间沧桑和磨难老人的人生感言，是一幕演绎人生苦难经历的戏剧。小说的叙事者"我"在去乡下收集民间歌谣的途中，遇到了一个爱讲故事的老人富贵，听他讲述了自己坎坷的人生经历。'),
('人类简史: 从动物到上帝', '[以色列] 尤瓦尔·赫拉利', '中信出版社', '9787508647357', 'https://img9.doubanio.com/view/subject/l/public/s27814883.jpg', '《人类简史》是以色列新锐历史学家的一部重磅作品。从十万年前有生命迹象开始到21世纪资本、科技交织的人类发展史。挑战长久以来形成的关于人类进化的常识，这本书带领读者穿越过去，重新了解人类近几万年的发展历程。');
