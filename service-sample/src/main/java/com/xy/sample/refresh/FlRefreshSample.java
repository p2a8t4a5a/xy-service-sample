package com.xy.sample.refresh;

public class FlRefreshSample {
    /**
     * refresh说明
     *
     * 第一: class RefreshScope
     * 他是一个Scope类，和spring写死的singleton,prototype,web中定义的RequestScope...是一个概念
     * Scope与Bean的关系: Bean定义时可以指定一个Scope，如默认定义的bean是singleton的，or 通过@Scope("name")指定
     * 在doGetBean时，Scope的作用:
     * if (mbd.isSingleton()) {
     * 	    ...
     * } else if (mbd.isPrototype()) {
     *      ...
     * } else {
     *      ...
     * } 完整代码 {@link org.springframework.beans.factory.support.AbstractBeanFactory}
     *
     * RefreshScope会被创建成Bean, with name=refresh,并被注册成IOC的Scope，通过@Scope
     *
     * 第二: @interface RefreshScope
     * @RefreshScope 上标注着 @Scope("refresh")
     *
     * 第三: dynamic refresh原理和使用
     *
     * @ConfigurationProperties 标记的配置类中的属性会被动态更新
     * {@link org.springframework.cloud.context.refresh.ContextRefresher}
     * {@link org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder}
     *
     * @RefreshScope 标记的bean
     * 该bean的Scope将是@Scope("refresh"),这将获取到对应的class RefreshScope，当该类的refresh方法被触发时，将会清除bean(仅该refresh作用域的bean)的缓存
     * 则下次获取bean时将会重新创建bean
     *
     * eg: 使用@RefreshScope标记bean,bean中的@Value动态刷新 note: 这种方式应该用@ConfigurationProperties替代
     * @RefreshScope
     * @RestController
     * public class RedissonClientTestController {
     *     @Value("${default.data}")
     *     private String cluster;
     * }
     * eg:
     * @Bean
     * @RefreshScope
     * public RedissonClient redissonClient(FlRedissonProperties properties) {
     *      Config config = null;
     *      try {
     *           config = Config.fromYAML(properties.getConfig());
     *      } catch (IOException e) {
     *           throw new BizException(() -> e.getMessage());
     *      }
     *      return Redisson.create(config);
     * }
     * @ConfigurationProperties(prefix = "spring.redis.redisson")
     * public class FlRedissonProperties {...}
     * 当redisson相关配置发生变化时,FlRedissonProperties中配置项得到更新
     * RedissonClient使用@RefreshScope标注，将会触发该bean的重新构造
     * 
     */




}
