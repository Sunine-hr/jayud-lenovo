# jayud-config
统一配置中心服务

### 开发配置
application-dev.properties

#生成环境配置
application-prod.properties

#测试环境配置
application-test.properties

#配置文件描述
#数据库配置
jayud.datasource.ip=192.168.0.231
jayud.datasource.username=root
jayud.datasource.password=Root!!2020
swagger.is.enable=true
#联想物流塔前缀路径
spider.admin.content.path=/v1
spider.web.datasource.name=jayud_spider

#如有新加配置直接在新增加配置即可
#redis数据源
redis.server.url=127.0.0.1
redis.server.port=6379
redis.server.password=
