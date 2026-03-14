# E-Commerce Backend System

<div align="center">

[![Java](https://img.shields.io/badge/Java-17%2B-brightgreen?style=for-the-badge&logo=java)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-red?style=for-the-badge&logo=redis)](https://redis.io/)
[![Elasticsearch](https://img.shields.io/badge/ES-7.17.9-yellow?style=for-the-badge&logo=elasticsearch)](https://www.elastic.co/)
[![RocketMQ](https://img.shields.io/badge/RocketMQ-4.9.4-red?style=for-the-badge&logo=apache)](https://rocketmq.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-brightgreen?style=for-the-badge)](LICENSE)

</div>

> 基于 Spring Boot 构建的分布式电商后端系统，提供完整的用户认证、商品管理、购物车、订单处理等功能。

## 📚 目录

- [项目介绍](#项目介绍)
- [技术架构](#技术架构)
- [功能模块](#功能模块)
- [快速开始](#快速开始)
- [项目结构](#项目结构)
- [API 接口](#api-接口)
- [部署指南](#部署指南)
- [相关文档](#相关文档)

## 🎯 项目介绍

本项目是一个功能完整的电商后端系统，采用微服务架构设计理念，支持高并发、大数据量场景。系统集成了 Redis 缓存、Elasticsearch 全文搜索、RocketMQ 消息队列等先进技术，提供高效、稳定的服务能力。

### 核心特性

- ✅ **用户认证** - Spring Security + JWT 双认证机制
- ✅ **商品搜索** - Elasticsearch 分布式全文搜索
- ✅ **订单处理** - RocketMQ 异步消息确保最终一致性
- ✅ **数据缓存** - Redis 多级缓存提升系统性能
- ✅ **API 文档** - Swagger3 自动生成可交互文档
- ✅ **数据库监控** - Druid 实时监控 SQL 性能

## 🏗 技术架构

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Layer                              │
│                   (Web / Mobile / Mini Program)                  │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                       API Gateway (Nginx)                        │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Spring Boot Application                       │
├─────────────────────────────────────────────────────────────────┤
│  Controller Layer                                               │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐           │
│  │  User    │ │ Product  │ │  Order   │ │  Cart    │           │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘           │
├─────────────────────────────────────────────────────────────────┤
│  Service Layer                                                   │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐           │
│  │  User    │ │ Product  │ │  Order   │ │  Cart    │           │
│  │  Service │ │  Service │ │  Service │ │  Service │           │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘           │
├─────────────────────────────────────────────────────────────────┤
│  Data Access Layer (MyBatis-Plus)                              │
└─────────────────────────────────────────────────────────────────┘
                                │
        ┌───────────────────────┼───────────────────────┐
        │                       │                       │
        ▼                       ▼                       ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   MySQL 8.0   │    │    Redis     │    │ Elasticsearch│
│  (主数据库)    │    │  (缓存层)     │    │  (搜索服务)   │
└──────────────┘    └──────────────┘    └──────────────┘
        │
        ▼
┌──────────────┐
│  RocketMQ    │
│  (消息队列)   │
└──────────────┘
```

### 技术栈

| 分类 | 技术 |
|------|------|
| 核心框架 | Spring Boot 2.7.18, Spring Security |
| ORM | MyBatis-Plus 3.5.3 |
| 数据库 | MySQL 8.0, Druid 1.2.20 |
| 缓存 | Redis (Spring Data Redis) |
| 搜索 | Elasticsearch 7.17.9 |
| 消息队列 | RocketMQ 4.9.4 |
| API文档 | Swagger 3.0 (Springfox) |
| 安全认证 | JWT |
| 构建工具 | Maven 3.6+ |
| Java版本 | JDK 17+ |

## 📦 功能模块

### 1. 用户模块

- 用户注册 / 登录
- JWT Token 认证
- 用户信息管理
- 密码加密存储 (BCrypt)

### 2. 商品模块

- 商品列表查询 (分页、分类筛选)
- 热门商品推荐
- 商品详情查看
- Elasticsearch 全文搜索
- Redis 缓存优化

### 3. 购物车模块

- 添加商品到购物车
- 修改商品数量
- 选择/取消选中商品
- 清空购物车

### 4. 订单模块

- 创建订单 (事务保证)
- 订单列表查询
- 取消订单 (库存回滚)
- 确认收货
- RocketMQ 消息通知

### 5. 分类模块

- 分类树形结构
- 层级关系管理

### 6. 地址模块

- 收货地址管理
- 新增/修改/删除地址
- 默认地址设置

## 🚀 快速开始

### 环境要求

| 组件 | 版本要求 |
|------|----------|
| JDK | 17+ |
| Maven | 3.6+ |
| MySQL | 8.0+ |
| Redis | 6.0+ |
| Elasticsearch | 7.x (可选) |
| RocketMQ | 4.9.x (可选) |

### 1. 克隆项目

```bash
git clone https://github.com/your-repo/ecommerce-backend.git
cd ecommerce-backend
```

### 2. 配置数据库

创建数据库并导入 SQL 脚本：

```bash
mysql -u root -p < src/main/resources/sql/schema.sql
```

### 3. 修改配置文件

编辑 `src/main/resources/application.yml`：

```yaml
server:
  port: 8080

spring:
  # 数据库配置
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    url: jdbc:mysql://localhost:3306/ecommerce?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    
  # Redis配置
  redis:
    host: localhost
    port: 6379
    
  # Elasticsearch配置
  elasticsearch:
    uris: http://localhost:9200
    
# RocketMQ配置
rocketmq:
  name-server: localhost:9876
  producer:
    group: ecommerce-producer-group
```

### 4. 编译运行

```bash
# 编译项目
mvn clean package -DskipTests

# 启动项目
java -jar target/ecommerce-1.0.0.jar
```

### 5. 访问服务

| 服务 | 地址 |
|------|------|
| API 接口 | http://localhost:8080 |
| Swagger 文档 | http://localhost:8080/swagger-ui/ |
| Druid 监控 | http://localhost:8080/druid/ (admin/admin) |

## 📂 项目结构

```
src/main/java/com/example/ecommerce/
├── config/                     # 配置类
│   ├── DruidConfig.java           # Druid 连接池配置
│   ├── ElasticsearchConfig.java   # Elasticsearch 配置
│   ├── JwtFilter.java             # JWT 认证过滤器
│   ├── JwtUtils.java              # JWT 工具类
│   ├── MyBatisPlusConfig.java     # MyBatis-Plus 配置
│   ├── RedisConfig.java           # Redis 配置
│   ├── RocketMQConfig.java        # RocketMQ 配置
│   ├── SecurityConfig.java        # Spring Security 配置
│   └── SwaggerConfig.java         # Swagger 配置
├── controller/                # 控制器层
│   ├── AddressController.java      # 地址管理
│   ├── CartController.java        # 购物车管理
│   ├── CategoryController.java    # 分类管理
│   ├── OrdersController.java       # 订单管理
│   ├── ProductController.java     # 商品管理
│   └── UserController.java         # 用户管理
├── entity/                   # 实体类
│   ├── Address.java               # 地址实体
│   ├── Cart.java                  # 购物车实体
│   ├── Category.java              # 分类实体
│   ├── OrderItem.java             # 订单项实体
│   ├── Orders.java                # 订单实体
│   ├── Product.java               # 商品实体
│   ├── ProductDocument.java       # ES 搜索文档
│   └── User.java                  # 用户实体
├── exception/                 # 异常处理
│   ├── BusinessException.java      # 业务异常
│   └── GlobalExceptionHandler.java # 全局异常处理
├── mapper/                    # 数据访问层
│   ├── AddressMapper.java
│   ├── CartMapper.java
│   ├── CategoryMapper.java
│   ├── OrderItemMapper.java
│   ├── OrdersMapper.java
│   ├── ProductMapper.java
│   └── UserMapper.java
├── mq/                       # 消息队列
│   ├── consumer/                  # 消费者
│   │   └── OrderConsumer.java
│   ├── OrderMessage.java         # 订单消息
│   └── StockMessage.java         # 库存消息
├── repository/               # ES 仓储
│   └── ProductSearchRepository.java
├── service/                  # 业务逻辑层
│   ├── impl/                       # 服务实现
│   │   ├── AddressServiceImpl.java
│   │   ├── CartServiceImpl.java
│   │   ├── CategoryServiceImpl.java
│   │   ├── OrderItemServiceImpl.java
│   │   ├── OrdersServiceImpl.java
│   │   ├── ProductServiceImpl.java
│   │   └── UserServiceImpl.java
│   └── interface/                 # 服务接口
└── common/                   # 通用类
    └── Result.java                # 统一返回结果
```

## 📋 API 接口

### 认证接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | /api/user/register | 用户注册 | 否 |
| POST | /api/user/login | 用户登录 | 否 |

### 用户模块

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/user/info | 获取用户信息 | 是 |
| PUT | /api/user/info | 更新用户信息 | 是 |

### 商品模块

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/product/list | 商品列表 | 否 |
| GET | /api/product/hot | 热门商品 | 否 |
| GET | /api/product/{id} | 商品详情 | 否 |
| GET | /api/product/search | ES搜索商品 | 否 |
| POST | /api/product/sync/{id} | 同步到ES | 是 |

### 购物车模块

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/cart/list | 购物车列表 | 是 |
| POST | /api/cart/add | 添加商品 | 是 |
| PUT | /api/cart/update/{id} | 更新数量 | 是 |
| DELETE | /api/cart/delete/{id} | 删除商品 | 是 |

### 订单模块

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/order/list | 订单列表 | 是 |
| GET | /api/order/{id} | 订单详情 | 是 |
| POST | /api/order/create | 创建订单 | 是 |
| PUT | /api/order/cancel/{id} | 取消订单 | 是 |
| PUT | /api/order/confirm/{id} | 确认收货 | 是 |

### 分类模块

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/category/tree | 分类树 | 否 |
| GET | /api/category/list | 分类列表 | 否 |

### 地址模块

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | /api/address/list | 地址列表 | 是 |
| POST | /api/address/add | 新增地址 | 是 |
| PUT | /api/address/update | 更新地址 | 是 |
| DELETE | /api/address/delete/{id} | 删除地址 | 是 |

## 📝 请求示例

### 用户登录

```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

响应：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1
  }
}
```

### 获取商品列表

```bash
curl -X GET "http://localhost:8080/api/product/list?pageNum=1&pageSize=10"
```

### 搜索商品

```bash
curl -X GET "http://localhost:8080/api/product/search?keyword=iPhone"
```

## 🖥 部署指南

### Docker 部署

```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: ecommerce
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

  elasticsearch:
    image: elasticsearch:7.17.9
    environment:
      - discovery.type=single-node
      - ES_JAVA_OPTS=-Xms512m -Xmx512m
    ports:
      - "9200:9200"

  rocketmq:
    image: apache/rocketmq:4.9.4
    ports:
      - "9876:9876"

  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
    environment:
      - SPRING_PROFILES_ACTIVE=prod

volumes:
  mysql-data:
```

## 📄 相关文档

- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/)
- [MyBatis-Plus 文档](https://baomidou.com/pages/24112f/)
- [Elasticsearch 官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)
- [RocketMQ 官方文档](https://rocketmq.apache.org/docs/quickStart/)
- [JWT 在线解码](https://jwt.io/)

## 🤝 贡献指南

欢迎提交 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/xxx`)
3. 提交更改 (`git commit -m 'Add xxx'`)
4. 推送分支 (`git push origin feature/xxx`)
5. 创建 Pull Request

## 📄 许可证

本项目基于 MIT 许可证开源，详见 [LICENSE](LICENSE) 文件。

## 📧 联系方式

- Email: developer@example.com
- GitHub: https://github.com/your-repo

---

<div align="center">

⭐️ Star 这个项目表示支持，谢谢！

</div>
