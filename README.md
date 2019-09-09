--目前已经完成的--- </br>
创建产品服务</br>
创建Redis缓存服务</br>
使用Ehcache + Redis实现二级缓存功能</br>
使用Spring Cloud来整合这几个服务</br>
增加认证鉴权服务（Spring Security + Oauth2）</br>

--计划要做的---</br>

增加网关服务（GetWay）
增加认证鉴权服务(Zuul + Oauth2 + JWT)</br>
增加服务配置中心(Spring cloud Bus)
给Product Service添加创建Cart,以及生成订单的接口</br>
创建MQ服务(Kafka + spring cloud stream)</br>
发送订单到MQ</br>
创建发送Email服务</br>
通过Email服务来消费MQ中得Order</br>
Spring hystrix



使用容器Docker来进行服务部署</br>
使用容器编排K8S来进行容器的管理</br>