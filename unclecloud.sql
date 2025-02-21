
-- 1，创建登录的用户角色（包含密码）
DROP USER IF EXISTS unclecloud;
CREATE ROLE unclecloud WITH LOGIN NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT NOREPLICATION CONNECTION LIMIT -1 PASSWORD 'UncleCloud3';

-- 2，创建数据库
DROP DATABASE IF EXISTS unclecloud;
CREATE DATABASE unclecloud WITH OWNER = unclecloud ENCODING = 'UTF8' CONNECTION LIMIT = -1;


-- 实例
DROP TABLE IF EXISTS t_instance;
CREATE TABLE t_instance (
    id                  serial				    not null,                               -- 实例的主键ID（从10000开始计数）
    user_id				varchar(36)				not null,                               -- 所属用户ID
	name               	varchar(128)            not null,                               -- 实例名称
	server_type			int2					not null default 0,						-- 主机类型（0：经济型；1：性能型；2：专业型；）
	os					int2					not null default 0,						-- 操作系统类型（0：Ubuntu 1；1：CentOS 6.5；2：CentOS 6.6；3：CentOS 6.7；4：CentOS 6.8；5：CentOS 6.9；6：CentOS 7；2：CentOS 8；）
	intranet			varchar(15)				null,									-- 内网
	internet			varchar(15)				null,									-- 公网IPv4
	port				varchar(5)				null,									-- 映射端口
	account				varchar(32)				null,									-- 账号名称
	password			varchar(32)				null,									-- 账号密码
	firewall_id			varchar(36)				null,									-- 防火墙ID
    status				int2					not null default 0,						-- 状态（0：运行中；1：已关机；2：已暂停；3：已删除；4：已销毁；5：救援中；）
    create_time         timestamptz             not null default now(),                 -- 实例的创建时间
    CONSTRAINT pk_t_instance PRIMARY KEY (id)
);


-- 产品
DROP TABLE IF EXISTS t_product;
CREATE TABLE t_product (
    id                  serial                  not null,                               -- 产品的主键ID
	name               	varchar(128)            not null,                               -- 产品名称
    create_time         timestamptz             not null default now(),                 -- 创建时间
    CONSTRAINT pk_t_product PRIMARY KEY (id)
);
-- 产品ID从10000
SELECT setval('t_product_id_seq', 10001, false);


-- 工单
DROP TABLE IF EXISTS t_worksheet;
CREATE TABLE t_worksheet (
    id                  serial                  not null,                               -- 工单的主键ID
    user_id             int                     not null,                               -- 工单的创建用户ID
    title               varchar(256)            not null,                               -- 工单标题
    describe            varchar(1024)           null,                                   -- 工单描述
    file                varchar(22)             null,                                   -- 工单附件
    status              int2                    not null default 0,                     -- 工单状态（0：待处理；1：处理中；2：已完成；）
    create_time         timestamptz             not null default now(),                 -- 创建时间
    CONSTRAINT pk_t_worksheet PRIMARY KEY (id)
);

-- 工单回复表
DROP TABLE IF EXISTS t_worksheet_reply;
CREATE TABLE t_worksheet_reply (
    id                  serial                  not null,                               -- 工单回复的主键ID
    worksheet_id        int                     not null,                               -- 工单ID
    content             varchar(1024)           not null,                               -- 回复内容
    reply_user_id       int                     not null,                               -- 回复用户ID
    reply_time          timestamptz             not null default now(),                 -- 回复时间
    CONSTRAINT pk_t_worksheet_reply PRIMARY KEY (id)
);


