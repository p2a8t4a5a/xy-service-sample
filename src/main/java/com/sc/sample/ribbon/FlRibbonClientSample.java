package com.sc.sample.ribbon;

import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * name属性指定RibbonClient的名称,每一个名称创建独立的ApplicationContext
 * 1.configuration指定RibbonClient的配置类
 * 默认配置类{@link org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration},RibbonClient由RibbonClientConfiguration中已有的配置以及自定义中的配置组成(其中后者通常会覆盖前者)
 * 2.RibbonClient的配置: application.yml
 *  <clientName>:  ##RibbonClient的名称
 *    ribbon:
 *      NFLoadBalancerClassName: ILoadBalancer
 *  ribbon:   ##如果不加<clientName>将对所有的RibbonClient有效
 *    NFLoadBalancerClassName: ILoadBalancer
 */
@RibbonClient(name = "flRibbonClientSample", configuration = FlRibbonConfiguration.class)
public class FlRibbonClientSample {

    /**
     * Rule - a logic component to determine which server to return from a list
     * Ping - a component running in background to ensure liveness of servers
     * ServerList - this can be static or dynamic. If it is dynamic (as used by DynamicServerListLoadBalancer),
     *              a background thread will refresh and filter the list at certain interval
     *
     * These components can be either set programmatically(如通过@RibbonClient) or be part of client configuration properties and created via reflection.
     * eg: in configuration properties:
     *      <clientName>.ribbon.NFLoadBalancerClassName:               ILoadBalancer
     *      <clientName>.ribbon.NFLoadBalancerRuleClassName:           IRule
     *      <clientName>.ribbon.NFLoadBalancerPingClassName:           IPing
     *      <clientName>.ribbon.NFLoadBalancerPingInterval             define ping internal
     *      <clientName>.ribbon.NFLoadBalancerMaxTotalPingTime         ?
     *      <clientName>.ribbon.NFLoadBalancerStatsClassName           ?
     *      <clientName>.ribbon.NIWSServerListClassName:               ServerList
     *      <clientName>.ribbon.NIWSServerListFilterClassName:         ServerListFilter
     *      <clientName>.ribbon.ServerListUpdaterClassName             ServerListUpdater
     *      <clientName>.ribbon.ServerListRefreshInterval              ServerList刷新时间
     *      <clientName>.ribbon.listOfServers                          静态配置的ServerList
     */

    /**
     * common Rules(负载均衡策略)
     *  1.RoundRobinRule
     *  2.AvailabilityFilteringRule
     *      By default, an instance is circuit tripped if the RestClient fails to make a connection to it for the last three times.
     *      Once an instance is circuit tripped, it will remain in this state for 30 seconds before the circuit is deemed as closed again.
     *      However, if it continues to fail connections, it will become "circuit tripped" again and the wait time for it to become "circuit closed" will increase exponentially to the number of consecutive failures.
     *
     *      # successive connection failures threshold to put the server in circuit tripped state, default 3
     *      niws.loadbalancer.<clientName>.connectionFailureCountThreshold
     *
     *      # Maximal period that an instance can remain in "unusable" state regardless of the exponential increase, default 30
     *      niws.loadbalancer.<clientName>.circuitTripMaxTimeoutSeconds
     *
     *      # threshold of concurrent connections count to skip the server, default is Integer.MAX_INT
     *      <clientName>.<clientConfigNameSpace>.ActiveConnectionsLimit
     *  3.WeightedResponseTimeRule
     *      For this rule, each server is given a weight according to its average response time. The longer the response time, the less weight it will get.
     *      The rule randomly picks a server where the possibility is determined by server's weight.
     *  and etc.
     */

    /**
     * ServerList
     *  1.static ServerList
     *      You can always set a static list of servers programatially with BaseLoadBalancer or its subclasses with the API BaseLoadBalancer.setServersList()
     *  2.ConfigurationBasedServerList
     *      通过properties配置ServerList, <clientName>.ribbon.listOfServers
     *      能够动态修改此属性值,like this:
     *       ConfigurationManager.getConfigInstance().setProperty("sample-client.ribbon.listOfServers", "www.linkedin.com:80,www.google.com:80");
     *  3.DiscoveryEnabledNIWSServerList
     *      This ServerList implementation gets the server list from Eureka client. The server cluster must be identified via VipAddress in a property. For example
     *
     *      <clientName>.ribbon.NIWSServerListClassName=com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList
     *      # the server must register itself with Eureka server with VipAddress "myservice"
     *      <clientName>.ribbon.DeploymentContextBasedVipAddresses=myservice
     *  4.Nacos,当使用nacos-discovery时，nacos提供了RibbonClient的配置
     *  {@link com.alibaba.cloud.nacos.ribbon.RibbonNacosAutoConfiguration}
     *
     *  and etc.
     */

    /**
     * ServerListFilter
     *   ServerListFilter is a component used by DynamicServerListLoadBalancer to filter the servers returned from a ServerList implementations.
     *   There are two implementations of ServerListFilter in Ribbon:
     *   1.ZoneAffinityServerListFilter
     *      Filters out the servers that are not in the same zone as the client, unless there are not servers available in the client zone.
     *      This filter can be enabled by specifying the following properties:
     *
     *      <clientName>.ribbon.EnableZoneAffinity=true
     *   2.ServerListSubsetFilter
     *      This filter makes sure that the client only sees a fixed subset of overall servers returned by the ServerList implementation.
     *      It can also replace servers in the subset of poor availability with new servers periodically. To enable this filter, specify the following properties
     *
     *      <clientName>.ribbon.NIWSServerListClassName=com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList
     *      # the server must register itself with Eureka server with VipAddress "myservice"
     *      <clientName>.ribbon.DeploymentContextBasedVipAddresses=myservice
     *      <clientName>.ribbon.NIWSServerListFilterClassName=com.netflix.loadbalancer.ServerListSubsetFilter
     *      # only show client 5 servers. default is 20.
     *      <clientName>.ribbon.ServerListSubsetFilter.size=5
     */


    /**
     * Ribbon core {@link com.sc.sample.ribbon.core.example.FlRibbonClientExample}
     */
}
