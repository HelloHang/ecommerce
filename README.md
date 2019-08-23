--目前已经完成的---
创建产品服务
创建Redis缓存服务
使用Ehcache + Redis实现二级缓存功能




--计划要做的---
给Product Service添加创建Cart,以及生成订单的接口
创建MQ服务
发送订单到MQ
创建发送Email服务
通过Email服务来消费MQ中得Order


使用Spring Cloud来整合这几个服务
使用容器Docker来进行服务部署
使用容器编排K8S来进行容器的管理