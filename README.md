#项目介绍
本项目为佳裕达公司平台项目，该项目统一规范命名，所有项目就jayud-platform为主入口，如要新增加项目时点击右建增加new--->modell，**请一定要遵守研发规范**

#项目特性
* 为了规范项目结构
* 项目采用服服务框架，确保所有服务独立运行，互不影响
* 项目结构
  * 服务注册与发现，配置中心[nacos]
    * 本地访问地址 http://192.168.0.231:8848/nacos  用户名:nacos  密码：nacos
  * 消息中间件[kafka]
  * 分布式系统协调[zookeeper与kafka结合使用]
  * Nexus[Maven仓库管理器]
    * 本地地址：http://192.168.0.231:8181/ 用户名:admin 密码： admin
  * Feign[声明式服务调用]  
  * SpringCloud Gateway[API网关]
  * 缓存采用redis
  * 文件服务器FasfDFs
  * 定时任务采用XXL-JOB
 
 # 项目目录结构 
 * jayud-common  帮助类
 * jayud-config 统一配置中心
 * jayud-file 统一文件上传
 * jayud-gateway 统一路由转发
 * jayud-msg 统一消息处理
 * jayud-task 统一定时任务项目
 * jayud-customer 客服模块
 * jayud-customs 报关模块
 * jayud-finance 财务模块
 * jayud-oms OMS统一项目
 