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
* jayud-tms 中港运输模块
* jayud-customs 报关模块
* jayud-finance 财务模块
* jayud-oms OMS统一项目
* jayud-email  电子邮件
* jayud-freight-air 空运
* jayud-Inland-transport 内陆运输
* jayud-mall 南京电商
* jayud-oauth  统一授权
* jayud-ocean-ship 海运
* jayud-scm  供应链
* jayud-storage  仓库
* jayud-tools  佳裕达集团工具项目
* jayud-trailer 拖车
* jayud-xxjob-task 定时任务
* jayud-work-flow 工作流项目



GIT版本管理规则
master分支
用于更新生产，必须保持与生产代码一致
master-年月日
develop分支
由开发人员在各自的feature分支开发完成后，合并至该分支
develop分支-年月日
release分支
测试环境中出现的bug，统一在该分支下进行修改，并推送至远程分支。修改内容必须合并回develop分支和master分支

项目规则
由于项目模块多，导致整个项目SQL脚本、不能及时同步，所以每个子项目必须加一个SQL文件夹，每当有增加或修改SQL时将脚本维护进去
注释规则:年月日研发人员，功能描述

配置文件规则
nacos自己新建命名空间标注为开发自己用的以后TEST标注测试环境，部署上线时会以此配置中为准