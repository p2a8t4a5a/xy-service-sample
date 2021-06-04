package com.xy.sample.ribbon.core.example;

import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.netflix.niws.client.http.RestClient;


/**
 * Ribbon core
 */
public class FlRibbonClientExample {

    public static void main(String argv[]) throws Exception {
        //1.加载properties
        ConfigurationManager.loadPropertiesFromResources("com/xy/sample/ribbon/core/example/ribbon-client-example.properties");
        System.out.println(ConfigurationManager.getConfigInstance().getProperties("sample-client.ribbon.listOfServers")); //打印属性
        //2.使用ClientFactory创建restClient,RestClient 继承 AbstractLoadBalancerAwareClient
        //          AbstractLoadBalancerAwareClient takes care of the load balancer integration, retry logic,
        //          and collection of statistics that is used as input to load balancer algorithms and for monitoring.
        RestClient client = (RestClient) ClientFactory.getNamedClient("sample-client");
        HttpRequest request = HttpRequest.newBuilder().uri("/").build();

        for (int i = 0; i < 20; i++)  {
            //3.使用restClient发起请求 load balancer integration, retry logic, and collection of statistics
            HttpResponse response = client.executeWithLoadBalancer(request);
            System.out.println("Status code for " + response.getRequestedURI() + "  :" + response.getStatus());
        }

        //4.获取LoadBalancer的默认实现
        ZoneAwareLoadBalancer defaultLoadBalance = (ZoneAwareLoadBalancer) client.getLoadBalancer();
        System.out.println(defaultLoadBalance.getLoadBalancerStats());

        //5.通过api修改listOfServers配置
        ConfigurationManager.getConfigInstance().setProperty(
                "sample-client.ribbon.listOfServers", "www.linkedin.com:80,www.google.com:80");

        //Wait until server list is refreshed (2 seconds refersh interval defined in properties file)
        Thread.sleep(3000);

        for (int i = 0; i < 20; i++)  {
            HttpResponse response = client.executeWithLoadBalancer(request);
            System.out.println("Status code for " + response.getRequestedURI() + "  : " + response.getStatus());
        }

        System.out.println(defaultLoadBalance.getLoadBalancerStats());

    }

    /**
     * 自定义RestClient:
     * 1.extend AbstractLoadBalancerAwareClient
     *  AbstractLoadBalancerAwareClient takes care of the load balancer integration, retry logic,
     *      and collection of statistics that is used as input to load balancer algorithms and for monitoring.
     * 2.配置: <clientName>.<nameSpace>.ClientClassName=<Your implementation class name>
     *
     */

}
