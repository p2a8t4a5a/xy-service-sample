package com.xy.sample.ribbon;

import org.springframework.context.annotation.Configuration;

/**
 * 对于主应用程序上下文，它不在@ComponentScan中。否则，它由所有@RibbonClients共享。
 * 如果您使用@ComponentScan，则应该采取措施避免将其包括在内（例如，可以将其放在单独的，不重叠的程序包中，或指定要在@ComponentScan）
 */
@Configuration
public class FlRibbonConfiguration {


    /*ribbon单独调试时打开
    @Bean
    public ServerList<Server> configurationBasedServerList() {
        ConfigurationBasedServerList serverList = new ConfigurationBasedServerList();
        DefaultClientConfigImpl config = new DefaultClientConfigImpl();
        config.setProperty(CommonClientConfigKey.ListOfServers, "www.baidu.com,https://www.springcloud.cc/");
        serverList.initWithNiwsConfig(config);
        return serverList;
    }*/

}
