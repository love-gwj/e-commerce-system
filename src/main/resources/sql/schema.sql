-- 电商系统数据库脚本

CREATE DATABASE IF NOT EXISTS ecommerce DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ecommerce;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `avatar` VARCHAR(255) COMMENT '头像',
    `gender` TINYINT DEFAULT 0 COMMENT '性别: 0-未知, 1-男, 2-女',
    `birthday` DATE COMMENT '生日',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID',
    `level` INT DEFAULT 1 COMMENT '层级',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `icon` VARCHAR(255) COMMENT '图标',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `description` TEXT COMMENT '商品描述',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `original_price` DECIMAL(10,2) COMMENT '原价',
    `stock` INT DEFAULT 0 COMMENT '库存',
    `category_id` BIGINT COMMENT '分类ID',
    `main_image` VARCHAR(255) COMMENT '主图',
    `images` TEXT COMMENT '图片列表(JSON)',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-下架, 1-上架',
    `sales` INT DEFAULT 0 COMMENT '销量',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 购物车表
CREATE TABLE IF NOT EXISTS `cart` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `quantity` INT DEFAULT 1 COMMENT '数量',
    `selected` TINYINT DEFAULT 1 COMMENT '是否选中',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 订单表
CREATE TABLE IF NOT EXISTS `orders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-待支付, 1-已支付, 2-已发货, 3-已完成, 4-已取消',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `receiver_address` VARCHAR(255) NOT NULL COMMENT '收货地址',
    `pay_time` DATETIME COMMENT '支付时间',
    `deliver_time` DATETIME COMMENT '发货时间',
    `receive_time` DATETIME COMMENT '收货时间',
    `cancel_time` DATETIME COMMENT '取消时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单项表
CREATE TABLE IF NOT EXISTS `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `product_id` BIGINT NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(255) COMMENT '商品图片',
    `price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `quantity` INT NOT NULL COMMENT '数量',
    `total_price` DECIMAL(10,2) NOT NULL COMMENT '小计',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

-- 地址表
CREATE TABLE IF NOT EXISTS `address` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) NOT NULL COMMENT '区/县',
    `detail_address` VARCHAR(255) NOT NULL COMMENT '详细地址',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地址表';

-- 插入测试数据
INSERT INTO `category` (`name`, `parent_id`, `level`, `sort`) VALUES 
('电子产品', 0, 1, 1),
('服装', 0, 1, 2),
('食品', 0, 1, 3),
('手机', 1, 2, 1),
('电脑', 1, 2, 2),
('上衣', 2, 2, 1),
('裤子', 2, 2, 2);

INSERT INTO `product` (`name`, `description`, `price`, `original_price`, `stock`, `category_id`, `status`, `sales`) VALUES
('iPhone 15 Pro', '苹果最新款手机', 8999.00, 9999.00, 100, 4, 1, 500),
('MacBook Pro', '苹果笔记本电脑', 15999.00, 17999.00, 50, 5, 1, 200),
('纯棉T恤', '舒适纯棉面料', 99.00, 199.00, 1000, 6, 1, 1000),
('牛仔裤', '经典直筒版型', 199.00, 299.00, 500, 7, 1, 800);
