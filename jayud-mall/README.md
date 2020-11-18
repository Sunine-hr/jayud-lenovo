# 项目介绍

本项目为佳裕达公司商城项目，目前主要服务于南京公公司

```
jayud-mall-admin    管理员端(后台)
jayud-mall-model    model
jayud-mall-service  service
jayud-mall-web      客户端(C端)
```



# jayud-mall-admin    管理员端(后台)

* 佳裕达公司人员使用
* 维护基础信息
  * 报关商品资料
  * 清关商品资料
* 客户
  * 编辑
  * 客户商品审核
    * 报关ID
    * 清关ID
* 报价
  * 报价模板
  * 报价



# jayud-mall-model:关于model对象的说明

* bo
* po
* vo

```
PO：persistent object，持久对象。
与数据库里表字段一一对应。PO是一些属性，以及set和get方法组成。
一般情况下，一个表，对应一个PO。是直接与操作数据库的crud相关。

VO：vlue object，又名：表现层对象，即view object。
通常用于业务层之间的数据传递，和PO一样也是仅仅包含数据而已。
但应是抽象出的业务对象，可以和表对应，也可以不，这根据业务的需要。
对于页面上要展示的对象，可以封装一个VO对象，将所需数据封装进去。

BO：bussiness object，业务对象。
封装业务逻辑的 java 对象 , 通过调用 DAO 方法 , 
结合 PO,VO 进行业务操作。 一个BO对象可以包括多个PO对象。
如常见的工作简历例子为例，简历可以理解为一个BO，简历又包括工作经历，
学习经历等，这些可以理解为一个个的PO，由多个PO组成BO。
```



# jayud-mall-service  service

* java
  * mapper
  * service
    * impl
* resources
  * mapper
    * *.xml



# jayud-mall-web      客户端(C端)

* 客户登录
  * customer
* 运价查询-即报价查询
  * 整柜
  * 散柜
* 订单
  * 下单



